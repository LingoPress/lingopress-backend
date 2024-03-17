package com.kidchang.lingopress.word;

import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.translate.TranslateService;
import com.kidchang.lingopress.translate.dto.request.TranslateTextRequest;
import com.kidchang.lingopress.translate.dto.response.TranslateTextResponse;
import com.kidchang.lingopress.user.UserRepository;
import com.kidchang.lingopress.word.dto.request.WordToLearnRequest;
import com.kidchang.lingopress.word.dto.response.WordToLearnResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final PressRepository pressRepository;
    private final TranslateService translateService;

    public WordToLearnResponse enrollWordToLearn(WordToLearnRequest request) {

        // user id
        Long userId = SecurityUtil.getUserId();
        // User user = userRepository.findById(userId)
        //    .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));

        // press id
        // 2. Press 가져오기
        // Press press = pressRepository.findById(request.pressId())
        //    .orElseThrow(() -> new GeneralException(Code.PRESS_NOT_FOUND));

        // 3. 뜻 해석하기
        TranslateTextResponse translateTextResponse = translateService.translateWithUsageTracker(
            TranslateTextRequest.builder()
                .originalText(request.word())
                .build());

        WordToLearn wordToLearn = WordToLearn.builder()
            // .user(user)
            .userId(userId)
            .word(request.word())
            .originalLineText(request.originalText())
            // .press(press)
            .pressId(request.pressId())
            .lineNumber(request.lineNumber())
            .translatedWord(translateTextResponse.getTranslatedText())
            .build();

        WordToLearn save = wordRepository.save(wordToLearn);

        return WordToLearnResponse.builder()
            .id(save.getId())
            .word(save.getWord())
            .originalLineText(save.getOriginalLineText())
            .lineNumber(save.getLineNumber())
            .translatedWord(save.getTranslatedWord())
            .build();
    }

    public List<WordToLearnResponse> getWordToLearn(Long pressId) {
        // user id, press id기반으로 유저가 모른다고 등록한 단어 가져오기
        Long userId = SecurityUtil.getUserId();

        List<WordToLearn> wordsToLearn = wordRepository.findAllByUserIdAndPressId(userId,
            pressId);
        
        return WordToLearnResponse.listOf(wordsToLearn);
    }
}