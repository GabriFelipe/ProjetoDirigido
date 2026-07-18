package com.example.projetodirigido.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Escala usada para tamanhos de CAIXAS/ÍCONES (não texto), a partir do
 * `fontScale` do app (o mesmo controlado pelos botões "−A" / "+A").
 *
 * Usamos uma faixa mais contida (0.9 a 1.3) do que a do texto (0.85 a 1.6):
 * se os círculos de ícone, por exemplo, crescessem na mesma proporção do
 * texto, ficariam grandes demais em telas pequenas. Ainda assim, crescem
 * um pouco junto com o texto, para o layout continuar proporcional e nada
 * ficar "pequeno demais" perto de letras maiores.
 */
fun boxScale(fontScale: Float): Float = fontScale.coerceIn(0.9f, 1.3f)

/** Aplica [boxScale] a um tamanho fixo em dp (ex: `44.scaledDp(fontScale)`). */
fun Int.scaledDp(fontScale: Float): Dp = (this * boxScale(fontScale)).dp