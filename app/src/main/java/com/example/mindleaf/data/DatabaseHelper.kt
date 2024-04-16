package com.example.mindleaf.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "favorite_quotes.db"
        private const val DATABASE_VERSION = 1
        internal const val TABLE_NAME = "favorite_quotes"
        private const val COLUMN_ID = "_id"
        internal const val COLUMN_QUOTE = "quote"
        const val COLUMN_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_QUOTE TEXT, $COLUMN_USER_ID TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
        if (oldVersion < 2) {
            // Add the user_id column to the existing table
            val alterTable = "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_USER_ID TEXT"
            db.execSQL(alterTable)
        }
    }
}