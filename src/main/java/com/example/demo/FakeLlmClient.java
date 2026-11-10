package com.example.demo;

import org.springframework.stereotype.Component;

//For development and testing.
@Component
public class FakeLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {
        // A canned reply, so the app and tests don't need a real LLM.
        return "{\"title\": \"Write the README\", \"done\": false}";
    }
}