package com.sandesDev.apiConsumer.listener;

import com.sandesDev.apiConsumer.records.ClientDto;
import com.sandesDev.apiConsumer.services.EmailServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class ClientConsumer {

    private EmailServices emailServices;

    private final Logger logger = LoggerFactory.getLogger(ClientConsumer.class);

    public ClientConsumer(EmailServices emailServices) {
        this.emailServices = emailServices;
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "tweet_email_kafka", partitions = { "1" }), containerFactory = "kafkaListenerContainerFactory")
    public void orderListener(ClientDto dto) {
        logger.info("Received Message Consumer 01: " + dto.username());
        emailServices.save(dto);
    }
    @KafkaListener(topicPartitions = @TopicPartition(topic = "tweet_email_kafka", partitions = { "0" }), containerFactory = "kafkaListenerContainerFactory")
    public void deleteListener(ClientDto dto) {
        logger.info("Received Message Consumer 02: " + dto.username());
        emailServices.delete(dto);
    }

}
