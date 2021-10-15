package com.tutorial.kafka.springkafka.service;

import com.tutorial.kafka.springkafka.model.Event;

public interface EventService {
    void save(Event event);
}
