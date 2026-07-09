package com.example.projetodirigido.ui.theme

import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val border: Color
)

val DefaultColors = AppColors(
    background = Color(0xFFFCF3E3),
    surface = Color(0xFFFFFFFF),
    textPrimary = Color(0xFF1B3A2A),
    textSecondary = Color(0xFF5B6B63),
    accent = Color(0xFFB7E0C4),
    border = Color(0xFFEAE0CC)
)

// Paleta usada quando o usuário ativa o botão "Contraste":
// fundo e texto com contraste bem mais forte, mantendo boa legibilidade.
val HighContrastColors = AppColors(
    background = Color(0xFF000000),
    surface = Color(0xFF1A1A1A),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFFFD54F),
    accent = Color(0xFFFFD54F),
    border = Color(0xFFFFFFFF)
)
