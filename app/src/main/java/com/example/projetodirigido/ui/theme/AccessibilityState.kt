package com.example.projetodirigido.ui.theme

import androidx.compose.runtime.compositionLocalOf

/**
 * Escala de fonte aplicada em todo o app (multiplicador sobre o tamanho base).
 * Controlada pelos botões "A-" e "A+" na barra superior.
 */
val LocalFontScale = compositionLocalOf { 1f }

/** Ativa/desativa o modo de alto contraste (fundo/texto com contraste reforçado). */
val LocalHighContrast = compositionLocalOf { false }

const val FONT_SCALE_MIN = 0.85f
const val FONT_SCALE_MAX = 1.6f
const val FONT_SCALE_STEP = 0.15f
