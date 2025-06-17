package com.tcc.blogperiferico.dto;

import java.time.LocalDateTime;

import com.tcc.blogperiferico.entities.Noticia;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.entities.Zona;

public class NoticiaDTO {

	private Long id;
	private Zona zona;
	private String titulo;
	private String texto;
	private String imagem;
	private LocalDateTime dataHoraCriacao;
	private Usuario idUsuario;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalDateTime getDataHoraCriacao() {
		return dataHoraCriacao;
	}
	public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}
	
	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	// Construtores
	public NoticiaDTO() {
	}

	public NoticiaDTO(Long id, Zona zona, String titulo, String texto, String imagem, LocalDateTime dataHoraCriacao, Usuario idUsuario) {
		this.id = id;
		this.zona = zona;
		this.titulo = titulo;
		this.texto = texto;
		this.imagem = imagem;
		this.dataHoraCriacao = dataHoraCriacao;
		this.idUsuario = idUsuario;
	}

	// Construtor para converter de Noticia para NoticiaDTO
	public NoticiaDTO(Noticia noticia) {
		this.id = noticia.getId();
		this.zona = noticia.getZona();
		this.titulo = noticia.getTitulo();
		this.texto = noticia.getTexto();
		this.imagem = noticia.getImagem();
		this.dataHoraCriacao = noticia.getDataHoraCriacao(); // Converte LocalDateTime para String
		this.idUsuario = noticia.getIdUsuario();
	}

}
