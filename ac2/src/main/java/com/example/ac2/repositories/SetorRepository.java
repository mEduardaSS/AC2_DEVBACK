package com.example.ac2.repositories;

import com.example.ac2.models.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SetorRepository
        extends JpaRepository<Setor, Integer> {

    // Consulta D: todos os setores com seus funcionários.
    @Query("""
        SELECT DISTINCT s
        FROM Setor s
        LEFT JOIN FETCH s.funcionarios
    """)
    List<Setor> listarComFuncionarios();

    @Query("""
        SELECT DISTINCT s
        FROM Setor s
        LEFT JOIN FETCH s.funcionarios
        WHERE s.id = :id
    """)
    Optional<Setor> buscarPorIdComFuncionarios(
        @Param("id") Integer id
    );
}