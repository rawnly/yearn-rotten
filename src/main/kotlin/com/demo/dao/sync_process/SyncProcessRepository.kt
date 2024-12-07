package com.demo.dao.sync_process

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SyncProcessRepository: PanacheRepository<SyncProcess> {
    fun findLastRunByClubID(clubID: String) = find("clubID = ?1 order by createdAt desc", clubID).firstResult()
}