package com.example.projetodirigido.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.EmergencyContact
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.ui.theme.LocalReadAloud

/**
 * Seção para cadastrar um contato e iniciar conversa no WhatsApp.
 * Card azul com campos de Nome e Telefone, e botão verde de WhatsApp.
 */
@Composable
fun WhatsAppSection(
    colors: AppColors,
    fontScale: Float,
    contact: EmergencyContact?,
    onSaveAndOpen: (name: String, phone: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var name by remember(contact) { mutableStateOf(contact?.name ?: "") }
    var phone by remember(contact) { mutableStateOf(contact?.phone ?: "") }
    var showError by remember { mutableStateOf(false) }
    val readAloud = LocalReadAloud.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.infoBackground, RoundedCornerShape(20.dp))
            .border(BorderStroke(2.dp, colors.infoBorder), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "Conversar no WhatsApp",
            fontSize = (22 * fontScale).sp,
            fontWeight = FontWeight.Bold,
            color = colors.infoText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Preencha os dados abaixo para falar com alguém.",
            fontSize = (14 * fontScale).sp,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    showError = false
                },
                label = { Text("Nome") },
                placeholder = { Text("Ex: João (filho)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    showError = false
                },
                label = { Text("Telefone") },
                placeholder = { Text("(11) 99999-9999") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "Preencha o nome e um telefone válido.",
                    fontSize = (12 * fontScale).sp,
                    color = colors.dangerText
                )
            }

            val isFilled = name.isNotBlank() && phone.filter { it.isDigit() }.length >= 8

            Button(
                onClick = {
                    readAloud("Salvar contato e conversar no WhatsApp")
                    val saved = onSaveAndOpen(name, phone)
                    showError = !saved
                },
                enabled = isFilled,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.whatsappButton,
                    contentColor = colors.onWhatsappButton,
                    disabledContainerColor = colors.whatsappButton.copy(alpha = 0.5f),
                    disabledContentColor = colors.onWhatsappButton.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Salvar contato e conversar no WhatsApp",
                    fontSize = (15 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
