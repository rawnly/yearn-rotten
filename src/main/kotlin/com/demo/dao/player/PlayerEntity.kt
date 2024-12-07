package com.demo.dao.player

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class PlayerEntity : PanacheEntity() {
    var active: Boolean = true
    var createdAt: LocalDateTime = LocalDateTime.now()
    var transferMarktPlayerId: Long = 0
    var version: Long = 1

    lateinit var name: String
    lateinit var position: String
    lateinit var dateOfBirth: LocalDate
    lateinit var clubID: String

    fun bumpVersion(): PlayerEntity {
        this.version++
        return this
    }
}
