package com.example.ac2.repositories;

import com.example.ac2.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjetoRepository
        extends JpaRepository<Projeto, Integer> {

    // Consulta A: projeto com os funcionários vinculados.
    @Query("""
        SELECT DISTINCT p
        FROM Projeto p
        LEFT JOIN FETCH p.funcionarios
        WHERE p.id = :id
    """)
    Optional<Projeto> buscarPorIdComFuncionarios(
        @Param("id") Integer id
    );

    // Consulta B: projetos com início dentro do intervalo.
    List<Projeto> findByDataInicioBetween(
        LocalDate inicio,
        LocalDate fim
    );
}