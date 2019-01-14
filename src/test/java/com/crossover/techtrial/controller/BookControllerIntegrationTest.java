package com.crossover.techtrial.controller;

import static com.crossover.techtrial.util.HttpEntityHelper.getHttpEntityForJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.crossover.techtrial.exceptions.BookNotFoundException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    private static final String GET_BOOKS_URL = "/api/book";
    private static final String POST_BOOK_URL = "/api/book";
    private static final String CREATE_BOOK_JSON = "{"
            + "\"title\": \"My test book\""
            + "}";
    private static final Long BOOK_NOT_FOUND_ID = 111L;
    private static final String GET_BOOK_NOT_FOUND_URL = "/api/book/111";
    private static final Long BOOK_ID = 1L;
    private static final String GET_BOOK_URL = "/api/book/" + BOOK_ID;

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Autowired
    private TestRestTemplate template;

    @MockBean
    private BookService bookService;

    private Book bookMock;

    @Before
    public void setUp() throws Exception {
        bookMock = new Book();
    }

    @Test
    public void givenNoBooksWhenGetBooksThenEmpty() throws Exception {
        //Arrange
        when(bookService.getAll()).thenReturn(Collections.emptyList());

        //Act
        final ResponseEntity<List> response = template.getForEntity(GET_BOOKS_URL, List.class);

        //Assert
        collector.checkThat(response.getBody().isEmpty(), is(true));
        collector.checkThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void givenBookJsonWhenSaveThenBookSaved() throws Exception {
        //Arrange
        doReturn(bookMock).when(bookService).save(any(Book.class));
        final HttpEntity book = getHttpEntityForJson(CREATE_BOOK_JSON);

        //Act
        final ResponseEntity<Book> response = template.postForEntity(POST_BOOK_URL, book, Book.class);

        //Assert
        collector.checkThat(response.getBody(), is(bookMock));
        collector.checkThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void givenWrongIdWhenGetRideByIdThenNotFound() throws Exception {
        //Arrange
        doThrow(new BookNotFoundException()).when(bookService).findById(BOOK_NOT_FOUND_ID);

        //Act
        final ResponseEntity<Book> response = template.getForEntity(GET_BOOK_NOT_FOUND_URL, Book.class);

        //Assert
        collector.checkThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void givenIdWhenGetRideByIdThenBookFound() throws Exception {
        //Arrange
        doReturn(bookMock).when(bookService).findById(BOOK_ID);

        //Act
        final ResponseEntity<Book> response = template.getForEntity(GET_BOOK_URL, Book.class);

        //Assert
        collector.checkThat(response.getBody(), is(bookMock));
        collector.checkThat(response.getStatusCode(), is(HttpStatus.OK));
    }
}
