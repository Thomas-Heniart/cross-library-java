package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TopMemberServiceImpl implements TopMemberService {

    private static final Integer TOP_FIVE_MEMBERS = 5;
    private static final Integer NULL_BOOK_COUNT = 0;

    private TransactionService transactionService;

    public TopMemberServiceImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public List<TopMemberDTO> getTopFiveMembersFromStartTimeToEndTime(final LocalDateTime startTime,
            final LocalDateTime endTime) {
        final List<Transaction> transactions = transactionService.findAllByDateOfReturnBetween(startTime, endTime);

        final Map<Member, Integer> memberAndBookCountMap = getMemberIntegerMapFromTransactions(transactions);

        return memberAndBookCountMap
                .keySet()
                .stream()
                .sorted((member1, member2) -> Integer
                        .compare(memberAndBookCountMap.get(member2), memberAndBookCountMap.get(member1)))
                .limit(TOP_FIVE_MEMBERS)
                .map(member -> new TopMemberDTO(member.getId(), member.getName(), member.getEmail(),
                        memberAndBookCountMap.get(member)))
                .collect(Collectors.toList());
    }

    private static Map<Member, Integer> getMemberIntegerMapFromTransactions(List<Transaction> transactions) {
        final Map<Member, Integer> memberAndBookCountMap = new HashMap<>();

        transactions.forEach(transaction -> {
            final Member member = transaction.getMember();
            final Integer bookCount = Optional.ofNullable(memberAndBookCountMap.get(member)).orElse(NULL_BOOK_COUNT);
            memberAndBookCountMap.put(member, bookCount + 1);
        });
        return memberAndBookCountMap;
    }
}
