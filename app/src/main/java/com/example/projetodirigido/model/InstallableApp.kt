package com.example.projetodirigido.model

/**
 * Representa um atalho para INSTALAR um app pela Google Play, mostrado na
 * seção "Aplicativos" da guia "Aprenda Passo a Passo".
 *
 * Diferente do [AppShortcut] (tela inicial, que ABRE um app já instalado),
 * este atalho sempre direciona para a página do app na Google Play, para
 * que o usuário possa instalar (ou abrir, se já tiver instalado).
 *
 * @param title Nome exibido para o usuário.
 * @param subtitle Descrição curta e simples do que o app faz.
 * @param emojiIcon Ícone simples (emoji) usado enquanto não há ícones vetoriais definidos.
 * @param playStorePackage Package name do app na Google Play (usado para montar o link).
 */
data class InstallableApp(
    val title: String,
    val subtitle: String,
    val emojiIcon: String,
    val playStorePackage: String
)

/**
 * Lista fixa de apps sugeridos para instalação, mostrada na guia de
 * tutoriais. Ao tocar em qualquer um, o app abre diretamente a página
 * correspondente na Google Play (usando o app da Play Store, se instalado,
 * ou o navegador como alternativa).
 */
object DefaultInstallableApps {
    val list = listOf(
        InstallableApp(
            title = "WhatsApp",
            subtitle = "Mensagens: converse com a família e amigos por texto, áudio e vídeo.",
            emojiIcon = "💬",
            playStorePackage = "com.whatsapp"
        ),
        InstallableApp(
            title = "Facebook",
            subtitle = "Rede social para ver e compartilhar fotos, notícias e novidades.",
            emojiIcon = "📘",
            playStorePackage = "com.facebook.katana"
        ),
        InstallableApp(
            title = "TikTok",
            subtitle = "Vídeos curtos: assista a vídeos rápidos e divertidos.",
            emojiIcon = "🎵",
            playStorePackage = "com.zhiliaoapp.musically"
        ),
        InstallableApp(
            title = "Spotify",
            subtitle = "Música na internet: ouça músicas, rádios e podcasts.",
            emojiIcon = "🎧",
            playStorePackage = "com.spotify.music"
        ),
        InstallableApp(
            title = "Netflix",
            subtitle = "Filmes e Séries: assista filmes e séries pela internet.",
            emojiIcon = "🎬",
            playStorePackage = "com.netflix.mediaclient"
        ),
        InstallableApp(
            title = "Amazon",
            subtitle = "Compras Online: compre produtos com entrega em casa.",
            emojiIcon = "🛒",
            playStorePackage = "com.amazon.mShop.android.shopping"
        ),
        InstallableApp(
            title = "ChatGPT",
            subtitle = "Assistência Geral: converse com uma inteligência artificial para tirar dúvidas.",
            emojiIcon = "🤖",
            playStorePackage = "com.openai.chatgpt"
        )
    )
}