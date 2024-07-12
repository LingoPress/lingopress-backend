package com.kidchang.lingopress.videoTranscriptions.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidchang.lingopress._base.config.RabbitMQConfig;
import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress.videoTranscriptions.VideoTranscriptionsService;
import com.kidchang.lingopress.videoTranscriptions.messaging.dto.MessageResponse;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageReceiver implements ChannelAwareMessageListener {

    private final VideoTranscriptionsService videoTranscriptionsService;
    private final ObjectMapper objectMapper;

    @Override
    @RabbitListener(queues = RabbitMQConfig.RESPONSE_QUEUE)
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String messageBody = new String(message.getBody());
            log.info("Received message: {}", messageBody);

            // JSON 문자열을 MessageResponse 레코드로 변환
            MessageResponse messageResponse = objectMapper.readValue(messageBody, MessageResponse.class);

            // 로그 출력
            log.info("ID: {}", messageResponse.queueId());
            log.info("Press ID: {}", messageResponse.pressId());
            log.info("Is Success: {}", messageResponse.isSuccess());

            boolean mailSendStatus;
            // 처리에 실패했다고 유저에게 말하고 videoTranscriptions 테이블에 status 수정
            if (messageResponse.isSuccess()) {
                mailSendStatus = videoTranscriptionsService.responseVideoTranscription(messageResponse.queueId(), messageResponse.pressId(), true);
            } else {
                log.error("비디오 작업에 실패함: {}", messageBody);
                mailSendStatus = videoTranscriptionsService.responseVideoTranscription(messageResponse.queueId(), messageResponse.pressId(), false);
            }

            if (!mailSendStatus) {
                log.error("메일 전송에 실패함: {}", messageBody);
            }

            // 메시지 성공 처리 후 Ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: ", e);
            String messageBody = new String(message.getBody());
            log.error("에러가 발생한 메시지: {}", messageBody);

            // 예외 발생 시 Nack 또는 Reject
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            throw new BusinessException(Code.RABBITMQ_MESSAGE_RECEIVE_ERROR, e);
        }
    }
}
