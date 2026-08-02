package com.example.projetodirigido.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.content.ComponentName

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
    fun openLauncherApp(
        context: Context,
        componentName: ComponentName
    ) {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            component = componentName

            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        }

        runCatching {
            context.startActivity(intent)
        }.onFailure {
            Toast.makeText(
                context,
                "Não foi possível abrir este aplicativo.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Abre a página de um app na Google Play, para instalar (ou abrir, se já
     * estiver instalado). Tenta primeiro abrir direto pelo app da Play Store
     * (link "market://"), que já cai na tela de instalação com um toque; se
     * a Play Store não puder abrir esse link (ex: não instalada), cai para
     * o link "https://" equivalente, que abre no navegador.
     *
     * @param playStorePackage Package name do app na Play Store, ex: "com.whatsapp".
     */
    fun openPlayStore(context: Context, playStorePackage: String) {
        try {
            val marketIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$playStorePackage")
            )
            context.startActivity(marketIntent)
        } catch (e: Exception) {
            openUrl(
                context,
                "https://play.google.com/store/apps/details?id=$playStorePackage"
            )
        }
    }

    /**
     * Abre o Google já com a pesquisa pronta, a partir de um texto digitado
     * pelo usuário (usado na caixa "Digite aqui sua dúvida" do tutorial
     * "Como pesquisar algo no Google"). Abre no navegador, que já mostra os
     * resultados da busca direto, sem precisar digitar de novo.
     */
    fun openGoogleSearch(context: Context, query: String) {
        val encodedQuery = Uri.encode(query.trim())
        openUrl(context, "https://www.google.com/search?q=$encodedQuery")
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
     * @param phoneWithDdi Número informado pelo usuário (pode estar com ou sem 55).
     */
    fun openWhatsAppChat(context: Context, phoneWithDdi: String, prefilledMessage: String) {
        var cleanPhone = phoneWithDdi.filter { it.isDigit() }

        // Remove o '0' do início se o usuário digitou (ex: 011... -> 11...)
        if (cleanPhone.startsWith("0")) {
            cleanPhone = cleanPhone.substring(1)
        }

        // Se o número tem 10 ou 11 dígitos (DDD + número), assume que é Brasil
        // e adiciona o DDI 55 automaticamente para o WhatsApp não confundir
        // com outros países (ex: +1 dos EUA).
        if ((cleanPhone.length == 10 || cleanPhone.length == 11) && !cleanPhone.startsWith("55")) {
            cleanPhone = "55$cleanPhone"
        }

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