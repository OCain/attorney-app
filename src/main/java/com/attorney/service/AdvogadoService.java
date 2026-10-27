package com.attorney.service;

import com.attorney.model.Advogado;
import com.attorney.model.Avaliacao;
import com.attorney.model.Cliente;
import com.attorney.repository.AdvogadoRepository;
import com.attorney.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdvogadoService {

    private static final int NOTA_MINIMA = 0;
    private static final int NOTA_MAXIMA = 10;
    private static final int TAMANHO_MAXIMO_COMENTARIO = 2000;

    private final AdvogadoRepository advogadoRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public AdvogadoService(AdvogadoRepository advogadoRepository, AvaliacaoRepository avaliacaoRepository) {
        this.advogadoRepository = advogadoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public List<Advogado> listarTodos() {
        return advogadoRepository.findAll();
    }

    public Optional<Advogado> buscarPorId(Long id) {
        return advogadoRepository.findById(id);
    }

    public Optional<Advogado> buscarPorEmail(String email) {
        return advogadoRepository.findByEmail(email);
    }

    public List<Advogado> buscarPorTermoOuEspecialidade(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return advogadoRepository.findAll();
        }
        return advogadoRepository.buscarPorTermoOuEspecialidade(termo.trim());
    }

    public List<String> listarEspecialidadesDisponiveis() {
        return advogadoRepository.findDistinctEspecialidades();
    }

    @Transactional
    public Advogado cadastrar(Advogado advogado) {
        if (advogadoRepository.existsByEmail(advogado.getEmail())) {
            throw new IllegalArgumentException("Já existe um cadastro com este e-mail.");
        }
        if (advogadoRepository.existsByOab(advogado.getOab())) {
            throw new IllegalArgumentException("Já existe um advogado cadastrado com este número de OAB.");
        }
        return advogadoRepository.save(advogado);
    }

    @Transactional
    public Avaliacao registrarAvaliacao(Long advogadoId, Cliente cliente, Integer nota, String comentario) {
        validarAvaliacao(cliente, nota, comentario);

        Advogado advogado = advogadoRepository.findById(advogadoId)
                .orElseThrow(() -> new IllegalArgumentException("Advogado não encontrado."));

        Avaliacao avaliacao = new Avaliacao(advogado, cliente, nota, comentario);
        Avaliacao salva = avaliacaoRepository.save(avaliacao);

        // Recalcula média de notas (de 0 a 10)
        Double media = avaliacaoRepository.calcularMediaNotaAdvogado(advogadoId);
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByAdvogadoIdOrderByDataAvaliacaoDesc(advogadoId);

        advogado.setNotaMedia(media != null ? Math.round(media * 10.0) / 10.0 : 0.0);
        advogado.setTotalAvaliacoes(avaliacoes.size());
        advogadoRepository.save(advogado);

        return salva;
    }

    private void validarAvaliacao(Cliente cliente, Integer nota, String comentario) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente é obrigatório para registrar uma avaliação.");
        }
        if (nota == null) {
            throw new IllegalArgumentException("A nota é obrigatória.");
        }
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 10.");
        }
        if (comentario == null || comentario.trim().isEmpty()) {
            throw new IllegalArgumentException("O comentário é obrigatório.");
        }
        if (comentario.length() > TAMANHO_MAXIMO_COMENTARIO) {
            throw new IllegalArgumentException("O comentário deve ter no máximo 2000 caracteres.");
        }
    }

    public List<Avaliacao> listarAvaliacoes(Long advogadoId) {
        return avaliacaoRepository.findByAdvogadoIdOrderByDataAvaliacaoDesc(advogadoId);
    }

    @Transactional
    public Advogado atualizarPerfil(Advogado advogado) {
        return advogadoRepository.save(advogado);
    }
}
