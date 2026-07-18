package com.example.projetodirigido.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.ui.theme.AppColors

/**
 * Barra fixa no topo com o nome do app e os controles de acessibilidade.
 * Fica visível em todas as telas para manter consistência (conforme protótipo).
 *
 * @param fontScale Mesma escala de fonte usada no resto do app: o título e
 *        os botões também crescem/diminuem com "−A" / "+A", para a barra
 *        continuar proporcional ao conteúdo abaixo dela.
 */
@Composable
fun AccessibleTopBar(
    colors: AppColors,
    isHighContrast: Boolean,
    fontScale: Float,
    onDecreaseFont: () -> Unit,
    onIncreaseFont: () -> Unit,
    onToggleContrast: () -> Unit,
    onReadScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            // Empurra todo o conteúdo para baixo da barra de status do
            // celular (relógio, sinal, bateria), para o título nunca ficar
            // por baixo/sobreposto a ela, em qualquer aparelho.
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Linha 1: nome do app + botão de leitura em voz alta. O título usa
        // peso (weight) e no máximo 1 linha, para nunca empurrar o botão
        // "Ler" para fora da tela em nomes maiores ou fontes maiores.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Assistente Digital",
                fontSize = (18 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            TopBarButton(text = "🔊 Ler", fontScale = fontScale, colors = colors, onClick = onReadScreen)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Linha 2: controles de acessibilidade. Fica dentro de uma rolagem
        // horizontal: assim, mesmo que o celular seja estreito ou o usuário
        // tenha aumentado a fonte do sistema (não só a do app), os botões
        // nunca quebram em duas linhas nem ficam cortados — na pior das
        // hipóteses, o usuário arrasta o dedo para o lado para ver o resto.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TopBarButton(text = "− A", fontScale = fontScale, colors = colors, onClick = onDecreaseFont)
            TopBarButton(text = "+ A", fontScale = fontScale, colors = colors, onClick = onIncreaseFont)
            TopBarButton(
                text = if (isHighContrast) "Contraste ✓" else "Contraste",
                fontScale = fontScale,
                colors = colors,
                onClick = onToggleContrast
            )
        }
    }
}

@Composable
private fun TopBarButton(
    text: String,
    fontScale: Float,
    colors: AppColors,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(1.dp, colors.border),
        // Padding um pouco mais enxuto que o padrão do Material, para caber
        // mais confortavelmente os 3 botões lado a lado em celulares
        // estreitos, mesmo com o texto "Contraste ✓" (o mais longo deles).
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colors.surface,
            contentColor = colors.textPrimary
        )
    ) {
        // maxLines = 1 + softWrap = false: o texto do botão NUNCA quebra em
        // duas linhas (era o problema do botão "Contraste"), nem quando o
        // aparelho está com a fonte do sistema aumentada.
        Text(
            text = text,
            fontSize = (14 * fontScale).sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Visible
        )
    }
}