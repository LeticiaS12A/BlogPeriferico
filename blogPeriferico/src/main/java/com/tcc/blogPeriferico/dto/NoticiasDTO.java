package com.tcc.blogPeriferico.dto;

import com.tcc.blogPeriferico.entities.Noticias;

public class NoticiasDTO {

	private Long id;
	private String local;
	private String texto;
	private String imagem;
	
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

	//Constructors
	public NoticiasDTO() {
		
	}

	public NoticiasDTO(Long id, String local, String texto, String imagem) {
		this.id = id;
		this.local = local;
		this.texto = texto;
		this.imagem = imagem;
	}
	
	public NoticiasDTO(Noticias n) {
		id = n.getId();
		local = n.getLocal();
		texto = n.getTexto();
		imagem = n.getImagem();
	}
	
	
}
