package com.tcc.blogperiferico.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.blogperiferico.dto.ComentarioDTO;
import com.tcc.blogperiferico.entities.Comentario;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.ComentarioRepository;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.services.ComentarioService;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin(origins = "*", allowCredentials = "*")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;
    
    @Autowired
    private ComentarioRepository comentarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Criar comentário
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ComentarioDTO> criarComentario(@RequestBody ComentarioDTO dto) {
        return ResponseEntity.ok(comentarioService.criarComentario(dto));
    }

    // Listar comentários por tipo
    @GetMapping("/noticia/{idNoticia}")
    public ResponseEntity<List<ComentarioDTO>> listarComentariosNoticia(@PathVariable Long idNoticia) {
        return ResponseEntity.ok(comentarioService.listarComentariosNoticia(idNoticia));
    }

    @GetMapping("/venda/{idVenda}")
    public ResponseEntity<List<ComentarioDTO>> listarComentariosVenda(@PathVariable Long idVenda) {
        return ResponseEntity.ok(comentarioService.listarComentariosVenda(idVenda));
    }

    @GetMapping("/vaga/{idVaga}")
    public ResponseEntity<List<ComentarioDTO>> listarComentariosVaga(@PathVariable Long idVaga) {
        return ResponseEntity.ok(comentarioService.listarComentariosVaga(idVaga));
    }

    @GetMapping("/doacao/{idDoacao}")
    public ResponseEntity<List<ComentarioDTO>> listarComentariosDoacao(@PathVariable Long idDoacao) {
        return ResponseEntity.ok(comentarioService.listarComentariosDoacao(idDoacao));
    }

    // Excluir comentário (ADMIN pode deletar qualquer um, AUTOR pode deletar apenas o seu)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirComentario(@PathVariable Long id, Authentication authentication) {
        // Busca o usuário logado
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca o comentário
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
        if (comentarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comentario comentario = comentarioOpt.get();

        // Verifica se é ADMINISTRADOR
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        // Verifica se é o autor do comentário
        boolean isAutor = comentario.getUsuario() != null &&
                         comentario.getUsuario().getId().equals(usuarioLogado.getId());

        // Se não for ADMIN nem AUTOR, nega acesso
        if (!isAdmin && !isAutor) {
            throw new org.springframework.security.authorization.AuthorizationDeniedException(
                "Você não tem permissão para excluir este comentário"
            );
        }

        // Exclui o comentário
        boolean excluido = comentarioService.excluirComentario(id);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
