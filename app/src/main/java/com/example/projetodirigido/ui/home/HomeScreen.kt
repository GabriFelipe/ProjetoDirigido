package com.example.projetodirigido.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetodirigido.data.LauncherAppsRepository
import com.example.projetodirigido.model.LauncherApp
import com.example.projetodirigido.model.Tutorial
import com.example.projetodirigido.ui.components.AccessibleTopBar
import com.example.projetodirigido.ui.components.BankPickerDialog
import com.example.projetodirigido.ui.components.EmergencySection
import com.example.projetodirigido.ui.components.ShortcutCard
import com.example.projetodirigido.ui.components.WhatsAppSection
import com.example.projetodirigido.ui.learn.LearnScreen
import com.example.projetodirigido.ui.learn.TutorialDetailScreen
import com.example.projetodirigido.ui.learn.buildTutorialSpeech
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.ui.theme.DefaultColors
import com.example.projetodirigido.ui.theme.HighContrastColors
import com.example.projetodirigido.ui.theme.LocalReadAloud
import com.example.projetodirigido.util.IntentHelper
import com.example.projetodirigido.util.TtsHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.example.projetodirigido.ui.apps.AppsScreen
import com.example.projetodirigido.model.EmergencyContact
import com.example.projetodirigido.data.AppIconLoader

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val appIconLoader = remember(context.applicationContext) {
        AppIconLoader(context.applicationContext)
    }

    DisposableEffect(appIconLoader) {
        onDispose {
            appIconLoader.close()
        }
    }

    val homePagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    val launcherAppsRepository = remember(context) {
        LauncherAppsRepository(context.applicationContext)
    }

    var installedApps by remember {
        mutableStateOf<List<LauncherApp>>(emptyList())
    }

    LaunchedEffect(Unit) {
        installedApps = withContext(Dispatchers.IO) {
            launcherAppsRepository.loadApps()
        }
    }

    LaunchedEffect(
        homePagerState.currentPage,
        installedApps,
        appIconLoader
    ) {
        if (
            homePagerState.currentPage == 1 &&
            installedApps.isNotEmpty()
        ) {
            coroutineScope {
                installedApps
                    .take(24)
                    .forEach { app ->
                        launch(Dispatchers.IO) {
                            appIconLoader.load(
                                app = app,
                                targetSizePx = 96
                            )
                        }
                    }
            }
        }
    }


    val fontScale by viewModel.fontScale.collectAsState()
    val highContrast by viewModel.highContrast.collectAsState()
    val emergencyContact by viewModel.emergencyContact.collectAsState()
    val whatsappContact: EmergencyContact? by viewModel.whatsappContact.collectAsState()
    val isReadingModeActive by viewModel.isReadingModeActive.collectAsState()
    val colors = if (highContrast) HighContrastColors else DefaultColors

    var route by remember { mutableStateOf<HomeRoute>(HomeRoute.Shortcuts) }
    var showBankPicker by remember { mutableStateOf(false) }

    // Botão de voltar do celular: dentro de um tutorial, volta para a lista
    // de tutoriais; dentro da guia "Aprenda passo a passo", volta para a
    // tela inicial (Atalhos rápidos). Só na tela inicial o botão de voltar
    // volta a ter o comportamento padrão do sistema (fechar/minimizar o
    // app), por isso o BackHandler fica desativado (enabled = false) nesse
    // caso — do contrário o botão de voltar nunca fecharia o app.
    BackHandler(enabled = route !is HomeRoute.Shortcuts) {
        route = when (route) {
            is HomeRoute.TutorialDetail -> HomeRoute.Learn
            is HomeRoute.Learn -> HomeRoute.Shortcuts
            is HomeRoute.Shortcuts -> HomeRoute.Shortcuts
        }
    }

    // TTS: criado uma vez por composição da tela e liberado ao sair.
    val ttsHelper = remember { TtsHelper(context) }
    DisposableEffect(Unit) {
        onDispose { ttsHelper.shutdown() }
    }

    // Função compartilhada com todo o app (via LocalReadAloud): qualquer
    // card/botão pode chamar isso no seu onClick para anunciar o que foi
    // tocado. Só fala de verdade quando o modo "🔊 Ler" está ativo — quando
    // desativado, a chamada não faz nada, então nenhum componente precisa
    // verificar esse estado por conta própria.
    val readAloud: (String) -> Unit = { text ->
        if (isReadingModeActive) {
            ttsHelper.speak(text)
        }
    }

    val today = remember {
        val formatter = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
        formatter.format(Date()).replaceFirstChar { it.uppercase() }
    }

    val greeting = "Olá! Auxílio de leitura ativado."
    val subtitle = "Clique novamente para desativar."

    CompositionLocalProvider(LocalReadAloud provides readAloud) {
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
                isReadingModeActive = isReadingModeActive,
                onReadScreen = {
                    val turnedOn = viewModel.toggleReadingMode()
                    if (turnedOn) {
                        // Ao ligar o modo, já lê a tela atual de cara — e, a
                        // partir daí, qualquer opção tocada também será lida.
                        when (val currentRoute = route) {
                            is HomeRoute.Shortcuts -> ttsHelper.speak("$greeting. $subtitle")
                            is HomeRoute.Learn -> ttsHelper.speak(
                                "Aprenda passo a passo. Toque em um tutorial e vamos ensinar com calma."
                            )
                            is HomeRoute.TutorialDetail -> ttsHelper.speak(
                                buildTutorialSpeech(currentRoute.tutorial)
                            )
                        }
                    } else {
                        // Ao desligar, interrompe qualquer fala em andamento.
                        ttsHelper.stop()
                    }
                }
            )

            when (val currentRoute = route) {
                is HomeRoute.Shortcuts -> {
                    HorizontalPager(
                        state = homePagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) { page ->
                        when (page) {
                            0 -> {
                                ShortcutsHomeContent(
                                    viewModel = viewModel,
                                    colors = colors,
                                    fontScale = fontScale,
                                    today = today,
                                    greeting = greeting,
                                    subtitle = subtitle,
                                    emergencyContact = emergencyContact,
                                    whatsappContact = whatsappContact,
                                    readAloud = readAloud,
                                    onOpenLearn = {
                                        route = HomeRoute.Learn
                                    },
                                    onShowBankPicker = {
                                        showBankPicker = true
                                    }
                                )
                            }

                            1 -> {
                                AppsScreen(
                                    apps = installedApps,
                                    iconLoader = appIconLoader,
                                    fontScale = fontScale,
                                    onOpenApp = { app ->
                                        readAloud(app.label)

                                        IntentHelper.openLauncherApp(
                                            context = context,
                                            componentName = app.componentName()
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                HomeRoute.Learn -> {
                    LearnScreen(
                        colors = colors,
                        fontScale = fontScale,
                        isHighContrast = highContrast,
                        onOpenTutorial = { tutorial ->
                            route = HomeRoute.TutorialDetail(tutorial)
                        },
                        onBack = {
                            route = HomeRoute.Shortcuts
                        }
                    )
                }

                is HomeRoute.TutorialDetail -> {
                    TutorialDetailScreen(
                        tutorial = currentRoute.tutorial,
                        colors = colors,
                        fontScale = fontScale,
                        onBack = {
                            route = HomeRoute.Learn
                        },
                        onReadTutorial = {
                            ttsHelper.speak(
                                buildTutorialSpeech(currentRoute.tutorial)
                            )
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
}

@Composable
private fun ShortcutsHomeContent(
    viewModel: HomeViewModel,
    colors: AppColors,
    fontScale: Float,
    today: String,
    greeting: String,
    subtitle: String,
    emergencyContact: EmergencyContact?,
    whatsappContact: EmergencyContact?,
    readAloud: (String) -> Unit,
    onOpenLearn: () -> Unit,
    onShowBankPicker: () -> Unit
) {
    val context = LocalContext.current

    val gridState = rememberLazyGridState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Text(
                        text = today,
                        fontSize = (13 * fontScale).sp,
                        color = colors.textSecondary
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = greeting,
                        fontSize = (26 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

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
                    onClick = onOpenLearn
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
                        readAloud(shortcut.title)

                        when {
                            shortcut.opensBankPicker -> {
                                onShowBankPicker()
                            }

                            shortcut.searchQuery != null -> {
                                IntentHelper.openGoogleSearch(
                                    context,
                                    shortcut.searchQuery
                                )
                            }

                            else -> {
                                IntentHelper.openApp(
                                    context,
                                    shortcut.packageName,
                                    shortcut.fallbackUrl
                                )
                            }
                        }
                    }
                )
            }

            item {
                Column(
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    WhatsAppSection(
                        colors = colors,
                        fontScale = fontScale,
                        contact = whatsappContact,
                        onSaveAndOpen = { name, phone ->
                            val saved =
                                viewModel.saveWhatsAppContact(
                                    name,
                                    phone
                                )

                            if (saved) {
                                IntentHelper.openWhatsAppChat(
                                    context,
                                    phone,
                                    "Olá $name, estou te mandando essa mensagem pelo aplicativo."
                                )
                            }

                            saved
                        }
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    EmergencySection(
                        colors = colors,
                        fontScale = fontScale,
                        contact = emergencyContact,
                        onSaveContact = { name, phone ->
                            viewModel.saveEmergencyContact(
                                name,
                                phone
                            )
                        }
                    )
                }
            }
        }
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
    val readAloud = LocalReadAloud.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                readAloud("Aprenda passo a passo")
                onClick()
            }),
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