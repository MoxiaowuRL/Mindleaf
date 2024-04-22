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

    fun addUserQuote(quote: Quote): Boolean {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USER_QUOTE, quote.content)
            put(DatabaseHelper.COLUMN_USER_AUTHOR, quote.author)
        }
        val id = database.insert(DatabaseHelper.USER_QUOTES_TABLE_NAME, null, values)
        return id != -1L
    }

    @SuppressLint("Range")
    fun getRandomUserQuote(): Quote? {
        var quote: Quote? = null
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
        if (cursor.moveToFirst()) {
            val content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_QUOTE))
            val author = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_AUTHOR))
            quote = Quote(content, author)
        }
        cursor.close()
        return quote

    }
}