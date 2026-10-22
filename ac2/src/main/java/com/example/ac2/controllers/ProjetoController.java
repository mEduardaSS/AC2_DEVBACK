package com.example.ac2.controllers;

import com.example.ac2.dtos.DadosProjetoDTO;
import com.example.ac2.dtos.ProjetoDTO;
import com.example.ac2.services.ProjetoService;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projetos")
@CrossOrigin(origins = "*")
public class ProjetoController {

    private final ProjetoService service;

    public ProjetoController(ProjetoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosProjetoDTO adicionar(
            @Valid @RequestBody ProjetoDTO projeto) {
        return service.adicionar(projeto);
    }

    @GetMapping
    public List<DadosProjetoDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public DadosProjetoDTO buscarProjetoPorId(
            @PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping("/{idProjeto}/funcionarios/{idFuncionario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vincularFuncionario(
            @PathVariable Integer idProjeto,
            @PathVariable Integer idFuncionario) {

        service.vincularFuncionario(idProjeto, idFuncionario);
    }

    @GetMapping("/por-periodo")
    public List<DadosProjetoDTO> buscarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim) {

        return service.buscarPorIntervalo(inicio, fim);
    }
}
