package com.crossover.techtrial.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.crossover.techtrial.exceptions.UserAlreadyExistsException;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {

    private static final Long WRONG_ID = -1L;
    private static final Long MEMBER_ID = 1L;
    private static final String ALREADY_EXISTS_EMAIL = "already@exists.com";
    private static final String USER_ALREADY_EXISTS = "This user already exists";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private Member member;
    @Mock
    private MemberRepository memberRepository;

    private MemberService memberService;

    @Before
    public void setUp() throws Exception {
        memberService = new MemberServiceImpl(memberRepository);
    }

    @Test
    public void givenMemberWhenSaveThenMemberSaved() throws Exception {
        //Arrange
        when(memberRepository.save(member)).thenReturn(member);

        //Act
        final Member memberSaved = memberService.save(member);

        //Assert
        assertThat(memberSaved, is(member));
    }

    @Test
    public void givenWrongIdWhenFindByIdThenNull() throws Exception {
        //Arrange
        when(memberRepository.findById(WRONG_ID)).thenReturn(Optional.empty());

        //Act
        final Member memberFound = memberService.findById(WRONG_ID);

        //Assert
        assertThat(memberFound, nullValue());
    }

    @Test
    public void givenIdWhenFindByIdThenMemberNotFound() throws Exception {
        //Arrange
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));

        //Act
        final Member memberFound = memberService.findById(MEMBER_ID);

        //Assert
        assertThat(memberFound, is(member));
    }

    @Test
    public void givenNoMembersWhenFindAllThenEmptyList() throws Exception {
        //Arrange
        when(memberRepository.findAll()).thenReturn(Collections.emptyList());

        //Act
        final List<Member> allMembers = memberService.findAll();

        //Assert
        assertTrue(allMembers.isEmpty());
    }

    @Test
    public void givenEmailAlreadyExistsWhenSaveThenException() throws Exception {
        //Arrange
        when(member.getEmail()).thenReturn(ALREADY_EXISTS_EMAIL);
        when(memberRepository.findByEmail(ALREADY_EXISTS_EMAIL)).thenReturn(member);

        //Assert
        exception.expect(UserAlreadyExistsException.class);
        exception.expectMessage(USER_ALREADY_EXISTS);

        //Act
        memberService.save(member);
    }
}