package com.crossover.techtrial.dto;

import java.io.Serializable;

public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = -5241781253380015253L;

    private Long memberId;
    private Long bookId;

    public TransactionDTO() {

    }

    public TransactionDTO(Long memberId, Long bookId) {
        this.memberId = memberId;
        this.bookId = bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public TransactionDTO setMemberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public Long getBookId() {
        return bookId;
    }

    public TransactionDTO setBookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }
}
