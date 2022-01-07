package com.mercadolivro.extension

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(name = this.name,
            email = this.email,
            status = CustomerStatus.ATIVO,
            password = this.password)
}

fun PutCustomerRequest.toCustomerModel(oldCustomer: CustomerModel): CustomerModel {
    return CustomerModel(id = oldCustomer.id,
            name = this.name,
            email = this.email,
            status = oldCustomer.status,
            password = oldCustomer.password)
}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(
            name = this.name,
            price = this.price,
            status = BookStatus.ATIVO,
            customer = customer
    )
}

fun PutBookRequest.toBookModel(bookToChange: BookModel): BookModel {
    return BookModel(
            id = bookToChange.id,
            name = this.name?: bookToChange.name,
            price = this.price?: bookToChange.price,
            status = bookToChange.status,
            customer = bookToChange.customer
    )
}

fun CustomerModel.toResponse(): CustomerResponse {
    return CustomerResponse(
            id = this.id,
            name = this.name,
            email = this.email,
            status = this.status
    )
}

fun BookModel.toResponse(): BookResponse {
    return BookResponse(
            id = this.id,
            name = this.name,
            price = this.price,
            customer = this.customer,
            status = this.status
    )
}
