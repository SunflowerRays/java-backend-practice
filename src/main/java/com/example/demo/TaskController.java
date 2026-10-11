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


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
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
    public Collection<Task> list() {

        List<Task> result = new ArrayList<>();
        for (TaskEntity e : repository.findAll()) {
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
}