package com.example.projetodirigido.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetodirigido.ui.components.AccessibleTopBar
import com.example.projetodirigido.ui.components.ShortcutCard
import com.example.projetodirigido.ui.components.VerticalScrollIndicator
import com.example.projetodirigido.ui.theme.DefaultColors
import com.example.projetodirigido.ui.theme.HighContrastColors
import com.example.projetodirigido.util.IntentHelper
import com.example.projetodirigido.util.TtsHelper
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val fontScale by viewModel.fontScale.collectAsState()
    val highContrast by viewModel.highContrast.collectAsState()
    val colors = if (highContrast) HighContrastColors else DefaultColors

    // TTS: criado uma vez por composição da tela e liberado ao sair.
    val ttsHelper = remember { TtsHelper(context) }
    DisposableEffect(Unit) {
        onDispose { ttsHelper.shutdown() }
    }

    val today = remember {
        val formatter = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
        formatter.format(Date()).replaceFirstChar { it.uppercase() }
    }

    val greeting = "Olá! Como posso ajudar você hoje?"
    val subtitle = "Escolha um atalho, aprenda algo novo ou converse com a Assistente."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        AccessibleTopBar(
            colors = colors,
            isHighContrast = highContrast,
            onDecreaseFont = viewModel::decreaseFont,
            onIncreaseFont = viewModel::increaseFont,
            onToggleContrast = viewModel::toggleContrast,
            onReadScreen = {
                ttsHelper.speak("$greeting. $subtitle")
            }
        )

        val gridState = rememberLazyGridState()

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 22.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        Text(
                            text = today,
                            fontSize = (13 * fontScale).sp,
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = greeting,
                            fontSize = (26 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = subtitle,
                            fontSize = (15 * fontScale).sp,
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Atalhos rápidos",
                            fontSize = (20 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                }

                items(viewModel.shortcuts) { shortcut ->
                    ShortcutCard(
                        shortcut = shortcut,
                        colors = colors,
                        fontScale = fontScale,
                        onClick = {
                            IntentHelper.openApp(context, shortcut.packageName, shortcut.fallbackUrl)
                        }
                    )
                }
            }

            // Barra de rolagem real: acompanha o LazyGridState (some quando
            // o conteúdo cabe todo na tela, se move junto com o dedo).
            VerticalScrollIndicator(
                state = gridState,
                trackColor = colors.border.copy(alpha = 0.3f),
                thumbColor = colors.textSecondary.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
                    .padding(vertical = 8.dp)
            )
        }
    }
}