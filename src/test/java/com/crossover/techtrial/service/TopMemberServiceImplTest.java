package com.crossover.techtrial.service;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TopMemberServiceImplTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime BEFORE_NOW = NOW.minusHours(1);
    private static final Integer TOP_MEMBERS_SIZE = 5;
    private static final Long BOOK_ID = 1L;

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Mock
    private TransactionService transactionService;

    private TopMemberService topMemberService;

    @Before
    public void setUp() throws Exception {
        topMemberService = new TopMemberServiceImpl(transactionService);
    }

    @Test
    public void givenTransactionsForSixMembersWhenGetTopMembersFromStartTimeToEndTimeThenFiveTopMembers() throws Exception {
        //Arrange
        final List<Transaction> transactions = mockTransactionsForSixMembers();
        when(transactionService.findAllByDateOfReturnBetween(BEFORE_NOW, NOW)).thenReturn(transactions);

        //Act
        final List<TopMemberDTO> topMemberDTOS = topMemberService
                .getTopFiveMembersFromStartTimeToEndTime(BEFORE_NOW, NOW);

        //Assert
        assertFiveTopMembers(topMemberDTOS);
    }

    private void assertFiveTopMembers(final List<TopMemberDTO> topMemberDTOS) {
        collector.checkThat(topMemberDTOS.size(), is(TOP_MEMBERS_SIZE));
        long i = TOP_MEMBERS_SIZE;
        for (TopMemberDTO topMemberDTO : topMemberDTOS) {
            collector.checkThat(topMemberDTO.getMemberId(), is(i));
            collector.checkThat(topMemberDTO.getEmail(), is("member" + i + "@email.com"));
            collector.checkThat(topMemberDTO.getName(), is("member" + i));
            collector.checkThat(topMemberDTO.getBookCount(), is((int) (i + 1)));
            i--;
        }
    }

    private List<Transaction> mockTransactionsForSixMembers() {
        final List<Transaction> transactions = new ArrayList<>();

        final Book bookMock = new Book();
        bookMock.setId(BOOK_ID);

        for (long i = 0; i < TOP_MEMBERS_SIZE + 1; i++) {
            final Member memberMock = new Member();
            memberMock.setId(i);
            memberMock.setName("member" + i);
            memberMock.setEmail("member" + i + "@email.com");

            for (long j = 0; j < i + 1; j++) {
                final Transaction transaction = new Transaction();
                transaction.setDateOfIssue(BEFORE_NOW);
                transaction.setDateOfReturn(NOW);
                transaction.setId(j);
                transaction.setMember(memberMock);
                transaction.setBook(bookMock);

                transactions.add(transaction);
            }
        }

        return transactions;
    }
}
