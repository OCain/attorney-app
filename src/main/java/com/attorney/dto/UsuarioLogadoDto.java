package com.attorney.dto;

import com.attorney.model.TipoUsuario;

public class UsuarioLogadoDto {
    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private String fotoPerfilUrl;
    private String identificador; // CPF para cliente, OAB para advogado

    public UsuarioLogadoDto() {
    }

    public UsuarioLogadoDto(Long id, String nome, String email, TipoUsuario tipo, String fotoPerfilUrl, String identificador) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.fotoPerfilUrl = fotoPerfilUrl;
        this.identificador = identificador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public boolean isCliente() {
        return tipo == TipoUsuario.CLIENTE;
    }

    public boolean isAdvogado() {
        return tipo == TipoUsuario.ADVOGADO;
    }
}
