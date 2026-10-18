package com.attorney.repository;

import com.attorney.model.Rps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RpsRepository extends JpaRepository<Rps, Long> {
    Optional<Rps> findByNumero(String numero);
    List<Rps> findByAdvogadoIdOrderByDataCriacaoDesc(Long advogadoId);
    List<Rps> findByClienteIdOrderByDataCriacaoDesc(Long clienteId);
}
