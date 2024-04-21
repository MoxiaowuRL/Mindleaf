package com.example.mindleaf.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class UserQuotesRepository private constructor(context: Context) {
    private val database: SQLiteDatabase = DatabaseHelper(context).writableDatabase

    companion object {
        private var instance: UserQuotesRepository? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = UserQuotesRepository(context)
            }
        }

        fun get(): UserQuotesRepository {
            return instance ?: throw IllegalStateException("UserQuotesRepository must be initialized")
        }
    }

    fun addUserQuote(quote: Quote) {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USER_QUOTE, quote.content)
            put(DatabaseHelper.COLUMN_USER_AUTHOR, quote.author)
        }
        database.insert(DatabaseHelper.USER_QUOTES_TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun getRandomUserQuote(): Quote? {
        val cursor = database.query(
            DatabaseHelper.USER_QUOTES_TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "RANDOM()",
            "1"
        )
        return if (cursor.moveToFirst()) {
            val content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_QUOTE))
            val author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_AUTHOR))
            Quote(content, author)
        } else {
            null
        }
    }
}