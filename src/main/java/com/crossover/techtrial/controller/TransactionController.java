/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TransactionDTO;
import com.crossover.techtrial.exceptions.BookAlreadyIssuedException;
import com.crossover.techtrial.exceptions.BookAlreadyReturnedException;
import com.crossover.techtrial.exceptions.BookNotFoundException;
import com.crossover.techtrial.exceptions.TooManyBooksIssuedException;
import com.crossover.techtrial.exceptions.TransactionNotFoundException;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author kshah
 */
@RestController
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     * Example Post Request :  { "bookId":1,"memberId":33 }
     */
    @PostMapping(path = "/api/transaction")
    public ResponseEntity<Transaction> issueBookToMember(@RequestBody final TransactionDTO transactionDTO) {
        try {
            return ResponseEntity.ok().body(transactionService.issueBookToMember(transactionDTO));
        } catch (BookAlreadyIssuedException | TooManyBooksIssuedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PatchMapping(path = "/api/transaction/{transaction-id}/return")
    public ResponseEntity<Transaction> returnBookTransaction(
            @PathVariable(name = "transaction-id") final Long transactionId) {
        try {
            return ResponseEntity.ok().body(transactionService.returnBookTransaction(transactionId));
        } catch (TransactionNotFoundException | BookAlreadyReturnedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
