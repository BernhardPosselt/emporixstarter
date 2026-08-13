package at.fyayc.backend.emporixapi

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runInterruptible
import java.util.concurrent.Executors

val virtualThreadDispatcher = Executors.newVirtualThreadPerTaskExecutor()
    .asCoroutineDispatcher()

/**
 * Use this function to wrap blocking Java code when used inside suspend
 * functions. This forks off a new virtual thread !losing thread local access!
 * that suspends properly in a coroutine
 */
suspend fun <T> runOnVirtualThreadAndInterruptible(operation: () -> T): T =
    runInterruptible(virtualThreadDispatcher) {
        operation()
    }