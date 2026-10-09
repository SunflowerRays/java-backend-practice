package com.example.demo;

import java.time.Instant;

public record TrackedEvent(String id, String user, String type, Instant timestamp) {
}