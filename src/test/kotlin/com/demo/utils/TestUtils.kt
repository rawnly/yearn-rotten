package com.demo.utils

import com.demo.dao.player.PlayerEntity
import com.demo.dao.player.PlayerRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

@ApplicationScoped
class TestUtils {
    @Inject
    lateinit var playerRepository: PlayerRepository

    fun insertPlayer(
        position: String = "random",
        active: Boolean = true,
        clubID: String = UUID.randomUUID().toString(),
        dateOfBirth: LocalDate = LocalDate.of(1999, 6, 18),
    ) : PlayerEntity {
        val p = PlayerEntity()
        p.name = UUID.randomUUID().toString()
        p.clubID = clubID
        p.transferMarktPlayerId = ThreadLocalRandom.current().nextLong(1, 100)
        p.dateOfBirth = dateOfBirth
        p.active = active
        p.version = 1
        p.position = position

        playerRepository.persist(p)

        return p
    }

    fun insertRandomPlayer(clubID: String) : PlayerEntity {
        val version = playerRepository.findLastVersionForClub(clubID)
        val p = PlayerEntity()
        p.name = UUID.randomUUID().toString()
        p.clubID = clubID
        p.transferMarktPlayerId = ThreadLocalRandom.current().nextLong(1, 100)
        p.dateOfBirth = LocalDate.of(1999, 6, 18)
        p.active = true
        p.version = version
        p.position = "random"

        playerRepository.persist(p)

        return p
    }


}