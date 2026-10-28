package com.anujmehra.dockerjibpoc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Greeting(Long id, @NotBlank @Size(max = 100) String message) {
}
