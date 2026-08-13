package at.fyayc.backend.emporixapi

import org.springframework.integration.redis.util.RedisLockRegistry
import org.springframework.stereotype.Service
import java.util.concurrent.locks.Lock

@Service
class RedisDistributedLock(
    private val redisLockRegistry: RedisLockRegistry,
) {
    suspend fun <T> locking(key: String, operation: suspend () -> T): T {
        val lock: Lock = redisLockRegistry.obtain(key)
        runOnVirtualThreadAndInterruptible {
            lock.lockInterruptibly()
        }
        return try {
            operation()
        } finally {
            lock.unlock()
        }
    }
}