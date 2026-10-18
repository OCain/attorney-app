package com.attorney.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensagens_contato")
public class MensagemContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advogado_id", nullable = false)
    private Advogado advogado;

    @NotBlank(message = "O assunto é obrigatório")
    @Column(nullable = false)
    private String assunto;

    @NotBlank(message = "A mensagem é obrigatória")
    @Column(nullable = false, columnDefinition = "TEXT", length = 10000)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "enviado_por", nullable = false)
    private TipoUsuario enviadoPor = TipoUsuario.CLIENTE;

    @Column(name = "lida")
    private boolean lida = false;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    public MensagemContato() {
        this.dataEnvio = LocalDateTime.now();
    }

    public MensagemContato(Cliente cliente, Advogado advogado, String assunto, String mensagem, TipoUsuario enviadoPor) {
        this.cliente = cliente;
        this.advogado = advogado;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.enviadoPor = enviadoPor;
        this.dataEnvio = LocalDateTime.now();
        this.lida = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Advogado getAdvogado() {
        return advogado;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public TipoUsuario getEnviadoPor() {
        return enviadoPor;
    }

    public void setEnviadoPor(TipoUsuario enviadoPor) {
        this.enviadoPor = enviadoPor;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
