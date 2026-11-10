package com.example.demo;

import jakarta.validation.constraints.NotBlank;

public record GenerateRequest(@NotBlank String prompt) {}