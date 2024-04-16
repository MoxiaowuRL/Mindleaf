package com.example.mindleaf.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context

object FavoriteQuotesRepository {
    private lateinit var databaseHelper: DatabaseHelper

    fun initialize(context: Context) {
        databaseHelper = DatabaseHelper(context)
    }

    fun addFavoriteQuote(quote: String, userId: String) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_QUOTE, quote)
            put(DatabaseHelper.COLUMN_USER_ID, userId)
        }
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    fun removeFavoriteQuote(quote: String, userId: String) {
        val db = databaseHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_NAME, "${DatabaseHelper.COLUMN_QUOTE} = ?AND ${DatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(quote))
        db.close()
    }

    @SuppressLint("Range")
    fun getFavoriteQuotes(userId: String): List<String> {
        val favoriteQuotes = mutableListOf<String>()
        val db = databaseHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_QUOTE),
            "${DatabaseHelper.COLUMN_USER_ID} = ?",
            arrayOf(userId),
            null,
            null,
            null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val quote = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUOTE))
            favoriteQuotes.add(quote)
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return favoriteQuotes
    }

    fun isFavoriteQuote(quote: String, userId: String): Boolean {
        val db = databaseHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_QUOTE),
            "${DatabaseHelper.COLUMN_QUOTE} = ? AND ${DatabaseHelper.COLUMN_USER_ID} = ?",
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