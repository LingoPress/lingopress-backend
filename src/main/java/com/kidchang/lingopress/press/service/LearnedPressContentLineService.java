package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.apiUsage.ApiUsageEnum;
import com.kidchang.lingopress.apiUsage.ApiUsageTracker;
import com.kidchang.lingopress.apiUsage.ApiUsageTrackerService;
import com.kidchang.lingopress.client.AITextSimilarityAnalysisClient;
import com.kidchang.lingopress.learningRecord.LearningRecordService;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.dto.request.TextSimilarityAnalysisRequest;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineMemoRequest;
import com.kidchang.lingopress.press.dto.request.TranslateContentLineRequest;
import com.kidchang.lingopress.press.dto.response.PressContentLineResponse;
import com.kidchang.lingopress.press.dto.response.TextSimilarityAnalysisResponse;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final AITextSimilarityAnalysisClient aiTextSimilarityAnalysisClient;
    private final ApiUsageTrackerService apiUsageTrackerService;

    private final PressService pressService;
    private UserService userService;

    @Transactional
    public PressContentLineResponse checkPressContentLine(TranslateContentLineRequest request) {
        User user = userService.getUser();
        Press press = getPress(request.pressId());
        PressContentLine pressContentLine = getPressContentLine(press.getId(), request.contentLineNumber());
        LearnedPress learnedPress = getOrCreateLearnedPress(user, press);
        LearnedPressContentLine learnedPressContentLine = getOrCreateLearnedPressContentLine(learnedPress, pressContentLine, user, request);

        return buildPressContentLineResponse(pressContentLine, learnedPressContentLine);
    }


    private Press getPress(Long pressId) {
        return pressService.getPressById(pressId);
    }

    private PressContentLine getPressContentLine(Long pressId, Integer contentLineNumber) {
        return pressContentLineRepository.findByPressIdAndLineNumber(pressId, contentLineNumber)
                .orElseThrow(() -> new BusinessException(Code.PRESS_CONTENT_LINE_NOT_FOUND));
    }

    private LearnedPress getOrCreateLearnedPress(User user, Press press) {
        return learnPressService.findOrCreateLearnedPress(user, press);
    }

    private LearnedPressContentLine getOrCreateLearnedPressContentLine(LearnedPress learnedPress, PressContentLine pressContentLine, User user, TranslateContentLineRequest request) {
        LearnedPressContentLine learnedPressContentLine = learnedPressContentLineRepository
                .findByLearnedPressAndPressContentLine(learnedPress, pressContentLine)
                .orElse(null);

        if (learnedPressContentLine == null || learnedPressContentLine.getIsCorrect() == null) {
            increaseLearningRecord(user.getId(), LocalDate.now());
        }

        if (learnedPressContentLine == null) {
            learnedPressContentLine = createNewLearnedPressContentLine(learnedPress, pressContentLine, user, request);
        } else {
            updateLearnedPressContentLine(learnedPressContentLine, request);
        }

        return learnedPressContentLine;
    }

    private void increaseLearningRecord(Long userId, LocalDate date) {
        learningRecordService.increaseLearningRecord(userId, date);
    }

    private LearnedPressContentLine createNewLearnedPressContentLine(LearnedPress learnedPress, PressContentLine pressContentLine, User user, TranslateContentLineRequest request) {
        LearnedPressContentLine newLearnedPressContentLine = LearnedPressContentLine.builder()
                .learnedPress(learnedPress)
                .pressContentLine(pressContentLine)
                .isCorrect(request.isCorrect())
                .lineNumber(request.contentLineNumber())
                .press(pressContentLine.getPress())
                .user(user)
                .userTranslatedLine(request.translateText())
                .build();
        learnedPressContentLineRepository.save(newLearnedPressContentLine);
        learnedPress.increaseTranslatedContentLineCount();
        if (request.isCorrect()) {
            learnedPress.increaseLearnedContentLineCount();
        }
        return newLearnedPressContentLine;
    }

    private void updateLearnedPressContentLine(LearnedPressContentLine learnedPressContentLine, TranslateContentLineRequest request) {
        if (learnedPressContentLine.getIsCorrect() != null) {
            if (!learnedPressContentLine.getIsCorrect() && request.isCorrect()) {
                learnedPressContentLine.getLearnedPress().increaseLearnedContentLineCount();
            }
            if (learnedPressContentLine.getIsCorrect() && !request.isCorrect()) {
                learnedPressContentLine.getLearnedPress().decreaseLearnedContentLineCount();
            }
        } else {
            if (request.isCorrect()) {
                learnedPressContentLine.getLearnedPress().increaseLearnedContentLineCount();
            }
            learnedPressContentLine.getLearnedPress().increaseTranslatedContentLineCount();
        }
        learnedPressContentLine.setIsCorrect(request.isCorrect());
        learnedPressContentLine.setUserTranslatedLine(request.translateText());
    }

    private PressContentLineResponse buildPressContentLineResponse(PressContentLine pressContentLine, LearnedPressContentLine learnedPressContentLine) {
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
        User user = userService.getUser();
        // 2. Press 가져오기
        Press press = getPress(pressId);

        // 3. PressContentLine 가져오기
        PressContentLine pressContentLine = getPressContentLine(pressId, request.contentLineNumber());

        // 4. LearnedPress 가져오기
        LearnedPress learnedPress = getOrCreateLearnedPress(user, press);

        // 5. 기존에 LearnedPressContentLine 있는지 확인
        LearnedPressContentLine learnedPressContentLine = getOrUpdateMemo(request, learnedPress, pressContentLine, press, user);

        // 기록된 LearnedPressContentLine을 가져오고, 없으면 isCorrect를 null로 설정해서 가져온다.
        return buildMemoResponse(learnedPressContentLine);
    }

    private LearnedPressContentLine getOrUpdateMemo(TranslateContentLineMemoRequest request, LearnedPress learnedPress, PressContentLine pressContentLine, Press press, User user) {
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
        return learnedPressContentLine;
    }

    private PressContentLineResponse buildMemoResponse(LearnedPressContentLine learnedPressContentLine) {
        return PressContentLineResponse.builder()
                .id(learnedPressContentLine.getId())
                .memo(learnedPressContentLine.getMemo())
                .build();
    }

    public Slice<PressContentLineResponse> getMemoList(Pageable pageable) {
        Long userId = SecurityUtil.getUserId();


        //해당 유저가 작성한 learnedPressContentLine 중 메모가 작성된 것만 가져온다.

        Slice<LearnedPressContentLine> learnedPressContentLines = learnedPressContentLineRepository
                .findByUserAndMemoIsNotNull(userId, pageable);

        return learnedPressContentLines.map(learnedPressContentLine -> PressContentLineResponse.builder()
                .id(learnedPressContentLine.getId())
                .pressContentLineNumber(learnedPressContentLine.getLineNumber())
                .userTranslatedLineText(learnedPressContentLine.getUserTranslatedLine())
                .machineTranslatedLineText(learnedPressContentLine.getPressContentLine().getTranslatedLineText())
                .originalLineText(learnedPressContentLine.getPressContentLine().getLineText())
                .isCorrect(learnedPressContentLine.getIsCorrect())
                .memo(learnedPressContentLine.getMemo())
                .pressId(learnedPressContentLine.getPress().getId())
                .build());
    }

    public TextSimilarityAnalysisResponse checkPressContentLineSimilarity(TextSimilarityAnalysisRequest request) {
        Long userId = SecurityUtil.getUserId();
        User user = userService.getUser();

        // 번역된 내용이 없으면 번역을 먼저 해야한다.
        createPressTranslationContentLine(request.press_id(), request.line_number(), user, request.original_text());

        ApiUsageTracker tracker = apiUsageTrackerService.createOrUpdateApiUsageTracker(userId, ApiUsageEnum.SIMILARITY);

        TextSimilarityAnalysisResponse textSimilarityAnalysisResponse = aiTextSimilarityAnalysisClient.checkPressContentLineSimilarity(request);

        return TextSimilarityAnalysisResponse.builder()
                .similarity(textSimilarityAnalysisResponse.similarity())
                .similarityApiUsage(tracker.getSimilarityApiCount())
                .build();
    }
}
