package com.crossover.techtrial.controller;

import static com.crossover.techtrial.util.HttpEntityHelper.getHttpEntityForJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.crossover.techtrial.dto.TransactionDTO;
import com.crossover.techtrial.exceptions.BookAlreadyIssuedException;
import com.crossover.techtrial.exceptions.BookNotFoundException;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.TransactionService;
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
public class TransactionControllerTest {

    private static final String TRANSACTION_POST_URL = "/api/transaction";
    private static final String POST_TRANSACTION_JSON = "{"
            + "\"bookId\":1,"
            + "\"memberId\":1"
            + "}";

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Autowired
    private TestRestTemplate template;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void givenBookAlreadyIssuedWhenIssueBookThenForbidden() throws Exception {
        //Arrange
        doThrow(new BookAlreadyIssuedException()).when(transactionService).issueBookToMember(any(TransactionDTO.class));
        final HttpEntity<Object> transactionJson = getHttpEntityForJson(POST_TRANSACTION_JSON);

        //Act
        final ResponseEntity<Transaction> response = template
                .postForEntity(TRANSACTION_POST_URL, transactionJson, Transaction.class);

        //Assert
        collector.checkThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void givenBookNotFoundWhenIssueBookThenNotFound() throws Exception {
        //Arrange
        doThrow(new BookNotFoundException()).when(transactionService).issueBookToMember(any(TransactionDTO.class));
        final HttpEntity<Object> transactionJson = getHttpEntityForJson(POST_TRANSACTION_JSON);

        //Act
        final ResponseEntity<Transaction> response = template
                .postForEntity(TRANSACTION_POST_URL, transactionJson, Transaction.class);

        //Assert
        collector.checkThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
