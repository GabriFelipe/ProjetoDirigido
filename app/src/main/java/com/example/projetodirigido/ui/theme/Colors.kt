package com.example.projetodirigido.ui.theme

import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val border: Color,
    // Paleta usada apenas no cartão de Emergência (fundo suave, borda e
    // botões em vermelho, texto do título em vermelho escuro).
    val dangerBackground: Color = Color(0xFFFBEAE8),
    val dangerBorder: Color = Color(0xFFE0453B),
    val dangerText: Color = Color(0xFFC62828),
    val dangerButton: Color = Color(0xFFD32F2F),
    val onDangerButton: Color = Color(0xFFFFFFFF)
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
    border = Color(0xFFFFFFFF),
    dangerBackground = Color(0xFF2A0E0C),
    dangerBorder = Color(0xFFFF6659),
    dangerText = Color(0xFFFF8A80),
    dangerButton = Color(0xFFFF5449),
    onDangerButton = Color(0xFF000000)
)