package com.kidchang.lingopress._base.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String REQUEST_QUEUE = "videoProcessingRequestQueue";
    public static final String RESPONSE_QUEUE = "videoProcessingResponseQueue";

    @Bean
    public Queue videoProcessingRequestQueue() {
        return new Queue(REQUEST_QUEUE, false);
    }

    @Bean
    public Queue videoProcessingResponseQueue() {
        return new Queue(RESPONSE_QUEUE, false);
    }
}

