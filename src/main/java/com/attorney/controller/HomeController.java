package com.attorney.controller;

import com.attorney.model.Advogado;
import com.attorney.model.Avaliacao;
import com.attorney.service.AdvogadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final AdvogadoService advogadoService;

    public HomeController(AdvogadoService advogadoService) {
        this.advogadoService = advogadoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Advogado> advogados = advogadoService.listarTodos();
        List<String> especialidades = advogadoService.listarEspecialidadesDisponiveis();

        model.addAttribute("advogadosDestaque", advogados.stream().limit(6).toList());
        model.addAttribute("especialidades", especialidades);
        return "index";
    }

    @GetMapping("/advogados")
    public String buscarAdvogados(@RequestParam(value = "busca", required = false) String busca,
                                  @RequestParam(value = "especialidade", required = false) String especialidade,
                                  Model model) {
        String termoPesquisa = (especialidade != null && !especialidade.trim().isEmpty()) ? especialidade : busca;
        List<Advogado> advogados = advogadoService.buscarPorTermoOuEspecialidade(termoPesquisa);
        List<String> especialidades = advogadoService.listarEspecialidadesDisponiveis();

        model.addAttribute("advogados", advogados);
        model.addAttribute("busca", busca);
        model.addAttribute("especialidadeSelecionada", especialidade);
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("totalEncontrados", advogados.size());
        return "advogados/busca";
    }

    @GetMapping("/advogados/{id}")
    public String perfilAdvogado(@PathVariable("id") Long id, Model model) {
        Advogado advogado = advogadoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Advogado não encontrado: " + id));

        List<Avaliacao> avaliacoes = advogadoService.listarAvaliacoes(id);

        model.addAttribute("advogado", advogado);
        model.addAttribute("avaliacoes", avaliacoes);
        return "advogados/perfil";
    }
}
