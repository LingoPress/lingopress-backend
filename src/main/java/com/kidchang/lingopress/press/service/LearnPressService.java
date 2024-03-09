package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.press.repository.LearnedPressRepository;
import com.kidchang.lingopress.user.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearnPressService {

    private final LearnedPressRepository learnedPressRepository;

    public LearnedPress findOrCreateLearnedPress(User user, Press press) {
        // 3. pressId와 userId를 기반으로 learnedPress를 조회한다.
        Optional<LearnedPress> learnedPress = learnedPressRepository.findByUserAndPress(
            user, press);
        log.info("@@@ learnedPress: {}", learnedPress.toString());

        // 4. learnedPress가 없으면 새로 생성한다.
        if (learnedPress.isEmpty()) {
            return learnedPressRepository.save(LearnedPress.builder()
                .user(user)
                .press(press)
                .build());
        }

        return learnedPress.get();
    }

}
