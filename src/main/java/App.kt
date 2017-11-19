// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import com.beust.klaxon.JsonObject
import java.net.URL

fun parseURL(url: String): JsonObject = URL(url)
    .readText()
    .let(::parse)

data class Post(val title: String, val content: String, val authorId: Int)

data class Author(val firstName: String, val lastName: String)

fun JsonObject.toPost(): Post {
    val title = this["title"] as String
    val content = this["content"] as String
    val authorId = this["userId"] as Int

    return Post(title, content, authorId)
}

fun JsonObject.toAuthor(): Author {
    val firstName = this["firstName"] as String
    val lastName = this["lastName"] as String

    return Author(firstName, lastName)
}

fun getPost(id: Int): Future<Post> = asyncFuture {
    parseURL("http://functionalhub.com/exercises/posts/$id").toPost()
}

fun getAuthor(id: Int): Future<Author> = asyncFuture {
    parseURL("http://functionalhub.com/exercises/users/$id").toAuthor()
}

fun main(args: Array<String>) {
    getPost(1)
        .flatMap { post ->
            getAuthor(post.authorId)
        }
        .runAsync { user ->
            println(user) // User(firstname: "Megan", lastName: "Sanchez")
        }

    Thread.sleep(2000)
}