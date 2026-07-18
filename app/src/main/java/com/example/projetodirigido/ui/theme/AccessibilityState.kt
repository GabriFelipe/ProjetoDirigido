package com.example.projetodirigido.ui.theme

import androidx.compose.runtime.compositionLocalOf

/**
 * Escala de fonte aplicada em todo o app (multiplicador sobre o tamanho base).
 * Controlada pelos botões "A-" e "A+" na barra superior.
 */
val LocalFontScale = compositionLocalOf { 1f }

/** Ativa/desativa o modo de alto contraste (fundo/texto com contraste reforçado). */
val LocalHighContrast = compositionLocalOf { false }

/**
 * Função global de "ler em voz alta". Qualquer componente clicável no app
 * pode chamar `LocalReadAloud.current(texto)` no seu onClick para anunciar
 * o que foi tocado.
 *
 * A função em si já sabe se o modo de leitura (botão "🔊 Ler" da barra
 * superior) está ativo ou não: quando desativado, ela simplesmente não faz
 * nada, então os componentes não precisam checar esse estado — só chamar a
 * função sempre que algo for clicado.
 */
val LocalReadAloud = compositionLocalOf<(String) -> Unit> { {} }

const val FONT_SCALE_MIN = 0.85f
const val FONT_SCALE_MAX = 1.6f
const val FONT_SCALE_STEP = 0.15f