package com.attorney.repository;

import com.attorney.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByAdvogadoIdOrderByDataAvaliacaoDesc(Long advogadoId);
    List<Avaliacao> findByClienteIdOrderByDataAvaliacaoDesc(Long clienteId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.advogado.id = :advogadoId")
    Double calcularMediaNotaAdvogado(@Param("advogadoId") Long advogadoId);
}
