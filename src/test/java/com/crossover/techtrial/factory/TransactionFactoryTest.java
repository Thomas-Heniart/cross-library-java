package com.crossover.techtrial.factory;

import static org.hamcrest.Matchers.is;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionFactoryTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2019, 1, 8, 18, 18);

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Mock
    private Book book;
    @Mock
    private Member member;

    private Clock testClock;
    private LocalDateTime now;
    private TransactionFactory transactionFactory;

    @Before
    public void setUp() throws Exception {
        testClock = Clock.fixed(FIXED_DATE.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        now = LocalDateTime.now(testClock);
        transactionFactory = TransactionFactory.getInstance();
    }

    @Test
    public void givenBookAndMemberWhenBuildTransactionForBookAndMemberThenTransaction() throws Exception {
        //Act
        final Transaction transaction = transactionFactory.buildTransactionForBookAndMember(book, member, testClock);

        //Assert
        collector.checkThat(transaction.getBook(), is(book));
        collector.checkThat(transaction.getMember(), is(member));
        collector.checkThat(transaction.getDateOfIssue().isEqual(now), is(true));
    }
}