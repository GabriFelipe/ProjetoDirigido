package com.example.projetodirigido.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Centraliza a lógica de abrir um app pelo package name.
 * Se o app não estiver instalado, abre o link equivalente no navegador
 * em vez de travar ou mostrar um erro técnico ao usuário.
 */
object IntentHelper {

    fun openApp(context: Context, packageName: String, fallbackUrl: String) {
        try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)

            if (launchIntent != null) {
                context.startActivity(launchIntent)
            } else {
                openUrl(context, fallbackUrl)
            }
        } catch (e: Exception) {
            // Se algo impedir a checagem/abertura do app (ex: restrição de
            // visibilidade de pacotes do Android 11+), cai para o navegador
            // em vez de travar o app.
            openUrl(context, fallbackUrl)
        }
    }

    fun openUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Não foi possível abrir. Verifique sua conexão com a internet.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Fluxo "1 clique WhatsApp": abre a conversa direto com o número informado
     * (com DDI) e uma mensagem pré-preenchida, sem precisar salvar o contato antes.
     *
     * @param phoneWithDdi Número completo, ex: "5511999999999" (sem espaços, +, ou traços).
     */
    fun openWhatsAppChat(context: Context, phoneWithDdi: String, prefilledMessage: String) {
        val cleanPhone = phoneWithDdi.filter { it.isDigit() }
        val encodedMessage = Uri.encode(prefilledMessage)
        val url = "https://wa.me/$cleanPhone?text=$encodedMessage"
        openUrl(context, url)
    }
}