package com.kidchang.lingopress.word;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.entity.PressTranslationContentLine;
import com.kidchang.lingopress.press.service.LearnedPressContentLineService;
import com.kidchang.lingopress.translate.TranslateService;
import com.kidchang.lingopress.translate.dto.request.LingoGptRequest;
import com.kidchang.lingopress.translate.dto.response.TranslateTextResponse;
import com.kidchang.lingopress.user.User;
import com.kidchang.lingopress.user.UserRepository;
import com.kidchang.lingopress.word.dto.request.WordToLearnRequest;
import com.kidchang.lingopress.word.dto.response.WordToLearnResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final PressRepository pressRepository;
    private final TranslateService translateService;
    private final LearnedPressContentLineService learnedPressContentLineService;

    public WordToLearnResponse enrollWordToLearn(WordToLearnRequest request) {

        // user id
        Long userId = SecurityUtil.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(Code.NOT_FOUND_USER));

        // press id
        // 2. Press 가져오기
        // Press press = pressRepository.findById(request.pressId())
        //    .orElseThrow(() -> new GeneralException(Code.PRESS_NOT_FOUND));

        // 번역된 내용이 없으면 번역을 먼저 해야한다.

        LingoGptRequest lingoGptRequest;
        PressTranslationContentLine pressTranslationContentLine = null;
        if (request.translatedText() == null) {
            pressTranslationContentLine = learnedPressContentLineService.createPressTranslationContentLine(request.pressId(), request.lineNumber(), user, request.originalText());

            lingoGptRequest = LingoGptRequest.builder()
                    .original_text(request.originalText())
                    .translated_text(pressTranslationContentLine.getTranslatedLineContent())
                    .word(request.word())
                    .target_language(user.getTargetLanguage())
                    .user_language(user.getUserLanguage())
                    .build();

        } else {
            lingoGptRequest = LingoGptRequest.builder()
                    .original_text(request.originalText())
                    .translated_text(request.translatedText())
                    .word(request.word())
                    .target_language(user.getTargetLanguage())
                    .user_language(user.getUserLanguage())
                    .build();

        }

        // 3. 뜻 해석하기
        TranslateTextResponse translateTextResponse = translateService.translateWithUsageTracker(lingoGptRequest);


        WordToLearn wordToLearn = WordToLearn.builder()
                // .user(user)
                .userId(userId)
                .word(request.word())
                .originalLineText(request.originalText())
                // .press(press)
                .pressId(request.pressId())
                .lineNumber(request.lineNumber())
                .language(user.getUserLanguage())
                .translatedWord(translateTextResponse.getTranslatedText())
                .build();

        WordToLearn save = wordRepository.save(wordToLearn);

        // WordToLearnResponse 빌더 생성
        WordToLearnResponse.WordToLearnResponseBuilder responseBuilder = WordToLearnResponse.builder()
                .id(save.getId())
                .word(save.getWord())
                .originalLineText(save.getOriginalLineText())
                .lineNumber(save.getLineNumber())
                .translatedWord(save.getTranslatedWord());

        // 조건에 따라 translatedLineText 설정
        if (pressTranslationContentLine != null) {
            responseBuilder.translatedLineText(pressTranslationContentLine.getTranslatedLineContent());
        }

        return responseBuilder.build();
    }

    public List<WordToLearnResponse> getWordToLearn(Long pressId) {
        // user id, press id기반으로 유저가 모른다고 등록한 단어 가져오기
        Long userId = SecurityUtil.getUserId();

        List<WordToLearn> wordsToLearn = wordRepository.findAllByUserIdAndPressIdOrderByIdDesc(userId,
                pressId);

        return WordToLearnResponse.listOf(wordsToLearn);
    }

    public List<WordToLearnResponse> getWordToLearn() {
        // user id기반으로 유저가 모른다고 등록한 단어 가져오기
        Long userId = SecurityUtil.getUserId();

        List<WordToLearn> wordsToLearn = wordRepository.findAllByUserIdOrderByIdDesc(userId);

        return WordToLearnResponse.listOf(wordsToLearn);
    }

    @Transactional
    public String updateWord(Long wordId, WordToLearnRequest translatedWord) {
        Optional<WordToLearn> word = wordRepository.findById(wordId);
        word.ifPresent(wordToLearn -> {
            wordToLearn.updateTranslatedWord(translatedWord.word());
        });

        // 번경된 단어 리턴
        return translatedWord.word();
    }

    public String deleteWord(Long wordId) {
        wordRepository.deleteById(wordId);
        return "success";
    }
}
