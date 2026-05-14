package com.example.tv_favorite_movie

object MovieList {
    val favorites: List<Movie> = listOf(
        create(
            1,
            "Sherlock",
            "BBC One",
            "A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.",
            "https://image.tmdb.org/t/p/w500/7WTsnHkbA0FaG6R9twfFde0I9hl.jpg"
        ),
        create(
            2,
            "Inception",
            "Warner Bros.",
            "A thief who steals corporate secrets through dream-sharing technology takes on an impossible heist.",
            "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg"
        ),
        create(
            3,
            "Interstellar",
            "Paramount",
            "A team travels through a wormhole in space in an attempt to ensure humanity's survival.",
            "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"
        )
    )

    private fun create(
        id: Long,
        title: String,
        studio: String,
        description: String,
        imageUrl: String
    ): Movie {
        return Movie().apply {
            this.id = id
            this.title = title
            this.studio = studio
            this.description = description
            this.cardImageUrl = imageUrl
        }
    }
}

