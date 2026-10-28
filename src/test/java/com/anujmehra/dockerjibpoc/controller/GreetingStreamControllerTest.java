package com.anujmehra.dockerjibpoc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GreetingStreamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAllStreamsNdjson() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/greetings-stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_NDJSON)
                        .content("[{\"message\":\"One\"},{\"message\":\"Two\"}]"))
                .andExpect(request().asyncStarted())
                .andReturn();

        String body = mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON))
                .andReturn().getResponse().getContentAsString();

        String[] lines = body.split("\n");
        assertThat(lines).hasSize(2);
        assertThat(lines[0]).contains("\"message\":\"One\"");
        assertThat(lines[1]).contains("\"message\":\"Two\"");
    }

    @Test
    void createAllRejectsBlankMessage() throws Exception {
        mockMvc.perform(post("/api/greetings-stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_NDJSON)
                        .content("[{\"message\":\"\"}]"))
                .andExpect(status().isBadRequest());
    }
}
