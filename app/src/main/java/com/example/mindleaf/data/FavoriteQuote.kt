package com.example.mindleaf.data

data class FavoriteQuote(val quote: String)

object FavoriteQuotesRepository {
    val favoriteQuotes = mutableListOf<FavoriteQuote>()

    fun addFavoriteQuote(quote: String) {
        favoriteQuotes.add(FavoriteQuote(quote))
    }

    fun removeFavoriteQuote(favoriteQuote: FavoriteQuote) {
        favoriteQuotes.remove(favoriteQuote)
    }
    fun isFavoriteQuote(quote: String): Boolean {
        return favoriteQuotes.any { it.quote == quote }
    }
}
