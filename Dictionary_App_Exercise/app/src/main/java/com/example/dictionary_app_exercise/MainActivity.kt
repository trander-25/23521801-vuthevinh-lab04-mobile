package com.example.dictionary_app_exercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dictionary_app_exercise.ui.theme.Dictionary_App_ExerciseTheme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DictionaryDbHelper
    private lateinit var repository: DictionaryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DictionaryDbHelper(this)
        repository = DictionaryRepository(dbHelper)
        enableEdgeToEdge()
        setContent {
            Dictionary_App_ExerciseTheme {
                DictionaryScreen { query -> repository.lookup(query) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}

@Composable
fun DictionaryScreen(lookup: (String) -> LookupResult) {
    var query by rememberSaveable { mutableStateOf("") }
    var result by remember { mutableStateOf<LookupResult?>(null) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    val enterWordMessage = stringResource(R.string.message_enter_word)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.dictionary_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(stringResource(R.string.search_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                val trimmed = query.trim()
                if (trimmed.isEmpty()) {
                    statusMessage = enterWordMessage
                    result = null
                } else {
                    statusMessage = null
                    result = lookup(trimmed)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.lookup_button))
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (statusMessage != null) {
            Text(
                text = statusMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        when (val lookupResult = result) {
            is LookupResult.ExactDefinition -> {
                Text(
                    text = stringResource(R.string.definition_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = lookupResult.definition)
            }
            is LookupResult.Suggestions -> {
                Text(
                    text = stringResource(R.string.suggestions_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyColumn {
                    items(lookupResult.entries) { entry ->
                        Text(text = entry.word)
                    }
                }
            }
            LookupResult.NotFound -> {
                Text(text = stringResource(R.string.message_no_matches))
            }
            null -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DictionaryScreenPreview() {
    Dictionary_App_ExerciseTheme {
        DictionaryScreen { query ->
            LookupResult.ExactDefinition(query, "Sample definition")
        }
    }
}