package com.attorney.repository;

import com.attorney.model.MensagemContato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemContatoRepository extends JpaRepository<MensagemContato, Long> {

    List<MensagemContato> findByAdvogadoIdOrderByDataEnvioDesc(Long advogadoId);

    List<MensagemContato> findByClienteIdOrderByDataEnvioDesc(Long clienteId);

    @Query("SELECT m FROM MensagemContato m WHERE " +
           "(m.cliente.id = :clienteId AND m.advogado.id = :advogadoId) " +
           "ORDER BY m.dataEnvio ASC")
    List<MensagemContato> findConversa(@Param("clienteId") Long clienteId, @Param("advogadoId") Long advogadoId);

    long countByAdvogadoIdAndLidaFalse(Long advogadoId);
}
