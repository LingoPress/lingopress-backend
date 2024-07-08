package com.kidchang.lingopress.videoTranscriptions.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidchang.lingopress._base.config.RabbitMQConfig;
import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress.videoTranscriptions.VideoTranscriptionsService;
import com.kidchang.lingopress.videoTranscriptions.messaging.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageReceiver {

    private final VideoTranscriptionsService videoTranscriptionsService;

    @RabbitListener(queues = RabbitMQConfig.RESPONSE_QUEUE)
    public void receiveResponse(String message) {
        log.info("Received message: {}", message);

        try {
            // ObjectMapper 인스턴스 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열을 MessageResponse 레코드로 변환
            MessageResponse messageResponse = objectMapper.readValue(message, MessageResponse.class);

            // 로그 출력
            log.info("ID: {}", messageResponse.queueId());
            log.info("Press ID: {}", messageResponse.pressId());

            if (!videoTranscriptionsService.responseVideoTranscription(messageResponse.queueId(), messageResponse.pressId())) {
                log.error("메일 전송에 실패함: {}", message);
            }

        } catch (Exception e) {
            throw new BusinessException(Code.RABBITMQ_MESSAGE_RECEIVE_ERROR, e);
        }
    }
}
