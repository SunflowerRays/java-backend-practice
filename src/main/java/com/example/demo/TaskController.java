package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collection;

import org.springframework.web.bind.annotation.DeleteMapping;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;
    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    public TaskController(TaskRepository repository, LlmClient llmClient, ObjectMapper objectMapper) {
        this.repository = repository;
        this.llmClient = llmClient;
        this.objectMapper = objectMapper;
    }

    // Creates a task. "done" is true only if the client sent true; otherwise false.
    // Returns 201 Created.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody Task input) {

        TaskEntity saved = repository.save(new TaskEntity(input.title(), Boolean.TRUE.equals(input.done())));
        return new Task(saved.getId(), saved.getTitle(), saved.isDone());

    }

    //lists all the extant tasks.
    @GetMapping
    public Collection<Task> list(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size) {

        //Pagination. Here page size is constrained to between 1 and 100 items.
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        List<Task> result = new ArrayList<>();
        for (TaskEntity e : repository.findAll(PageRequest.of(safePage, safeSize, Sort.by("id"))).getContent()) {
            result.add(new Task(e.getId(), e.getTitle(), e.isDone()));
        }
        return result;
    }

    // Gets a task by the id in the URL. If no task has that id, returns 404.
    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {

        return repository.findById(id)
                .map(e -> new Task(e.getId(), e.getTitle(), e.isDone()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }
    // Removes the task with that id and returns 204 (success, no content).
    // If there's no such task, returns 404.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        repository.deleteById(id);

    }
    @PostMapping("/generate")
    public String generate(@Valid @RequestBody GenerateRequest request) {
        String reply = llmClient.generate(request.prompt());
        parseReply(reply);
        return reply;
    }

    //Reads reply as JSON and parses its contents according to the Task record class.
    private Task parseReply(String reply) {
        try {
            return objectMapper.readValue(reply, Task.class);
        } catch (JacksonException e) {
            throw new InvalidLlmReplyException("Model reply was not valid JSON", e);
        }
    }

    }