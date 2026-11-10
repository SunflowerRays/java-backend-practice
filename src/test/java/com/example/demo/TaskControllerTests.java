package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTests {

    @Autowired
    private TaskController controller;

    @Autowired
    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        for (int i = 1; i <= 5; i++) {
            repository.save(new TaskEntity("Task " + i, false));
        }
    }



    // region Validation
    @Autowired
    private MockMvc mockMvc;
    @Test
    void blankTitleIsRejectedWithClearError() throws Exception {
        //Arrange:
        String json = "{\"title\": \"\", \"done\": false}";
        //Act & Assert:
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Validation failed")))
                .andExpect(content().string(containsString("\"title\":\"must not be blank\"")));
    }
    @Test
    void validTaskIsCreated() throws Exception {
        //Arrange:
        String json = "{\"title\": \"Write the README\", \"done\": false}";
        //Act & Assert:
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
    // endregion





    //region Pagination
    @Test
    void firstPageReturnsFirstTwoTasks() {
        //Act:
        List<Task> page = new ArrayList<>(controller.list(0, 2));
        //Assert:
        assertEquals(2, page.size());
        assertEquals("Task 1", page.get(0).title());
        assertEquals("Task 2", page.get(1).title());
    }

    @Test
    void lastPageMayBeShorter() {
        //Act:
        List<Task> page = new ArrayList<>(controller.list(2, 2));
        //Assert:
        assertEquals(1, page.size());
        assertEquals("Task 5", page.get(0).title());
    }

    @Test
    void sizeBelowOneIsTreatedAsOne() {
        //Act:
        List<Task> page = new ArrayList<>(controller.list(0, 0));
        //Assert:
        assertEquals(1, page.size());
    }

    @Test
    void negativePageIsTreatedAsFirstPage() {
        //Act:
        List<Task> page = new ArrayList<>(controller.list(-3, 2));
        //Assert:
        assertEquals("Task 1", page.get(0).title());
    }
    //endregion

    // region Generate
    @Test
    void generateReturnsClientReply() throws Exception {
        //Arrange:
        String json = "{\"prompt\": \"plan my week\"}";
        //Act & Assert:
        mockMvc.perform(post("/tasks/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Write the README")));
    }

    @Test
    void generateRejectsBlankPrompt() throws Exception {
        //Arrange:
        String json = "{\"prompt\": \"\"}";
        //Act & Assert:
        mockMvc.perform(post("/tasks/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    void generateReturns502WhenReplyIsMalformed() throws Exception {
        //Arrange:
        String json = "{\"prompt\": \"[[malformed]]\"}";
        //Act & Assert:
        mockMvc.perform(post("/tasks/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502));
    }


    // endregion


}