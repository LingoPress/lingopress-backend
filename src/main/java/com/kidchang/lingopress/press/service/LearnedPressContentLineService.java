package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineRequest;
import com.kidchang.lingopress.press.dto.response.PressContentLineResponse;
import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.LearnedPressContentLine;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.press.entity.PressContentLine;
import com.kidchang.lingopress.press.repository.LearnedPressContentLineRepository;
import com.kidchang.lingopress.press.repository.PressContentLineRepository;
import com.kidchang.lingopress.user.User;
import com.kidchang.lingopress.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearnedPressContentLineService {

    private final UserRepository userRepository;
    private final PressRepository pressRepository;
    private final LearnPressService learnPressService;
    private final LearnedPressContentLineRepository learnedPressContentLineRepository;
    private final PressContentLineRepository pressContentLineRepository;

    @Transactional
    public PressContentLineResponse checkPressContentLine(TranslateContentLineRequest request) {
        Long pressId = request.pressId();
        // 1. User 가져오기
        Long userId = SecurityUtil.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(Code.NOT_FOUND_USER));

        // 2. Press 가져오기
        Press press = pressRepository.findById(pressId)
                .orElseThrow(() -> new BusinessException(Code.PRESS_NOT_FOUND));

        // 3. PressContentLine 가져오기
        PressContentLine pressContentLine = pressContentLineRepository.findByPressIdAndLineNumber(
                        pressId,
                        request.contentLineNumber())
                .orElseThrow(() -> new BusinessException(Code.PRESS_NOT_FOUND));

        // 4. LearnedPressLine 가져오기
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(user, press);

        // 5. 기존에 LearnedPressContentLine 있는지 확인
        LearnedPressContentLine learnedPressContentLine = learnedPressContentLineRepository
                .findByLearnedPressAndPressContentLine(learnedPress, pressContentLine)
                .orElse(null);
        if (learnedPressContentLine == null) {
            learnedPressContentLine = LearnedPressContentLine.builder()
                    .learnedPress(learnedPress)
                    .pressContentLine(pressContentLine)
                    .isCorrect(request.isCorrect())
                    .lineNumber(request.contentLineNumber())
                    .press(press)
                    .user(user)
                    .userTranslatedLine(request.translateText())
                    .build();
            learnedPressContentLineRepository.save(
                    learnedPressContentLine);

        } else {
            // 이미 있는 문장이라면, 기존 카운트 내역 제거
            if (learnedPressContentLine.getIsCorrect()) {
                learnedPress.decreaseTranslatedLineCount();
            }
            learnedPressContentLine.setIsCorrect(request.isCorrect());
            learnedPressContentLine.setUserTranslatedLine(request.translateText());
        }

        // 문장이 맞으면 learnedPress의 번역한 문장 수를 증가시킨다.
        if (request.isCorrect()) {
            learnedPress.increaseTranslatedLineCount();
        }

        return PressContentLineResponse.builder()
                .originalLineText(pressContentLine.getLineText())
                .userTranslatedLineText(learnedPressContentLine.getUserTranslatedLine())
                .id(learnedPressContentLine.getId())
                .isCorrect(learnedPressContentLine.getIsCorrect())
                .userTranslatedLineText(learnedPressContentLine.getUserTranslatedLine())
                .build();

    }

}
