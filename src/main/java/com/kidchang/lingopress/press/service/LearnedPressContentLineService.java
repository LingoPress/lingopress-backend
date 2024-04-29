package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.learningRecord.LearningRecordService;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineMemoRequest;
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

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LearnedPressContentLineService {

    private final UserRepository userRepository;
    private final PressRepository pressRepository;
    private final LearnPressService learnPressService;
    private final LearnedPressContentLineRepository learnedPressContentLineRepository;
    private final PressContentLineRepository pressContentLineRepository;
    private final LearningRecordService learningRecordService;

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

        // 4. LearnedPress 가져오기
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(user, press);

        // 5. 기존에 LearnedPressContentLine 있는지 확인
        LearnedPressContentLine learnedPressContentLine = learnedPressContentLineRepository
                .findByLearnedPressAndPressContentLine(learnedPress, pressContentLine)
                .orElse(null);

        // 학습한 문장수 카운트
        // 만약 LearnedPressContentLine이 없거나, isCorrect가 null이라면(메모만 이용), 번역한 문장 수를 증가시킨다.
        if (learnedPressContentLine == null || learnedPressContentLine.getIsCorrect() == null) {
            LocalDate date = LocalDate.now();
            learningRecordService.increaseLearningRecord(userId, date);
        }

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
            learnedPressContentLineRepository.save(learnedPressContentLine);
            // 문장이 맞은지 여부에 상관없이 문장을 처음 해석하면 카운트 증가
            learnedPress.increaseTranslatedContentLineCount();
        } else {
            // 이미 있는 문장이라면, 기존 카운트 내역 제거
            if (learnedPressContentLine.getIsCorrect() != null) {
                learnedPress.decreaseLearnedContentLineCount();
            }

            // 만약 checkPressContentLine메서드를 실행할 때 isCorrect를 안보내면 에러 발생
            learnedPressContentLine.setIsCorrect(request.isCorrect());
            learnedPressContentLine.setUserTranslatedLine(request.translateText());
        }

        // 문장이 맞으면 learnedPress의 번역한 문장 수를 증가시킨다.
        if (request.isCorrect()) {
            learnedPress.increaseLearnedContentLineCount();
        }

        return PressContentLineResponse.builder()
                .originalLineText(pressContentLine.getLineText())
                .userTranslatedLineText(learnedPressContentLine.getUserTranslatedLine())
                .id(learnedPressContentLine.getId())
                .isCorrect(learnedPressContentLine.getIsCorrect())
                .userTranslatedLineText(learnedPressContentLine.getUserTranslatedLine())
                .build();

    }

    @Transactional
    public PressContentLineResponse writePressContentLineMemo(TranslateContentLineMemoRequest request) {
        // TODO duplicate code 줄이기
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

        // 4. LearnedPress 가져오기
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(user, press);

        // 5. 기존에 LearnedPressContentLine 있는지 확인
        LearnedPressContentLine learnedPressContentLine = learnedPressContentLineRepository
                .findByLearnedPressAndPressContentLine(learnedPress, pressContentLine)
                .orElse(null);
        if (learnedPressContentLine == null) {
            // isCorrect, userTranslatedLine을 null로 설정해서 가져온다.
            learnedPressContentLine = LearnedPressContentLine.builder()
                    .learnedPress(learnedPress)
                    .pressContentLine(pressContentLine)
                    .isCorrect(null)
                    .lineNumber(request.contentLineNumber())
                    .press(press)
                    .user(user)
                    .userTranslatedLine(null)
                    // 메모 저장
                    .memo(request.memo())
                    .build();
            learnedPressContentLineRepository.save(
                    learnedPressContentLine);
        } else {
            // 메모 저장
            learnedPressContentLine.setMemo(request.memo());
        }


        // 기록된 LearnedPressContentLine을 가져오고, 없으면 isCorrect를 null로 설정해서 가져온다.
        return PressContentLineResponse.builder()
                .id(learnedPressContentLine.getId())
                .memo(learnedPressContentLine.getMemo())
                .build();

    }

}
