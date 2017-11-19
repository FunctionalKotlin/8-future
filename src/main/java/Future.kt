// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class Future<A>(val task: ((A) -> Unit) -> Unit)

fun <A> asyncFuture(getValue: () -> A): Future<A> =
    Future { continuation ->
        launch(CommonPool) {
            continuation(getValue())
        }
    }

fun main(args: Array<String>) {
    val getNumber = {
        23 + 19
    }

    val future = asyncFuture { getNumber }
}