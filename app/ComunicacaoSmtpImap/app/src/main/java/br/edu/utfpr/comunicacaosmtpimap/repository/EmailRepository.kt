package br.edu.utfpr.comunicacaosmtpimap.repository

import br.edu.utfpr.comunicacaosmtpimap.model.EmailConfig
import br.edu.utfpr.comunicacaosmtpimap.model.EmailMessage
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.search.FlagTerm

class EmailRepository {

    fun sendEmail(config: EmailConfig, message: EmailMessage) {
        // 1- Configurar Propriedades
        val props = Properties().apply {
            put("mail.smtp.host", config.smtpServer)
            put("mail.smtp.socketFactory.port", config.smtpPort)
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", config.smtpPort)
        }

        // 2- Autenticação
        val auth = object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(config.email, config.password)
            }
        }

        // 3- Sessão
        val session = Session.getDefaultInstance(props, auth)

        try {
            // 4- Mensagem
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress(config.email))
                addRecipient(Message.RecipientType.TO, InternetAddress(message.to))
                subject = message.subject
                setText(message.body)
            }

            // 5- Transporte
            Transport.send(mimeMessage)
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw e
        }
    }

    fun fetchEmails(config: EmailConfig): List<EmailMessage> {
        val props = Properties().apply {
            put("mail.imap.host", config.imapServer)
            put("mail.imap.port", config.imapPort)
            put("mail.store.protocol", "imaps")
        }

        val session = Session.getInstance(props)
        val store = session.getStore("imaps")
        store.connect(config.imapServer, config.email, config.password)

        val inbox = store.getFolder("INBOX")
        inbox.open(Folder.READ_ONLY)

        val messages = inbox.messages
        val emailList = mutableListOf<EmailMessage>()

        // Get last 10 messages for simplicity
        val start = if (messages.size > 10) messages.size - 10 else 0
        for (i in messages.size - 1 downTo start) {
            val msg = messages[i]
            emailList.add(
                EmailMessage(
                    id = msg.messageNumber.toString(),
                    from = msg.from[0].toString(),
                    subject = msg.subject ?: "(No Subject)",
                    body = msg.content.toString(),
                    date = msg.sentDate.toString()
                )
            )
        }

        inbox.close(false)
        store.close()

        return emailList
    }
}
