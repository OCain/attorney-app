package com.attorney.controller;

import com.attorney.dto.UsuarioLogadoDto;
import com.attorney.model.Advogado;
import com.attorney.model.Cliente;
import com.attorney.service.AdvogadoService;
import com.attorney.service.AuthService;
import com.attorney.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;
    private final ClienteService clienteService;
    private final AdvogadoService advogadoService;

    public AuthController(AuthService authService, ClienteService clienteService, AdvogadoService advogadoService) {
        this.authService = authService;
        this.clienteService = clienteService;
        this.advogadoService = advogadoService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "redirect", required = false) String redirect,
                            Model model, HttpSession session) {
        if (authService.getUsuarioLogado(session) != null) {
            return "redirect:/";
        }
        model.addAttribute("redirect", redirect);
        return "auth/login";
    }

    @PostMapping("/login")
    public String realizarLogin(@RequestParam("email") String email,
                                @RequestParam("senha") String senha,
                                @RequestParam(value = "redirect", required = false) String redirect,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            UsuarioLogadoDto usuario = authService.autenticar(email, senha, session);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Bem-vindo de volta, " + usuario.getNome() + "!");

            if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("login")) {
                return "redirect:" + redirect;
            }

            if (usuario.isAdvogado()) {
                return "redirect:/advogado/dashboard";
            } else {
                return "redirect:/cliente/dashboard";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/cadastro")
    public String selecionarTipoCadastro() {
        return "auth/selecao-cadastro";
    }

    @GetMapping("/cadastro/cliente")
    public String formCadastroCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "auth/cadastro-cliente";
    }

    @PostMapping("/cadastro/cliente")
    public String cadastrarCliente(Cliente cliente,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            Cliente salvo = clienteService.cadastrar(cliente);
            // Autentica automaticamente após o cadastro
            authService.autenticar(salvo.getEmail(), salvo.getSenha(), session);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Bem-vindo ao Attorney App.");
            return "redirect:/cliente/dashboard";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/cadastro/cliente";
        }
    }

    @GetMapping("/cadastro/advogado")
    public String formCadastroAdvogado(Model model) {
        model.addAttribute("advogado", new Advogado());
        return "auth/cadastro-advogado";
    }

    @PostMapping("/cadastro/advogado")
    public String cadastrarAdvogado(Advogado advogado,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            Advogado salvo = advogadoService.cadastrar(advogado);
            authService.autenticar(salvo.getEmail(), salvo.getSenha(), session);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro profissional concluído com sucesso!");
            return "redirect:/advogado/dashboard";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/cadastro/advogado";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        authService.logout(session);
        redirectAttributes.addFlashAttribute("mensagemInfo", "Você saiu da sua conta com segurança.");
        return "redirect:/";
    }
}
