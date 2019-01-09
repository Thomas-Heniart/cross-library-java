/**
 *
 */
package com.crossover.techtrial.service;

import java.util.List;
import com.crossover.techtrial.model.Book;

/**
 * BookService interface for Books.
 *
 * @author cossover
 */
public interface BookService {

    List<Book> getAll();

    Book save(Book p);

    Book findById(Long bookId);
}
