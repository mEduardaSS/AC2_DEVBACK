package com.example.ac2.services;

import com.example.ac2.dtos.ProjetoDTO;
import com.example.ac2.dtos.DadosProjetoDTO;
import com.example.ac2.dtos.FuncionarioResumoDTO;
import com.example.ac2.exceptions.RecursoNaoEncontradoException;
import com.example.ac2.exceptions.RegraNegocioException;
import com.example.ac2.models.Funcionario;
import com.example.ac2.models.Projeto;
import com.example.ac2.repositories.ProjetoRepository;
import com.example.ac2.repositories.FuncionarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public ProjetoService(
            ProjetoRepository projetoRepository,
            FuncionarioRepository funcionarioRepository) {
        this.projetoRepository = projetoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Transactional
    public DadosProjetoDTO adicionar(ProjetoDTO dto) {
        validarDatas(dto.dataInicio(), dto.dataFim());

        Projeto projeto = new Projeto(
            dto.descricao().trim(),
            dto.dataInicio(),
            dto.dataFim()
        );

        return converter(projetoRepository.save(projeto));
    }

    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> listarTodos() {
        return projetoRepository.findAll()
            .stream()
            .map(this::converter)
            .toList();
    }

    @Transactional(readOnly = true)
    public DadosProjetoDTO buscarPorId(Integer id) {
        Projeto projeto = projetoRepository
            .buscarPorIdComFuncionarios(id)
            .orElseThrow(() ->
                new RecursoNaoEncontradoException(
                    "Projeto não encontrado: " + id
                )
            );

        return converter(projeto);
    }

    @Transactional
    public void vincularFuncionario(
            Integer idProjeto, Integer idFuncionario) {

        Projeto projeto = projetoRepository
            .buscarPorIdComFuncionarios(idProjeto)
            .orElseThrow(() ->
                new RecursoNaoEncontradoException(
                    "Projeto não encontrado: " + idProjeto
                )
            );

        Funcionario funcionario = funcionarioRepository
            .findById(idFuncionario)
            .orElseThrow(() ->
                new RecursoNaoEncontradoException(
                    "Funcionário não encontrado: " + idFuncionario
                )
            );

        projeto.getFuncionarios().add(funcionario);
        projetoRepository.save(projeto);
    }

    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarPorIntervalo(
            LocalDate inicio, LocalDate fim) {

        validarDatas(inicio, fim);

        return projetoRepository
            .findByDataInicioBetween(inicio, fim)
            .stream()
            .map(this::converter)
            .toList();
    }

    private void validarDatas(
            LocalDate inicio, LocalDate fim) {

        if (inicio == null || fim == null) {
            throw new RegraNegocioException(
                "As datas de início e fim são obrigatórias."
            );
        }

        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException(
                "A data de fim não pode ser anterior à data de início."
            );
        }
    }

    private DadosProjetoDTO converter(Projeto projeto) {
        List<FuncionarioResumoDTO> funcionarios =
            projeto.getFuncionarios()
                .stream()
                .map(f -> new FuncionarioResumoDTO(
                    f.getId(), f.getNome()
                ))
                .toList();

        return new DadosProjetoDTO(
            projeto.getId(),
            projeto.getDescricao(),
            projeto.getDataInicio(),
            projeto.getDataFim(),
            funcionarios
        );
    }
}