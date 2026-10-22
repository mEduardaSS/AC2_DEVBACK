package com.example.ac2.services;


import com.example.ac2.dtos.DadosProjetoDTO;
import com.example.ac2.dtos.FuncionarioDTO;
import com.example.ac2.dtos.FuncionarioResumoDTO;
import com.example.ac2.exceptions.RecursoNaoEncontradoException;
import com.example.ac2.models.Funcionario;
import com.example.ac2.models.Setor;
import com.example.ac2.repositories.FuncionarioRepository;
import com.example.ac2.repositories.SetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            SetorRepository setorRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
    }

    @Transactional
    public FuncionarioResumoDTO adicionar(FuncionarioDTO dto) {
        Funcionario funcionario = new Funcionario(
            dto.nome().trim()
        );

        if (dto.idSetor() != null) {
            Setor setor = setorRepository.findById(dto.idSetor())
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException(
                        "Setor não encontrado: " + dto.idSetor()
                    )
                );

            funcionario.setSetor(setor);
        }

        funcionario = funcionarioRepository.save(funcionario);

        return new FuncionarioResumoDTO(
            funcionario.getId(),
            funcionario.getNome()
        );
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResumoDTO> listarTodos() {
        return funcionarioRepository.findAll()
            .stream()
            .map(f -> new FuncionarioResumoDTO(
                f.getId(), f.getNome()
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarProjetos(
            Integer idFuncionario) {

        Funcionario funcionario = funcionarioRepository
            .buscarPorIdComProjetos(idFuncionario)
            .orElseThrow(() ->
                new RecursoNaoEncontradoException(
                    "Funcionário não encontrado: " + idFuncionario
                )
            );

        return funcionario.getProjetos()
            .stream()
            .map(p -> new DadosProjetoDTO(
                p.getId(),
                p.getDescricao(),
                p.getDataInicio(),
                p.getDataFim(),
                p.getFuncionarios()
                    .stream()
                    .map(f -> new FuncionarioResumoDTO(
                        f.getId(), f.getNome()
                    ))
                    .toList()
            ))
            .toList();
    }
}
