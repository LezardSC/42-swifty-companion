package com.fortytwo.swifty.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fortytwo.swifty.data.model.IntraUser
import com.fortytwo.swifty.ui.common.UiState

@Composable
fun SearchScreen(
    onUserFound: (String) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is UiState.Success) {
            onUserFound((state as UiState.Success<IntraUser>).data.login)
            viewModel.resetError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Swifty Companion", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Login") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.search(query) },
            enabled = state !is UiState.Loading
        ) {
            Text("Search")
        }
        when (val s = state) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            else -> Unit
        }
    }
}
