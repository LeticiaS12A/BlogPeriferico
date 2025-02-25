package com.tcc.blogPeriferico.dto;

import com.tcc.blogPeriferico.entities.Anuncio;

import jakarta.persistence.Column;

public class AnuncioDTO {

	private Long id;
	private String local;
	@Column(columnDefinition = "TEXT")
	private String texto;
	@Column(columnDefinition = "TEXT")
	private String imagem;
	private Float preco;
	private int telefone;
	
	//Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public Float getPreco() {
		return preco;
	}
	public void setPreco(Float preco) {
		this.preco = preco;
	}
	public int getTelefone() {
		return telefone;
	}
	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}
	
	//Constructors
	public AnuncioDTO() {
	}
	public AnuncioDTO(Long id, String local, String texto, String imagem, Float preco, int telefone) {
		this.id = id;
		this.local = local;
		this.texto = texto;
		this.imagem = imagem;
		this.preco = preco;
		this.telefone = telefone;
	}
	
	public AnuncioDTO(Anuncio v) {
		id = v.getId();
		local = v.getLocal();
		texto = v.getTexto();
		imagem = v.getImagem();
		preco = v.getPreco();
		telefone = v.getTelefone();
	}
	
}
