package com.attorney.service;

import com.attorney.model.TermoGlossario;
import com.attorney.repository.TermoGlossarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GlossarioService {

    private static final Logger log = LoggerFactory.getLogger(GlossarioService.class);

    private final TermoGlossarioRepository termoGlossarioRepository;

    public GlossarioService(TermoGlossarioRepository termoGlossarioRepository) {
        this.termoGlossarioRepository = termoGlossarioRepository;
    }

    @Transactional
    public void carregarGlossarioInicialSeNecessario() {
        if (termoGlossarioRepository.count() > 0) {
            log.info("Glossário jurídico já populado com {} termos.", termoGlossarioRepository.count());
            return;
        }

        log.info("Carregando glossário jurídico a partir do arquivo bruto...");
        try {
            ClassPathResource resource = new ClassPathResource("data/glossario-juridico-bruto.txt");
            if (!resource.exists()) {
                log.warn("Arquivo data/glossario-juridico-bruto.txt não encontrado no classpath.");
                return;
            }

            List<TermoGlossario> termosParaSalvar = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                String currentTermo = null;
                StringBuilder currentSignificado = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();

                    if (trimmed.equalsIgnoreCase("Jurídico")) {
                        // Salva o termo anterior se houver
                        if (currentTermo != null && currentSignificado.length() > 0) {
                            termosParaSalvar.add(new TermoGlossario(currentTermo, currentSignificado.toString().trim()));
                            currentSignificado.setLength(0);
                            currentTermo = null;
                        }
                        // Próxima linha não vazia será o nome do termo
                        String nextLine;
                        while ((nextLine = reader.readLine()) != null) {
                            String nextTrimmed = nextLine.trim();
                            if (!nextTrimmed.isEmpty()) {
                                currentTermo = nextTrimmed;
                                break;
                            }
                        }
                    } else if (currentTermo != null) {
                        if (!trimmed.isEmpty()) {
                            if (currentSignificado.length() > 0) {
                                currentSignificado.append(" ");
                            }
                            currentSignificado.append(trimmed);
                        }
                    }
                }

                // Salva o último termo se houver
                if (currentTermo != null && currentSignificado.length() > 0) {
                    termosParaSalvar.add(new TermoGlossario(currentTermo, currentSignificado.toString().trim()));
                }
            }

            if (!termosParaSalvar.isEmpty()) {
                termoGlossarioRepository.saveAll(termosParaSalvar);
                log.info("Glossário jurídico carregado com sucesso: {} termos cadastrados.", termosParaSalvar.size());
            }

        } catch (Exception e) {
            log.error("Erro ao carregar glossário jurídico inicial: {}", e.getMessage(), e);
        }
    }

    public Page<TermoGlossario> listarPaginado(String busca, Pageable pageable) {
        if (busca != null && !busca.trim().isEmpty()) {
            return termoGlossarioRepository.buscarPorTextoPaginado(busca.trim(), pageable);
        }
        return termoGlossarioRepository.findAllByOrderByTermoAsc(pageable);
    }

    public List<TermoGlossario> buscarPorTexto(String busca) {
        if (busca == null || busca.trim().isEmpty()) {
            return List.of();
        }
        return termoGlossarioRepository.buscarPorTexto(busca.trim());
    }

    public List<TermoGlossario> buscarPorLetra(String letra) {
        if (letra == null || letra.trim().isEmpty()) {
            return List.of();
        }
        return termoGlossarioRepository.findByPrimeiraLetra(letra.trim().toUpperCase());
    }
}
