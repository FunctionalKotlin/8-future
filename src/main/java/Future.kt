// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch


class Future<A>(val task: ((A) -> Unit) -> Unit) {
    companion object {
        fun <A> async(getValue: () -> A): Future<A> {
            val task: ((A) -> Unit) -> Unit = { continuation ->
                launch(CommonPool) {
                    continuation(getValue())
                }
            }

            return Future(task)
        }
    }
}

fun main(args: Array<String>) {
    val getNumber = {
        23 + 19
    }

    val task: ((Int) -> Unit) -> Unit = { continuation ->
        continuation(getNumber())
    }

    val future = Future(task)
}