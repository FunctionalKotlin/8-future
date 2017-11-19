import java.time.LocalDate
import java.time.temporal.ChronoUnit

// Copyright © FunctionalKotlin.com 2017. All rights reserved.

typealias AsyncResult<A, E> = Future<Result<A, E>>
typealias Validator<A, E> = (A) -> AsyncResult<A, E>

infix operator fun <T, E> (Validator<T, E>).plus(
    validator: Validator<T, E>): Validator<T, E> = {
        this(it).flatMap {
            when(it) {
                is Success -> validator(it.value)
                is Failure -> Future.pure(it)
            }
        }
    }

infix fun <A, E> (Validator<A, E>).or(
    validator: Validator<A, E>): Validator<A, E> = { a ->
        this(a).flatMap {
            when(it) {
                is Failure -> validator(a)
                is Success -> Future.pure(it)
            }
        }
    }

fun <A> validate(with: (A) -> Boolean): (A) -> A? = { it.takeIf(with) }

infix fun <A, E> ((A) -> A?).orElseFail(with: E): Validator<A, E> = { a ->
    asyncFuture { this(a)?.let(::Success) ?: Failure(with) }
}

object Validators {
    val Name: Validator<String, UserError> =
        validate<String>(with = { !it.isEmpty() && it.length <= 15 })
            .orElseFail(with = UserError.USERNAME_OUT_OF_BOUNDS)

    val Password: Validator<String, UserError> =
        validate<String>(with = { it.length >= 10 })
            .orElseFail(with = UserError.PASSWORD_TOO_SHORT)

    val Premium: Validator<User, UserError> =
        validate<User>(with = { it.premium })
            .orElseFail(with = UserError.MUST_BE_PREMIUM)

    val Newsletter: Validator<User, UserError> =
        validate<User>(with = { it.newsletter })
            .orElseFail(with = UserError.MUST_BE_PREMIUM)

    val Adult: Validator<LocalDate, UserError> =
        validate<LocalDate>(with = { birthDate ->
            val today = LocalDate.now()

            ChronoUnit.YEARS.between(birthDate, today) >= 18
        }).orElseFail(with = UserError.MUST_BE_ADULT)

    val Email: Validator<String, UserError> =
        validate<String>(with = {
            Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
                .matches(it)
        }).orElseFail(with = UserError.WRONG_EMAIL)
}