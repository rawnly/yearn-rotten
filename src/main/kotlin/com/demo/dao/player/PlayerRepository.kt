package com.demo.dao.player

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PlayerRepository: PanacheRepository<PlayerEntity> {
    fun findLastInsertedForClubId(clubID: String): PlayerEntity? {
        return find("clubID = ?1 order by createdAt desc", clubID).firstResult()
    }

    fun findLastVersionForClub(clubID: String): Long {
        find("clubID = ?1 order by version desc", clubID).firstResult()?.let {
            return it.version
        } ?: return 1
    }

    fun findOutdated(clubID: String, minVersion: Long): List<PlayerEntity> {
         val q = find("clubID = ?1 and version < ?2", clubID, minVersion)

        println(q.toString())

        return q.list()
    }
}