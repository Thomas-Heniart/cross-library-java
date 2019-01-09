package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TransactionDTO;
import com.crossover.techtrial.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction issueBookToMember(TransactionDTO transactionDTO);

    Transaction returnBookTransaction(Long transactionId);

    List<Transaction> findAllByDateOfReturnBetween(LocalDateTime startTime, LocalDateTime endTime);
}
