package com.example.ac2.controllers;

import com.example.ac2.dtos.DadosSetorDTO;
import com.example.ac2.dtos.SetorDTO;
import com.example.ac2.exceptions.RecursoNaoEncontradoException;
import com.example.ac2.exceptions.RegraNegocioException;
import com.example.ac2.models.Setor;
import com.example.ac2.services.SetorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/setores")
@CrossOrigin(origins = "*")
public class SetorController {

    private final SetorService service;

    public SetorController(SetorService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosSetorDTO adicionar(
            @Valid @RequestBody SetorDTO setor) {
        return service.adicionar(setor);
    }

    @GetMapping("/{id}")
    public DadosSetorDTO buscarSetorPorId(
            @PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<DadosSetorDTO> listarTodos() {
        return service.listarTodos();
    }
}