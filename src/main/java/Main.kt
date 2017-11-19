import java.time.LocalDate

// Copyright © FunctionalKotlin.com 2017. All rights reserved.

fun main(args: Array<String>) {
    createUser("alex", "functionalkotlin", true, false, LocalDate.of(1990, 1, 1), "alex@functionalhub.com")
        .runSync()
        .map { it.name }
        .ifSuccess { println(it) }
}