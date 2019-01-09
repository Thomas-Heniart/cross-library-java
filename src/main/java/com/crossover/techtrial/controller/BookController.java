/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.exceptions.BookNotFoundException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * BookController for Book related APIs.
 *
 * @author crossover
 */
@RestController
public class BookController {

    private BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/book")
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PostMapping(path = "/api/book")
    public ResponseEntity<Book> saveBook(@RequestBody final Book book) {
        return ResponseEntity.ok(bookService.save(book));
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/book/{book-id}")
    public ResponseEntity<Book> getRideById(@PathVariable(name = "book-id") final Long bookId) {
        try {
            return ResponseEntity.ok(bookService.findById(bookId));
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
