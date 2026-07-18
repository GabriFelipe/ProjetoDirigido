package com.example.projetodirigido.ui.learn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.DefaultInstallableApps
import com.example.projetodirigido.model.DefaultTutorials
import com.example.projetodirigido.model.InstallableApp
import com.example.projetodirigido.model.Tutorial
import com.example.projetodirigido.ui.components.InstallableAppCard
import com.example.projetodirigido.ui.components.TutorialCard
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.util.IntentHelper

/**
 * Corpo da guia "Aprenda Passo a Passo": lista de tutoriais em texto que
 * ensinam, com calma, a usar funções do dia a dia (Pix, WhatsApp, e-mail,
 * etc). Ao tocar em um card, abre o tutorial correspondente em texto
 * ([onOpenTutorial]).
 *
 * Não inclui a barra superior de acessibilidade: a tela que a chama (ex:
 * [com.example.projetodirigido.ui.home.HomeScreen]) já mostra a
 * `AccessibleTopBar` acima do conteúdo, para manter os botões −A / +A /
 * Contraste / Ler sempre no mesmo lugar em qualquer aba do app.
 *
 * @param onBack Chamado ao tocar em "← Voltar", no topo da tela; retorna
 *        para a tela inicial (Atalhos rápidos).
 */
@Composable
fun LearnScreen(
    colors: AppColors,
    fontScale: Float,
    isHighContrast: Boolean,
    onOpenTutorial: (Tutorial) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    tutorials: List<Tutorial> = DefaultTutorials.list,
    installableApps: List<InstallableApp> = DefaultInstallableApps.list
) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        // Botão "Voltar" sempre visível no topo da guia, para o usuário
        // conseguir retornar à tela inicial (Atalhos rápidos) a qualquer
        // momento, do mesmo jeito que já funciona dentro de um tutorial.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBack,
                shape = MaterialTheme.shapes.extraLarge,
                border = BorderStroke(1.dp, colors.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colors.surface,
                    contentColor = colors.textPrimary
                )
            ) {
                Text(text = "← Voltar", fontSize = (14 * fontScale).sp)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    Text(
                        text = "Aprenda passo a passo",
                        fontSize = (26 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Toque em um tutorial e vamos ensinar com calma.",
                        fontSize = (15 * fontScale).sp,
                        color = colors.textSecondary
                    )
                }
            }

            items(tutorials) { tutorial ->
                TutorialCard(
                    tutorial = tutorial,
                    colors = colors,
                    fontScale = fontScale,
                    isHighContrast = isHighContrast,
                    onClick = { onOpenTutorial(tutorial) }
                )
            }

            item {
                Column(modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)) {
                    Text(
                        text = "Aplicativos",
                        fontSize = (20 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Toque em um app para instalar direto da Google Play.",
                        fontSize = (14 * fontScale).sp,
                        color = colors.textSecondary
                    )
                }
            }

            items(installableApps) { app ->
                InstallableAppCard(
                    app = app,
                    colors = colors,
                    fontScale = fontScale,
                    onClick = { IntentHelper.openPlayStore(context, app.playStorePackage) }
                )
            }
        }
    }
}