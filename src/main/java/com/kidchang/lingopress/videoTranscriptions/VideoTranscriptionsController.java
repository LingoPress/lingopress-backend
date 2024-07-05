package com.kidchang.lingopress.videoTranscriptions;

import com.kidchang.lingopress.videoTranscriptions.dto.VideoTranscriptionsRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/video-transcriptions")
@RequiredArgsConstructor
public class VideoTranscriptionsController {

    private final VideoTranscriptionsService videoTranscriptionsService;

    @Operation(summary = "비디오 자막 생성 요청")
    @PostMapping("")
    public void requestVideoTranscription(
            @RequestBody VideoTranscriptionsRequest request
    ) {
        // 비디오 자막 생성 요청
        videoTranscriptionsService.requestVideoTranscription(request.language(), request.videoUrl());


    }


}

