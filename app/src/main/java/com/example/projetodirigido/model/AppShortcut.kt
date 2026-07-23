package com.example.projetodirigido.model

/**
 * Representa um atalho exibido na tela inicial (ex: WhatsApp, Gmail, etc).
 *
 * @param title Nome exibido para o usuário (já simplificado/renomeado).
 * @param subtitle Descrição curta do que o atalho faz.
 * @param emojiIcon Ícone simples (emoji) usado enquanto não há ícones vetoriais definidos.
 * @param packageName Pacote do app real no Android (usado para abrir via Intent).
 * @param fallbackUrl Link usado caso o app não esteja instalado (abre no navegador).
 * @param searchQuery Se preenchido, o atalho ignora [packageName]/[fallbackUrl] e
 *        abre direto uma busca do Google com esse texto. Necessário para atalhos
 *        como "Previsão do tempo": abrir o app do Google pelo pacote só leva à
 *        tela inicial dele (Discover), sem nenhuma busca — então nunca mostra o
 *        resultado desejado. Forçando uma busca de verdade, o resultado (ex: o
 *        card de clima) sempre aparece, com ou sem o app do Google instalado.
 * @param opensBankPicker Se true, o atalho ignora [packageName]/[fallbackUrl] e,
 *        em vez de abrir um app direto, mostra uma lista para o usuário escolher
 *        entre os principais bancos (ver [com.example.projetodirigido.model.DefaultBanks]).
 */
data class AppShortcut(
    val title: String,
    val subtitle: String,
    val emojiIcon: String,
    val packageName: String,
    val fallbackUrl: String,
    val searchQuery: String? = null,
    val opensBankPicker: Boolean = false
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
            title = "Fotos e Vídeos",
            subtitle = "Veja suas fotos e vídeos no celular pelo Google Fotos",
            emojiIcon = "🖼️",
            packageName = "com.google.android.apps.photos",
            fallbackUrl = "https://photos.google.com/"
        ),
        AppShortcut(
            title = "Acesse seu banco",
            subtitle = "Escolha seu banco para entrar",
            emojiIcon = "🏦",
            packageName = "",
            fallbackUrl = "",
            opensBankPicker = true
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
            fallbackUrl = "https://www.google.com/search?q=previsao+do+tempo",
            searchQuery = "previsão do tempo"
        )
    )
}