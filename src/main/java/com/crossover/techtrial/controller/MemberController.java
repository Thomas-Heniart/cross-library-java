/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.exceptions.InvalidTimeRangeException;
import com.crossover.techtrial.exceptions.UserAlreadyExistsException;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TopMemberService;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author crossover
 */

@RestController
public class MemberController {

    private static final String DEFAULT_CONSTRAINT_ERROR = "Constraint violation";

    private MemberService memberService;
    private TopMemberService topMemberService;

    public MemberController(final MemberService memberService, final TopMemberService topMemberService) {
        this.memberService = memberService;
        this.topMemberService = topMemberService;
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PostMapping(path = "/api/member")
    public ResponseEntity<Member> register(@RequestBody final Member p) {
        try {
            return ResponseEntity.ok(memberService.save(p));
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    e.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).findFirst()
                            .orElse(
                                    DEFAULT_CONSTRAINT_ERROR), e);
        }
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/member")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/member/{member-id}")
    public ResponseEntity<Member> getMemberById(@PathVariable(name = "member-id") final Long memberId) {
        final Member member = memberService.findById(memberId);
        if (member != null) {
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * This API returns the top 5 members who issued the most books within the search duration.
     * Only books that have dateOfIssue and dateOfReturn within the mentioned duration should be counted.
     * Any issued book where dateOfIssue or dateOfReturn is outside the search, should not be considered.
     *
     * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
     */
    @GetMapping(path = "/api/member/top-member")
    public ResponseEntity<List<TopMemberDTO>> getTopMembers(
            @RequestParam(value = "startTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") final LocalDateTime startTime,
            @RequestParam(value = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") final LocalDateTime endTime) {
        try {
            return ResponseEntity.ok(topMemberService.getTopFiveMembersFromStartTimeToEndTime(startTime, endTime));
        } catch (InvalidTimeRangeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
