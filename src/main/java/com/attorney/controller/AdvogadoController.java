package com.attorney.controller;

import com.attorney.dto.UsuarioLogadoDto;
import com.attorney.model.Advogado;
import com.attorney.model.Avaliacao;
import com.attorney.model.MensagemContato;
import com.attorney.model.Rps;
import com.attorney.service.AdvogadoService;
import com.attorney.service.AuthService;
import com.attorney.service.MensagemContatoService;
import com.attorney.service.RpsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/advogado")
public class AdvogadoController {

    private final AuthService authService;
    private final AdvogadoService advogadoService;
    private final RpsService rpsService;
    private final MensagemContatoService mensagemContatoService;

    public AdvogadoController(AuthService authService,
                              AdvogadoService advogadoService,
                              RpsService rpsService,
                              MensagemContatoService mensagemContatoService) {
        this.authService = authService;
        this.advogadoService = advogadoService;
        this.rpsService = rpsService;
        this.mensagemContatoService = mensagemContatoService;
    }

    private Advogado verificarAdvogadoLogado(HttpSession session) {
        UsuarioLogadoDto user = authService.getUsuarioLogado(session);
        if (user == null || !user.isAdvogado()) {
            return null;
        }
        return advogadoService.buscarPorId(user.getId()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Advogado advogado = verificarAdvogadoLogado(session);
        if (advogado == null) {
            return "redirect:/login?redirect=/advogado/dashboard";
        }

        List<Rps> processos = rpsService.listarPorAdvogado(advogado.getId());
        List<MensagemContato> mensagens = mensagemContatoService.listarPorAdvogado(advogado.getId());
        List<Avaliacao> avaliacoes = advogadoService.listarAvaliacoes(advogado.getId());

        model.addAttribute("advogado", advogado);
        model.addAttribute("processos", processos);
        model.addAttribute("mensagens", mensagens);
        model.addAttribute("avaliacoes", avaliacoes);
        return "advogado/dashboard";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Advogado advogado = verificarAdvogadoLogado(session);
        if (advogado == null) {
            return "redirect:/login?redirect=/advogado/perfil";
        }
        model.addAttribute("advogado", advogado);
        return "advogado/perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@RequestParam("nome") String nome,
                                  @RequestParam("especialidade") String especialidade,
                                  @RequestParam("telefone") String telefone,
                                  @RequestParam("endereco") String endereco,
                                  @RequestParam("biografia") String biografia,
                                  @RequestParam(value = "fotoPerfilUrl", required = false) String fotoPerfilUrl,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Advogado advogado = verificarAdvogadoLogado(session);
        if (advogado == null) {
            return "redirect:/login";
        }

        advogado.setNome(nome);
        advogado.setEspecialidade(especialidade);
        advogado.setTelefone(telefone);
        advogado.setEndereco(endereco);
        advogado.setBiografia(biografia);
        if (fotoPerfilUrl != null && !fotoPerfilUrl.trim().isEmpty()) {
            advogado.setFotoPerfilUrl(fotoPerfilUrl.trim());
        }

        advogadoService.atualizarPerfil(advogado);

        // Atualiza sessão
        UsuarioLogadoDto user = authService.getUsuarioLogado(session);
        user.setNome(advogado.getNome());
        user.setFotoPerfilUrl(advogado.getFotoPerfilUrl());
        session.setAttribute(AuthService.SESSION_USER_KEY, user);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Perfil atualizado com sucesso!");
        return "redirect:/advogado/perfil";
    }
}
