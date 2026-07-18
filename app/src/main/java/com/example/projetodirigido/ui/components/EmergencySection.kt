package com.example.projetodirigido.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetodirigido.model.EmergencyContact
import com.example.projetodirigido.model.EmergencyNumbers
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.util.rememberPhoneCaller

/**
 * Seção de emergência: permite cadastrar um contato da família para ligar
 * com 1 toque, e sempre mostra SAMU/Bombeiros/Polícia também a 1 toque.
 *
 * @param contact Contato já salvo (ou null se ainda não cadastrou nenhum).
 * @param onSaveContact Chamado ao tocar em "Salvar contato". Retorna se o
 *        ViewModel aceitou os dados; se false, mostramos um erro na tela.
 */
@Composable
fun EmergencySection(
    colors: AppColors,
    fontScale: Float,
    contact: EmergencyContact?,
    onSaveContact: (name: String, phone: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    val call = rememberPhoneCaller()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.dangerBackground, RoundedCornerShape(20.dp))
            .border(BorderStroke(2.dp, colors.dangerBorder), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "Emergência",
            fontSize = (22 * fontScale).sp,
            fontWeight = FontWeight.Bold,
            color = colors.dangerText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Em caso de urgência, toque em um dos botões abaixo.",
            fontSize = (14 * fontScale).sp,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        FamilyContactCard(
            colors = colors,
            fontScale = fontScale,
            contact = contact,
            onSaveContact = onSaveContact,
            onCall = { phone -> call(phone) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            EmergencyNumbers.list.forEach { number ->
                EmergencyCallButton(
                    label = number.name,
                    phone = number.phone,
                    icon = emojiFor(number.name),
                    colors = colors,
                    fontScale = fontScale,
                    onClick = { call(number.phone) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun emojiFor(serviceName: String): String = when (serviceName) {
    "SAMU" -> "❤️"
    "Bombeiros" -> "🔥"
    "Polícia" -> "🛡️"
    else -> "📞"
}

@Composable
private fun FamilyContactCard(
    colors: AppColors,
    fontScale: Float,
    contact: EmergencyContact?,
    onSaveContact: (name: String, phone: String) -> Boolean,
    onCall: (String) -> Unit
) {
    // Modo de edição/cadastro: começa aberto se ainda não há contato salvo.
    // A chave (contact == null) faz esse estado resetar sozinho para "fechado"
    // assim que o primeiro cadastro é salvo (transição null -> não-null).
    var isEditing by remember(contact == null) { mutableStateOf(contact == null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        if (contact != null && !isEditing) {
            // Já existe um contato salvo: mostra nome/telefone e os botões
            // de Editar e Ligar (1 toque), sem precisar preencher o
            // formulário de novo.
            Text(
                text = "Contato da família",
                fontSize = (13 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Nome e telefone sempre em largura total, um abaixo do outro:
            // isso evita que, em telas estreitas ou com a fonte ampliada
            // (botão "+A"), o texto seja espremido pelos botões ao lado até
            // quase 0dp de largura e quebre letra por letra. Os botões ficam
            // numa linha própria logo abaixo, também com largura garantida.
            Text(
                text = contact.name,
                fontSize = (17 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = formatPhoneDisplay(contact.phone),
                fontSize = (14 * fontScale).sp,
                color = colors.textSecondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { isEditing = true },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, colors.border),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.textPrimary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "✏️ Editar",
                        fontSize = (14 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                Button(
                    onClick = { onCall(contact.phone) },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.dangerButton,
                        contentColor = colors.onDangerButton
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "📞 Ligar",
                        fontSize = (15 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        } else {
            // Formulário de cadastro OU edição. Quando editando um contato
            // que já existe, os campos vêm pré-preenchidos com os valores
            // atuais para o usuário só corrigir o que precisar.
            var name by remember(contact) { mutableStateOf(contact?.name ?: "") }
            var phone by remember(contact) { mutableStateOf(contact?.phone ?: "") }
            var showError by remember { mutableStateOf(false) }

            Text(
                text = if (contact != null) "Editar contato da família:" else "Cadastre um contato da família:",
                fontSize = (14 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Em telas estreitas, os dois campos lado a lado ficariam
            // apertados demais (ex: rótulo "Telefone" cortado). Por isso,
            // medimos o espaço disponível e empilhamos os campos em vez de
            // colocá-los lado a lado quando a largura for pequena.
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val stackFields = maxWidth < 340.dp

                if (stackFields) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                showError = false
                            },
                            label = { Text("Nome") },
                            placeholder = { Text("Ex: Maria (filha)") },
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
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                showError = false
                            },
                            label = { Text("Nome") },
                            placeholder = { Text("Ex: Maria (filha)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
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
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (showError) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Preencha o nome e um telefone válido para salvar.",
                    fontSize = (12 * fontScale).sp,
                    color = colors.dangerText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val isFilled = name.isNotBlank() && phone.filter { it.isDigit() }.length >= 8

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val saved = onSaveContact(name, phone)
                        if (saved) {
                            isEditing = false
                        }
                        showError = !saved
                    },
                    enabled = isFilled,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = colors.textPrimary,
                        disabledContainerColor = colors.accent.copy(alpha = 0.5f),
                        disabledContentColor = colors.textPrimary.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (contact != null) "✓ Salvar alterações" else "✓ Salvar contato",
                        fontSize = (15 * fontScale).sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // "Cancelar" só faz sentido quando já existia um contato
                // salvo antes (edição) — em um cadastro novo não há para
                // onde voltar. Fica em largura total, abaixo do botão de
                // salvar, para nunca ser espremido a ponto de quebrar em
                // "Canc" / "elar" como acontecia ao lado do botão largo.
                if (contact != null) {
                    TextButton(
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = (14 * fontScale).sp,
                            color = colors.textSecondary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun EmergencyCallButton(
    label: String,
    phone: String,
    icon: String,
    colors: AppColors,
    fontScale: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(colors.dangerButton, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(text = icon, fontSize = (24 * fontScale).sp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = (16 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.onDangerButton
            )
            Text(
                text = "📞 $phone",
                fontSize = (13 * fontScale).sp,
                color = colors.onDangerButton.copy(alpha = 0.9f)
            )
        }
    }
}

private fun formatPhoneDisplay(digits: String): String {
    return when (digits.length) {
        11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
        10 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 6)}-${digits.substring(6)}"
        else -> digits
    }
}