package com.tcc.blogperiferico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tcc.blogperiferico.dto.ComentarioDTO;
import com.tcc.blogperiferico.entities.Comentario;
import com.tcc.blogperiferico.entities.Doacao;
import com.tcc.blogperiferico.entities.Noticia;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.entities.Vaga;
import com.tcc.blogperiferico.entities.Venda;
import com.tcc.blogperiferico.enums.UsuarioRole;
import com.tcc.blogperiferico.repository.ComentarioRepository;
import com.tcc.blogperiferico.repository.DoacaoRepository;
import com.tcc.blogperiferico.repository.NoticiaRepository;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.repository.VagaRepository;
import com.tcc.blogperiferico.repository.VendaRepository;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;

    // Criar comentário
    public ComentarioDTO criarComentario(ComentarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Comentario comentario = new Comentario();
        comentario.setTexto(dto.getTexto());
        comentario.setDataHoraCriacao(LocalDateTime.now());
        comentario.setUsuario(usuario);

        if (dto.getIdNoticia() != null) {
            Noticia noticia = noticiaRepository.findById(dto.getIdNoticia())
                    .orElseThrow(() -> new IllegalArgumentException("Notícia não encontrada"));
            comentario.setNoticia(noticia);
        } else if (dto.getIdVenda() != null) {
            Venda venda = vendaRepository.findById(dto.getIdVenda())
                    .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));
            comentario.setVenda(venda);
        } else if (dto.getIdVaga() != null) {
            Vaga vaga = vagaRepository.findById(dto.getIdVaga())
                    .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));
            comentario.setVaga(vaga);
        } else if (dto.getIdDoacao() != null) {
            Doacao doacao = doacaoRepository.findById(dto.getIdDoacao())
                    .orElseThrow(() -> new IllegalArgumentException("Doação não encontrada"));
            comentario.setDoacao(doacao);
        } else {
            throw new IllegalArgumentException("É necessário informar o tipo da postagem (Notícia, Venda, Vaga ou Doação)");
        }

        comentario = comentarioRepository.save(comentario);
        return new ComentarioDTO(comentario);
    }

    // Listar comentários por tipo (corrigido)
    public List<ComentarioDTO> listarComentariosNoticia(Long idNoticia) {
        return comentarioRepository.findByNoticia_Id(idNoticia).stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
    }

    public List<ComentarioDTO> listarComentariosVenda(Long idVenda) {
        return comentarioRepository.findByVenda_Id(idVenda).stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
    }

    public List<ComentarioDTO> listarComentariosVaga(Long idVaga) {
        return comentarioRepository.findByVaga_Id(idVaga).stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
    }

    public List<ComentarioDTO> listarComentariosDoacao(Long idDoacao) {
        return comentarioRepository.findByDoacao_Id(idDoacao).stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
    }

    // Excluir comentário
    public boolean excluirComentario(Long id) {
        if (comentarioRepository.existsById(id)) {
            comentarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
