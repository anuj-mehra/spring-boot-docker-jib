package com.anujmehra.dockerjibpoc.controller;

import com.anujmehra.dockerjibpoc.model.Greeting;
import com.anujmehra.dockerjibpoc.service.GreetingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.OutputStream;
import java.util.List;

/** Batch-creates greetings and streams each created greeting back as one NDJSON line. */
@RestController
@RequestMapping("/api/greetings-stream")
public class GreetingStreamController {

    private final GreetingService greetingService;
    private final ObjectMapper objectMapper;

    public GreetingStreamController(GreetingService greetingService, ObjectMapper objectMapper) {
        this.greetingService = greetingService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<StreamingResponseBody> createAll(@RequestBody List<@Valid Greeting> requests) {
        StreamingResponseBody body = (OutputStream out) -> {
            for (Greeting request : requests) {
                Greeting created = greetingService.create(request.message());
                out.write(objectMapper.writeValueAsBytes(created));
                out.write('\n');
                out.flush();
            }
        };
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(body);
    }
}
