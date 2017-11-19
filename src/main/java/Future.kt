// Copyright © FunctionalKotlin.com 2017. All rights reserved.

class Future<A>(val task: ((A) -> Unit) -> Unit) {
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