package com.mercadolivro.service

import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.extension.toResponse
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
        val bookRepository: BookRepository,

) {

    fun createBook(book: BookModel) {
        bookRepository.save(book)

    }

    fun findAllBooks(pageable: Pageable): Page<BookModel> {
       return bookRepository.findAll(pageable)
    }

    fun findActiveBooks(pageable: Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.ATIVO, pageable)

    }

    fun findBooksById(id: Int): BookModel {
        return bookRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML101.message.format(id), Errors.ML101.code) }

    }

    fun deleteBook(id: Int) {
        val book = findBooksById(id)

        book.status = BookStatus.CANCELADO

        updateBook(book)

    }

    fun updateBook(book: BookModel) {
        bookRepository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCustomer(customer)
        for(book in books) {
            book.status = BookStatus.DELETADO
        }

        bookRepository.saveAll(books)

    }

    fun findAllByIds(bookIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(bookIds).toList()

    }


    fun purchase(books: MutableList<BookModel>) {
        books.map {
            it.status = BookStatus.VENDIDO
        }

        bookRepository.saveAll(books)
    }

    fun findSoldBooks(pageable: Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.VENDIDO, pageable)

    }

}
