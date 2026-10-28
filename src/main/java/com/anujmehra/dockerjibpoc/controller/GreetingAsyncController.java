package com.anujmehra.dockerjibpoc.controller;

import com.anujmehra.dockerjibpoc.model.Greeting;
import com.anujmehra.dockerjibpoc.service.GreetingService;
import jakarta.validation.Valid;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/** Creates a greeting off the request thread; the servlet thread is released until the future completes. */
@RestController
@RequestMapping("/api/greetings-async")
public class GreetingAsyncController {

    private final GreetingService greetingService;
    private final AsyncTaskExecutor taskExecutor;

    public GreetingAsyncController(GreetingService greetingService, AsyncTaskExecutor taskExecutor) {
        this.greetingService = greetingService;
        this.taskExecutor = taskExecutor;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Greeting>> create(@Valid @RequestBody Greeting request) {
        return CompletableFuture.supplyAsync(() -> greetingService.create(request.message()), taskExecutor)
                .thenApply(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }
}
