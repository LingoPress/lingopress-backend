package com.kidchang.lingopress.videoTranscriptions.messaging;

import com.kidchang.lingopress._base.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {

    @RabbitListener(queues = RabbitMQConfig.RESPONSE_QUEUE)
    public void receiveResponse(String message) {
        log.info("Received message: {}", message);
        // 응답 메시지 처리 로직 추가 - 이메일 보내기, DB 업데이트 등

    }
}
