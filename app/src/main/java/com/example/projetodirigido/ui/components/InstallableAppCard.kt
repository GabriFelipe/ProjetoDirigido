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
import com.example.projetodirigido.model.InstallableApp
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.ui.theme.scaledDp

/**
 * Card grande e tocável de um app para instalar, na seção "Aplicativos" da
 * guia "Aprenda Passo a Passo". Segue o mesmo padrão visual do
 * [ShortcutCard] (ícone + título + descrição, área de toque generosa), mas
 * inclui um selo "Instalar" para deixar claro que o toque leva à Google
 * Play, e não abre o app diretamente (o usuário pode não ter instalado
 * ainda).
 */
@Composable
fun InstallableAppCard(
    app: InstallableApp,
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
                    .size(44.scaledDp(fontScale))
                    .background(colors.accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = app.emojiIcon, fontSize = (20 * fontScale).sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.title,
                    fontSize = (16 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = app.subtitle,
                    fontSize = (13 * fontScale).sp,
                    color = colors.textSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Selo "Instalar": deixa claro que o toque leva à Google Play,
            // diferente dos atalhos da tela inicial (que abrem o app direto).
            Box(
                modifier = Modifier
                    .background(colors.accent, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Instalar",
                    fontSize = (12 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }
        }
    }
}