package com.example.ac2.services;

import com.example.ac2.dtos.DadosSetorDTO;
import com.example.ac2.dtos.FuncionarioResumoDTO;
import com.example.ac2.dtos.SetorDTO;
import com.example.ac2.exceptions.RecursoNaoEncontradoException;
import com.example.ac2.models.Setor;
import com.example.ac2.repositories.SetorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SetorService {

    private final SetorRepository setorRepository;

    public SetorService(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    @Transactional
    public DadosSetorDTO adicionar(SetorDTO dto) {
        Setor setor = setorRepository.save(
            new Setor(dto.nome().trim())
        );

        return converter(setor);
    }

    @Transactional(readOnly = true)
    public DadosSetorDTO buscarPorId(Integer id) {
        Setor setor = setorRepository
            .buscarPorIdComFuncionarios(id)
            .orElseThrow(() ->
                new RecursoNaoEncontradoException(
                    "Setor não encontrado: " + id
                )
            );

        return converter(setor);
    }

    @Transactional(readOnly = true)
    public List<DadosSetorDTO> listarTodos() {
        return setorRepository.listarComFuncionarios()
            .stream()
            .map(this::converter)
            .toList();
    }

    private DadosSetorDTO converter(Setor setor) {
        List<FuncionarioResumoDTO> funcionarios =
            setor.getFuncionarios()
                .stream()
                .map(f -> new FuncionarioResumoDTO(
                    f.getId(), f.getNome()
                ))
                .toList();

        return new DadosSetorDTO(
            setor.getId(),
            setor.getNome(),
            funcionarios
        );
    }
}
