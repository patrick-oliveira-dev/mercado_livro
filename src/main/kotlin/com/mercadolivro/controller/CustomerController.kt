package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toResponse
import com.mercadolivro.model.BookModel
import com.mercadolivro.security.UserCanOnlyAccessTheirOwnResource
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("customer")
class CustomerController(
        val customerService: CustomerService

        ) {



    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAllCustomers(@RequestParam name: String?): List<CustomerResponse> {
       return customerService.getAllCustomers(name).map { it.toResponse() }
    }

    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    fun getById(@PathVariable id: Int): CustomerResponse {
        return customerService.getById(id).toResponse()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody @Valid customer: PostCustomerRequest) {
        customerService.createCustomer(customer.toCustomerModel())
    }

    @PutMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCustomer(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerSaved = customerService.getById(id)
        customerService.putCustomer(customer.toCustomerModel(customerSaved))
    }

    @DeleteMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Int) {
        customerService.deleteCustomer(id)
    }

    @GetMapping("{id}/sold-books")
    @UserCanOnlyAccessTheirOwnResource
    fun getBooksByCustomerId(@PathVariable id: Int): MutableList<BookModel> {
        return customerService.getBooksByCustomerId(id)
    }

    @GetMapping("{id}/my-books")
    @UserCanOnlyAccessTheirOwnResource
    fun findAllMyBooks(@PathVariable id: Int): MutableList<BookModel> {
        return customerService.findAllMyBooks(id)
    }

}