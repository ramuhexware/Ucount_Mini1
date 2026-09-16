package com.freddie.ucount.access.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserKafkaEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserKafkaEventListener.class);

    @KafkaListener(topics = "ucount-user-events", groupId = "ucount-access-group", autoStartup = "false")
    public void consumeUserEvent(String message) {
        log.info("Received Kafka Counterparty Lifecycle Event: {}", message);
    }
}
