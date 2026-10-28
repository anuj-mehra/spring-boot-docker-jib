package com.anujmehra.dockerjibpoc.service;

import com.anujmehra.dockerjibpoc.exception.GreetingNotFoundException;
import com.anujmehra.dockerjibpoc.model.Greeting;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** In-memory greeting store; swap for a repository when persistence is needed. */
@Service
public class GreetingService {

    private final Map<Long, Greeting> greetings = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    public List<Greeting> findAll() {
        return List.copyOf(greetings.values());
    }

    public Greeting findById(Long id) {
        Greeting greeting = greetings.get(id);
        if (greeting == null) {
            throw new GreetingNotFoundException(id);
        }
        return greeting;
    }

    public Greeting create(String message) {
        Greeting greeting = new Greeting(idSequence.incrementAndGet(), message);
        greetings.put(greeting.id(), greeting);
        return greeting;
    }

    public void delete(Long id) {
        if (greetings.remove(id) == null) {
            throw new GreetingNotFoundException(id);
        }
    }
}
