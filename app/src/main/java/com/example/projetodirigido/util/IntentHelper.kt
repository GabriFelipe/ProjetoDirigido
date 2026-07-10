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

    /**
     * Liga diretamente para o número (1 clique, sem passar pelo discador),
     * usada quando o app já tem a permissão CALL_PHONE concedida.
     */
    fun call(context: Context, phoneNumber: String) {
        try {
            val cleanNumber = phoneNumber.filter { it.isDigit() }
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$cleanNumber"))
            context.startActivity(intent)
        } catch (e: Exception) {
            // Sem permissão (ou outro erro) -> cai para o discador, que não
            // exige permissão e ainda deixa o número pronto, faltando só
            // tocar em ligar.
            dial(context, phoneNumber)
        }
    }

    /**
     * Abre o discador com o número já preenchido. Não requer permissão,
     * mas exige mais um toque do usuário para confirmar a ligação.
     */
    fun dial(context: Context, phoneNumber: String) {
        try {
            val cleanNumber = phoneNumber.filter { it.isDigit() }
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanNumber"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Não foi possível abrir o discador.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}