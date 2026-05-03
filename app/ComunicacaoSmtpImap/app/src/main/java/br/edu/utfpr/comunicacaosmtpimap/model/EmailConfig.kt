package br.edu.utfpr.comunicacaosmtpimap.model

data class EmailConfig(
    val email: String = "",
    val password: String = "",
    val smtpServer: String = "smtp.gmail.com",
    val smtpPort: String = "465",
    val imapServer: String = "imap.gmail.com",
    val imapPort: String = "993"
)
