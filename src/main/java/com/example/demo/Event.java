package com.example.demo;

import java.time.Instant;

public record Event(String user, String type, Instant timestamp) {
}