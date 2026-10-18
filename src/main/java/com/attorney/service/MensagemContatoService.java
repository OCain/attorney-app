package com.attorney.service;

import com.attorney.model.Advogado;
import com.attorney.model.Cliente;
import com.attorney.model.MensagemContato;
import com.attorney.model.TipoUsuario;
import com.attorney.repository.AdvogadoRepository;
import com.attorney.repository.ClienteRepository;
import com.attorney.repository.MensagemContatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MensagemContatoService {

    private final MensagemContatoRepository mensagemContatoRepository;
    private final ClienteRepository clienteRepository;
    private final AdvogadoRepository advogadoRepository;

    public MensagemContatoService(MensagemContatoRepository mensagemContatoRepository,
                                  ClienteRepository clienteRepository,
                                  AdvogadoRepository advogadoRepository) {
        this.mensagemContatoRepository = mensagemContatoRepository;
        this.clienteRepository = clienteRepository;
        this.advogadoRepository = advogadoRepository;
    }

    @Transactional
    public MensagemContato enviarMensagem(Long clienteId, Long advogadoId, String assunto, String mensagem, TipoUsuario enviadoPor) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        Advogado advogado = advogadoRepository.findById(advogadoId)
                .orElseThrow(() -> new IllegalArgumentException("Advogado não encontrado."));

        MensagemContato msg = new MensagemContato(cliente, advogado, assunto, mensagem, enviadoPor);
        return mensagemContatoRepository.save(msg);
    }

    public List<MensagemContato> listarConversa(Long clienteId, Long advogadoId) {
        return mensagemContatoRepository.findConversa(clienteId, advogadoId);
    }

    public List<MensagemContato> listarPorAdvogado(Long advogadoId) {
        return mensagemContatoRepository.findByAdvogadoIdOrderByDataEnvioDesc(advogadoId);
    }

    public List<MensagemContato> listarPorCliente(Long clienteId) {
        return mensagemContatoRepository.findByClienteIdOrderByDataEnvioDesc(clienteId);
    }

    public long contarNaoLidasAdvogado(Long advogadoId) {
        return mensagemContatoRepository.countByAdvogadoIdAndLidaFalse(advogadoId);
    }
}
