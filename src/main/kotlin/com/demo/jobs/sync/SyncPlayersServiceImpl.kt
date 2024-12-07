package com.demo.jobs.sync

import com.demo.dao.player.PlayerEntity
import com.demo.dao.player.PlayerRepository
import com.demo.dao.player.PlayerServiceImpl
import com.demo.dao.player.TransferMarktClubPlayerMapper
import com.demo.dao.sync_process.SyncProcess
import com.demo.dao.sync_process.SyncProcessRepository
import com.demo.rest.client.transfermarkt.TransferMarktAPIClient
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class SyncPlayersServiceImpl: SyncPlayersService {
    @Inject
    private lateinit var playerRepository: PlayerRepository

    @Inject
    private lateinit var syncProcessRepo: SyncProcessRepository

    @Inject
    @RestClient
    private lateinit var tmApiClient: TransferMarktAPIClient

    @Inject
    private lateinit var playersService: PlayerServiceImpl

    override fun syncClubPlayers(clubID: String): Int {
        val response = tmApiClient.getClubPlayers(clubID)

        val sortedPlayers = response.players.sortedBy { it.id }

        val lastExecution = syncProcessRepo.findLastRunByClubID(clubID)
        val process = this.start(clubID, lastExecution?.version)

        try {
            when {
                lastExecution != null && lastExecution.isInProgress() -> {
                    println("Execution already in progress, skipping...")
                    syncProcessRepo.delete(process)
                    return process.status.toInt()
                }
                // resume last execution by fetching the last inserted player and start from there
                lastExecution != null && lastExecution.isFailed() -> {
                    println("Resuming last execution...")

                    process.version = lastExecution.version

                    val startFrom = this.getLastInsertedPlayer(clubID)?.let {
                        sortedPlayers.withIndex()
                            .first { p ->
                                p.value.id == it.transferMarktPlayerId
                            }.index
                    } ?: 0

                    // insert all the players
                    sortedPlayers
                        .withIndex()
                        .forEach {
                            // skip the ones already inserted
                            if ( it.index <= startFrom ) return@forEach

                            playerRepository.persist(
                                TransferMarktClubPlayerMapper(response.id, it.value, process.version)
                                    .toEntity()
                            )
                        }
                }
                // different version we have to perform a check across all the players and update the
                // version of the ones present in the api response, then delete all the ones not updated.
                // NOTE: this applies even if the last execution has not been completed
                lastExecution != null && lastExecution.version != process.version -> {
                    println("Different version detected: ${lastExecution.version} -> ${process.version}")

                    val outdatedPlayers = playersService.findOutdated(process.clubID, process.version).associateBy { it.transferMarktPlayerId }
                    val apiPlayers  = sortedPlayers.associateBy { it.id }
                    val updatedPlayersIds = mutableSetOf<Long>()

                    outdatedPlayers.forEach { id, player ->
                        val tmPlayer = apiPlayers[id]

                        if (tmPlayer != null) {
                            player.bumpVersion()
                            playerRepository.persist(player)
                            updatedPlayersIds.add(id)

                            return@forEach
                        }

                        playerRepository.delete(player)
                    }

                    sortedPlayers.forEach {
                        if (updatedPlayersIds.contains(it.id)) return@forEach

                        playerRepository.persist(
                            TransferMarktClubPlayerMapper(response.id, it, process.version)
                                .toEntity()
                        )
                    }
                }
                else -> {
                    println("Starting new execution...")

                    sortedPlayers
                        .asSequence()
                        .forEach {
                            playerRepository.persist(
                                TransferMarktClubPlayerMapper(response.id, it, process.version)
                                    .toEntity()
                            )
                        }
                }
            }

            syncProcessRepo.persist(
                process.setStatusCompleted()
            )
            return process.status.toInt()
        } catch (ex : Exception) {
            ex.printStackTrace()
            syncProcessRepo.persist(
                process.setStatusFailed(ex.message)
            )

            return process.status.toInt()
        }
    }

    private fun getLastInsertedPlayer(clubID: String): PlayerEntity? {
        return playerRepository.findLastInsertedForClubId(clubID)
    }


    private fun start(clubID: String, version: Long?): SyncProcess {
        val process = SyncProcess()

        process.clubID = clubID
        process.version = (version ?: 0) + 1
        process.setStatusInProgress()

        syncProcessRepo.persist(process)

        return process
    }
}