package com.example.demo;

import jakarta.validation.constraints.NotBlank;

public record Task(Long id, @NotBlank String title, Boolean done) {
}