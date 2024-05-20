package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.press.dto.response.LearnedPressResponse;
import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.press.repository.LearnedPressRepository;
import com.kidchang.lingopress.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearnPressService {

    private final LearnedPressRepository learnedPressRepository;

    public LearnedPress findOrCreateLearnedPress(User user, Press press) {
        // 3. pressId와 userId를 기반으로 learnedPress를 조회한다.
        Optional<LearnedPress> learnedPress = learnedPressRepository.findByUserAndPress(
                user, press);

        // 4. learnedPress가 없으면 새로 생성한다.
        return learnedPress.orElseGet(() -> learnedPressRepository.save(LearnedPress.builder()
                .user(user)
                .press(press)
                .userLanguage(user.getUserLanguage())
                .build()));

    }


    public Slice<LearnedPressResponse> getLearnedPressList(Pageable pageable) {
        Long userId = SecurityUtil.getUserId();
        Slice<LearnedPress> pressSlice = learnedPressRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);
        Slice<LearnedPressResponse> learnedPressResponses = pressSlice.map(
                LearnedPressResponse::from);
        return learnedPressResponses;
    }

}
