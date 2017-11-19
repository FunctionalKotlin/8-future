// Copyright © FunctionalKotlin.com 2017. All rights reserved.

typealias Validator<T, E> = (T) -> Result<T, E>

infix operator fun <T, E> (Validator<T, E>)
    .plus(validator: Validator<T, E>): Validator<T, E> = {
        this(it).flatMap(validator)
    }

infix fun <A, E> (Validator<A, E>)
    .or(validator: Validator<A, E>): Validator<A, E> = {
        val result = this(it)

        when(result) {
            is Success -> result
            is Failure -> validator(it)
        }
    }

fun <A> validate(with: (A) -> Boolean): (A) -> A? = { it.takeIf(with) }

infix fun <A, E> ((A) -> A?).orElseFail(with: E): Validator<A, E> = { a ->
    this(a)?.let(::Success) ?: Failure(with)
}

fun <A, E> allOf(vararg validators: Validator<A, E>): Validator<A, E> =
    validators.fold(::Success) { acc, validator -> acc + validator }

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
}