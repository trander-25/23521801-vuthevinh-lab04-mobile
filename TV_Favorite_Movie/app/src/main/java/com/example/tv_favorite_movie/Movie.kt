package com.example.tv_favorite_movie

import java.io.Serializable
import java.net.URI
import java.net.URISyntaxException

class Movie : Serializable {
    var id: Long = 0
    var title: String = ""
    var studio: String = ""
    var description: String = ""
    var cardImageUrl: String = ""

    fun getCardImageURI(): URI? {
        return try {
            URI(cardImageUrl)
        } catch (e: URISyntaxException) {
            null
        }
    }

    override fun toString(): String {
        return "Movie { id=$id, title=$title }"
    }

    companion object {
        private const val serialVersionUID = 7275661750759686531L
    }
}

