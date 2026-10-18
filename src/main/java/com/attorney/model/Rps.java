package com.attorney.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rps_relacao_servicos")
public class Rps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(name = "nome_empresa")
    private String nomeEmpresa = "Attorney App Intermediação Jurídica LTDA";

    @Column(length = 20)
    private String cnpj = "00.000.000/0001-99";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advogado_id", nullable = false)
    private Advogado advogado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "nome_advogado")
    private String nomeAdvogado;

    @Column(name = "oab_advogado")
    private String oab;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "cpf_cliente")
    private String cpf;

    @Column(name = "descricao_servico", length = 2000)
    private String descricaoServico;

    @Column(name = "status")
    private String status = "EM_ANDAMENTO"; // EM_ANDAMENTO, CONCLUIDO, CANCELADO

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    public Rps() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Rps(String numero, Advogado advogado, Cliente cliente, String descricaoServico) {
        this.numero = numero;
        this.advogado = advogado;
        this.cliente = cliente;
        this.nomeAdvogado = advogado != null ? advogado.getNome() : null;
        this.oab = advogado != null ? advogado.getOab() : null;
        this.nomeCliente = cliente != null ? cliente.getNome() : null;
        this.cpf = cliente != null ? cliente.getCpf() : null;
        this.descricaoServico = descricaoServico;
        this.dataCriacao = LocalDateTime.now();
        this.status = "EM_ANDAMENTO";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Advogado getAdvogado() {
        return advogado;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
        if (advogado != null) {
            this.nomeAdvogado = advogado.getNome();
            this.oab = advogado.getOab();
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            this.nomeCliente = cliente.getNome();
            this.cpf = cliente.getCpf();
        }
    }

    public String getNomeAdvogado() {
        return nomeAdvogado;
    }

    public void setNomeAdvogado(String nomeAdvogado) {
        this.nomeAdvogado = nomeAdvogado;
    }

    public String getOab() {
        return oab;
    }

    public void setOab(String oab) {
        this.oab = oab;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
