package com.sharetimer.apiservice.adapter.output.persistence

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface TimerRepository : JpaRepository<TimerJpaEntity, UUID> {
    @EntityGraph(attributePaths = ["timestamps"])
    fun findWithTimestampsById(id: UUID): Optional<TimerJpaEntity>
}
