package com.kidchang.lingopress.videoTranscriptions;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress.mail.EmailMessage;
import com.kidchang.lingopress.mail.MailService;
import com.kidchang.lingopress.press.PressRepository;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.user.User;
import com.kidchang.lingopress.user.UserService;
import com.kidchang.lingopress.videoTranscriptions.dto.VideoTranscriptionsResponse;
import com.kidchang.lingopress.videoTranscriptions.messaging.MessageSender;
import com.kidchang.lingopress.videoTranscriptions.messaging.dto.MessageRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoTranscriptionsService {
    private final VideoTranscriptionsRepository videoTranscriptionsRepository;
    private final MessageSender messageSender;
    private final PressRepository pressRepository;
    private final UserService userService;
    private final MailService mailService;

    public VideoTranscriptionsResponse requestVideoTranscription(String language, String videoUrl) {
        // 비디오 자막 생성 요청
        User user = userService.getUser();
        VideoTranscriptions videoTranscriptions = videoTranscriptionsRepository.save(VideoTranscriptions.builder()
                .language(language)
                .videoUrl(videoUrl)
                .user(user)
                .build());

        // rabbitmq에 메시지 전송
        messageSender.sendRequest(MessageRequest.builder()
                .language(language)
                .videoUrl(videoUrl)
                .id(videoTranscriptions.getId())
                .build().toString());

        return VideoTranscriptionsResponse.builder()
                .id(videoTranscriptions.getId())
                .processingStatus(VideoProcessingEnum.PROCESSING)
                .build();
    }

    @Transactional
    public boolean responseVideoTranscription(Long queueId, Long pressId) {
        // 비디오 자막 생성 완료 응답 처리
        Optional<VideoTranscriptions> videoTranscriptions = videoTranscriptionsRepository.findById(queueId);
        User user = videoTranscriptions.get().getUser();

        // pressId 로 press 조회 후 owner에 user 추가
        Press press = pressRepository.findById(pressId)
                .orElseThrow(() -> new BusinessException(Code.PRESS_NOT_FOUND));

        System.out.println("@@" + press.getId() + " " + user.getId());
        press.addOwner(user);
        videoTranscriptions.get().updateProcessingStatus(VideoProcessingEnum.COMPLETED);
        // 메일 보내기
        // 아래 링크를 클릭하여 접속하세요.\n https://lingopress.me/press/${pressId}
        String messageContent = "아래 링크를 클릭하여 접속하세요.\n https://lingopress.me/lingopress/" + pressId;
        EmailMessage emailMessage = EmailMessage.builder()
                .to(user.getEmail())
                .subject("요청하신 비디오 자막 생성이 완료되었습니다.")
                .message(messageContent)
                .build();

        if (!mailService.sendMail(emailMessage)) {
            throw new BusinessException(Code.MESSAGING_EXCEPTION);
        }

        return true;
    }
}
