package com.tcc.blogperiferico.dto;

import com.tcc.blogperiferico.entities.Venda;
import com.tcc.blogperiferico.entities.Zona;
import java.time.LocalDateTime;

public class VendaDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String imagem;
    private Float valor;
    private String telefone;
    private String cpf;
    private Zona zona;
    private LocalDateTime dataHoraCriacao;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Float getValor() { return valor; }
    public void setValor(Float valor) { this.valor = valor; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }

    public LocalDateTime getDataHoraCriacao() { return dataHoraCriacao; }
    public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) { this.dataHoraCriacao = dataHoraCriacao; }

    // Construtores
    public VendaDTO() {}

    public VendaDTO(Venda venda) {
        this.id = venda.getId();
        this.titulo = venda.getTitulo();
        this.descricao = venda.getDescricao();
        this.imagem = venda.getImagem();
        this.valor = venda.getValor();
        this.telefone = venda.getTelefone();
        this.cpf = venda.getCpf();
        this.zona = venda.getZona();
        this.dataHoraCriacao = venda.getDataHoraCriacao();
    }
}
