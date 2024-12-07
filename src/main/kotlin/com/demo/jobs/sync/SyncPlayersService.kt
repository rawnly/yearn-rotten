package com.demo.jobs.sync

interface SyncPlayersService {
    fun syncClubPlayers(clubID: String): Int
}