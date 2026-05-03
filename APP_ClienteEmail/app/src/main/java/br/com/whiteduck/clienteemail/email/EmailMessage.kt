package br.com.whiteduck.clienteemail.email

data class EmailMessage(
    val to: String,
    val subject: String,
    val body: String
)
