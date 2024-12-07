package com.demo.dao.player

import java.time.LocalDate
import java.time.LocalDateTime

interface PlayerService {
    fun getPlayers(
        position: String? = null,
        active: Boolean?  = null,
        clubId: String?   = null,
        birthDateRange: ClosedRange<LocalDate>? = null
    ): List<PlayerEntity>
    
    fun findOutdated(clubID: String, version: Long): List<PlayerEntity>
}