package com.nexvia.digital_banking;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransferEventConsumer {

    @KafkaListener(topics = "completed-transfers")
    public void consume(TransferEvent event) {
        System.out.println("Kafka Event Received:");
        System.out.println(event);
    }
}