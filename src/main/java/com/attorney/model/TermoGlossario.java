package com.attorney.model;

import jakarta.persistence.*;

@Entity
@Table(name = "termos_glossario", indexes = {
    @Index(name = "idx_termo_nome", columnList = "termo")
})
public class TermoGlossario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String termo;

    @Column(nullable = false, columnDefinition = "TEXT", length = 10000)
    private String significado;

    @Column(length = 100)
    private String categoria = "Jurídico";

    public TermoGlossario() {
    }

    public TermoGlossario(String termo, String significado) {
        this.termo = termo;
        this.significado = significado;
        this.categoria = "Jurídico";
    }

    public TermoGlossario(String termo, String significado, String categoria) {
        this.termo = termo;
        this.significado = significado;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public String getSignificado() {
        return significado;
    }

    public void setSignificado(String significado) {
        this.significado = significado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
