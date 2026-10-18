package com.attorney.repository;

import com.attorney.model.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {
    Optional<Advogado> findByEmail(String email);
    Optional<Advogado> findByOab(String oab);
    boolean existsByEmail(String email);
    boolean existsByOab(String oab);

    List<Advogado> findByEspecialidadeIgnoreCase(String especialidade);

    @Query("SELECT a FROM Advogado a WHERE " +
           "LOWER(a.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(a.especialidade) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(a.biografia) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Advogado> buscarPorTermoOuEspecialidade(@Param("termo") String termo);

    @Query("SELECT DISTINCT a.especialidade FROM Advogado a ORDER BY a.especialidade ASC")
    List<String> findDistinctEspecialidades();
}
