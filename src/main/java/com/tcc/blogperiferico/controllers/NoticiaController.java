package com.tcc.blogperiferico.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.blogperiferico.dto.NoticiaDTO;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.services.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/noticias")
@CrossOrigin(origins = "*", allowCredentials = "*")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Criar notícia com upload de imagem
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<NoticiaDTO> criarNoticia(
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        NoticiaDTO dto = objectMapper.readValue(dtoJson, NoticiaDTO.class);
        NoticiaDTO nova = noticiaService.criarNoticia(dto, file, usuarioLogado);

        return ResponseEntity.ok(nova);
    }

    // Listar todas notícias
    @GetMapping
    public ResponseEntity<List<NoticiaDTO>> listarNoticias() {
        List<NoticiaDTO> noticias = noticiaService.listarNoticias();
        return ResponseEntity.ok(noticias);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<NoticiaDTO> buscarPorId(@PathVariable Long id) {
        Optional<NoticiaDTO> noticia = noticiaService.buscarPorId(id);
        return noticia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar notícia
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<NoticiaDTO> atualizarNoticia(
            @PathVariable Long id,
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        NoticiaDTO dto = objectMapper.readValue(dtoJson, NoticiaDTO.class);
        Optional<NoticiaDTO> atualizado = noticiaService.atualizarNoticia(id, dto, file, usuarioLogado);

        return atualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir notícia (ADMIN pode deletar qualquer uma, AUTOR pode deletar apenas a sua)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirNoticia(@PathVariable Long id, Authentication authentication) {
        // Busca o usuário logado
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca a notícia
        Optional<NoticiaDTO> noticiaOpt = noticiaService.buscarPorId(id);
        if (noticiaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        NoticiaDTO noticia = noticiaOpt.get();
        
        // Verifica se é ADMINISTRADOR
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        
        // Verifica se é o autor da notícia
        boolean isAutor = noticia.getIdUsuario() != null && 
                         noticia.getIdUsuario().equals(usuarioLogado.getId());
        
        // Se não for ADMIN nem AUTOR, nega acesso
        if (!isAdmin && !isAutor) {
            throw new org.springframework.security.authorization.AuthorizationDeniedException(
                "Você não tem permissão para excluir esta notícia"
            );
        }
        
        // Exclui a notícia
        boolean excluido = noticiaService.excluirNoticia(id);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}