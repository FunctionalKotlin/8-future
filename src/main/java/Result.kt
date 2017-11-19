// Copyright © FunctionalKotlin.com 2017. All rights reserved.

sealed class Result<out A, out E>

data class Success<out A>(val value: A) : Result<A, Nothing>()

data class Failure<out E>(val error: E) : Result<Nothing, E>()

fun <A, E, B> Result<A, E>.map(transform: (A) -> B): Result<B, E> =
    flatMap { transform(it).let { Success(it) } }

fun <A, E, B> Result<A, E>.flatMap(
    transform: (A) -> Result<B, E>): Result<B, E> = when(this) {
        is Success -> transform(value)
        is Failure -> this
    }

fun <A, B, E> Result<A, E>.apply(
    resultAB: Result<(A) -> B, E>): Result<B, E> =
        flatMap { a -> resultAB.map { it(a) } }

fun <A, E> Result<A, E>.ifSuccess(execute: (A) -> Unit) {
    if (this is Success) execute(this.value)
}

fun <A> pure(a: A): Result<A, Nothing> = Success(a)

infix fun <A, B, E> ((A) -> B).map(
    result: Result<A, E>): Result<B, E> = result.map(this)

infix fun <A, B, E> Result<(A) -> B, E>.ap(
    result: Result<A, E>): Result<B, E> = result.apply(this)

infix fun <A, E, B> Result<A, E>.bind(
    transform: (A) -> Result<B, E>): Result<B, E> = flatMap(transform)