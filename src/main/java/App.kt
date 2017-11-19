// Copyright © FunctionalKotlin.com 2017. All rights reserved.

import com.beust.klaxon.JsonObject
import java.net.URL

fun parseURL(url: String): JsonObject = URL(url)
    .readText()
    .let(::parse)

data class Post(val title: String, val content: String, val authorId: Int)

val Post.wordCount: Int
    get() = content.split(" ").size

data class Author(val firstName: String, val lastName: String, val lastPostId: Int)

val Author.lastPost: Future<Post>
    get() = getPost(lastPostId)

fun JsonObject.toPost(): Post {
    val title = this["title"] as String
    val content = this["content"] as String
    val authorId = this["userId"] as Int

    return Post(title, content, authorId)
}

fun JsonObject.toAuthor(): Author {
    val firstName = this["firstName"] as String
    val lastName = this["lastName"] as String
    val lastPostId = this["lastPost"] as Int

    return Author(firstName, lastName, lastPostId)
}

fun getPost(id: Int): Future<Post> = asyncFuture {
    parseURL("http://functionalhub.com/exercises/posts/$id").toPost()
}

fun getAuthor(id: Int): Future<Author> = asyncFuture {
    parseURL("http://functionalhub.com/exercises/users/$id").toAuthor()
}

fun topFive(): Future<List<Int>> = asyncFuture {
    parseURL("http://functionalhub.com/exercises/top-users")
        .map { it.value as String }
        .map(String::toInt)
}

fun average(first: Int, second: Int, third: Int, fourth: Int, fifth: Int): Int =
    (first + second + third + fourth + fifth) / 5

fun main(args: Array<String>) {
    val lastPostWordCount: (Int) -> Future<Int> = {
        getAuthor(it).flatMap { it.lastPost }.map { it.wordCount }
    }

    getPost(1)
        .flatMap { post ->
            getAuthor(post.authorId)
        }
        .runAsync { user ->
            println(user) // User(firstname: "Megan", lastName: "Sanchez")
        }

    val average = topFive().flatMap { topFive ->
        ::average.curried() map
            lastPostWordCount(topFive[0]) ap
            lastPostWordCount(topFive[1]) ap
            lastPostWordCount(topFive[2]) ap
            lastPostWordCount(topFive[3]) ap
            lastPostWordCount(topFive[4])
    }.runSync()

    println(average)

    Thread.sleep(2000)
}