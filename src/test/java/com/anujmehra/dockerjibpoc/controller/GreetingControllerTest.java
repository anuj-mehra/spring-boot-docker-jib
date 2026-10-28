package com.anujmehra.dockerjibpoc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloReturnsGreeting() throws Exception {
        mockMvc.perform(get("/api/greetings/hello").param("name", "Anuj"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Anuj!"));
    }

    @Test
    void createThenFetchGreeting() throws Exception {
        mockMvc.perform(post("/api/greetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"Hi there\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.message").value("Hi there"));
    }

    @Test
    void createRejectsBlankMessage() throws Exception {
        mockMvc.perform(post("/api/greetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownIdReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/greetings/9999"))
                .andExpect(status().isNotFound());
    }
}
