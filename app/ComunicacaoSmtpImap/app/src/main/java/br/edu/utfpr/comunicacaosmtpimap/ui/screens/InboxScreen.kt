package br.edu.utfpr.comunicacaosmtpimap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.utfpr.comunicacaosmtpimap.viewmodel.EmailUiState
import br.edu.utfpr.comunicacaosmtpimap.viewmodel.EmailViewModel

@Composable
fun InboxScreen(viewModel: EmailViewModel) {
    val inbox by viewModel.inbox
    val uiState by viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.fetchInbox()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState is EmailUiState.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (uiState is EmailUiState.Error) {
            Text(
                text = (uiState as EmailUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(inbox) { email ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = email.from, style = MaterialTheme.typography.labelLarge)
                        Text(text = email.subject, style = MaterialTheme.typography.titleMedium)
                        Text(text = email.date, style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = email.body.take(100) + if (email.body.length > 100) "..." else "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
