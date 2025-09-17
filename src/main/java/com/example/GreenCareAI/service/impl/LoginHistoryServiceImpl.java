package com.example.GreenCareAI.service.impl;

import com.example.GreenCareAI.entity.LoginHistory;
import com.example.GreenCareAI.entity.User;
import com.example.GreenCareAI.repository.LoginHistoryRepository;
import com.example.GreenCareAI.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    public void saveLogin(User user) {
        LoginHistory history = LoginHistory.builder()
                .user(user)
                .loginAt(LocalDateTime.now())
                .build();
        loginHistoryRepository.save(history);
    }

    @Override
    public Map<String, Long> getLoginsPerDay() {
        List<Object[]> results = loginHistoryRepository.countLoginsPerDay();
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String date = row[0].toString();
            Long count = (Long) row[1];
            map.put(date, count);
        }
        return map;
    }

    @Override
    public Long getLoginsLast30Days() {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(30);
        return loginHistoryRepository.countLoginsLast30Days(fromDate);
    }
}
