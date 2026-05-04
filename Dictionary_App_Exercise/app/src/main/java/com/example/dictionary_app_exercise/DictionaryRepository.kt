package com.example.dictionary_app_exercise

interface DictionaryDataSource {
    fun getExactDefinition(word: String): String?
    fun searchWord(query: String): List<DictionaryEntry>
}

class DictionaryRepository(private val dataSource: DictionaryDataSource) {
    fun lookup(query: String): LookupResult {
        val exactDefinition = dataSource.getExactDefinition(query)
        if (exactDefinition != null) {
            return LookupResult.ExactDefinition(query, exactDefinition)
        }

        val suggestions = dataSource.searchWord(query)
        return if (suggestions.isNotEmpty()) {
            LookupResult.Suggestions(suggestions)
        } else {
            LookupResult.NotFound
        }
    }
}
