package com.nexvia.digital_banking;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferEventProducer {

    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;

    public TransferEventProducer(
            KafkaTemplate<String, TransferEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TransferEvent event) {
        kafkaTemplate.send("completed-transfers", event);
    }
}