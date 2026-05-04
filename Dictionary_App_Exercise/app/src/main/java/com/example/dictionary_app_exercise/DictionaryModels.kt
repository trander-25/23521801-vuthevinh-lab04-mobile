package com.example.dictionary_app_exercise

data class DictionaryEntry(
    val word: String,
    val definition: String
)

sealed class LookupResult {
    data class ExactDefinition(
        val word: String,
        val definition: String
    ) : LookupResult()

    data class Suggestions(
        val entries: List<DictionaryEntry>
    ) : LookupResult()

    object NotFound : LookupResult()
}

