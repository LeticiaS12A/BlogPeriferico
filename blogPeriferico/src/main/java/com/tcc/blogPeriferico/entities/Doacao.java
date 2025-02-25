package com.tcc.blogPeriferico.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name  = "tb_doacao")
public class Doacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String local;
	@Column(columnDefinition = "TEXT")
	private String texto;
	@Column(columnDefinition = "TEXT")
	private String imagem;
	private String tipoItem;
	
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
	public String getTipoItem() {
		return tipoItem;
	}
	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}
	
	//Constructors
	public Doacao() {
	}
	public Doacao(Long id, String local, String texto, String imagem, String tipoItem) {
		this.id = id;
		this.local = local;
		this.texto = texto;
		this.imagem = imagem;
		this.tipoItem = tipoItem;
	}
	
	
	
}
