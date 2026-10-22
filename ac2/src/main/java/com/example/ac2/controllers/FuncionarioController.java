package com.example.ac2.controllers;

import com.example.ac2.dtos.DadosProjetoDTO;
import com.example.ac2.dtos.FuncionarioDTO;
import com.example.ac2.dtos.FuncionarioResumoDTO;
import com.example.ac2.services.FuncionarioService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FuncionarioResumoDTO adicionar(
            @Valid @RequestBody FuncionarioDTO funcionario) {
        return service.adicionar(funcionario);
    }

    @GetMapping
    public List<FuncionarioResumoDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}/projetos")
    public List<DadosProjetoDTO> buscarProjetos(
            @PathVariable Integer id) {
        return service.buscarProjetos(id);
    }
}