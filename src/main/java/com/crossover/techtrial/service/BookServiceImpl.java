/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.exceptions.BookNotFoundException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * @author crossover
 */
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAll() {
        final List<Book> personList = new ArrayList<>();
        bookRepository.findAll().forEach(personList::add);
        return personList;

    }

    public Book save(final Book p) {
        return bookRepository.save(p);
    }

    @Override
    public Book findById(final Long bookId) {
        final Optional<Book> bookOptional = bookRepository.findById(bookId);
        return bookOptional.orElseThrow(BookNotFoundException::new);
    }
}
