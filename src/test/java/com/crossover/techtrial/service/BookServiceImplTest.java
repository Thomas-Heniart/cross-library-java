package com.crossover.techtrial.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.crossover.techtrial.exceptions.BookNotFoundException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

    private static final Long NO_BOOK_ID = -1L;
    private static final Long BOOK_ID = 1L;
    private static final String BOOK_NOT_FOUND = "Book not found";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private BookRepository bookRepository;
    @Mock
    private Book book;

    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    public void givenNoBooksWhenGetAllThenEmpty() throws Exception {
        //Arrange
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        //Act
        final List<Book> books = bookService.getAll();

        //Assert
        assertThat(books.isEmpty(), is(true));
    }

    @Test
    public void givenBooksWhenGetAllThenBooks() throws Exception {
        //Arrange
        final Iterable<Book> booksMocked = mockBooks();
        when(bookRepository.findAll()).thenReturn(booksMocked);

        //Act
        final List<Book> books = bookService.getAll();

        //Assert
        assertThat(books, is(booksMocked));
    }

    @Test
    public void givenBookWhenSaveThenBookSaved() throws Exception {
        //Arrange
        when(bookRepository.save(book)).thenReturn(book);

        //Act
        final Book bookSaved = bookService.save(book);

        //Assert
        assertThat(bookSaved, is(book));
    }

    @Test
    public void givenNoBookWhenFindByIdThenException() throws Exception {
        //Arrange
        when(bookRepository.findById(NO_BOOK_ID)).thenReturn(Optional.empty());

        //Assert
        exception.expect(BookNotFoundException.class);
        exception.expectMessage(BOOK_NOT_FOUND);

        //Act
        bookService.findById(NO_BOOK_ID);
    }

    @Test
    public void givenBookWhenFindByIdThenBook() throws Exception {
        //Arrange
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        //Act
        final Book bookFound = bookService.findById(BOOK_ID);

        //Assert
        assertThat(bookFound, is(book));
    }

    private Iterable<Book> mockBooks() {
        return Collections.singletonList(book);
    }
}
