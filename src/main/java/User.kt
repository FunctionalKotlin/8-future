// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import Validators.Adult
import Validators.Email
import Validators.Name
import Validators.Newsletter
import Validators.Password
import Validators.Premium
import java.time.LocalDate

data class User(
    val name: String, val password: String,
    val premium: Boolean, val newsletter: Boolean,
    val birthDate: LocalDate, val email: String)

enum class UserError {
    USERNAME_OUT_OF_BOUNDS,
    PASSWORD_TOO_SHORT,
    MUST_BE_PREMIUM,
    MUST_ACCEPT_NEWSLETTER,
    MUST_BE_ADULT,
    WRONG_EMAIL
}

fun createUser(
    name: String, password: String, premium: Boolean,
    newsletter: Boolean, birthDate: LocalDate,
    email: String): AsyncResult<User, UserError> =
        Future.pure(Result.pure(::User.curried())) ap
            Name(name) ap
            Password(password) ap
            Future.pure(Result.pure(premium)) ap
            Future.pure(Result.pure(newsletter)) ap
            Adult(birthDate) ap
            Email(email) bind
            (Premium or Newsletter)