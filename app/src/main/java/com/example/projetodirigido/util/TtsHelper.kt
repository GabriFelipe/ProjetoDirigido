package com.example.projetodirigido.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Encapsula o TextToSpeech do Android. Deve ser criado uma vez (ex: dentro de
 * um DisposableEffect na tela) e ter shutdown() chamado quando a tela sair de tela.
 */
class TtsHelper(context: Context) {

    private var isReady = false
    private val tts: TextToSpeech = TextToSpeech(context) { status ->
        isReady = status == TextToSpeech.SUCCESS
    }.also {
        it.language = Locale("pt", "BR")
    }

    fun speak(text: String) {
        if (isReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "assistente_digital_tts")
        }
    }

    /** Interrompe qualquer fala em andamento, sem liberar o TextToSpeech (ele continua utilizável depois). */
    fun stop() {
        if (isReady) {
            tts.stop()
        }
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}