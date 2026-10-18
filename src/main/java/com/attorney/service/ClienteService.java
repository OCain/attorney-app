package com.attorney.service;

import com.attorney.model.Cliente;
import com.attorney.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Já existe um cadastro com este e-mail.");
        }
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("Já existe um cadastro com este CPF.");
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizarFotoPerfil(Long clienteId, String fotoUrl) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        cliente.setFotoPerfilUrl(fotoUrl);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizarPerfil(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
