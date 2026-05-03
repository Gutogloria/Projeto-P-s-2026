package br.edu.utfpr.comunicacaosmtpimap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import br.edu.utfpr.comunicacaosmtpimap.viewmodel.EmailViewModel

@Composable
fun LoginScreen(viewModel: EmailViewModel, onLoginSuccess: () -> Unit) {
    val config by viewModel.config
    var email by remember { mutableStateOf(config.email) }
    var password by remember { mutableStateOf(config.password) }
    var smtpServer by remember { mutableStateOf(config.smtpServer) }
    var smtpPort by remember { mutableStateOf(config.smtpPort) }
    var imapServer by remember { mutableStateOf(config.imapServer) }
    var imapPort by remember { mutableStateOf(config.imapPort) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Configuração de E-mail", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha (ou App Password)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = smtpServer,
                onValueChange = { smtpServer = it },
                label = { Text("Servidor SMTP") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = smtpPort,
                onValueChange = { smtpPort = it },
                label = { Text("Porta SMTP") },
                modifier = Modifier.width(100.dp)
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = imapServer,
                onValueChange = { imapServer = it },
                label = { Text("Servidor IMAP") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = imapPort,
                onValueChange = { imapPort = it },
                label = { Text("Porta IMAP") },
                modifier = Modifier.width(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateConfig(
                    config.copy(
                        email = email,
                        password = password,
                        smtpServer = smtpServer,
                        smtpPort = smtpPort,
                        imapServer = imapServer,
                        imapPort = imapPort
                    )
                )
                onLoginSuccess()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}
