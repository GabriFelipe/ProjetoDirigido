package com.example.projetodirigido.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.projetodirigido.model.BankOption
import com.example.projetodirigido.model.DefaultBanks
import com.example.projetodirigido.ui.theme.AppColors
import com.example.projetodirigido.ui.theme.scaledDp

/**
 * Janela que lista os principais bancos para o usuário escolher qual deles
 * abrir, usada pelo atalho "Acesse seu banco" da tela inicial.
 *
 * @param onBankSelected Chamado com o banco tocado; quem chama essa função
 *        é responsável por abrir o app/navegador (ex: via [IntentHelper]) e
 *        por fechar esse diálogo.
 * @param onDismiss Chamado ao tocar em "Cancelar" ou fora da lista.
 */
@Composable
fun BankPickerDialog(
    colors: AppColors,
    fontScale: Float,
    onBankSelected: (BankOption) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "Escolha seu banco",
                fontSize = (20 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Toque no banco para abrir o aplicativo dele.",
                fontSize = (14 * fontScale).sp,
                color = colors.textSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn com altura máxima: em telas menores ou com fonte
            // ampliada, a lista dos 6 bancos rola dentro do próprio diálogo
            // em vez de estourar a altura da tela.
            LazyColumn(
                modifier = Modifier.heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(DefaultBanks.list) { bank ->
                    BankRow(
                        bank = bank,
                        colors = colors,
                        fontScale = fontScale,
                        onClick = { onBankSelected(bank) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cancelar",
                fontSize = (15 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDismiss)
                    .padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun BankRow(
    bank: BankOption,
    colors: AppColors,
    fontScale: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.scaledDp(fontScale))
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = bank.iconRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = bank.name,
                fontSize = (16 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}