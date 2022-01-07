package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.extension.toResponse
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("book")
class BookController (
        val bookService: BookService,
        val customerService: CustomerService
){

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody @Valid request: PostBookRequest) {
       val customer = customerService.getById(request.customerId)
        bookService.createBook(request.toBookModel(customer))
    }

    @GetMapping
    fun findAllBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> {
        return bookService.findAllBooks(pageable).map { it.toResponse() }
    }

    @GetMapping("/active")
    fun findActiveBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> {
        return bookService.findActiveBooks(pageable).map { it.toResponse() }
    }

    @GetMapping("/{id}")
    fun findBooksById(@PathVariable id: Int): BookResponse {
        return bookService.findBooksById(id).toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) {
        bookService.deleteBook(id)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateBook(@PathVariable id: Int, @RequestBody book: PutBookRequest) {
        val bookSaved = bookService.findBooksById(id)
        bookService.updateBook(book.toBookModel(bookSaved))
    }

}