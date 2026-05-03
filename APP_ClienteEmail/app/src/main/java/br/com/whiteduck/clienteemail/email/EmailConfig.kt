package br.com.whiteduck.clienteemail.email

data class EmailConfig(
    val host: String,
    val port: Int = 587,
    val username: String,
    val password: String,
    val useTls: Boolean = true
)
