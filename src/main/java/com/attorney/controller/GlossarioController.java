package com.attorney.controller;

import com.attorney.model.TermoGlossario;
import com.attorney.service.GlossarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GlossarioController {

    private final GlossarioService glossarioService;

    public GlossarioController(GlossarioService glossarioService) {
        this.glossarioService = glossarioService;
    }

    @GetMapping("/glossario")
    public String listarGlossario(@RequestParam(value = "busca", required = false) String busca,
                                  @RequestParam(value = "letra", required = false) String letra,
                                  @RequestParam(value = "pagina", defaultValue = "0") int pagina,
                                  @RequestParam(value = "tamanho", defaultValue = "15") int tamanho,
                                  Model model) {

        List<TermoGlossario> listaFiltrada = null;
        Page<TermoGlossario> paginaTermos = null;

        if (letra != null && !letra.trim().isEmpty()) {
            listaFiltrada = glossarioService.buscarPorLetra(letra.trim());
            model.addAttribute("letraSelecionada", letra.toUpperCase());
            model.addAttribute("totalTermos", listaFiltrada.size());
            model.addAttribute("termos", listaFiltrada);
        } else {
            Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("termo").ascending());
            paginaTermos = glossarioService.listarPaginado(busca, pageable);
            model.addAttribute("paginaTermos", paginaTermos);
            model.addAttribute("termos", paginaTermos.getContent());
            model.addAttribute("totalTermos", paginaTermos.getTotalElements());
        }

        model.addAttribute("busca", busca);
        model.addAttribute("alfabeto", "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars()
                .mapToObj(c -> String.valueOf((char) c))
                .toList());

        return "glossario/index";
    }
}
