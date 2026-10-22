package com.example.ac2.repositories;

import com.example.ac2.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Integer> {

    // Consulta C: funcionário com seus projetos.
    @Query("""
        SELECT DISTINCT f
        FROM Funcionario f
        LEFT JOIN FETCH f.projetos
        WHERE f.id = :id
    """)
    Optional<Funcionario> buscarPorIdComProjetos(
        @Param("id") Integer id
    );
}