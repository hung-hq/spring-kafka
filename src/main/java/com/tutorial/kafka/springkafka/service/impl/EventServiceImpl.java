package com.tutorial.kafka.springkafka.service.impl;

import com.tutorial.kafka.springkafka.model.Event;
import com.tutorial.kafka.springkafka.repository.EventRepository;
import com.tutorial.kafka.springkafka.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }
}
