package com.demo.sync

import com.demo.dao.player.PlayerRepository
import com.demo.jobs.sync.SyncPlayersServiceImpl
import com.demo.utils.TestUtils
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
class SyncServiceTest {
    @Inject
    lateinit var playerRepository: PlayerRepository

    @Inject
    lateinit var syncPlayersService: SyncPlayersServiceImpl

    @Inject
    lateinit var utils: TestUtils

    @Test
    @Transactional
    fun testAddAllPlayers() {
        Assertions.assertEquals(0, playerRepository.count())
        Assertions.assertEquals(1, syncPlayersService.syncClubPlayers("5"))
        Assertions.assertTrue(playerRepository.count() > 0)
    }

    @Test
    @Transactional
    fun testRemoveOutSyncPlayers() {
        Assertions.assertEquals(0, playerRepository.count())
        Assertions.assertEquals(1, syncPlayersService.syncClubPlayers("5"))
        utils.insertRandomPlayer("5")
        utils.insertRandomPlayer("5")

        val count = playerRepository.count()
        Assertions.assertTrue(count > 0)
        Assertions.assertEquals(1,syncPlayersService.syncClubPlayers("5"))

        val countAfterSync = playerRepository.count()
        Assertions.assertTrue(countAfterSync > 0)
        // check if the random inserted players have been removed
        Assertions.assertEquals(count - 2, countAfterSync)
    }

    @Test
    @Transactional
    fun testAddMissingPlayers() {
        Assertions.assertEquals(1, syncPlayersService.syncClubPlayers("5"))
        val count = playerRepository.count()

        playerRepository.find("clubID", "5").firstResult()?.let {
            playerRepository.delete(it)
        }

        playerRepository.find("clubID", "5").firstResult()?.let {
            playerRepository.delete(it)
        }

        val countAfterDelete = playerRepository.count()

        Assertions.assertEquals(count - 2, countAfterDelete)
        Assertions.assertEquals(1, syncPlayersService.syncClubPlayers("5"))

        Assertions.assertEquals(count, playerRepository.count())
    }
}