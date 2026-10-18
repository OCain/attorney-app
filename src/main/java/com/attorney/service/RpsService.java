package com.attorney.service;

import com.attorney.model.Advogado;
import com.attorney.model.Cliente;
import com.attorney.model.Rps;
import com.attorney.repository.AdvogadoRepository;
import com.attorney.repository.ClienteRepository;
import com.attorney.repository.RpsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RpsService {

    private final RpsRepository rpsRepository;
    private final ClienteRepository clienteRepository;
    private final AdvogadoRepository advogadoRepository;

    public RpsService(RpsRepository rpsRepository, ClienteRepository clienteRepository, AdvogadoRepository advogadoRepository) {
        this.rpsRepository = rpsRepository;
        this.clienteRepository = clienteRepository;
        this.advogadoRepository = advogadoRepository;
    }

    @Transactional
    public Rps criarRps(Long clienteId, Long advogadoId, String descricaoServico) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        Advogado advogado = advogadoRepository.findById(advogadoId)
                .orElseThrow(() -> new IllegalArgumentException("Advogado não encontrado."));

        String numero = "RPS-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        Rps rps = new Rps(numero, advogado, cliente, descricaoServico);
        return rpsRepository.save(rps);
    }

    public List<Rps> listarPorAdvogado(Long advogadoId) {
        return rpsRepository.findByAdvogadoIdOrderByDataCriacaoDesc(advogadoId);
    }

    public List<Rps> listarPorCliente(Long clienteId) {
        return rpsRepository.findByClienteIdOrderByDataCriacaoDesc(clienteId);
    }
}
