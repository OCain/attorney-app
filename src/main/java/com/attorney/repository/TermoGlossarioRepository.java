package com.attorney.repository;

import com.attorney.model.TermoGlossario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermoGlossarioRepository extends JpaRepository<TermoGlossario, Long> {

    @Query("SELECT t FROM TermoGlossario t WHERE " +
           "LOWER(t.termo) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
           "LOWER(t.significado) LIKE LOWER(CONCAT('%', :busca, '%')) ORDER BY t.termo ASC")
    List<TermoGlossario> buscarPorTexto(@Param("busca") String busca);

    @Query("SELECT t FROM TermoGlossario t WHERE " +
           "LOWER(t.termo) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
           "LOWER(t.significado) LIKE LOWER(CONCAT('%', :busca, '%'))")
    Page<TermoGlossario> buscarPorTextoPaginado(@Param("busca") String busca, Pageable pageable);

    Page<TermoGlossario> findAllByOrderByTermoAsc(Pageable pageable);

    @Query("SELECT t FROM TermoGlossario t WHERE UPPER(t.termo) LIKE CONCAT(:letra, '%') ORDER BY t.termo ASC")
    List<TermoGlossario> findByPrimeiraLetra(@Param("letra") String letra);
}
