package com.example.projetodirigido.model

import androidx.annotation.DrawableRes
import com.example.projetodirigido.R

/**
 * Representa um banco na lista de escolha do atalho "Acesse seu banco".
 *
 * @param name Nome exibido para o usuário.
 * @param iconRes Logo oficial do banco, em res/drawable (ver pasta
 *        `res/drawable` incluída neste pacote — copie os arquivos para
 *        `app/src/main/res/drawable/` no seu projeto).
 * @param packageName Pacote do app oficial do banco no Android.
 * @param fallbackUrl Link usado caso o app não esteja instalado (abre no navegador).
 */
data class BankOption(
    val name: String,
    @DrawableRes val iconRes: Int,
    val packageName: String,
    val fallbackUrl: String
)

/**
 * Lista fixa dos principais bancos, mostrada quando o usuário toca no
 * atalho "Acesse seu banco". Pacotes conferidos na Google Play em julho/2026.
 */
object DefaultBanks {
    val list = listOf(
        BankOption(
            name = "Itaú Unibanco",
            iconRes = R.drawable.ic_bank_itau,
            packageName = "com.itau",
            fallbackUrl = "https://www.itau.com.br"
        ),
        BankOption(
            name = "Banco do Brasil",
            iconRes = R.drawable.ic_bank_bb,
            packageName = "br.com.bb.android",
            fallbackUrl = "https://www.bb.com.br"
        ),
        BankOption(
            name = "Bradesco",
            iconRes = R.drawable.ic_bank_bradesco,
            packageName = "com.bradesco",
            fallbackUrl = "https://www.bradesco.com.br"
        ),
        BankOption(
            name = "Caixa Econômica Federal",
            iconRes = R.drawable.ic_bank_caixa,
            packageName = "br.com.gabba.Caixa",
            fallbackUrl = "https://www.caixa.gov.br"
        ),
        BankOption(
            name = "Santander",
            iconRes = R.drawable.ic_bank_santander,
            packageName = "com.santander.app",
            fallbackUrl = "https://www.santander.com.br"
        ),
        BankOption(
            name = "Nubank",
            iconRes = R.drawable.ic_bank_nubank,
            packageName = "com.nu.production",
            fallbackUrl = "https://nubank.com.br"
        )
    )
}