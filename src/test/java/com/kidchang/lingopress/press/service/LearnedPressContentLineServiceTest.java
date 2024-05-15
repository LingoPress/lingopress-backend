package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress.learningRecord.LearningRecordService;
import com.kidchang.lingopress.press.PressSteps;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.kidchang.lingopress.user.UserSteps.getNewUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev") // 괄호 안에 실행 환경을 명시해준다.
class LearnedPressContentLineServiceTest {

    @Mock
    private LearnedPressContentLineRepository learnedPressContentLineRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PressService pressService;
    @Mock
    private PressContentLineRepository pressContentLineRepository;
    @Mock
    private LearnPressService learnPressService;
    @Mock
    private LearningRecordService learningRecordService;
    @InjectMocks
    private LearnedPressContentLineService learnedPressContentLineService;

    @Test
    @DisplayName("특정 줄 메모 작성후 옳음->틀림")
    void checkPressContentLine() {
        // given

        // SecurityContext에 모의 Authentication 객체 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        Long pressId = 1L;
        int lineNumber = 1;
        TranslateContentLineRequest 뉴스1번째줄_옳음 = new TranslateContentLineRequest(pressId, lineNumber, "hello", true);
        TranslateContentLineRequest 뉴스1번째줄_틀림 = new TranslateContentLineRequest(pressId, lineNumber, "hello", false);


        User newUser = getNewUser();


        Press newPress = PressSteps.getNewPress(pressId);


        PressContentLine newPressContentLine = PressSteps.getNewPressContentLine(newPress);


        LearnedPress newLearnedPress = PressSteps.getNewLearnedPress(newUser, newPress);

        // 댓글만 작성된 LearnedPressContentLine
        LearnedPressContentLine newLearnedPressContentLine = PressSteps.getNewLearnedPressContentLineOnlyMemo(newLearnedPress, newPressContentLine, lineNumber, newPress, newUser);


        given(userRepository.findById(newUser.getId()))
                .willReturn(Optional.of(newUser));

        given(learnedPressContentLineRepository.findByLearnedPressAndPressContentLine(
                newLearnedPress,
                newPressContentLine))
                .willReturn(Optional.of(newLearnedPressContentLine));

        given(pressContentLineRepository.findByPressIdAndLineNumber(
                pressId,
                1))
                .willReturn(Optional.of(newPressContentLine));

        given(learnPressService.findOrCreateLearnedPress(any(), any()))
                .willReturn(newLearnedPress);

        // when
        learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_옳음);
        PressContentLineResponse pressContentLineResponse = learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_틀림);

        // then
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(newUser, newPress);

        assertThat(learnedPress.getLearnedContentLine()).isEqualTo(0);
        assertThat(learnedPress.getTranslatedContentLine()).isEqualTo(1);
        assertThat(pressContentLineResponse.isCorrect()).isFalse();
        assertThat(pressContentLineResponse.userTranslatedLineText()).isEqualTo("hello");


    }


}