package com.attorney.controller;

import com.attorney.dto.UsuarioLogadoDto;
import com.attorney.model.Advogado;
import com.attorney.model.Cliente;
import com.attorney.model.MensagemContato;
import com.attorney.model.TipoUsuario;
import com.attorney.service.AdvogadoService;
import com.attorney.service.AuthService;
import com.attorney.service.ClienteService;
import com.attorney.service.MensagemContatoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ContatoChatController {

    private final AuthService authService;
    private final ClienteService clienteService;
    private final AdvogadoService advogadoService;
    private final MensagemContatoService mensagemContatoService;

    public ContatoChatController(AuthService authService,
                                 ClienteService clienteService,
                                 AdvogadoService advogadoService,
                                 MensagemContatoService mensagemContatoService) {
        this.authService = authService;
        this.clienteService = clienteService;
        this.advogadoService = advogadoService;
        this.mensagemContatoService = mensagemContatoService;
    }

    @PostMapping("/contato/enviar")
    public String enviarContato(@RequestParam("advogadoId") Long advogadoId,
                                @RequestParam("assunto") String assunto,
                                @RequestParam("mensagem") String mensagem,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UsuarioLogadoDto user = authService.getUsuarioLogado(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("mensagemInfo", "Faça login para entrar em contato com o advogado.");
            return "redirect:/login?redirect=/advogados/" + advogadoId;
        }

        if (!user.isCliente()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Apenas clientes podem enviar propostas e contatos a advogados.");
            return "redirect:/advogados/" + advogadoId;
        }

        try {
            mensagemContatoService.enviarMensagem(user.getId(), advogadoId, assunto, mensagem, TipoUsuario.CLIENTE);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Mensagem enviada com sucesso! O advogado responderá em breve.");
            return "redirect:/chat/" + advogadoId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao enviar mensagem: " + e.getMessage());
            return "redirect:/advogados/" + advogadoId;
        }
    }

    @GetMapping("/chat/{advogadoId}")
    public String telaChat(@PathVariable("advogadoId") Long advogadoId,
                           HttpSession session,
                           Model model) {
        UsuarioLogadoDto user = authService.getUsuarioLogado(session);
        if (user == null) {
            return "redirect:/login?redirect=/chat/" + advogadoId;
        }

        Advogado advogado = advogadoService.buscarPorId(advogadoId)
                .orElseThrow(() -> new IllegalArgumentException("Advogado não encontrado: " + advogadoId));

        Long clienteId;
        if (user.isCliente()) {
            clienteId = user.getId();
        } else {
            // Se for advogado abrindo o chat, verifica cliente selecionado na request ou primeiro contato
            clienteId = (Long) session.getAttribute("chat_cliente_id");
            if (clienteId == null) {
                List<MensagemContato> msgs = mensagemContatoService.listarPorAdvogado(advogado.getId());
                clienteId = msgs.isEmpty() ? null : msgs.get(0).getCliente().getId();
            }
        }

        List<MensagemContato> conversa = (clienteId != null)
                ? mensagemContatoService.listarConversa(clienteId, advogado.getId())
                : List.of();

        Cliente cliente = (clienteId != null) ? clienteService.buscarPorId(clienteId).orElse(null) : null;

        model.addAttribute("advogado", advogado);
        model.addAttribute("cliente", cliente);
        model.addAttribute("conversa", conversa);
        model.addAttribute("usuarioLogado", user);
        return "chat/conversa";
    }

    @PostMapping("/chat/enviar")
    public String enviarMensagemChat(@RequestParam("advogadoId") Long advogadoId,
                                     @RequestParam(value = "clienteId", required = false) Long clienteId,
                                     @RequestParam("mensagem") String mensagem,
                                     HttpSession session) {
        UsuarioLogadoDto user = authService.getUsuarioLogado(session);
        if (user == null) {
            return "redirect:/login";
        }

        TipoUsuario enviadoPor = user.getTipo();
        Long cId = user.isCliente() ? user.getId() : clienteId;

        if (cId != null && !mensagem.trim().isEmpty()) {
            mensagemContatoService.enviarMensagem(cId, advogadoId, "Chat Attorney", mensagem.trim(), enviadoPor);
        }

        return "redirect:/chat/" + advogadoId;
    }

    @PostMapping("/avaliar/{advogadoId}")
    public String registrarAvaliacao(@PathVariable("advogadoId") Long advogadoId,
                                     @RequestParam("nota") Integer nota,
                                     @RequestParam("comentario") String comentario,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        UsuarioLogadoDto user = authService.getUsuarioLogado(session);
        if (user == null || !user.isCliente()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Apenas clientes autenticados podem avaliar advogados.");
            return "redirect:/advogados/" + advogadoId;
        }

        Cliente cliente = clienteService.buscarPorId(user.getId()).orElse(null);
        if (cliente == null) {
            return "redirect:/login";
        }

        try {
            advogadoService.registrarAvaliacao(advogadoId, cliente, nota, comentario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Avaliação enviada com sucesso! Obrigado por contribuir.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao registrar avaliação: " + e.getMessage());
        }

        return "redirect:/advogados/" + advogadoId;
    }
}
