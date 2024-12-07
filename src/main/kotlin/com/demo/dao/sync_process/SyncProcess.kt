package com.demo.dao.sync_process

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class SyncProcess: PanacheEntity() {

    // 0 - in progress
    // 1 - completed
    // 2 - failed
    var status: UByte = 0u
    var failureReason: String? = null
    var version: Long = 1
    var createdAt: LocalDateTime = LocalDateTime.now()

    lateinit var clubID: String

    fun setStatusInProgress(): SyncProcess {
        this.status = 0u
        return this
    }

    fun setStatusCompleted(): SyncProcess {
        this.status = 1u
        return this
    }

    fun setStatusFailed(reason: String? = null): SyncProcess {
        this.status = 2u
        this.failureReason = reason
        return this
    }

    fun isFailed() = this.status.toUInt() == 2u
    fun isInProgress() = this.status.toUInt() == 0u
}