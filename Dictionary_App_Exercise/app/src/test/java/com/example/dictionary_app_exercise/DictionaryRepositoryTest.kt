package com.example.dictionary_app_exercise

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DictionaryRepositoryTest {
    private val fakeDataSource = object : DictionaryDataSource {
        override fun getExactDefinition(word: String): String? {
            return if (word.equals("kotlin", ignoreCase = true)) {
                "a modern programming language"
            } else {
                null
            }
        }

        override fun searchWord(query: String): List<DictionaryEntry> {
            return if (query.contains("and", ignoreCase = true)) {
                listOf(DictionaryEntry("android", "mobile os"))
            } else {
                emptyList()
            }
        }
    }

    private val repository = DictionaryRepository(fakeDataSource)

    @Test
    fun lookup_returnsExactDefinition_whenWordExists() {
        val result = repository.lookup("kotlin")
        assertTrue(result is LookupResult.ExactDefinition)
        val exact = result as LookupResult.ExactDefinition
        assertEquals("kotlin", exact.word)
        assertEquals("a modern programming language", exact.definition)
    }

    @Test
    fun lookup_returnsSuggestions_whenExactMissing() {
        val result = repository.lookup("and")
        assertTrue(result is LookupResult.Suggestions)
        val suggestions = result as LookupResult.Suggestions
        assertEquals(1, suggestions.entries.size)
        assertEquals("android", suggestions.entries.first().word)
    }

    @Test
    fun lookup_returnsNotFound_whenNoMatches() {
        val result = repository.lookup("zzz")
        assertTrue(result is LookupResult.NotFound)
    }
}

