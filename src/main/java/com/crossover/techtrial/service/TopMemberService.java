package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopMemberDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface TopMemberService {

    List<TopMemberDTO> getTopFiveMembersFromStartTimeToEndTime(LocalDateTime startTime, LocalDateTime endTime);
}
