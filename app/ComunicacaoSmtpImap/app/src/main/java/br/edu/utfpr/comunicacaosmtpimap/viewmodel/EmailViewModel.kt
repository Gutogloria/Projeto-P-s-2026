package br.edu.utfpr.comunicacaosmtpimap.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.comunicacaosmtpimap.model.EmailConfig
import br.edu.utfpr.comunicacaosmtpimap.model.EmailMessage
import br.edu.utfpr.comunicacaosmtpimap.repository.EmailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class EmailUiState {
    object Idle : EmailUiState()
    object Loading : EmailUiState()
    data class Success(val message: String) : EmailUiState()
    data class Error(val message: String) : EmailUiState()
}

class EmailViewModel : ViewModel() {
    private val repository = EmailRepository()

    private val _config = mutableStateOf(EmailConfig())
    val config: State<EmailConfig> = _config

    private val _uiState = mutableStateOf<EmailUiState>(EmailUiState.Idle)
    val uiState: State<EmailUiState> = _uiState

    private val _inbox = mutableStateOf<List<EmailMessage>>(emptyList())
    val inbox: State<List<EmailMessage>> = _inbox

    fun updateConfig(newConfig: EmailConfig) {
        _config.value = newConfig
    }

    fun sendEmail(to: String, subject: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = EmailUiState.Loading
            try {
                val message = EmailMessage(to = to, subject = subject, body = body)
                repository.sendEmail(_config.value, message)
                _uiState.value = EmailUiState.Success("E-mail enviado com sucesso!")
            } catch (e: Exception) {
                _uiState.value = EmailUiState.Error("Falha ao enviar e-mail: ${e.message}")
            }
        }
    }

    fun fetchInbox() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = EmailUiState.Loading
            try {
                val emails = repository.fetchEmails(_config.value)
                _inbox.value = emails
                _uiState.value = EmailUiState.Idle
            } catch (e: Exception) {
                _uiState.value = EmailUiState.Error("Falha ao buscar e-mails: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = EmailUiState.Idle
    }
}
