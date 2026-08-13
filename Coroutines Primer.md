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

* Code inside supplyAsync loses ThreadLocal access
* Blocking code inside async does not run on a virtual thread, leading to thread starvation
* The thread continues to run even if the other CompletableFuture aborted, leading to a memory leak. 

**TL;DR**: Parallel code is hard; think long and hard if you really want to add it.

## A Better Solution: Structured Concurrency