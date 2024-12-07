package com.demo.rest.client.transfermarkt

import jakarta.json.bind.annotation.JsonbCreator
import jakarta.json.bind.annotation.JsonbDateFormat
import jakarta.json.bind.annotation.JsonbProperty
import java.time.LocalDateTime

data class TransferMarktClubPlayersResponse
@JsonbCreator constructor (
    @JsonbProperty val id: String,
    @JsonbProperty val players: Set<TransferMarktClubPlayer>,

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @JsonbProperty val updatedAt: LocalDateTime
)
