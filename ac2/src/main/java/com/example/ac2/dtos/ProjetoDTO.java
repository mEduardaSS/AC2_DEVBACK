package com.example.ac2.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ProjetoDTO(

    @NotBlank(message = "Informe a descrição do projeto.")
    @Size(max = 200)
    String descricao,

    @NotNull(message = "Informe a data de início.")
    LocalDate dataInicio,

    @NotNull(message = "Informe a data de fim.")
    LocalDate dataFim

) {
}