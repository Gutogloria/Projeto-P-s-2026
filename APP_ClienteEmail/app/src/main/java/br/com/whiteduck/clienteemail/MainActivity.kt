package br.com.whiteduck.clienteemail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.whiteduck.clienteemail.email.EmailConfig
import br.com.whiteduck.clienteemail.email.EmailMessage
import br.com.whiteduck.clienteemail.email.EmailReceiver
import br.com.whiteduck.clienteemail.email.EmailSender
import br.com.whiteduck.clienteemail.email.ReceivedEmail
import br.com.whiteduck.clienteemail.ui.theme.ClienteEmailTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClienteEmailTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Cliente Email") }) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Enviar", "Receber")

    Column(modifier = modifier.fillMaxSize()) {
        SecondaryTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (tabIndex) {
            0 -> SendTab()
            1 -> ReceiveTab()
        }
    }
}

@androidx.compose.runtime.Composable
fun SendTab(modifier: Modifier = Modifier) {
    var to by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = to,
            onValueChange = { to = it },
            label = { Text("Para") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Assunto") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Mensagem") },
            minLines = 5,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val config = EmailConfig(
                    host = "mail.whiteduck.com.br",
                    username = "utfpr@whiteduck.com.br",
                    password = "@TestePos!@#"
                )
                scope.launch {
                    isLoading = true
                    status = "Enviando..."
                    val result = EmailSender(config).send(
                        EmailMessage(to = to, subject = subject, body = body)
                    )
                    isLoading = false
                    status = if (result.isSuccess) "Email enviado!" else "Erro: ${result.exceptionOrNull()?.message}"
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator() else Text("Enviar")
        }
        if (status.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(text = status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@androidx.compose.runtime.Composable
fun ReceiveTab(modifier: Modifier = Modifier) {
    var emails by remember { mutableStateOf<List<ReceivedEmail>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = {
                val config = EmailConfig(
                    host = "mail.whiteduck.com.br",
                    port = 993,
                    username = "utfpr@whiteduck.com.br",
                    password = "@TestePos2026!@#"
                )
                scope.launch {
                    isLoading = true
                    status = "Buscando emails..."
                    val result = EmailReceiver(config).receive()
                    isLoading = false
                    result.fold(
                        onSuccess = {
                            emails = it
                            status = "${it.size} email(s) encontrado(s)"
                        },
                        onFailure = {
                            val msg = it.message ?: ""
                            status = if (msg.contains("authentication", true) ||
                                msg.contains("Authenticate", true) ||
                                msg.contains("login", true)
                            ) {
                                "Erro de autenticação. Verifique usuário/senha"
                            } else {
                                "Erro: $msg"
                            }
                        }
                    )
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator() else Text("Buscar emails")
        }
        Spacer(Modifier.height(8.dp))
        if (status.isNotEmpty()) {
            Text(text = status, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(emails) { email ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "De: ${email.from}", style = MaterialTheme.typography.labelMedium)
                        Text(text = email.subject, style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(4.dp))
                        Text(text = email.body.take(200), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
