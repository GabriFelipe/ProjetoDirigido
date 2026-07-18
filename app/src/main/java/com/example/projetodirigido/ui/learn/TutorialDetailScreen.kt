package com.example.projetodirigido.ui.learn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.Tutorial
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.ui.theme.LocalReadAloud
import com.example.projetodirigido.ui.theme.scaledDp
import com.example.projetodirigido.util.IntentHelper

/**
 * Tela de detalhe de um tutorial: abre "em uma aba" dentro do próprio app
 * (ou seja, substitui o conteúdo da tela, sem sair do aplicativo) mostrando
 * o passo a passo em texto, na ordem certa, com números grandes e fáceis de
 * seguir.
 *
 * @param onBack Chamado ao tocar em "Voltar", retorna para a lista de
 *        tutoriais (guia "Aprenda Passo a Passo").
 * @param onReadTutorial Chamado ao tocar em "Ouvir tutorial"; a tela só
 *        aciona o texto a ser lido (título + passos + dica), quem realmente
 *        fala é o TTS já usado no resto do app.
 */
@Composable
fun TutorialDetailScreen(
    tutorial: Tutorial,
    colors: AppColors,
    fontScale: Float,
    onBack: () -> Unit,
    onReadTutorial: () -> Unit,
    modifier: Modifier = Modifier
) {
    val readAloud = LocalReadAloud.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Cabeçalho simples da aba: "Voltar" sempre visível no topo, para o
        // usuário nunca se sentir preso dentro do tutorial.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { readAloud("Voltar"); onBack() },
                shape = MaterialTheme.shapes.extraLarge,
                border = BorderStroke(1.dp, colors.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colors.surface,
                    contentColor = colors.textPrimary
                )
            ) {
                Text(text = "← Voltar", fontSize = (14 * fontScale).sp)
            }

            OutlinedButton(
                onClick = onReadTutorial,
                shape = MaterialTheme.shapes.extraLarge,
                border = BorderStroke(1.dp, colors.border),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colors.surface,
                    contentColor = colors.textPrimary
                )
            ) {
                Text(text = "🔊 Ouvir", fontSize = (14 * fontScale).sp)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = tutorial.emojiIcon, fontSize = (30 * fontScale).sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = tutorial.title,
                            fontSize = (24 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "🕒 ${tutorial.durationMinutes} minutos de leitura",
                        fontSize = (14 * fontScale).sp,
                        color = colors.accentText,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tutorial.subtitle,
                        fontSize = (15 * fontScale).sp,
                        color = colors.textSecondary
                    )
                }
            }

            itemsIndexed(tutorial.steps) { index, step ->
                TutorialStepRow(
                    stepNumber = index + 1,
                    text = step,
                    colors = colors,
                    fontScale = fontScale
                )
            }

            if (tutorial.tip != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.accent.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = tutorial.tip,
                            fontSize = (14 * fontScale).sp,
                            color = colors.textPrimary
                        )
                    }
                }
            }

            // Caixa "Digite aqui sua dúvida": só aparece no tutorial "Como
            // pesquisar algo no Google", como uma forma de já praticar o que
            // acabou de aprender, sem precisar sair do tutorial.
            if (tutorial.id == "google") {
                item {
                    GoogleSearchBox(colors = colors, fontScale = fontScale)
                }
            }

            item {
                Button(
                    onClick = { readAloud("Concluí esse tutorial"); onBack() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = colors.textPrimary
                    )
                ) {
                    Text(
                        text = "✓ Concluí esse tutorial",
                        fontSize = (16 * fontScale).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TutorialStepRow(
    stepNumber: Int,
    text: String,
    colors: AppColors,
    fontScale: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.scaledDp(fontScale))
                .background(colors.accent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$stepNumber",
                fontSize = (14 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }
        Text(
            text = text,
            fontSize = (16 * fontScale).sp,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Caixa de prática mostrada só no fim do tutorial "Como pesquisar algo no
 * Google": o usuário digita uma dúvida real e, ao tocar em "Pesquisar", o
 * app abre o Google já com a busca pronta (sem precisar digitar de novo lá).
 */
@Composable
private fun GoogleSearchBox(
    colors: AppColors,
    fontScale: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Agora é sua vez!",
            fontSize = (16 * fontScale).sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Digite aqui sua dúvida", fontSize = (15 * fontScale).sp) },
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = (15 * fontScale).sp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accent,
                unfocusedBorderColor = colors.border,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        val readAloud = LocalReadAloud.current
        Button(
            onClick = {
                readAloud("Pesquisar")
                IntentHelper.openGoogleSearch(context, query)
            },
            enabled = query.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent,
                contentColor = colors.textPrimary
            )
        ) {
            Text(
                text = "🔍 Pesquisar",
                fontSize = (16 * fontScale).sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** Monta o texto completo do tutorial para ser lido em voz alta pelo TTS. */
fun buildTutorialSpeech(tutorial: Tutorial): String {
    val stepsText = tutorial.steps
        .mapIndexed { index, step -> "Passo ${index + 1}: $step" }
        .joinToString(separator = ". ")
    val tipText = tutorial.tip?.let { ". $it" } ?: ""
    return "${tutorial.title}. $stepsText$tipText"
}