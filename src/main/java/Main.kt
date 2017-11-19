// Copyright © FunctionalKotlin.com 2017. All rights reserved.

fun main(args: Array<String>) {
    createUser("alex", "functionalkotlin", true, false)
        .runSync()
        .map { it.name }
        .ifSuccess { println(it) }
}