package com.tcc.blogperiferico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.blogperiferico.dto.NoticiaDTO;
import com.tcc.blogperiferico.entities.Noticia;
import com.tcc.blogperiferico.repository.NoticiaRepository;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiasRepository;

    // Criar novo anúncio
    public NoticiaDTO criarNoticia(NoticiaDTO dto) {
    	Noticia noticias = new Noticia(dto.getId(), dto.getZona(), dto.getTitulo(), dto.getTexto(), dto.getImagem(), dto.getDataHoraCriacao(), dto.getIdUsuario());
    	noticias = noticiasRepository.save(noticias);
        return new NoticiaDTO(noticias.getId(), dto.getZona(), dto.getTexto(), dto.getTexto(), dto.getImagem(), dto.getDataHoraCriacao(), dto.getIdUsuario());
    }

    // Listar todos os anúncios
    public List<NoticiaDTO> listarNoticias() {
        List<Noticia> noticias = noticiasRepository.findAll();
        return noticias.stream()
                .map(n -> new NoticiaDTO(n.getId(), n.getZona(), n.getTitulo(), n.getTexto(), n.getImagem(), n.getDataHoraCriacao(), n.getIdUsuario()))
                .collect(Collectors.toList());
    }

    // Buscar anúncio por ID
    public Optional<NoticiaDTO> buscarPorId(Long id) {
        Optional<Noticia> noticias = noticiasRepository.findById(id);
        return noticias.map(n -> new NoticiaDTO(n.getId(), n.getZona(), n.getTitulo(), n.getTexto(), n.getImagem(), n.getDataHoraCriacao(), n.getIdUsuario()));
    }

    // Atualizar anúncio
    public Optional<NoticiaDTO> atualizarNoticia(Long id, NoticiaDTO dto) {
        Optional<Noticia> noticiasOpt = noticiasRepository.findById(id);
        if (noticiasOpt.isPresent()) {
        	Noticia noticias = noticiasOpt.get();
        	noticias.setZona(dto.getZona());
        	noticias.setTitulo(dto.getTitulo());
        	noticias.setTexto(dto.getTexto());
        	noticias.setImagem(dto.getImagem());
        	noticias.setDataHoraCriacao(dto.getDataHoraCriacao());
        	noticiasRepository.save(noticias);
            return Optional.of(new NoticiaDTO(noticias.getId(), noticias.getZona(), noticias.getTitulo(), noticias.getTexto(), noticias.getImagem(), noticias.getDataHoraCriacao(), noticias.getIdUsuario()));
        }
        return Optional.empty();
    }

    // Excluir anúncio
    public boolean excluirNoticia(Long id) {
        if (noticiasRepository.existsById(id)) {
            noticiasRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
