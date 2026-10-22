package com.example.ac2.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FuncionarioDTO(

    @NotBlank(message = "Informe o nome do funcionário.")
    @Size(max = 120)
    String nome,

    Integer idSetor

) {
}