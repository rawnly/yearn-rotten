package com.demo.dao.player

import com.demo.rest.client.transfermarkt.TransferMarktClubPlayer
import java.time.LocalDate
import java.time.LocalDateTime


class TransferMarktClubPlayerMapper {
    private var player: PlayerEntity = PlayerEntity()

    constructor(
        clubID: String,
        player: TransferMarktClubPlayer,
        version: Long
    ) {
        val now = LocalDate.now()

        this.player.transferMarktPlayerId = player.id
        this.player.name = player.name
        this.player.position = player.position
        this.player.dateOfBirth = player.dateOfBirth
        this.player.version = version
        this.player.active = now >= player.joinedOn && now <= player.contract
        this.player.clubID = clubID
    }

    fun toEntity(): PlayerEntity {
        return this.player
    }
}
