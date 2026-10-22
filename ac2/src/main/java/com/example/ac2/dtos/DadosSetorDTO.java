package com.example.ac2.dtos;


import java.util.List;

public record DadosSetorDTO(
    Integer id,
    String nome,
    List<FuncionarioResumoDTO> funcionarios
) {
}
