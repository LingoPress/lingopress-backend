package com.kidchang.lingopress.press.service;

import com.kidchang.lingopress._base.constant.Code;
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

    public Slice<PressResponse> getPressList(Pageable pageable) {
        Slice<Press> pressSlice = pressRepository.findAll(pageable);
        Slice<PressResponse> pressResponse = pressSlice.map(PressResponse::from);
        return pressResponse;
    }

    public Press getPressById(Long pressId) {
        return pressRepository.findById(pressId)
                .orElseThrow(() -> new BusinessException(Code.PRESS_NOT_FOUND));
    }

    public PressContentResponse getPressDetail(Long pressId) {
        Press press = getPressById(pressId);

        // 혹시 로그인되어있으면, 해당 유저가 번역한 정보가 있는지 확인하고 매칭시켜야함.
        Long userId = SecurityUtil.getUserId();
        if (userId != null) {
            User user = userRepository.findById(userId).get();

            List<PressContentLineResponse> pressContent = learnedPressContentLineRepository.findByUserAndPressAndPressContent(
                    user.getId(),
                    press.getId());
            return PressContentResponse.from(press, pressContent);
        }
        List<PressContentLine> pressContentList = pressContentLineRepository.findAllByPressId(
                press.getId());

        List<PressContentLineResponse> pressContentLineResponseList = pressContentList.stream()
                .map(PressContentLineResponse::from).toList();
        return PressContentResponse.from(press, pressContentLineResponseList);
    }


}
