package com.example.projetodirigido.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.Tutorial
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.ui.theme.scaledDp

/**
 * Card grande e tocável de um tutorial, na lista da guia "Aprenda Passo a
 * Passo". Segue o mesmo padrão de área de toque generosa do [ShortcutCard],
 * mas em largura total (uma coluna só), como no protótipo.
 */
@Composable
fun TutorialCard(
    tutorial: Tutorial,
    colors: AppColors,
    fontScale: Float,
    isHighContrast: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.scaledDp(fontScale))
                    .background(iconBackgroundFor(tutorial.id, colors, isHighContrast), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = tutorial.emojiIcon, fontSize = (20 * fontScale).sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tutorial.title,
                    fontSize = (17 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tutorial.subtitle,
                    fontSize = (14 * fontScale).sp,
                    color = colors.textSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🕒", fontSize = (12 * fontScale).sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${tutorial.durationMinutes} minutos",
                        fontSize = (13 * fontScale).sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.accentText
                    )
                }
            }
        }
    }
}

/**
 * Cor de fundo do círculo do ícone, uma por tutorial, para ficar fácil de
 * diferenciar os cards rapidamente (como no protótipo). Em alto contraste,
 * usamos sempre a cor de destaque padrão do tema para manter a legibilidade.
 */
private fun iconBackgroundFor(tutorialId: String, colors: AppColors, isHighContrast: Boolean): Color {
    if (isHighContrast) return colors.accent
    return when (tutorialId) {
        "pix" -> Color(0xFFDFF3E1)
        "whatsapp" -> Color(0xFFD9F0EC)
        "videochamada" -> Color(0xFFF3E1F5)
        "email" -> Color(0xFFEDE6F7)
        "google" -> Color(0xFFDCEBFB)
        "instalar_app" -> Color(0xFFDCEBFB)
        else -> colors.accent
    }
}