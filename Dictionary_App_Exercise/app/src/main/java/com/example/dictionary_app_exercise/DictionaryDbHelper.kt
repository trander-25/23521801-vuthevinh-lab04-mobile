package com.example.dictionary_app_exercise

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DictionaryDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    DictionaryDataSource {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_WORDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_WORD TEXT NOT NULL UNIQUE,
                $COLUMN_DEFINITION TEXT NOT NULL
            )
            """.trimIndent()
        )
        seedWords(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORDS")
        onCreate(db)
    }

    override fun getExactDefinition(word: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_DEFINITION FROM $TABLE_WORDS WHERE $COLUMN_WORD = ? COLLATE NOCASE",
            arrayOf(word)
        )
        return try {
            if (cursor.moveToFirst()) cursor.getString(0) else null
        } finally {
            cursor.close()
        }
    }

    override fun searchWord(query: String): List<DictionaryEntry> {
        val results = mutableListOf<DictionaryEntry>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_WORD, $COLUMN_DEFINITION FROM $TABLE_WORDS WHERE $COLUMN_WORD LIKE ? COLLATE NOCASE ORDER BY $COLUMN_WORD ASC",
            arrayOf("%$query%")
        )
        try {
            while (cursor.moveToNext()) {
                val word = cursor.getString(0)
                val definition = cursor.getString(1)
                results.add(DictionaryEntry(word, definition))
            }
        } finally {
            cursor.close()
        }
        return results
    }

    private fun seedWords(db: SQLiteDatabase) {
        val samples = listOf(
            "hello" to "a greeting used when meeting someone",
            "verisimilitude" to "the appearance of being true or real",
            "android" to "an open-source mobile operating system by Google",
            "kotlin" to "a modern programming language for the JVM and Android"
        )
        for ((word, definition) in samples) {
            val values = ContentValues().apply {
                put(COLUMN_WORD, word)
                put(COLUMN_DEFINITION, definition)
            }
            db.insert(TABLE_WORDS, null, values)
        }
    }

    companion object {
        private const val DATABASE_NAME = "DictionaryDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_WORDS = "words"
        private const val COLUMN_ID = "id"
        private const val COLUMN_WORD = "word"
        private const val COLUMN_DEFINITION = "definition"
    }
}
