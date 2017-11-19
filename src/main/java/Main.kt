// Copyright © FunctionalKotlin.com 2017. All rights reserved.

fun main(args: Array<String>) {
    createUser("alex", "functionalkotlin", true, false)
        .map { it.name }
        .ifSuccess { println(it) }
}