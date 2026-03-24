package com.sharetimer.apiservice.adapter.output.persistence

import com.sharetimer.apiservice.adapter.output.persistence.TimerMapper.toDomain
import com.sharetimer.apiservice.adapter.output.persistence.TimerMapper.toJpaEntity
import com.sharetimer.apiservice.application.port.output.LoadTimerPort
import com.sharetimer.apiservice.application.port.output.SaveTimerPort
import com.sharetimer.apiservice.domain.model.Timer
import org.springframework.stereotype.Component
import java.util.UUID

@Component("timerPersistenceAdapter")
class TimerPersistenceAdapter(
    private val timerRepository: TimerRepository,
) : LoadTimerPort,
    SaveTimerPort {
    override fun loadTimer(timerId: UUID): Timer? =
        timerRepository
            .findWithTimestampsById(timerId)
            .map { it.toDomain() }
            .orElse(null)

    override fun saveTimer(timer: Timer): Timer {
        val savedEntity = timerRepository.saveAndFlush(timer.toJpaEntity())
        return savedEntity.toDomain()
    }

    override fun deleteTimer(timerId: UUID) {
        timerRepository.deleteById(timerId)
    }
}
