package com.example.projetodirigido.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetodirigido.data.EmergencyContactRepository
import com.example.projetodirigido.model.AppShortcut
import com.example.projetodirigido.model.DefaultShortcuts
import com.example.projetodirigido.model.EmergencyContact
import com.example.projetodirigido.ui.theme.FONT_SCALE_MAX
import com.example.projetodirigido.ui.theme.FONT_SCALE_MIN
import com.example.projetodirigido.ui.theme.FONT_SCALE_STEP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Nota: fontScale/highContrast continuam só em memória de propósito (não
 * fazem falta entre sessões). Já o emergencyContact é persistido em disco
 * via DataStore (`EmergencyContactRepository`), porque perder o contato da
 * família cadastrado seria bem ruim para quem depende dele numa emergência.
 *
 * Por causa do DataStore, o ViewModel agora precisa de um Context (via
 * AndroidViewModel/Application). Isso não muda nada na tela: `viewModel()`
 * no Compose já sabe criar um AndroidViewModel automaticamente.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val emergencyContactRepository = EmergencyContactRepository(application)

    private val _fontScale = MutableStateFlow(1f)
    val fontScale: StateFlow<Float> = _fontScale.asStateFlow()

    private val _highContrast = MutableStateFlow(false)
    val highContrast: StateFlow<Boolean> = _highContrast.asStateFlow()

    // Modo "🔊 Ler" ativado na barra superior: enquanto estiver ligado,
    // qualquer opção tocada no app é lida em voz alta. Tocar em "Ler" de
    // novo desliga o modo (e para a fala em andamento).
    private val _isReadingModeActive = MutableStateFlow(false)
    val isReadingModeActive: StateFlow<Boolean> = _isReadingModeActive.asStateFlow()

    /** Alterna o modo de leitura e retorna o novo estado (true = ligado). */
    fun toggleReadingMode(): Boolean {
        _isReadingModeActive.value = !_isReadingModeActive.value
        return _isReadingModeActive.value
    }

    // Carrega o contato salvo assim que o app abre e mantém sincronizado
    // com o disco (qualquer save/clear reflete aqui automaticamente).
    val emergencyContact: StateFlow<EmergencyContact?> =
        emergencyContactRepository.contactFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

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

    /**
     * Salva (ou atualiza, se já existia um) o contato de emergência se nome
     * e telefone forem válidos, e persiste em disco. Retorna true se salvou,
     * false se os dados estavam incompletos (a tela usa o retorno para
     * mostrar uma mensagem de erro amigável).
     */
    fun saveEmergencyContact(name: String, phone: String): Boolean {
        val cleanName = name.trim()
        val cleanPhone = phone.filter { it.isDigit() }

        if (cleanName.isEmpty() || cleanPhone.length < 8) {
            return false
        }

        viewModelScope.launch {
            emergencyContactRepository.save(EmergencyContact(name = cleanName, phone = cleanPhone))
        }
        return true
    }

    fun clearEmergencyContact() {
        viewModelScope.launch {
            emergencyContactRepository.clear()
        }
    }
}