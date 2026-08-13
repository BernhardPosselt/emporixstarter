# Coroutines Primer

Let's say we want to do things asynchronously or in parallel. In Java, you'd naively fork off a CompletableFuture like this:

```java
import java.util.concurrent.CompletableFuture;

void main() {
    var c1 = CompletableFuture.supplyAsync(() -> operation1());
    var c2 = CompletableFuture.supplyAsync(() -> operation2());
    var result = c1.get() + c2.get();
}
```

What's the issue with this approach? The following:

1. The thread continues to run even if the other CompletableFuture aborted, leading to a memory leak.
2. Blocking code inside async does not run on a virtual thread, leading to thread starvation
3. Code inside supplyAsync loses ThreadLocal access
4. Shared state in operation1 and operation2 that is not thread safe will fail

**TL;DR**: Parallel code is hard; think long and hard if you really want to add it.

## A Better Solution: Structured Concurrency

The solutions to these problems are the following:

1. We build an abstraction around CompletableFutures that cancels other futures if any of them is canceled
2. We run blocking code on an event loop that can suspend properly (aka VirtualThread)
3. We copy readonly ThreadLocals into the CompletableFuture and hope they are never written to; ideally we don't need ThreadLocals at all
4. We read through the entirety of operation1 and 2 to ensure that every piece of code is thread safe; this includes checking for concurrent writes to the same files

Coroutines solve 1 and 2:

```kt
val result = runBlocking(Dispatchers.Default) {
    val c1 = async { operation1() }
    val c2 = async { operation2() }
    return c1.await() + c2.await()
}
```

runBlocking starts an event loop and parks the current thread until all events inside of it have finished. If it is run inside a Virtual Thread, it integrates into that machinery. You can pass a strategy on where to run computations:

* **No parameter
  **: runs on the current scope if run inside one, or on the current thread, simulating multithreading with a FIFO queue. This means that you don't get true parallelism
* **Default**: thread pool with threads equal to cpu threads. Use this one for computations
* **IO**: large thread pool that can fork off a lot of threads waiting for IO
* **Main**: main thread in Android or GUI applications
* **Unconfined**: Runs on the current thread until it suspends, then picks whatever thread is active

## Bridging Coroutine and non Coroutine Code

Ideally, your main function is already a suspending method. Spring technically supports suspending controller methods like:

```kt
@Controller
class Controller {
    @GetMapping("/")
    suspend fun handleRequest() {
        ...
    }
}
```

but these run on Unconfined dispatcher which is prone to lose ThreadLocal context. Reading relevant GitHub issues, suspending functions were never intended to be used on Spring MVC, and are only properly supported on Webflux.

This leaves us with the following:

* Run MVC with Virtual Threads enabled
* Inside a controller method, use **runBlocking(Dispatchers.Default)
  ** to start coroutines; runBlocking suspends on the Virtual Thread by default
* Anything available from Spring itself (transactions, session, etc) should be done outside of coroutines because it potentially relies on ThreadLocals

Blocking Java code needs special care, since anything inside coroutines is not guaranteed to run on a virtual thread. If you need to use Files.writeText() or similar blocking Java APIs, you need to wrap those in a Virtual Thread like this:

```kt
val virtualThreadDispatcher = Executors.newVirtualThreadPerTaskExecutor()
    .asCoroutineDispatcher()

// runInterruptible converts JVM InterruptExceptions into CancellationExceptions
suspend fun <T> runOnVirtualThreadAndInterruptible(operation: () -> T): T =
    runInterruptible(virtualThreadDispatcher) {
        operation()
    }

fun main() {
    runBlocking(Dispatchers.Default) {
        val contents = runOnVirtualThreadAndInterruptible {
            Files.readText(...)
        }
    }
}
```

## Suspend Functions

Once inside a coroutine world, you can call suspending functions. Suspending functions work exactly the same as async functions in JavaScript, but they are automatically awaited. Meaning that this will execute sequentially

```kt
suspend fun example() {
    oneSuspendingFunction()
    anotherSuspendingFunction()
}
```

If you need them to be started concurrently by not awaiting them and instead aggregating them similar to Promise.all, you need a
**async** (and a coroutineScope if not directly inside runBlocking):

```kt
suspend fun example() = coroutineScope {
    val first = async { oneSuspendingFunction() }
    val second = async { anotherSuspendingFunction() }
    val resultFirst = first.await()
    val resultSecond = second.await()
}
```

## Changing Dispatchers

Sometimes, you want specific tasks to be run on different thread pools. For instance KTOR client, a library that does HTTP calls, does I/O and therefore always wants to run on the IO dispatcher (
**this is already built in for KTOR!**).

To do that, use **withContext**:

```kt
suspend fun example() = coroutineScope {
    val first = async { oneSuspendingFunction() }
    val second = async {
        withContext(Dispatchers.IO) {
            suspendFunctionWantingIO()
        }
    }
    val resultFirst = first.await()
    val resultSecond = second.await()
}
```

## Cancellation

The main benefit that coroutines give us is cleanup. Given this example:

```kt
suspend fun example() = coroutineScope {
    val first = async { oneSuspendingFunction() }
    val second = async { anotherSuspendingFunction() }
    val resultFirst = first.await()
    val resultSecond = second.await()
}
```

If any of the async blocks throws an exception (that is not a CancellationException), all other coroutines in the same scope will be canceled. Then it walks up to the closest parent, and continues to cancel everything until it arrived at the root scope. 

However, in order for a coroutine to be cancellable, it needs to support it. Think about what would happen if you just killed a database transaction in the JVM outright: it would be left in an incomplete state.

Many libraries like KTOR or functions in **kotlinx.coroutines** like delay already support cancellation out of the box, but if you need to do it yourself, you can use isActive inside a loop or ensureActive between steps

```kt
suspend fun example() = coroutineScope {
    while(isActive) {
        doWork()
    }
}

suspend fun example() = coroutineScope {
  doWork()
  ensureActive()
  doOtherWork()
}
```