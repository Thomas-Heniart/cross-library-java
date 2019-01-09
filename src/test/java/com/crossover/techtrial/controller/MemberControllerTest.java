/**
 *
 */
package com.crossover.techtrial.controller;

import static com.crossover.techtrial.util.HttpEntityHelper.getHttpEntityForJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.crossover.techtrial.exceptions.InvalidTimeRangeException;
import com.crossover.techtrial.exceptions.UserAlreadyExistsException;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TopMemberService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author kshah
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    private static final String GET_MEMBER_NOT_FOUND_URL = "/api/member/1111";
    private static final Long MEMBER_ID = 1L;
    private static final String GET_EXISTING_MEMBER_URL = "/api/member/" + MEMBER_ID;
    private static final String REGISTRATION_URL = "/api/member";
    private static final String MEMBER_CREATE_JSON =
            "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\","
                    + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }";
    private static final String GET_ALL_MEMBERS = "/api/member";
    private static final String GET_TOP_MEMBERS_URL = "/api/member/top-member";

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Autowired
    private TestRestTemplate template;

    @MockBean
    private MemberService memberService;
    @MockBean
    private TopMemberService topMemberService;

    private Member basicMockedMember;

    private List<Member> members;

    @Before
    public void setUp() throws Exception {
        basicMockedMember = new Member();
        final Member secondMockedMember = new Member();
        final Member thirdMockedMember = new Member();

        members = Arrays.asList(basicMockedMember, secondMockedMember, thirdMockedMember);
    }

    @Test
    public void givenMemberJsonWhenRegistrationThenMemberRegistered() throws Exception {
        //Arrange
        doReturn(basicMockedMember).when(memberService).save(any(Member.class));
        final HttpEntity<Object> member = getHttpEntityForJson(MEMBER_CREATE_JSON);

        //Act
        final ResponseEntity<Member> response = template.postForEntity(REGISTRATION_URL, member, Member.class);

        //Assert
        collector.checkThat(response.getBody(), is(basicMockedMember));
        collector.checkThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void givenNoMemberWhenGetMemberByIdThenMemberNotFound() throws Exception {
        //Act
        final ResponseEntity<Member> response = template.getForEntity(GET_MEMBER_NOT_FOUND_URL, Member.class);

        //Assert
        collector.checkThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void givenExistingMemberWhenGetMemberByIdThenMemberFound() throws Exception {
        //Arrange
        doReturn(basicMockedMember).when(memberService).findById(MEMBER_ID);

        //Act
        final ResponseEntity<Member> response = template.getForEntity(GET_EXISTING_MEMBER_URL, Member.class);

        //Assert
        collector.checkThat(response.getBody(), is(basicMockedMember));
        collector.checkThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void givenMembersWhenGetAllThenMembers() throws Exception {
        //Arrange
        doReturn(members).when(memberService).findAll();

        //Act
        final ResponseEntity<List> response = template.getForEntity(GET_ALL_MEMBERS, List.class);

        //Assert
        collector.checkThat(response.getBody().size(), is(members.size()));
        collector.checkThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void givenMemberAlreadyExistsWhenRegistrationThenBadRequest() throws Exception {
        //Arrange
        doThrow(new UserAlreadyExistsException()).when(memberService).save(any(Member.class));
        final HttpEntity<Object> member = getHttpEntityForJson(MEMBER_CREATE_JSON);

        //Act
        final ResponseEntity<Member> response = template.postForEntity(REGISTRATION_URL, member, Member.class);

        //Assert
        collector.checkThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void givenInvalidTimeRangeWhenGetTopMembersThenBadRequest() throws Exception {
        //Arrange
        doThrow(new InvalidTimeRangeException()).when(topMemberService)
                .getTopFiveMembersFromStartTimeToEndTime(any(LocalDateTime.class), any(LocalDateTime.class));

        //Act
        final ResponseEntity<Object> response = template.getForEntity(GET_TOP_MEMBERS_URL, Object.class);

        //Assert
        collector.checkThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}
