package com.attorney.service;

import com.attorney.dto.UsuarioLogadoDto;
import com.attorney.model.Advogado;
import com.attorney.model.Cliente;
import com.attorney.model.TipoUsuario;
import com.attorney.repository.AdvogadoRepository;
import com.attorney.repository.ClienteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    public static final String SESSION_USER_KEY = "USUARIO_LOGADO";

    private final ClienteRepository clienteRepository;
    private final AdvogadoRepository advogadoRepository;

    public AuthService(ClienteRepository clienteRepository, AdvogadoRepository advogadoRepository) {
        this.clienteRepository = clienteRepository;
        this.advogadoRepository = advogadoRepository;
    }

    public UsuarioLogadoDto autenticar(String email, String senha, HttpSession session) {
        if (email == null || senha == null) {
            throw new IllegalArgumentException("E-mail e senha são obrigatórios.");
        }

        String emailTrimmed = email.trim().toLowerCase();

        // 1. Tenta autenticar como Cliente
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(emailTrimmed);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (cliente.getSenha().equals(senha)) {
                UsuarioLogadoDto dto = new UsuarioLogadoDto(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        TipoUsuario.CLIENTE,
                        cliente.getFotoPerfilUrl(),
                        cliente.getCpf()
                );
                session.setAttribute(SESSION_USER_KEY, dto);
                return dto;
            }
        }

        // 2. Tenta autenticar como Advogado
        Optional<Advogado> advOpt = advogadoRepository.findByEmail(emailTrimmed);
        if (advOpt.isPresent()) {
            Advogado adv = advOpt.get();
            if (adv.getSenha().equals(senha)) {
                UsuarioLogadoDto dto = new UsuarioLogadoDto(
                        adv.getId(),
                        adv.getNome(),
                        adv.getEmail(),
                        TipoUsuario.ADVOGADO,
                        adv.getFotoPerfilUrl(),
                        adv.getOab()
                );
                session.setAttribute(SESSION_USER_KEY, dto);
                return dto;
            }
        }

        throw new IllegalArgumentException("E-mail ou senha incorretos.");
    }

    public UsuarioLogadoDto getUsuarioLogado(HttpSession session) {
        return (UsuarioLogadoDto) session.getAttribute(SESSION_USER_KEY);
    }

    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        session.invalidate();
    }
}
