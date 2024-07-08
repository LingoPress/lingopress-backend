package com.kidchang.lingopress.videoTranscriptions.messaging;

import com.kidchang.lingopress._base.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendRequest(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.REQUEST_QUEUE, message);
    }
}

