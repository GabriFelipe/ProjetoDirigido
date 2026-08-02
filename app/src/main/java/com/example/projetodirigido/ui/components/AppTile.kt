package com.example.projetodirigido.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.LauncherApp
import com.example.projetodirigido.data.AppIconLoader
///adicionado depois em baixo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.example.projetodirigido.data.AppIconData
@Composable
fun AppTile(
    app: LauncherApp,
    iconLoader: AppIconLoader,
    fontScale: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    /*
     * O ícone é exibido em aproximadamente 64 dp.
     * Limitamos a decodificação a 160 px mesmo em telas muito densas.
     */
    val targetSizePx = remember(density) {
        with(density) {
            56.dp
                .roundToPx()
                .coerceIn(72, 112)
        }
    }

    val iconData by produceState<AppIconData?>(
        initialValue = null,
        key1 = app.key,
        key2 = targetSizePx
    ) {
        value = iconLoader.load(
            app = app,
            targetSizePx = targetSizePx
        )
    }

    val tileColor = iconData?.let {
        Color(it.tileColor)
    } ?: Color(
        AppIconLoader.fallbackColorFor(app.packageName)
    )

    val contentColor =
        if (tileColor.luminance() > 0.45f) {
            Color.Black
        } else {
            Color.White
        }

    val imageBitmap = remember(iconData?.bitmap) {
        iconData?.bitmap?.asImageBitmap()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .semantics {
                contentDescription = "Abrir ${app.label}"
            }
            .clickable(
                role = Role.Button,
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = tileColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            } else {
                /*
                 * Placeholder estático.
                 * Evite dezenas de indicadores animados simultaneamente.
                 */
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = contentColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = app.label
                            .firstOrNull()
                            ?.uppercase()
                            ?: "?",
                        color = contentColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = app.label,
                color = contentColor,
                fontSize = (15 * fontScale).sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Evita blocos praticamente brancos ou pretos, que poderiam prejudicar
 * a distinção visual dos cartões.
 */
@Composable
private fun Color.accessibleTileColor(): Color {
    return when {
        luminance() > 0.88f -> copy(
            red = red * 0.78f,
            green = green * 0.78f,
            blue = blue * 0.78f
        )

        luminance() < 0.05f -> MaterialTheme.colorScheme.primary

        else -> this
    }
}