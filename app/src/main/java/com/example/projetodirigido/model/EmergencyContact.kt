package com.example.projetodirigido.model

/**
 * Contato de emergÃŠncia cadastrado pelo usuÃĄrio (ex: um filho ou cuidador),
 * para ligar com um toque em caso de urgÃŠncia.
 */
data class EmergencyContact(
    val name: String,
    val phone: String
)

/**
 * NÃšmeros de emergÃŠncia fixos do Brasil, sempre disponÃ­veis independente
 * de cadastro do usuÃĄrio.
 */
object EmergencyNumbers {
    val list = listOf(
        EmergencyContact(name = "SAMU", phone = "192"),
        EmergencyContact(name = "Bombeiros", phone = "193"),
        EmergencyContact(name = "Polícia", phone = "190")
    )
}