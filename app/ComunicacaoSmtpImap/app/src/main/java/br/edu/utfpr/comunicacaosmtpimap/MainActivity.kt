package br.edu.utfpr.comunicacaosmtpimap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.edu.utfpr.comunicacaosmtpimap.ui.EmailApp
import br.edu.utfpr.comunicacaosmtpimap.ui.theme.ComunicacaoSmtpImapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComunicacaoSmtpImapTheme {
                EmailApp()
            }
        }
    }
}
