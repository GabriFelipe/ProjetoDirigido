package com.example.projetodirigido.model

/**
 * Um tutorial em texto exibido dentro da guia "Aprenda Passo a Passo".
 *
 * @param id Identificador único, usado para navegar até a tela de detalhe certa.
 * @param title Nome do tutorial (ex: "Como fazer um Pix").
 * @param subtitle Frase curta explicando o benefício, mostrada no card da lista.
 * @param emojiIcon Ícone simples (emoji) mostrado no círculo colorido do card.
 * @param durationMinutes Tempo estimado de leitura, mostrado no card (ex: "5 minutos").
 * @param steps Passos numerados do tutorial, em ordem, em linguagem simples e direta.
 * @param tip Dica final opcional, mostrada em destaque no fim do tutorial.
 */
data class Tutorial(
    val id: String,
    val title: String,
    val subtitle: String,
    val emojiIcon: String,
    val durationMinutes: Int,
    val steps: List<String>,
    val tip: String? = null
)

/**
 * Lista fixa de tutoriais da guia "Aprenda Passo a Passo", baseada no protótipo.
 * Escrita em linguagem simples e acolhedora, pensada para quem está aprendendo
 * a usar o celular agora.
 */
object DefaultTutorials {
    val list = listOf(
        Tutorial(
            id = "pix",
            title = "Como fazer um Pix",
            subtitle = "Envie dinheiro de forma rápida e segura.",
            emojiIcon = "💵",
            durationMinutes = 5,
            steps = listOf(
                "Abra o aplicativo do seu banco no celular.",
                "Procure e toque na opção \"Pix\", geralmente na tela inicial do banco.",
                "Toque em \"Enviar\" ou \"Transferir\".",
                "Escolha a chave Pix da pessoa: pode ser o CPF, telefone, e-mail ou uma chave aleatória. Se preferir, use a câmera para ler um QR Code.",
                "Confira o nome que aparece na tela. Ele deve ser o da pessoa para quem você quer mandar o dinheiro.",
                "Digite o valor que deseja enviar e toque em \"Continuar\".",
                "Revise todas as informações com calma e, se estiver tudo certo, toque em \"Confirmar\" ou \"Finalizar\".",
                "Pode ser que o banco peça sua senha ou sua digital para confirmar. É normal, é assim que ele protege o seu dinheiro."
            ),
            tip = "Dica: sempre confira o nome da pessoa antes de confirmar o Pix. Se algo parecer errado, cancele e peça ajuda a alguém de confiança."
        ),
        Tutorial(
            id = "whatsapp",
            title = "Como enviar uma mensagem no WhatsApp",
            subtitle = "Converse com sua família e amigos.",
            emojiIcon = "💬",
            durationMinutes = 3,
            steps = listOf(
                "Abra o aplicativo do WhatsApp, ícone verde com um telefone desenhado.",
                "Toque no botão verde redondo com um lápis ou balão de conversa, geralmente no canto da tela.",
                "Escolha, na sua lista de contatos, a pessoa para quem você quer mandar a mensagem.",
                "Toque na caixa de texto, na parte de baixo da tela, onde está escrito \"Mensagem\".",
                "Digite o que você quer dizer usando o teclado do celular.",
                "Toque no botão verde com uma setinha (➤) para enviar a mensagem.",
                "Pronto! A mensagem aparece na tela e, quando a pessoa ler, aparecem dois tracinhos azuis ao lado dela."
            ),
            tip = "Dica: para mandar um áudio em vez de escrever, segure o botão do microfone, fale, e solte para enviar."
        ),
        Tutorial(
            id = "videochamada",
            title = "Como fazer uma videochamada",
            subtitle = "Veja a família pela câmera do celular.",
            emojiIcon = "📹",
            durationMinutes = 4,
            steps = listOf(
                "Abra o WhatsApp e toque na conversa com a pessoa que você quer ver.",
                "Procure o ícone de uma câmera de vídeo no canto de cima da tela.",
                "Toque nesse ícone para iniciar a chamada de vídeo.",
                "Espere alguns segundos enquanto o celular conecta a chamada.",
                "Quando a pessoa atender, o rosto dela vai aparecer na tela grande, e o seu rosto aparece pequeno em um cantinho.",
                "Fale normalmente, como em uma conversa cara a cara. Aproxime ou afaste o celular para ajustar a imagem.",
                "Para encerrar a chamada, toque no botão vermelho redondo, geralmente na parte de baixo da tela."
            ),
            tip = "Dica: fique em um lugar com boa luz e boa conexão de internet (Wi-Fi) para a imagem não travar."
        ),
        Tutorial(
            id = "email",
            title = "Como enviar um e-mail",
            subtitle = "Mande mensagens escritas pela internet.",
            emojiIcon = "✉️",
            durationMinutes = 5,
            steps = listOf(
                "Abra o aplicativo Gmail (ou outro app de e-mail) no seu celular.",
                "Toque no botão redondo, geralmente com um lápis ou um sinal de mais (+), no canto da tela.",
                "No campo \"Para\", digite o endereço de e-mail da pessoa que vai receber a mensagem.",
                "No campo \"Assunto\", escreva em poucas palavras sobre o que é o e-mail.",
                "Toque na área maior, abaixo do assunto, e escreva a sua mensagem.",
                "Revise o que você escreveu com calma.",
                "Toque no botão de enviar, geralmente um avião de papel, no canto de cima da tela."
            ),
            tip = "Dica: confira se o e-mail da pessoa está escrito certinho, sem espaços, antes de enviar."
        ),
        Tutorial(
            id = "google",
            title = "Como pesquisar algo no Google",
            subtitle = "Encontre qualquer informação na internet.",
            emojiIcon = "🔍",
            durationMinutes = 2,
            steps = listOf(
                "Abra o aplicativo do Google no seu celular (ícone colorido com a letra \"G\").",
                "Toque na barra branca que aparece escrito \"Pesquisar\" ou tem o desenho de uma lupa.",
                "Digite o que você quer saber, com suas próprias palavras. Por exemplo: \"previsão do tempo hoje\".",
                "Toque na tecla \"Buscar\" ou na lupa do teclado para confirmar a pesquisa.",
                "Veja a lista de resultados que aparece na tela e toque no que parecer mais interessante para você.",
                "Para voltar à pesquisa, toque na setinha de voltar, no canto de baixo ou de cima da tela."
            ),
            tip = "Dica: você também pode tocar no microfone da barra de pesquisa e simplesmente falar o que quer procurar."
        ),
        Tutorial(
            id = "instalar_app",
            title = "Como instalar um aplicativo",
            subtitle = "Coloque novos apps no celular.",
            emojiIcon = "🧩",
            durationMinutes = 4,
            steps = listOf(
                "Abra a Play Store, a loja de aplicativos do celular (ícone colorido de um triângulo).",
                "Toque na barra de pesquisa, na parte de cima da tela.",
                "Digite o nome do aplicativo que você quer instalar e toque em buscar.",
                "Toque no aplicativo certo dentro da lista de resultados.",
                "Toque no botão verde \"Instalar\".",
                "Espere a barrinha de carregamento terminar. Isso pode levar alguns minutos, dependendo da internet.",
                "Quando terminar, o botão muda para \"Abrir\". Toque nele para começar a usar o aplicativo novo."
            ),
            tip = "Dica: só instale aplicativos pela Play Store oficial, é o jeito mais seguro de evitar vírus."
        )
    )
}