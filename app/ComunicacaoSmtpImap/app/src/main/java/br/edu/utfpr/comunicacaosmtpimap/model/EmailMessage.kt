package br.edu.utfpr.comunicacaosmtpimap.model

data class EmailMessage(
    val id: String = "",
    val from: String = "",
    val to: String = "",
    val subject: String = "",
    val body: String = "",
    val date: String = ""
)
