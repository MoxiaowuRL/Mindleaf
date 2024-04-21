package com.example.mindleaf.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "quotes.db"
        private const val DATABASE_VERSION = 3

        // Favorite quotes table
        internal const val FAVORITE_QUOTES_TABLE_NAME = "favorite_quotes"
        private const val COLUMN_FAVORITE_ID = "_id"
        internal const val COLUMN_FAVORITE_QUOTE = "quote"
        const val COLUMN_USER_ID = "user_id"
        internal const val COLUMN_LIKES_COUNT = "likes_count"

        // User-posted quotes table
        internal const val USER_QUOTES_TABLE_NAME = "user_quotes"
        private const val COLUMN_USER_QUOTE_ID = "_id"
        internal const val COLUMN_USER_QUOTE = "quote"
        internal const val COLUMN_USER_AUTHOR = "author"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create favorite quotes table
        val createFavoriteQuotesTable = buildString {
            append("CREATE TABLE $FAVORITE_QUOTES_TABLE_NAME ($COLUMN_FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT, ")
            append("$COLUMN_FAVORITE_QUOTE TEXT, ")
            append("$COLUMN_USER_ID TEXT, ")
            append("$COLUMN_LIKES_COUNT INTEGER DEFAULT 0)")
        }
        db.execSQL(createFavoriteQuotesTable)

        // Create user-posted quotes table
        val createUserQuotesTable = buildString {
            append("CREATE TABLE $USER_QUOTES_TABLE_NAME ($COLUMN_USER_QUOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, ")
            append("$COLUMN_USER_QUOTE TEXT, ")
            append("$COLUMN_USER_AUTHOR TEXT)")
        }
        db.execSQL(createUserQuotesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
        if (oldVersion < 2) {
            // Add the likes_count column to the existing favorite_quotes table
            val alterFavoriteQuotesTable = "ALTER TABLE $FAVORITE_QUOTES_TABLE_NAME ADD COLUMN $COLUMN_LIKES_COUNT INTEGER DEFAULT 0"
            db.execSQL(alterFavoriteQuotesTable)
        }

        if (oldVersion < 3) {
            // Create the user_quotes table
            val createUserQuotesTable = buildString {
                append("CREATE TABLE $USER_QUOTES_TABLE_NAME ($COLUMN_USER_QUOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, ")
                append("$COLUMN_USER_QUOTE TEXT, ")
                append("$COLUMN_USER_AUTHOR TEXT)")
            }
            db.execSQL(createUserQuotesTable)
        }
    }
}