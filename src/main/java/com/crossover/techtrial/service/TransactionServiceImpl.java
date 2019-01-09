package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TransactionDTO;
import com.crossover.techtrial.exceptions.BookAlreadyIssuedException;
import com.crossover.techtrial.exceptions.BookAlreadyReturnedException;
import com.crossover.techtrial.exceptions.InvalidTimeRangeException;
import com.crossover.techtrial.exceptions.TooManyBooksIssuedException;
import com.crossover.techtrial.exceptions.TransactionNotFoundException;
import com.crossover.techtrial.factory.TransactionFactory;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String BOOK_ID_KEY = "bookId";
    private static final String MEMBER_ID_KEY = "memberId";
    private static final int MAX_PENDING_TRANSACTIONS = 5;

    private TransactionRepository transactionRepository;
    private BookService bookService;
    private MemberService memberService;

    public TransactionServiceImpl(final TransactionRepository transactionRepository, final BookService bookService,
            final MemberService memberService) {
        this.transactionRepository = transactionRepository;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    /* @TODO: Handle member null, wrap MAP API */
    @Override
    public Transaction issueBookToMember(final TransactionDTO transactionDTO) {
        final Long bookId = transactionDTO.getBookId();
        final Long memberId = transactionDTO.getMemberId();

        final Book book = bookService.findById(bookId);
        final Member member = memberService.findById(memberId);

        checkExistingTransactionsForBookId(bookId);

        checkCurrentTransactionsForMemberId(memberId);

        final Transaction transaction = TransactionFactory.getInstance().buildTransactionForBookAndMember(book, member);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction returnBookTransaction(final Long transactionId) {
        final Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);

        if (transactionOptional.isPresent()) {
            final Transaction transaction = transactionOptional.get();
            if (transaction.getDateOfReturn() == null) {
                transaction.setDateOfReturn(LocalDateTime.now());
                return transactionRepository.save(transaction);
            } else {
                throw new BookAlreadyReturnedException();
            }
        } else {
            throw new TransactionNotFoundException();
        }
    }

    @Override
    public List<Transaction> findAllByDateOfReturnBetween(final LocalDateTime startTime, final LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeRangeException();
        }

        return transactionRepository.findAllByDateOfReturnBetween(startTime, endTime);
    }

    private void checkExistingTransactionsForBookId(final Long bookId) {
        final Optional<Transaction> transactionOptional = transactionRepository
                .findByBook_IdAndDateOfReturnNull(bookId);

        if (transactionOptional.isPresent() && transactionOptional.get().getDateOfReturn() == null) {
            throw new BookAlreadyIssuedException();
        }
    }

    private void checkCurrentTransactionsForMemberId(final Long memberId) {
        final List<Transaction> currentTransactions = transactionRepository
                .findAllByMember_IdAndAndDateOfReturnNull(memberId);

        if (currentTransactions.size() >= MAX_PENDING_TRANSACTIONS) {
            throw new TooManyBooksIssuedException();
        }
    }
}
