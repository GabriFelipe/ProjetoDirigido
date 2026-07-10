package com.example.projetodirigido.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Hook de "ligar com 1 clique" DE VERDADE — sem passar pelo discador.
 *
 * Assim que a seção de Emergência aparece na tela, se a permissão
 * `CALL_PHONE` ainda não foi concedida, o app já pede ela na hora
 * (diálogo padrão do Android, aparece só uma vez na vida do app).
 * Assim, quando a pessoa realmente precisar tocar em "SAMU"/"Bombeiros"/
 * "Polícia"/"Ligar", a chamada já sai direto, sem diálogo no meio.
 *
 * Se o usuário negar a permissão (ou ainda não respondeu quando tocar em
 * algum botão), a chamada cai para `ACTION_DIAL` (abre o discador com o
 * número pronto) só como rede de segurança — nunca deixa o botão quebrado.
 *
 * Uso:
 *   val call = rememberPhoneCaller()
 *   Button(onClick = { call("192") }) { Text("SAMU") }
 */
@Composable
fun rememberPhoneCaller(): (String) -> Unit {
    val context = LocalContext.current
    val currentContext = rememberUpdatedState(context)

    // Guarda o número que o usuário tentou ligar enquanto aguarda a resposta
    // do diálogo de permissão do sistema (só é usado se alguém tocar em
    // ligar antes da permissão proativa abaixo já ter sido respondida).
    val pendingNumber = remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val number = pendingNumber.value
        pendingNumber.value = null
        if (number != null) {
            if (granted) {
                IntentHelper.call(currentContext.value, number)
            } else {
                IntentHelper.dial(currentContext.value, number)
            }
        }
    }

    // Pede a permissão assim que a tela de Emergência é composta, para que
    // o primeiro toque em SAMU/Bombeiros/Polícia já ligue direto, sem
    // depender de o usuário ter tocado em algo antes.
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            currentContext.value,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    return remember {
        { phoneNumber: String ->
            val hasPermission = ContextCompat.checkSelfPermission(
                currentContext.value,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                IntentHelper.call(currentContext.value, phoneNumber)
            } else {
                // Permissão ainda não respondida (ex: usuário tocou muito
                // rápido) -> guarda o número e, se o diálogo proativo acima
                // já estiver sendo exibido, a resposta cai no callback dele.
                // Senão, pede de novo aqui mesmo.
                pendingNumber.value = phoneNumber
                permissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }
}