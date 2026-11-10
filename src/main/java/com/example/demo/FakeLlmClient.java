package com.example.demo;

import org.springframework.stereotype.Component;

//For development and testing.
@Component
public class FakeLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {
        // A canned reply, so the app and tests don't need a real LLM.

        if (prompt.contains("[[malformed]]")) {
            return "this is not JSON";
        }

        return "{\"title\": \"Write the README\", \"done\": false}";
    }

}