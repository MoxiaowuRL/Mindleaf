package com.example.mindleaf.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

object FavoriteQuotesRepository {
    private lateinit var databaseHelper: DatabaseHelper

    fun initialize(context: Context) {
        databaseHelper = DatabaseHelper(context)
    }

    fun addFavoriteQuote(quote: Quote, userId: String) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_FAVORITE_QUOTE, quote.content)
            put(DatabaseHelper.COLUMN_FAVORITE_AUTHOR, quote.author)
            put(DatabaseHelper.COLUMN_USER_ID, userId)
            put(DatabaseHelper.COLUMN_LIKES_COUNT, quote.likesCount)
        }
        db.insert(DatabaseHelper.FAVORITE_QUOTES_TABLE_NAME, null, values)
        incrementQuoteLikes(quote.content, db)
        db.close()
    }

    private fun incrementQuoteLikes(quote: String, db: SQLiteDatabase) {
        val updateLikes = "UPDATE ${DatabaseHelper.FAVORITE_QUOTES_TABLE_NAME} SET ${DatabaseHelper.COLUMN_LIKES_COUNT} = ${DatabaseHelper.COLUMN_LIKES_COUNT} + 1 WHERE ${DatabaseHelper.COLUMN_FAVORITE_QUOTE} = ?"
        db.execSQL(updateLikes, arrayOf(quote))
    }

    fun removeFavoriteQuote(quote: String,author: String, userId: String) {
        val db = databaseHelper.writableDatabase
        db.delete(
            DatabaseHelper.FAVORITE_QUOTES_TABLE_NAME,
            "${DatabaseHelper.COLUMN_FAVORITE_QUOTE} = ? AND ${DatabaseHelper.COLUMN_FAVORITE_AUTHOR} = ? AND ${DatabaseHelper.COLUMN_USER_ID} = ?",
            arrayOf(quote, author, userId)
        )
        db.close()
    }

    @SuppressLint("Range")
    fun getTopLikedQuotes(): List<Quote> {
        val topQuotes = mutableListOf<Quote>()
        val db = databaseHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.FAVORITE_QUOTES_TABLE_NAME,
            arrayOf(
                DatabaseHelper.COLUMN_FAVORITE_QUOTE,
                DatabaseHelper.COLUMN_FAVORITE_AUTHOR,
                "MAX(${DatabaseHelper.COLUMN_LIKES_COUNT}) AS ${DatabaseHelper.COLUMN_LIKES_COUNT}"
            ),
            null,
            null,
            DatabaseHelper.COLUMN_FAVORITE_QUOTE,
            null,
            "${DatabaseHelper.COLUMN_LIKES_COUNT} DESC",
            "20"
        )
        cursor?.let {
            while (it.moveToNext()) {
                val content = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_FAVORITE_QUOTE))
                val author = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_FAVORITE_AUTHOR))
                val likesCount = it.getInt(it.getColumnIndex(DatabaseHelper.COLUMN_LIKES_COUNT))
                topQuotes.add(Quote(content, author, likesCount))
            }
            it.close()
        }
        db.close()
        return topQuotes
    }

    @SuppressLint("Range")
    fun getFavoriteQuotes(userId: String): List<Quote> {
        val favoriteQuotes = mutableListOf<Quote>()
        val db = databaseHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.FAVORITE_QUOTES_TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_FAVORITE_QUOTE, DatabaseHelper.COLUMN_FAVORITE_AUTHOR, DatabaseHelper.COLUMN_LIKES_COUNT),
            "${DatabaseHelper.COLUMN_USER_ID} = ?",
            arrayOf(userId),
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val content = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_FAVORITE_QUOTE))
                val author = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_FAVORITE_AUTHOR))
                val likesCount = it.getInt(it.getColumnIndex(DatabaseHelper.COLUMN_LIKES_COUNT))
                favoriteQuotes.add(Quote(content, author, likesCount))
            }
            it.close()
        }
        db.close()
        return favoriteQuotes
    }

    fun isFavoriteQuote(quote: String, userId: String): Boolean {
        val db = databaseHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.FAVORITE_QUOTES_TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_FAVORITE_QUOTE),
            "${DatabaseHelper.COLUMN_FAVORITE_QUOTE} = ? AND ${DatabaseHelper.COLUMN_USER_ID} = ?",
            arrayOf(quote, userId),
            null,
            null,
            null
        )
        val isFavorite = cursor.count > 0
        cursor.close()
        db.close()
        return isFavorite
    }
}