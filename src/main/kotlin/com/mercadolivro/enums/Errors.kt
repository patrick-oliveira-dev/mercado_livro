package com.mercadolivro.enums

enum class Errors (val code: String, val message: String) {

    ML001("ML-001", "Invalid Request"),
    ML002("ML-002", "Books not available for this purchase"),
    ML101("ML-101", "Book [%s] not exists"),
    ML102("ML-102", "Cannot update book with status [%s]"),
    ML201("ML-201", "Customer [%s] not exists")

}