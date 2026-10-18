package com.attorney.controller;

import com.attorney.dto.UsuarioLogadoDto;
import com.attorney.model.Cliente;
import com.attorney.model.MensagemContato;
import com.attorney.model.Rps;
import com.attorney.service.AuthService;
import com.attorney.service.ClienteService;
import com.attorney.service.MensagemContatoService;
import com.attorney.service.RpsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final AuthService authService;
    private final ClienteService clienteService;
    private final RpsService rpsService;
    private final MensagemContatoService mensagemContatoService;

    @Value("${app.upload.dir:./uploads/profile-pictures/}")
    private String uploadDir;

    public ClienteController(AuthService authService,
                             ClienteService clienteService,
                             RpsService rpsService,
                             MensagemContatoService mensagemContatoService) {
        this.authService = authService;
        this.clienteService = clienteService;
        this.rpsService = rpsService;
        this.mensagemContatoService = mensagemContatoService;
    }

    private Cliente verificarClienteLogado(HttpSession session) {
        UsuarioLogadoDto user = authService.getUsuarioLogado(session);
        if (user == null || !user.isCliente()) {
            return null;
        }
        return clienteService.buscarPorId(user.getId()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Cliente cliente = verificarClienteLogado(session);
        if (cliente == null) {
            return "redirect:/login?redirect=/cliente/dashboard";
        }

        List<Rps> processos = rpsService.listarPorCliente(cliente.getId());
        List<MensagemContato> mensagens = mensagemContatoService.listarPorCliente(cliente.getId());

        model.addAttribute("cliente", cliente);
        model.addAttribute("processos", processos);
        model.addAttribute("mensagens", mensagens);
        return "cliente/dashboard";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Cliente cliente = verificarClienteLogado(session);
        if (cliente == null) {
            return "redirect:/login?redirect=/cliente/perfil";
        }
        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@RequestParam("nome") String nome,
                                  @RequestParam("endereco") String endereco,
                                  @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
                                  @RequestParam(value = "fotoUrlWeb", required = false) String fotoUrlWeb,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Cliente cliente = verificarClienteLogado(session);
        if (cliente == null) {
            return "redirect:/login";
        }

        try {
            cliente.setNome(nome);
            cliente.setEndereco(endereco);

            // Upload de arquivo de foto (RF005)
            if (fotoFile != null && !fotoFile.isEmpty()) {
                Path dir = Paths.get(uploadDir);
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                }

                String filename = "cliente-" + cliente.getId() + "-" + UUID.randomUUID().toString().substring(0, 8) + "-" + fotoFile.getOriginalFilename();
                Path targetLocation = dir.resolve(filename);
                Files.copy(fotoFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                String publicUrl = "/uploads/profile-pictures/" + filename;
                cliente.setFotoPerfilUrl(publicUrl);
            } else if (fotoUrlWeb != null && !fotoUrlWeb.trim().isEmpty()) {
                cliente.setFotoPerfilUrl(fotoUrlWeb.trim());
            }

            clienteService.atualizarPerfil(cliente);

            // Atualiza sessão
            UsuarioLogadoDto user = authService.getUsuarioLogado(session);
            user.setNome(cliente.getNome());
            user.setFotoPerfilUrl(cliente.getFotoPerfilUrl());
            session.setAttribute(AuthService.SESSION_USER_KEY, user);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Perfil atualizado com sucesso!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar a imagem: " + e.getMessage());
        }

        return "redirect:/cliente/perfil";
    }
}
