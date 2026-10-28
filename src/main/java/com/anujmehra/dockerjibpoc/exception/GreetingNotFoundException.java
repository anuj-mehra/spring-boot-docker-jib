package com.anujmehra.dockerjibpoc.exception;

public class GreetingNotFoundException extends RuntimeException {

    public GreetingNotFoundException(Long id) {
        super("Greeting not found: " + id);
    }
}
