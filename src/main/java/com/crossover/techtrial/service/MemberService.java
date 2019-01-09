/**
 *
 */
package com.crossover.techtrial.service;

import java.util.List;
import com.crossover.techtrial.model.Member;

/**
 * RideService for rides.
 *
 * @author crossover
 */
public interface MemberService {

    Member save(Member member);

    Member findById(Long memberId);

    List<Member> findAll();
}
