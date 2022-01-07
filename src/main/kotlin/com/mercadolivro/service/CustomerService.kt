package com.mercadolivro.service

import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
        val customerRepository: CustomerRepository,
        val bookService: BookService
) {

    fun getAllCustomers(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(it)
        }

        return customerRepository.findAll().toList()
    }

    fun createCustomer(customer: CustomerModel) {
        customerRepository.save(customer)

    }

    fun getById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code) }
    }

    fun putCustomer(customer: CustomerModel) {

        if(!customerRepository.existsById(customer.id!!)){
            throw Exception()
        }

        customerRepository.save(customer)
    }

    fun deleteCustomer(id: Int) {

        val customer = getById(id)
        bookService.deleteByCustomer(customer)

        customerRepository.deleteById(id)
    }

    fun emailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)

    }

}