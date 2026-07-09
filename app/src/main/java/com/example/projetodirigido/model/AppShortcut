package com.example.projetodirigido.model

/**
 * Representa um atalho exibido na tela inicial (ex: WhatsApp, Gmail, etc).
 *
 * @param title Nome exibido para o usuário (já simplificado/renomeado).
 * @param subtitle Descrição curta do que o atalho faz.
 * @param emojiIcon Ícone simples (emoji) usado enquanto não há ícones vetoriais definidos.
 * @param packageName Pacote do app real no Android (usado para abrir via Intent).
 * @param fallbackUrl Link usado caso o app não esteja instalado (abre no navegador).
 */
data class AppShortcut(
    val title: String,
    val subtitle: String,
    val emojiIcon: String,
    val packageName: String,
    val fallbackUrl: String
)

/**
 * Lista fixa de atalhos da tela inicial, baseada no protótipo.
 * Em uma versão futura, isso pode vir de um Room/DataStore para permitir customização.
 */
object DefaultShortcuts {
    val list = listOf(
        AppShortcut(
            title = "WhatsApp",
            subtitle = "Conversar com a família",
            emojiIcon = "💬",
            packageName = "com.whatsapp",
            fallbackUrl = "https://web.whatsapp.com"
        ),
        AppShortcut(
            title = "YouTube",
            subtitle = "Assistir vídeos",
            emojiIcon = "▶️",
            packageName = "com.google.android.youtube",
            fallbackUrl = "https://www.youtube.com"
        ),
        AppShortcut(
            title = "Gmail",
            subtitle = "Ver seus e-mails",
            emojiIcon = "✉️",
            packageName = "com.google.android.gm",
            fallbackUrl = "https://mail.google.com"
        ),
        AppShortcut(
            title = "Google",
            subtitle = "Pesquisar na internet",
            emojiIcon = "🔍",
            packageName = "com.google.android.googlequicksearchbox",
            fallbackUrl = "https://www.google.com"
        ),
        AppShortcut(
            title = "Banco do Brasil",
            subtitle = "Acessar sua conta",
            emojiIcon = "🏦",
            packageName = "br.com.bb.android",
            fallbackUrl = "https://www.bb.com.br"
        ),
        AppShortcut(
            title = "Drogasil",
            subtitle = "Comprar remédios",
            emojiIcon = "💊",
            packageName = "com.raiadrogasil.drogasil",
            fallbackUrl = "https://www.drogasil.com.br"
        ),
        AppShortcut(
            title = "Google Meet",
            subtitle = "Fazer videochamada",
            emojiIcon = "📹",
            packageName = "com.google.android.apps.tachyon",
            fallbackUrl = "https://meet.google.com"
        ),
        AppShortcut(
            title = "Previsão do tempo",
            subtitle = "Ver o clima de hoje",
            emojiIcon = "☁️",
            packageName = "com.google.android.googlequicksearchbox",
            fallbackUrl = "https://www.google.com/search?q=previsao+do+tempo"
        )
    )
}
