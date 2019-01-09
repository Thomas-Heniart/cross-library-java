/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.exceptions.UserAlreadyExistsException;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * @author crossover
 */
@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    public MemberServiceImpl(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(final Member member) {
        final Member existingMember = memberRepository.findByEmail(member.getEmail());
        if (existingMember == null) {
            return memberRepository.save(member);
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    @Override
    public Member findById(final Long memberId) {
        final Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElse(null);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
