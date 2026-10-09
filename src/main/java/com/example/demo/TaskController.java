package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collection;

import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    // Creates a task. "done" is true only if the client sent true; otherwise false.
    // Returns 201 Created.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody Task input) {
        long id = nextId.getAndIncrement();
        Task task = new Task(id, input.title(), Boolean.TRUE.equals(input.done()));
        tasks.put(id, task);
        return task;

    }


    //lists all the extant tasks.
    @GetMapping
    public Collection<Task> list() {
        return tasks.values();
    }

    // Gets a task by the id in the URL. If no task has that id, returns 404.
    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return task;
    }
    // Removes the task with that id and returns 204 (success, no content).
    // If there's no such task, returns 404.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (tasks.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
    }

}