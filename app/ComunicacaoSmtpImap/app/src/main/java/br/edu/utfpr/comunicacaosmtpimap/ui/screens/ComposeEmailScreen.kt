package br.edu.utfpr.comunicacaosmtpimap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.utfpr.comunicacaosmtpimap.viewmodel.EmailUiState
import br.edu.utfpr.comunicacaosmtpimap.viewmodel.EmailViewModel

@Composable
fun ComposeEmailScreen(viewModel: EmailViewModel) {
    var to by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    val uiState by viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Nova Mensagem", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = to,
            onValueChange = { to = it },
            label = { Text("Para") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Assunto") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Mensagem") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState is EmailUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            Button(
                onClick = { viewModel.sendEmail(to, subject, body) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }

        when (val state = uiState) {
            is EmailUiState.Success -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.primary)
            }
            is EmailUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}
