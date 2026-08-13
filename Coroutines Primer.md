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
    val c1 = async {operation1()}
    val c2 = async {operation2()}
    return c1.await() + c2.await()
}
```

runBlocking starts an event loop and parks the current thread until all events inside of it have finished. You can pass a strategy on where to run computations:

* **No parameter**: runs on the current scope or Dispatchers.Default if there is none
* **Default**: thread pool with threads equal to cpu threads. Use this one for computations
* **IO**: large thread pool that can fork off a lot of threads waiting for IO
* **Main**: main thread in Android or GUI applications
* **Unconfined**: Runs on the current thread until it suspends, then picks whatever thread is active
