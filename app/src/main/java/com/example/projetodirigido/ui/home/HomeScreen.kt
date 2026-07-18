package com.example.projetodirigido.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.projetodirigido.model.Tutorial
import com.example.projetodirigido.ui.components.AccessibleTopBar
import com.example.projetodirigido.ui.components.BankPickerDialog
import com.example.projetodirigido.ui.components.EmergencySection
import com.example.projetodirigido.ui.components.ShortcutCard
import com.example.projetodirigido.ui.learn.LearnScreen
import com.example.projetodirigido.ui.learn.TutorialDetailScreen
import com.example.projetodirigido.ui.learn.buildTutorialSpeech
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.ui.theme.DefaultColors
import com.example.projetodirigido.ui.theme.HighContrastColors
import com.example.projetodirigido.util.IntentHelper
import com.example.projetodirigido.util.TtsHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Telas navegáveis dentro do HomeScreen. A navegação é feita só com estado
 * local (sem depender de nenhuma biblioteca de navegação extra): trocar de
 * [HomeRoute] troca o conteúdo mostrado abaixo da [AccessibleTopBar], que
 * continua sempre visível e com o mesmo estado de fonte/contraste.
 */
private sealed class HomeRoute {
    object Shortcuts : HomeRoute()
    object Learn : HomeRoute()
    data class TutorialDetail(val tutorial: Tutorial) : HomeRoute()
}

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val fontScale by viewModel.fontScale.collectAsState()
    val highContrast by viewModel.highContrast.collectAsState()
    val emergencyContact by viewModel.emergencyContact.collectAsState()
    val colors = if (highContrast) HighContrastColors else DefaultColors

    var route by remember { mutableStateOf<HomeRoute>(HomeRoute.Shortcuts) }
    var showBankPicker by remember { mutableStateOf(false) }

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
            // Mesma lógica do topo: em celulares com navegação por gestos
            // (edge-to-edge), evita que o conteúdo de baixo (ex: botão
            // "Concluí esse tutorial") fique escondido atrás da barra de
            // navegação do sistema.
            .navigationBarsPadding()
            // Reduz a altura disponível da tela quando o teclado abre, para
            // que a área rolável (LazyVerticalGrid) fique menor que o
            // teclado ocupa. Isso, combinado com o comportamento padrão do
            // Compose de rolar automaticamente o campo focado para dentro
            // da área visível, evita que o teclado numérico cubra os campos
            // "Nome"/"Telefone" do formulário de emergência.
            .imePadding()
    ) {
        AccessibleTopBar(
            colors = colors,
            isHighContrast = highContrast,
            fontScale = fontScale,
            onDecreaseFont = viewModel::decreaseFont,
            onIncreaseFont = viewModel::increaseFont,
            onToggleContrast = viewModel::toggleContrast,
            onReadScreen = {
                when (val currentRoute = route) {
                    is HomeRoute.Shortcuts -> ttsHelper.speak("$greeting. $subtitle")
                    is HomeRoute.Learn -> ttsHelper.speak(
                        "Aprenda passo a passo. Toque em um tutorial e vamos ensinar com calma."
                    )
                    is HomeRoute.TutorialDetail -> ttsHelper.speak(
                        buildTutorialSpeech(currentRoute.tutorial)
                    )
                }
            }
        )

        when (val currentRoute = route) {
            is HomeRoute.Shortcuts -> {
                val gridState = rememberLazyGridState()

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        state = gridState,
                        // Coluna única e de largura total: com 2 colunas fixas,
                        // nomes mais longos ("Banco do Brasil", "Previsão do
                        // tempo") ou a fonte ampliada (botão "+A") deixavam o
                        // card estreito demais e o texto era cortado
                        // ("Banco ...", "Previs..."). Em largura total, o
                        // texto sempre tem espaço para quebrar linha em vez
                        // de truncar, em qualquer tamanho de fonte.
                        columns = GridCells.Fixed(1),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
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
                            }
                        }

                        item {
                            LearnEntryCard(
                                colors = colors,
                                fontScale = fontScale,
                                onClick = { route = HomeRoute.Learn }
                            )
                        }

                        item {
                            Text(
                                text = "Atalhos rápidos",
                                fontSize = (20 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        items(viewModel.shortcuts) { shortcut ->
                            ShortcutCard(
                                shortcut = shortcut,
                                colors = colors,
                                fontScale = fontScale,
                                onClick = {
                                    when {
                                        shortcut.opensBankPicker -> showBankPicker = true
                                        shortcut.searchQuery != null ->
                                            IntentHelper.openGoogleSearch(context, shortcut.searchQuery)
                                        else ->
                                            IntentHelper.openApp(context, shortcut.packageName, shortcut.fallbackUrl)
                                    }
                                }
                            )
                        }

                        item {
                            Column(modifier = Modifier.padding(top = 20.dp)) {
                                EmergencySection(
                                    colors = colors,
                                    fontScale = fontScale,
                                    contact = emergencyContact,
                                    onSaveContact = { name, phone ->
                                        viewModel.saveEmergencyContact(name, phone)
                                    }
                                )
                            }
                        }
                    }

                    // Barra de rolagem lateral escondida a pedido: o
                    // LazyVerticalGrid continua rolável normalmente, só a
                    // "pílula" visual do lado direito não é mais desenhada.
                }
            }

            is HomeRoute.Learn -> {
                LearnScreen(
                    colors = colors,
                    fontScale = fontScale,
                    isHighContrast = highContrast,
                    onOpenTutorial = { tutorial -> route = HomeRoute.TutorialDetail(tutorial) },
                    onBack = { route = HomeRoute.Shortcuts }
                )
            }

            is HomeRoute.TutorialDetail -> {
                TutorialDetailScreen(
                    tutorial = currentRoute.tutorial,
                    colors = colors,
                    fontScale = fontScale,
                    onBack = { route = HomeRoute.Learn },
                    onReadTutorial = {
                        ttsHelper.speak(buildTutorialSpeech(currentRoute.tutorial))
                    }
                )
            }
        }
    }

    if (showBankPicker) {
        BankPickerDialog(
            colors = colors,
            fontScale = fontScale,
            onBankSelected = { bank ->
                IntentHelper.openApp(context, bank.packageName, bank.fallbackUrl)
                showBankPicker = false
            },
            onDismiss = { showBankPicker = false }
        )
    }
}

/**
 * Card de entrada para a guia "Aprenda Passo a Passo", mostrado no topo da
 * tela inicial, logo abaixo da saudação. Segue o mesmo padrão visual dos
 * cards de atalho, mas em largura total e com cor de destaque, para se
 * diferenciar como a "porta de entrada" dos tutoriais.
 */
@Composable
private fun LearnEntryCard(
    colors: AppColors,
    fontScale: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colors.accent.copy(alpha = 0.35f)),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(text = "📘", fontSize = (28 * fontScale).sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Aprenda passo a passo",
                    fontSize = (18 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Text(
                    text = "Tutoriais em texto para aprender com calma.",
                    fontSize = (14 * fontScale).sp,
                    color = colors.textSecondary
                )
            }
            Text(text = "›", fontSize = (24 * fontScale).sp, color = colors.textSecondary)
        }
    }
}