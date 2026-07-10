package com.example.projetodirigido.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.ui.theme.AppColors

/**
 * Barra fixa no topo com o nome do app e os controles de acessibilidade.
 * Fica visível em todas as telas para manter consistência (conforme protótipo).
 */
@Composable
fun AccessibleTopBar(
    colors: AppColors,
    isHighContrast: Boolean,
    onDecreaseFont: () -> Unit,
    onIncreaseFont: () -> Unit,
    onToggleContrast: () -> Unit,
    onReadScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Linha 1: apenas o nome do app + botão de leitura em voz alta.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Assistente Digital",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Linha 2: controles de acessibilidade, agora mais abaixo para não
        // disputar espaço com o título e evitar que quebrem de forma estranha.
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TopBarButton(text = "− A", colors = colors, onClick = onDecreaseFont)
            TopBarButton(text = "+ A", colors = colors, onClick = onIncreaseFont)
            TopBarButton(text = "🔊 Ler", colors = colors, onClick = onReadScreen)
            TopBarButton(
                text = if (isHighContrast) "Contraste ✓" else "Contraste",
                colors = colors,
                onClick = onToggleContrast
            )
        }
    }
}

@Composable
private fun TopBarButton(text: String, colors: AppColors, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(1.dp, colors.border),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colors.surface,
            contentColor = colors.textPrimary
        )
    ) {
        Text(text = text, fontSize = 14.sp)
    }
}