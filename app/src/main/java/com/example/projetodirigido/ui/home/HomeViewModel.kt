package com.example.projetodirigido.ui.home

import androidx.lifecycle.ViewModel
import com.example.projetodirigido.model.AppShortcut
import com.example.projetodirigido.model.DefaultShortcuts
import com.example.projetodirigido.ui.theme.FONT_SCALE_MAX
import com.example.projetodirigido.ui.theme.FONT_SCALE_MIN
import com.example.projetodirigido.ui.theme.FONT_SCALE_STEP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Nota: por enquanto o estado vive só em memória (MutableStateFlow).
 * Quando plugarmos o DataStore (armazenamento local, sem login),
 * basta persistir fontScale/highContrast aqui dentro sem mudar a UI.
 */
class HomeViewModel : ViewModel() {

    private val _fontScale = MutableStateFlow(1f)
    val fontScale: StateFlow<Float> = _fontScale.asStateFlow()

    private val _highContrast = MutableStateFlow(false)
    val highContrast: StateFlow<Boolean> = _highContrast.asStateFlow()

    val shortcuts: List<AppShortcut> = DefaultShortcuts.list

    fun increaseFont() {
        _fontScale.value = (_fontScale.value + FONT_SCALE_STEP).coerceAtMost(FONT_SCALE_MAX)
    }

    fun decreaseFont() {
        _fontScale.value = (_fontScale.value - FONT_SCALE_STEP).coerceAtLeast(FONT_SCALE_MIN)
    }

    fun toggleContrast() {
        _highContrast.value = !_highContrast.value
    }
}
