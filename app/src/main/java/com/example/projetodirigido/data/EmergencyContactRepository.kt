package com.example.projetodirigido.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.projetodirigido.model.EmergencyContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.emergencyContactDataStore by preferencesDataStore(name = "emergency_contact")

/**
 * Persiste o contato de emergência da família em disco (DataStore), para
 * que ele continue salvo mesmo depois de fechar o app ou reiniciar o
 * celular — diferente de fontScale/highContrast, que ainda são só em
 * memória de propósito (não fazem falta entre uma sessão e outra).
 */
class EmergencyContactRepository(context: Context) {

    private val appContext = context.applicationContext
    private val nameKey = stringPreferencesKey("emergency_contact_name")
    private val phoneKey = stringPreferencesKey("emergency_contact_phone")

    /** Emite o contato salvo (ou null) toda vez que ele muda. */
    val contactFlow: Flow<EmergencyContact?> = appContext.emergencyContactDataStore.data.map { prefs ->
        val name = prefs[nameKey]
        val phone = prefs[phoneKey]
        if (!name.isNullOrBlank() && !phone.isNullOrBlank()) {
            EmergencyContact(name = name, phone = phone)
        } else {
            null
        }
    }

    suspend fun save(contact: EmergencyContact) {
        appContext.emergencyContactDataStore.edit { prefs ->
            prefs[nameKey] = contact.name
            prefs[phoneKey] = contact.phone
        }
    }

    suspend fun clear() {
        appContext.emergencyContactDataStore.edit { prefs ->
            prefs.remove(nameKey)
            prefs.remove(phoneKey)
        }
    }
}