package com.demo.rest.client.transfermarkt

import jakarta.json.bind.annotation.JsonbCreator
import jakarta.json.bind.annotation.JsonbDateFormat
import jakarta.json.bind.annotation.JsonbProperty
import java.time.LocalDate

data class TransferMarktClubPlayer @JsonbCreator constructor (
    @JsonbProperty val id: Long,
    @JsonbProperty val name: String,
    @JsonbProperty val position: String,

    @JsonbDateFormat("MMM d, yyyy")
    @JsonbProperty val dateOfBirth: LocalDate,

    @JsonbDateFormat("MMM d, yyyy")
    @JsonbProperty val joinedOn: LocalDate,

    @JsonbDateFormat("MMM d, yyyy")
    @JsonbProperty val contract: LocalDate,
)
