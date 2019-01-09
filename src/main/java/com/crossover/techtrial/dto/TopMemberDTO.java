/**
 *
 */
package com.crossover.techtrial.dto;

import java.io.Serializable;

/**
 * @author crossover
 */
public class TopMemberDTO implements Serializable {

    private static final long serialVersionUID = -5241781253380015253L;

    /**
     * Constructor for TopMemberDTO
     */
    public TopMemberDTO(final Long memberId,
            final String name,
            final String email,
            final Integer bookCount) {
        this.name = name;
        this.email = email;
        this.memberId = memberId;
        this.bookCount = bookCount;
    }

    private Long memberId;

    private String name;

    private String email;

    private Integer bookCount;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(final Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(final Integer bookCount) {
        this.bookCount = bookCount;
    }
}
