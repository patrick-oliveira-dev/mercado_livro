package com.mercadolivro.enums

enum class Errors (val code: String, val message: String) {

    ML001("ML-001", "Invalid Request"),
    ML002("ML-002", "Books not available for this purchase"),
    ML003("ML-003", "The customer [%s] no have books"),
    ML101("ML-101", "Book [%s] not exists"),
    ML102("ML-102", "Cannot update book with status [%s]"),
    ML201("ML-201", "Customer [%s] not exists"),
    ML301("ML-301", "Acesso Negado"),
    ML302("ML-302", "Token Invalido")

}