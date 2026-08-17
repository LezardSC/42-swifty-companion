package com.fortytwo.swifty.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fortytwo.swifty.data.model.IntraUser
import com.fortytwo.swifty.ui.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    login: String,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(login) { viewModel.load(login) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(login) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> ErrorMessage(s, Modifier.align(Alignment.Center))
                is UiState.Success -> ProfileContent(s.data)
                is UiState.Idle -> Unit
            }
        }
    }
}

@Composable
private fun ErrorMessage(state: UiState.Error, modifier: Modifier = Modifier) {
    Text(
        text = state.message,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier.padding(24.dp)
    )
}

@Composable
private fun ProfileContent(user: IntraUser) {
    LazyColumn {
        item { ProfileHeader(user) }
    }
}

@Composable
private fun ProfileHeader(user: IntraUser) {
    Column {
        Spacer(Modifier.height(12.dp))
        Text(user.fullName, style = MaterialTheme.typography.titleLarge)
        Text("@${user.login}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        ProfileInfoRow("Email", user.email)
        ProfileInfoRow("Phone", user.phone ?: "hidden")
        ProfileInfoRow("Location", user.location ?: "not on campus")
        ProfileInfoRow("Wallet", "${user.wallet}₳")
        ProfileInfoRow("Evaluation points", "${user.correctionPoint}")
        ProfileInfoRow("Level", user.cursusUsers.lastOrNull()?.let { "%.2f".format(it.level) } ?: "-")
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}