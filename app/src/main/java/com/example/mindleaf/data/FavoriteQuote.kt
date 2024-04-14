package com.example.mindleaf.data

data class FavoriteQuote(val quote: String)

object FavoriteQuotesRepository {
    val favoriteQuotes = mutableListOf<String>()

    fun addFavoriteQuote(quote: String) {
        favoriteQuotes.add(quote)
    }

    fun removeFavoriteQuote(quote: String) {
        favoriteQuotes.remove(quote)
    }

    fun isFavoriteQuote(quote: String): Boolean {
        return favoriteQuotes.contains(quote)
    }
}
