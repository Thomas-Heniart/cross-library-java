package com.crossover.techtrial.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.crossover.techtrial.dto.TransactionDTO;
import com.crossover.techtrial.exceptions.BookAlreadyIssuedException;
import com.crossover.techtrial.exceptions.BookAlreadyReturnedException;
import com.crossover.techtrial.exceptions.InvalidTimeRangeException;
import com.crossover.techtrial.exceptions.TooManyBooksIssuedException;
import com.crossover.techtrial.exceptions.TransactionNotFoundException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
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
public class TransactionServiceImplTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime AFTER_NOW = NOW.plusHours(1);

    private static final String BOOK_ID_KEY = "bookId";
    private static final Long BOOK_ID_VALUE = 1L;
    private static final String MEMBER_ID_KEY = "memberId";
    private static final Long MEMBER_ID_VALUE = 1L;
    private static final String BOOK_ALREADY_ISSUED = "Book already issued";
    private static final String BOOK_ALREADY_RETURNED = "Book already returned";
    private static final Long TRANSACTION_ID = 1L;
    private static final Long WRONG_TRANSACTION_ID = -1L;
    private static final String TRANSACTION_NOT_FOUND = "Transaction not found";
    private static final String INVALID_TIME_RANGE = "Invalid time range chosen";
    private static final String TOO_MANY_BOOKS_ISSUED = "Too many books issued";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private BookService bookService;
    @Mock
    private MemberService memberService;
    @Mock
    private Book book;
    @Mock
    private Member member;
    @Mock
    private Transaction transaction;
    @Mock
    private TransactionDTO transactionDTO;

    private TransactionService transactionService;

    @Before
    public void setUp() throws Exception {
        transactionService = new TransactionServiceImpl(transactionRepository, bookService, memberService);
        mockTransactionDTO();
    }

    @Test
    public void givenParamsWhenIssueBookToMemberThenTransactionSaved() throws Exception {
        //Arrange
        when(bookService.findById(BOOK_ID_VALUE)).thenReturn(book);
        when(memberService.findById(MEMBER_ID_VALUE)).thenReturn(member);
        doReturn(transaction).when(transactionRepository).save(any(Transaction.class));

        //Act
        final Transaction resultTransaction = transactionService.issueBookToMember(transactionDTO);

        //Assert
        assertThat(resultTransaction, is(transaction));
    }

    @Test
    public void givenAlreadyIssuedBookWhenIssuedBookToMemberThenException() throws Exception {
        //Arrange
        when(transactionRepository.findByBook_IdAndDateOfReturnNull(BOOK_ID_VALUE))
                .thenReturn(Optional.of(transaction));
        when(transaction.getDateOfReturn()).thenReturn(null);

        //Assert
        exception.expect(BookAlreadyIssuedException.class);
        exception.expectMessage(BOOK_ALREADY_ISSUED);

        //Act
        transactionService.issueBookToMember(transactionDTO);
    }

    @Test
    public void givenTransactionIdWhenReturnBookTransactionThenTransactionDateUpdated() throws Exception {
        //Arrange
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        //Act
        final Transaction transactionUpdated = transactionService.returnBookTransaction(TRANSACTION_ID);

        //Assert
        assertThat(transactionUpdated, is(transaction));
        verify(transaction).setDateOfReturn(any(LocalDateTime.class));
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void givenTransactionAlreadyReturnsWhenReturnBookTransactionThenException() throws Exception {
        //Arrange
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));
        when(transaction.getDateOfReturn()).thenReturn(NOW);

        //Assert
        exception.expect(BookAlreadyReturnedException.class);
        exception.expectMessage(BOOK_ALREADY_RETURNED);

        //Act
        transactionService.returnBookTransaction(TRANSACTION_ID);
    }

    @Test
    public void givenWrongTransactionIdWhenReturnBookTransactionThenException() throws Exception {
        //Arrange
        when(transactionRepository.findById(WRONG_TRANSACTION_ID)).thenReturn(Optional.empty());

        //Assert
        exception.expect(TransactionNotFoundException.class);
        exception.expectMessage(TRANSACTION_NOT_FOUND);

        //Act
        transactionService.returnBookTransaction(WRONG_TRANSACTION_ID);
    }

    @Test
    public void givenStartTimeAfterEndTimeWhenFindAllInRangeThenException() throws Exception {
        //Assert
        exception.expect(InvalidTimeRangeException.class);
        exception.expectMessage(INVALID_TIME_RANGE);

        //Act
        transactionService.findAllByDateOfReturnBetween(AFTER_NOW, NOW);
    }

    @Test
    public void givenStartTimeAndEndTimeWhenFindAllInRangeThenTransactions() throws Exception {
        //Arrange
        final List<Transaction> transactionList = mockTransactions();
        when(transactionRepository.findAllByDateOfReturnBetween(NOW, AFTER_NOW))
                .thenReturn(transactionList);

        //Act
        final List<Transaction> result = transactionService.findAllByDateOfReturnBetween(NOW, AFTER_NOW);

        //Assert
        assertThat(result, is(transactionList));
    }

    @Test
    public void givenFiveBooksIssuedAndNotReturnedWhenIssueBookToMemberThenException() throws Exception {
        //Arrange
        final List<Transaction> transactionList = mockFiveTransactionsNotReturned();
        when(transactionRepository.findAllByMember_IdAndAndDateOfReturnNull(MEMBER_ID_VALUE))
                .thenReturn(transactionList);

        //Assert
        exception.expect(TooManyBooksIssuedException.class);
        exception.expectMessage(TOO_MANY_BOOKS_ISSUED);

        //Act
        transactionService.issueBookToMember(transactionDTO);
    }

    private void mockTransactionDTO() {
        when(transactionDTO.getBookId()).thenReturn(BOOK_ID_VALUE);
        when(transactionDTO.getMemberId()).thenReturn(MEMBER_ID_VALUE);
    }

    private List<Transaction> mockTransactions() {
        return Collections.singletonList(transaction);
    }

    private List<Transaction> mockFiveTransactionsNotReturned() {
        return Arrays.asList(transaction, transaction, transaction, transaction, transaction);
    }
}
