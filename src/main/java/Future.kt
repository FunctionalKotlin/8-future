// Copyright © FunctionalKotlin.com 2017. All rights reserved.

class Future(val task: () -> Unit) {
}

fun main(args: Array<String>) {
    val getNumber = {
        23 + 19
    }

    val task = {
        getNumber()

        Unit
    }

    val future = Future(task)
}