package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Role
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
        val customerRepository: CustomerRepository,
        val bookRepository: BookRepository,
        val bookService: BookService,
        val purchaseRepository: PurchaseRepository,
        val bCrypt: BCryptPasswordEncoder
) {

    fun getAllCustomers(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(it)
        }

        return customerRepository.findAll().toList()
    }

    fun createCustomer(customer: CustomerModel) {
       val customerCopy = customer.copy(
               roles = setOf(Role.CUSTOMER),
               password = bCrypt.encode(customer.password)
        )
        customerRepository.save(customerCopy)

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

    fun getBooksByCustomerId(id: Int): MutableList<BookModel> {
        val customer = getById(id)
        val books = bookRepository.findByCustomer(customer)
        val soldBooks: MutableList<BookModel> = mutableListOf()
        for(book in books) {
            if (book.status == BookStatus.VENDIDO){
                soldBooks.add(book)
            } else {
                throw NotFoundException(Errors.ML003.message.format(id), Errors.ML003.code)
            }
        }

        return soldBooks.toMutableList()

    }

    fun findAllMyBooks(id: Int): MutableList<BookModel> {
        val customer = getById(id)
        val purchases = purchaseRepository.findByCustomer(customer)
        val myBooks: MutableList<BookModel> = mutableListOf()
        purchases.forEach {
            myBooks.addAll(it.books)
        }

        return myBooks

    }
}