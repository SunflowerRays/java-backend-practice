package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DedupingEventCounterTest {

    private DedupingEventCounter counter;
    private List<TrackedEvent> events;
    @BeforeEach
    void setUp() {
        events = List.of(
                new TrackedEvent("e1", "anna", "login", Instant.now()),
                new TrackedEvent("e1", "anna", "click", Instant.now()),
                new TrackedEvent("f2", "anna",  "click", Instant.now()),
                new TrackedEvent("e1", "anna",  "logout", Instant.now())
        );

        counter = new DedupingEventCounter();
    }

    @Test
    void DetectDuplicates() {

        //Act & Assert:
        assertTrue(counter.process(events.get(0)));
        assertFalse(counter.process(events.get(1)));
        assertTrue(counter.process(events.get(2)));
        assertFalse(counter.process(events.get(3)));
    }

    @Test
    void countsEventsByUser() {

        //Arrange:
        Map<String, Integer> counts = new HashMap<>();

        //Act:
        for (TrackedEvent event : events) {
            counter.process(event);
        }

        //Assert:
        assertEquals(2, counter.getCountsByUser().get("anna"));
    }

}
