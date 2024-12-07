package com.demo.dao.player

import com.demo.utils.QueryBuilder
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

@ApplicationScoped
class PlayerServiceImpl: PlayerService {
    @Inject
    lateinit var playerRepository: PlayerRepository

    override fun getPlayers(
        position: String?,
        active: Boolean?,
        clubId: String?,
        birthDateRange: ClosedRange<LocalDate>?
    ): List<PlayerEntity> {
        val params = mutableMapOf<String, Any>()

        position?.let { params["position"] = it }
        active?.let { params["active"] = it }
        clubId?.let { params["clubID"] = it }
        birthDateRange?.let { params["dateOfBirth"] = it }

        if ( params.isEmpty() ) return playerRepository.listAll()

        val queryItems = QueryBuilder.build(params)

        return playerRepository.find(queryItems.first, queryItems.second).list()
    }

    override fun findOutdated(clubID: String, version: Long): List<PlayerEntity> {
        return playerRepository.findOutdated(clubID, version)
    }
}