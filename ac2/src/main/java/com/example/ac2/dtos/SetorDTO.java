package com.example.ac2.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetorDTO(

    @NotBlank(message = "Informe o nome do setor.")
    @Size(max = 100)
    String nome

) {
}