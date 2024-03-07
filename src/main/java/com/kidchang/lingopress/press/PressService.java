package com.kidchang.lingopress.press;

import com.kidchang.lingopress.press.dto.response.PressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PressService {

    private final PressRepository pressRepository;

    public Slice<PressResponse> getPressList(Pageable pageable) {
        Slice<Press> pressSlice = pressRepository.findAll(pageable);
        Slice<PressResponse> pressResponse = pressSlice.map(PressResponse::from);
        return pressResponse;
    }
}
