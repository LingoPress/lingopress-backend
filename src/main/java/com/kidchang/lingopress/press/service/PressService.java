package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.dto.response.PressContentLineResponse;
import com.kidchang.lingopress.press.dto.response.PressContentResponse;
import com.kidchang.lingopress.press.dto.response.PressResponse;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.press.entity.PressContentLine;
import com.kidchang.lingopress.press.repository.LearnedPressContentLineRepository;
import com.kidchang.lingopress.press.repository.LearnedPressRepository;
import com.kidchang.lingopress.press.repository.PressContentLineRepository;
import com.kidchang.lingopress.user.User;
import com.kidchang.lingopress.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PressService {

    private final PressRepository pressRepository;
    private final PressContentLineRepository pressContentLineRepository;
    private final UserRepository userRepository;
    private final LearnedPressRepository learnedPressRepository;
    private final LearnedPressContentLineRepository learnedPressContentLineRepository;

    public Slice<PressResponse> getPressList(Pageable pageable, LanguageEnum acceptLanguage) {
        // 유저 정보가 있다면 학습을 원하는 언어만 추출
        Long userId = SecurityUtil.getUserId();
        if (userId != null) {
            User user = userRepository.findById(userId).get();
            LanguageEnum targetLanguage = user.getTargetLanguage();
            LanguageEnum userLanguage = user.getUserLanguage();
            return pressRepository.findAllByTargetLanguageAndUserLanguage(targetLanguage, userLanguage, pageable);
        }
        // Slice<Press> pressSlice = pressRepository.findAll(pageable);
        return pressRepository.findAllByUserLanguage(acceptLanguage, pageable);
    }

    public Press getPressById(Long pressId) {
        return pressRepository.findById(pressId)
                .orElseThrow(() -> new BusinessException(Code.PRESS_NOT_FOUND));
    }

    public PressResponse getPressByIdAndUserLanguage(Long pressId, LanguageEnum userLanguage) {
        return pressRepository.findByIdAndUserLanguage(pressId, userLanguage)
                .orElseThrow(() -> new BusinessException(Code.PRESS_NOT_FOUND));
    }

    public PressContentResponse getPressDetail(Long pressId) {
        // TODO: 어디는 press 어디는 pressResponse 를 from에 리턴해줌. 리팩터링 필요
        // 혹시 로그인되어있으면, 해당 유저가 번역한 정보가 있는지 확인하고 매칭시켜야함.
        Long userId = SecurityUtil.getUserId();
        if (userId != null) {
            User user = userRepository.findById(userId).get();
            PressResponse pressResponse = getPressByIdAndUserLanguage(pressId, user.getUserLanguage());

            List<PressContentLineResponse> pressContent = learnedPressContentLineRepository.findByUserAndPressAndPressContent(
                    user.getId(),
                    pressResponse.id(), user.getUserLanguage());
            return PressContentResponse.from(pressResponse, pressContent);
        }

        Press press = getPressById(pressId);

        List<PressContentLine> pressContentList = pressContentLineRepository.findAllByPressId(
                press.getId());

        List<PressContentLineResponse> pressContentLineResponseList = pressContentList.stream()
                .map(PressContentLineResponse::from).toList();
        return PressContentResponse.from(press, pressContentLineResponseList);
    }


}
