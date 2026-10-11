package com.example.demo;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventCounterTest {

    @Test
    void countsEventsPerUser() {

        // 1. Arrange: build a small list of Events (2 for anna, 1 for max)
        List<Event> events = List.of(
                new Event("anna", "login", Instant.now()),
                new Event("anna", "click", Instant.now()),
                new Event("max", "login", Instant.now())
        );
        Map<String, Integer> counts = new HashMap<>();
        EventCounter counter = new EventCounter();

        // 2. Act: call countByUser on it
        counts = counter.countByUser(events);

        // 3. Assert:
        assertEquals(2, counts.get("anna"));
        assertEquals(1, counts.get("max"));
    }

    @Test
    void checksEventsByTypeListIsEmpty() {

        //Assert: Empty List
        List<Event> events = List.of(
        );
        Map<String, Integer> counts = new HashMap<>();
        EventCounter counter = new EventCounter();

        //Act: call countByType on it
        counts = counter.countByType(events);

        //Assert:
        assertTrue(counts.isEmpty());
    }

}