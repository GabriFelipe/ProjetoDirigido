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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.AppShortcut
import com.example.projetodirigido.ui.theme.AppColors

/**
 * Card grande e tocável representando um atalho (ex: WhatsApp, Gmail).
 * Área de toque generosa (todo o card), seguindo boas práticas de
 * acessibilidade para usuários com menor precisão motora.
 */
@Composable
fun ShortcutCard(
    shortcut: AppShortcut,
    colors: AppColors,
    fontScale: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(colors.accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = shortcut.emojiIcon, fontSize = (20 * fontScale).sp)
            }

            Column {
                Text(
                    text = shortcut.title,
                    fontSize = (16 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Text(
                    text = shortcut.subtitle,
                    fontSize = (13 * fontScale).sp,
                    color = colors.textSecondary
                )
            }
        }
    }
}
