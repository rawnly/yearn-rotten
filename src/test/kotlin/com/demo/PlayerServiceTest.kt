package com.demo

import com.demo.dao.player.PlayerServiceImpl
import com.demo.utils.TestUtils
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import java.time.LocalDate

@QuarkusTest
class PlayerServiceTest {
    @Inject
    private lateinit var testUtils: TestUtils

    @Inject
    private lateinit var playerService: PlayerServiceImpl

    @Test
    @Transactional
    fun testPositionFilter() {
        this.insertPlayers()

        val players = playerService.getPlayers(
            "goalkeeper"
        )

        assert(players.size == 1)
    }

    @Test
    @Transactional
    fun testActiveFilter() {
        this.insertPlayers()

        val players = playerService.getPlayers(
            null,
            true
        )

        assert(players.size == 1)
    }

    @Test
    @Transactional
    fun testClubFilter() {
        this.insertPlayers()

        val players = playerService.getPlayers(
            null,
            null,
            "5"
        )

        assert(players.size == 2)
    }

    @Test
    @Transactional
    fun testBirthDateRangeFilter() {
        this.insertPlayers()

        val range = LocalDate.of(1980, 1, 1) ..LocalDate.of(2000, 12, 1)
        val players = playerService.getPlayers(
            null,
            null,
            null,
            range
        )

        assert(players.size == 2)
    }

    @Test
    @Transactional
    fun testCombinedFilters() {
        testUtils.insertPlayer(
            "goalkeeper",
            true,
            "5",
            LocalDate.of(2000, 1, 1)
        )

        testUtils.insertPlayer(
            "goalkeeper",
            true,
            "5",
            LocalDate.of(1999, 1, 1)
        )

        testUtils.insertPlayer(
            "goalkeeper",
            true,
            "6",
            LocalDate.of(1978, 1, 1)
        )

        testUtils.insertPlayer(
            "string",
            false,
            "5",
            LocalDate.of(1995, 1, 1)
        )

        testUtils.insertPlayer(
            "randomstring",
            false,
            "8",
            LocalDate.of(2001, 1, 1)
        )


        val range = LocalDate.of(1980, 1, 1) ..LocalDate.of(2000, 12, 1)
        val players = playerService.getPlayers(
            "goalkeeper",
            true,
            "5",
            range
        )

        assert(players.size == 2)
    }


    fun insertPlayers() {
        testUtils.insertPlayer(
            "goalkeeper",
            false,
            "5",
            LocalDate.of(2000, 1, 1)
        )

        testUtils.insertPlayer(
            "random",
            true,
            "5",
            LocalDate.of(1991, 1, 1)
        )
    }
}