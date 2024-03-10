package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
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
            .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));
        log.info("@@@ userId: {}", userId);

        // 2. Press 가져오기
        Press press = pressRepository.findById(pressId)
            .orElseThrow(() -> new GeneralException(Code.PRESS_NOT_FOUND));

        // 3. PressContent 가져오기
        PressContentLine pressContent = pressContentLineRepository.findByPressIdAndLineNumber(
                pressId,
                request.contentLineNumber())
            .orElseThrow(() -> new GeneralException(Code.PRESS_NOT_FOUND));

        // 4. LearnedPress 가져오기
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(user, press);

        // 5. 기존에 LearnedPressContentLine 있는지 확인
        LearnedPressContentLine learnedPressContentLine = learnedPressContentLineRepository
            .findByLearnedPressAndPressContent(learnedPress, pressContent)
            .orElse(null);
        if (learnedPressContentLine == null) {
            learnedPressContentLine = LearnedPressContentLine.builder()
                .learnedPress(learnedPress)
                .pressContent(pressContent)
                .isCorrect(request.isCorrect())
                .pressContentLineNumber(request.contentLineNumber())
                .press(press)
                .user(user)
                .userTranslatedLine(request.translateText())
                .build();
            learnedPressContentLineRepository.save(
                learnedPressContentLine);

        } else {
            learnedPressContentLine.setIsCorrect(request.isCorrect());
            learnedPressContentLine.setUserTranslatedLine(request.translateText());
        }

        return PressContentLineResponse.builder()
            .originalLineText(pressContent.getLineText())
            .userTranslatedLineText(learnedPressContentLine.getUserTranslatedLine())
            .id(learnedPressContentLine.getId())
            .isCorrect(learnedPressContentLine.getIsCorrect())
            .userTranslatedLineText(learnedPressContentLine.getUserTranslatedLine())
            .build();

    }

}
