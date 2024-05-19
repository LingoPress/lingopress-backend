package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress.learningRecord.LearningRecordService;
import com.kidchang.lingopress.press.PressSteps;
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
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    private static TranslateContentLineRequest get뉴스1번째줄_옳음(Long pressId, int lineNumber) {
        return new TranslateContentLineRequest(pressId, lineNumber, "hello", true);
    }

    @NotNull
    private static TranslateContentLineRequest get뉴스1번째줄_틀림(Long pressId, int lineNumber) {
        return new TranslateContentLineRequest(pressId, lineNumber, "hello", false);
    }


    @Test
    @DisplayName("특정 줄 메모 작성 후 수정")
    void 특정줄_메모_작성_후_수정() {
        // given

        // SecurityContext에 모의 Authentication 객체 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long pressId = 1L;
        int lineNumber = 1;
        TranslateContentLineMemoRequest 뉴스1번째줄_메모_수정 = new TranslateContentLineMemoRequest(pressId, lineNumber, "hello");
        User newUser = getNewUser();
        Press newPress = PressSteps.getNewPress(pressId);
        PressContentLine newPressContentLine = PressSteps.getNewPressContentLine(newPress);
        LearnedPress newLearnedPress = PressSteps.getNewLearnedPress(newUser, newPress);
        LearnedPressContentLine newLearnedPressContentLine = PressSteps.getNewLearnedPressContentLineOnlyMemo(newLearnedPress, newPressContentLine, lineNumber, newPress, newUser);

        given(learnedPressContentLineRepository.findByLearnedPressAndPressContentLine(
                newLearnedPress,
                newPressContentLine))
                .willReturn(Optional.of(newLearnedPressContentLine));
        given(pressService.getPressById(pressId))
                .willReturn(newPress);

        given(userRepository.findById(newUser.getId()))
                .willReturn(Optional.of(newUser));

        given(pressContentLineRepository.findByPressIdAndLineNumber(
                pressId,
                1))
                .willReturn(Optional.of(newPressContentLine));

        given(learnPressService.findOrCreateLearnedPress(any(), any()))
                .willReturn(newLearnedPress);
        // 댓글 작성하기
        PressContentLineResponse pressContentLineResponse = learnedPressContentLineService.writePressContentLineMemo(뉴스1번째줄_메모_수정);


        // then

        assertThat(pressContentLineResponse.memo()).isEqualTo("hello");
    }


    @Test
    @DisplayName("특정 줄 메모 작성")
    void 특정줄_메모_작성() {
        // given

        // SecurityContext에 모의 Authentication 객체 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long pressId = 1L;
        int lineNumber = 1;
        TranslateContentLineRequest 뉴스1번째줄_옳음 = get뉴스1번째줄_옳음(pressId, lineNumber);
        TranslateContentLineMemoRequest 뉴스1번째줄_메모 = new TranslateContentLineMemoRequest(pressId, lineNumber, "hello_memooooo");
        User newUser = getNewUser();
        Press newPress = PressSteps.getNewPress(pressId);
        PressContentLine newPressContentLine = PressSteps.getNewPressContentLine(newPress);
        LearnedPress newLearnedPress = PressSteps.getNewLearnedPress(newUser, newPress);


        given(pressService.getPressById(pressId))
                .willReturn(newPress);

        given(userRepository.findById(newUser.getId()))
                .willReturn(Optional.of(newUser));

        given(pressContentLineRepository.findByPressIdAndLineNumber(
                pressId,
                1))
                .willReturn(Optional.of(newPressContentLine));

        given(learnPressService.findOrCreateLearnedPress(any(), any()))
                .willReturn(newLearnedPress);
        // 댓글 작성하기
        PressContentLineResponse pressContentLineResponse = learnedPressContentLineService.writePressContentLineMemo(뉴스1번째줄_메모);

        // then

        assertThat(pressContentLineResponse.memo()).isEqualTo("hello_memooooo");
    }

    @Test
    @DisplayName("특정 줄 메모 작성후 옳음->틀림")
    void 특정줄_메모_맞_틀() {
        // given

        // SecurityContext에 모의 Authentication 객체 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        Long pressId = 1L;
        int lineNumber = 1;
        TranslateContentLineRequest 뉴스1번째줄_옳음 = get뉴스1번째줄_옳음(pressId, lineNumber);
        TranslateContentLineRequest 뉴스1번째줄_틀림 = get뉴스1번째줄_틀림(pressId, lineNumber);


        User newUser = getNewUser();


        Press newPress = PressSteps.getNewPress(pressId);


        PressContentLine newPressContentLine = PressSteps.getNewPressContentLine(newPress);


        LearnedPress newLearnedPress = PressSteps.getNewLearnedPress(newUser, newPress);

        // 댓글만 작성된 LearnedPressContentLine
        LearnedPressContentLine newLearnedPressContentLine = PressSteps.getNewLearnedPressContentLineOnlyMemo(newLearnedPress, newPressContentLine, lineNumber, newPress, newUser);

        given(pressService.getPressById(pressId))
                .willReturn(newPress);

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

    @Test
    @DisplayName("특정 줄 메모 작성후 틀림->옳음")
    void 특정줄_메모_틀_맞() {
        // given

        // SecurityContext에 모의 Authentication 객체 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        Long pressId = 1L;
        int lineNumber = 1;
        TranslateContentLineRequest 뉴스1번째줄_옳음 = get뉴스1번째줄_옳음(pressId, lineNumber);
        TranslateContentLineRequest 뉴스1번째줄_틀림 = get뉴스1번째줄_틀림(pressId, lineNumber);


        User newUser = getNewUser();


        Press newPress = PressSteps.getNewPress(pressId);


        PressContentLine newPressContentLine = PressSteps.getNewPressContentLine(newPress);


        LearnedPress newLearnedPress = PressSteps.getNewLearnedPress(newUser, newPress);

        // 댓글만 작성된 LearnedPressContentLine
        LearnedPressContentLine newLearnedPressContentLine = PressSteps.getNewLearnedPressContentLineOnlyMemo(newLearnedPress, newPressContentLine, lineNumber, newPress, newUser);

        given(pressService.getPressById(pressId))
                .willReturn(newPress);

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
        learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_틀림);
        PressContentLineResponse pressContentLineResponse = learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_옳음);

        // then
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(newUser, newPress);

        assertThat(learnedPress.getLearnedContentLine()).isEqualTo(1);
        assertThat(learnedPress.getTranslatedContentLine()).isEqualTo(1);
        assertThat(pressContentLineResponse.isCorrect()).isTrue();
        assertThat(pressContentLineResponse.userTranslatedLineText()).isEqualTo("hello");
    }

    @Test
    @DisplayName("특정 줄 메모 작성후 틀림->틀림->틀림")
    void 특정줄_메모_틀_틀_틀() {
        // given

        // SecurityContext에 모의 Authentication 객체 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        Long pressId = 1L;
        int lineNumber = 1;
        TranslateContentLineRequest 뉴스1번째줄_틀림 = get뉴스1번째줄_틀림(pressId, lineNumber);

        User newUser = getNewUser();


        Press newPress = PressSteps.getNewPress(pressId);


        PressContentLine newPressContentLine = PressSteps.getNewPressContentLine(newPress);


        LearnedPress newLearnedPress = PressSteps.getNewLearnedPress(newUser, newPress);

        // 댓글만 작성된 LearnedPressContentLine
        LearnedPressContentLine newLearnedPressContentLine = PressSteps.getNewLearnedPressContentLineOnlyMemo(newLearnedPress, newPressContentLine, lineNumber, newPress, newUser);

        given(pressService.getPressById(pressId))
                .willReturn(newPress);

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
        learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_틀림);
        learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_틀림);
        PressContentLineResponse pressContentLineResponse = learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_틀림);

        // then
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(newUser, newPress);

        assertThat(learnedPress.getLearnedContentLine()).isEqualTo(0);
        assertThat(learnedPress.getTranslatedContentLine()).isEqualTo(1);
        assertThat(pressContentLineResponse.isCorrect()).isFalse();
        assertThat(pressContentLineResponse.userTranslatedLineText()).isEqualTo("hello");
    }

    @Test
    @DisplayName("특정 줄 메모 작성후 맞음->맞음->맞음")
    void 특정줄_메모_맞_맞_맞() {
        // given

        // SecurityContext에 모의 Authentication 객체 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        Long pressId = 1L;
        int lineNumber = 1;
        TranslateContentLineRequest 뉴스1번째줄_옳음 = get뉴스1번째줄_옳음(pressId, lineNumber);

        User newUser = getNewUser();


        Press newPress = PressSteps.getNewPress(pressId);


        PressContentLine newPressContentLine = PressSteps.getNewPressContentLine(newPress);


        LearnedPress newLearnedPress = PressSteps.getNewLearnedPress(newUser, newPress);

        // 댓글만 작성된 LearnedPressContentLine
        LearnedPressContentLine newLearnedPressContentLine = PressSteps.getNewLearnedPressContentLineOnlyMemo(newLearnedPress, newPressContentLine, lineNumber, newPress, newUser);

        given(pressService.getPressById(pressId))
                .willReturn(newPress);

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
        learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_옳음);
        PressContentLineResponse pressContentLineResponse = learnedPressContentLineService.checkPressContentLine(뉴스1번째줄_옳음);

        // then
        LearnedPress learnedPress = learnPressService.findOrCreateLearnedPress(newUser, newPress);

        assertThat(learnedPress.getLearnedContentLine()).isEqualTo(1);
        assertThat(learnedPress.getTranslatedContentLine()).isEqualTo(1);
        assertThat(pressContentLineResponse.isCorrect()).isTrue();
        assertThat(pressContentLineResponse.userTranslatedLineText()).isEqualTo("hello");
    }

}