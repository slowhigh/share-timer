package com.sharetimer.apiservice.adapter.output.cache

import com.sharetimer.apiservice.application.port.output.LoadTimerPort
import com.sharetimer.apiservice.application.port.output.SaveTimerPort
import com.sharetimer.apiservice.domain.model.Timer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.UUID

/** Caching decorator for Timer persistence operations using CacheModel */
@Primary
@Component
class TimerCacheAdapter(
    @param:Qualifier("timerPersistenceAdapter") private val persistenceAdapter: LoadTimerPort,
    @param:Qualifier("timerPersistenceAdapter")
    private val savePersistenceAdapter: SaveTimerPort,
    private val cacheManager: CacheManager,
) : LoadTimerPort,
    SaveTimerPort {
    companion object {
        private const val CACHE_NAME = "timers"
    }

    override fun loadTimer(timerId: UUID): Timer? {
        val cache =
            cacheManager.getCache(CACHE_NAME) ?: return persistenceAdapter.loadTimer(timerId)

        cache.get(timerId, TimerCacheEntity::class.java)?.let {
            return it.toDomain()
        }

        return persistenceAdapter.loadTimer(timerId)?.also {
            cache.put(timerId, TimerCacheEntity.from(it))
        }
    }

    override fun saveTimer(timer: Timer): Timer {
        val saved = savePersistenceAdapter.saveTimer(timer)
        timer.id?.let { cacheManager.getCache(CACHE_NAME)?.evict(it) }
        return saved
    }

    override fun deleteTimer(timerId: UUID) {
        savePersistenceAdapter.deleteTimer(timerId)
        cacheManager.getCache(CACHE_NAME)?.evict(timerId)
    }
}
