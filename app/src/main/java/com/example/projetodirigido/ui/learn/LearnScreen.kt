package com.example.projetodirigido.ui.learn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.DefaultTutorials
import com.example.projetodirigido.model.Tutorial
import com.example.projetodirigido.ui.components.TutorialCard
import com.example.projetodirigido.ui.theme.AppColors

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
 */
@Composable
fun LearnScreen(
    colors: AppColors,
    fontScale: Float,
    isHighContrast: Boolean,
    onOpenTutorial: (Tutorial) -> Unit,
    modifier: Modifier = Modifier,
    tutorials: List<Tutorial> = DefaultTutorials.list
) {
    LazyColumn(
        modifier = modifier
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
    }
}