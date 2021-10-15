package com.tutorial.kafka.springkafka.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutorial.kafka.springkafka.common.constant.AppConst;
import com.tutorial.kafka.springkafka.model.Event;
import com.tutorial.kafka.springkafka.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Log4j2
public class EventListener {
    public static AtomicLong receivedMessageCounter = new AtomicLong();
    private final EventService eventService;

    public EventListener(EventService eventService) {
        this.eventService = eventService;
    }

//    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}",
//            autoStartup = "${spring.kafka.listener.auto.start:false}")
//    public void singleMessageConsumerWithManualAck(String message, Acknowledgment acknowledgment, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
//        if (receivedMessageCounter.get() < 1000) {
//            log.info("Partition: {} receivedMessagedCount:{}", partition, receivedMessageCounter.get());
//        }
//        consumeMessage(message);
//        acknowledgment.acknowledge();
//    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}",
            autoStartup = "${spring.kafka.listener.auto.start:false}")
    public void batchConsumerWithManualAck(List<String> messages, Acknowledgment acknowledgment, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        if (receivedMessageCounter.get() < 1000) {
            log.info("Partition: {} batchSize: {} receivedMessagedCount:{}", partition, messages.size(), receivedMessageCounter.get());
        }
        for (String msg : messages) {
            consumeMessage(msg);
            acknowledgment.acknowledge();
        }
    }

    public void consumeMessage(String message) {
        receivedMessageCounter.getAndIncrement();
        Event event = convertMsgToEvent(message);
        if (event != null) {
//            eventService.save(event);
            // calculate the time taken since producer started till now
            long diffInMilli = Math.abs(Instant.now().toEpochMilli() - event.getCreatedAt().toEpochMilli());
            long diff = TimeUnit.MILLISECONDS.convert(diffInMilli, TimeUnit.MILLISECONDS);
            if (receivedMessageCounter.get() == AppConst.MESSAGE_COUNT) {
                log.info("receivedMessagedCount:{} Took: {} ms", receivedMessageCounter.get(), diff);
                log.info("Consumer finished.");
            }
        }
    }

    private Event convertMsgToEvent(String message) {
        Event event = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            event = objectMapper.readValue(message, Event.class);
        } catch (JsonProcessingException e) {
            log.error(e);
        }
        return event;
    }
}
