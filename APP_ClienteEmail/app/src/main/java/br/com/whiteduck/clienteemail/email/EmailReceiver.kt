package br.com.whiteduck.clienteemail.email

import jakarta.mail.Folder
import jakarta.mail.Message
import jakarta.mail.Multipart
import jakarta.mail.Part
import jakarta.mail.Session
import jakarta.mail.Store
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties

data class ReceivedEmail(
    val from: String,
    val subject: String,
    val body: String
)

class EmailReceiver(private val config: EmailConfig) {

    suspend fun receive(folderName: String = "INBOX", maxMessages: Int = 10): Result<List<ReceivedEmail>> =
        withContext(Dispatchers.IO) {
            try {
                val props = Properties().apply {
                    put("mail.store.protocol", "imap")
                    put("mail.imap.host", config.host)
                    put("mail.imap.port", config.port.toString())
                    put("mail.imap.auth", "true")
                    if (config.port == 993) {
                        put("mail.imap.ssl.enable", "true")
                    } else {
                        put("mail.imap.starttls.enable", "true")
                    }
                    put("mail.imap.connectiontimeout", "15000")
                    put("mail.imap.timeout", "15000")
                }

                val session = Session.getInstance(props)
                val store: Store = session.getStore("imap")
                store.connect(config.host, config.username, config.password)

                val folder: Folder = store.getFolder(folderName)
                folder.open(Folder.READ_ONLY)

                val messageCount = folder.messageCount
                val start = maxOf(1, messageCount - maxMessages + 1)
                val messages = folder.getMessages(start, messageCount)

                val emails = messages.map { msg -> toReceivedEmail(msg) }

                folder.close(false)
                store.close()

                Result.success(emails)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun toReceivedEmail(msg: Message): ReceivedEmail {
        val mime = msg as MimeMessage
        return ReceivedEmail(
            from = (mime.from.firstOrNull() as? InternetAddress)?.address ?: "",
            subject = mime.subject ?: "(sem assunto)",
            body = getText(mime)
        )
    }

    private fun getText(part: Part): String {
        return when {
            part.isMimeType("text/plain") -> part.content.toString()
            part.isMimeType("text/html") -> part.content.toString()
            part.isMimeType("multipart/*") -> {
                val multipart = part.content as Multipart
                (0 until multipart.count)
                    .mapNotNull { i ->
                        val bodyPart = multipart.getBodyPart(i)
                        if (bodyPart.isMimeType("text/plain")) bodyPart.content.toString()
                        else null
                    }
                    .firstOrNull()
                    ?: (0 until multipart.count)
                        .map { getText(multipart.getBodyPart(it)) }
                        .firstOrNull()
                    ?: "(conteúdo não extraído)"
            }
            else -> "(conteúdo não textual: ${part.contentType})"
        }
    }
}
