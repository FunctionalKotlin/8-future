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

fun main(args: Array<String>) {
    val future = asyncFuture { 23 + 19 }

    launch(CommonPool) {
        val number = future.task.await()

        println(number)
    }

    Thread.sleep(2000)
}