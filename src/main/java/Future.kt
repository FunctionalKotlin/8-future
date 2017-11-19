// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

typealias FutureTask<A> = Deferred<A>

class Future<out A>(val task: FutureTask<A>)

fun <A> asyncFuture(getValue: () -> A): Future<A> =
    Future(async(CommonPool) {
        getValue()
    })

fun <A> Future<A>.runAsync(onComplete: (A) -> Unit) {
    launch(CommonPool) {
        onComplete(task.await())
    }
}

fun <A, B> Future<A>.map(transform: (A) -> B): Future<B> =
    Future(async(CommonPool) {
        transform(this@map.task.await())
    })

fun main(args: Array<String>) {
    asyncFuture { 23 + 19 }
        .map { it + 3 }
        .runAsync(::println)
}