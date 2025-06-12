package com.tcc.blogperiferico.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tcc.blogperiferico.dto.NoticiaDTO;
import com.tcc.blogperiferico.services.NoticiaService;

@RestController
@RequestMapping("/noticias")
public class NoticiaController {

    @Autowired
    private NoticiaService noticiasService;

    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<NoticiaDTO> criarNoticia(@RequestBody NoticiaDTO dto) {
        NoticiaDTO nova = noticiasService.criarNoticia(dto);
        return ResponseEntity.ok(nova);
    }

    @GetMapping
    public ResponseEntity<List<NoticiaDTO>> listarNoticias() {
        List<NoticiaDTO> noticias = noticiasService.listarNoticias();
        return ResponseEntity.ok(noticias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticiaDTO> buscarPorId(@PathVariable Long id) {
        Optional<NoticiaDTO> noticia = noticiasService.buscarPorId(id);
        return noticia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<NoticiaDTO> atualizarNoticia(@PathVariable Long id, @RequestBody NoticiaDTO dto) {
        Optional<NoticiaDTO> atualizado = noticiasService.atualizarNoticia(id, dto);
        return atualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirNoticia(@PathVariable Long id) {
        boolean excluido = noticiasService.excluirNoticia(id);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
