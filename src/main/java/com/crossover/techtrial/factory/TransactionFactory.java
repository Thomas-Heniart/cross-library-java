package com.crossover.techtrial.factory;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import java.time.Clock;
import java.time.LocalDateTime;

public class TransactionFactory {

    private static TransactionFactory ourInstance = new TransactionFactory();

    public static TransactionFactory getInstance() {
        return ourInstance;
    }

    private TransactionFactory() {
    }

    public Transaction buildTransactionForBookAndMember(final Book book, final Member member) {
        return buildTransactionForBookAndMember(book, member, Clock.systemDefaultZone());
    }

    Transaction buildTransactionForBookAndMember(final Book book, final Member member, final Clock clock) {
        final Transaction transaction = new Transaction();

        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setDateOfIssue(LocalDateTime.now(clock));

        return transaction;
    }
}
