// Copyright © FunctionalKotlin.com 2017. All rights reserved.

fun <A, B, C, D, E, F, G> ((A, B, C, D, E, F) -> G)
    .curried(): (A) -> (B) -> (C) -> (D) -> (E) -> (F) -> G =
    { a -> { b -> { c -> { d -> { e -> { f -> this(a, b, c, d, e, f) } } } } } }

fun <A, B, C, D, E, F> ((A, B, C, D, E) -> F)
    .curried(): (A) -> (B) -> (C) -> (D) -> (E) -> F =
    { a -> { b -> { c -> { d -> { e -> this(a, b, c, d, e) } } } } }

fun <A, B, C, D, E> ((A, B, C, D) -> E)
    .curried(): (A) -> (B) -> (C) -> (D) -> E =
    { a -> { b -> { c -> { d -> this(a, b, c, d) } } } }

fun <A, B, C, D> ((A, B, C) -> D)
    .curried(): (A) -> (B) -> (C) -> D =
    { a -> { b -> { c -> this(a, b, c) } } }

fun <A, B, C> ((A, B) -> C)
    .curried(): (A) -> (B) -> C =
    { a -> { b -> this(a, b) } }