package com.tcc.blogperiferico.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.blogperiferico.dto.DoacaoDTO;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.services.DoacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doacoes")
@CrossOrigin(origins = "*", allowCredentials = "*")
public class DoacaoController {

    @Autowired
    private DoacaoService doacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Criar nova doação com upload de imagem
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<DoacaoDTO> criarDoacao(
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        DoacaoDTO dto = objectMapper.readValue(dtoJson, DoacaoDTO.class);
        DoacaoDTO nova = doacaoService.criarDoacao(dto, file, usuarioLogado);

        return ResponseEntity.ok(nova);
    }

    // Listar todas as doações
    @GetMapping
    public ResponseEntity<List<DoacaoDTO>> listarDoacoes() {
        List<DoacaoDTO> doacoes = doacaoService.listarDoacoes();
        return ResponseEntity.ok(doacoes);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<DoacaoDTO> buscarPorId(@PathVariable Long id) {
        Optional<DoacaoDTO> doacao = doacaoService.buscarPorId(id);
        return doacao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar doação (dados + imagem nova)
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<DoacaoDTO> atualizarDoacao(
            @PathVariable Long id,
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        DoacaoDTO dto = objectMapper.readValue(dtoJson, DoacaoDTO.class);
        Optional<DoacaoDTO> atualizado = doacaoService.atualizarDoacao(id, dto, file, usuarioLogado);

        return atualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir doação (ADMIN pode deletar qualquer uma, AUTOR pode deletar apenas a sua)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDoacao(@PathVariable Long id, Authentication authentication) {
        // Busca o usuário logado
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca a doação
        Optional<DoacaoDTO> doacaoOpt = doacaoService.buscarPorId(id);
        if (doacaoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        DoacaoDTO doacao = doacaoOpt.get();

        // Verifica se é ADMINISTRADOR
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        // Verifica se é o autor da doação
        boolean isAutor = doacao.getIdUsuario() != null &&
                         doacao.getIdUsuario().equals(usuarioLogado.getId());

        // Se não for ADMIN nem AUTOR, nega acesso
        if (!isAdmin && !isAutor) {
            throw new org.springframework.security.authorization.AuthorizationDeniedException(
                "Você não tem permissão para excluir esta doação"
            );
        }

        // Exclui a doação
        boolean excluido = doacaoService.excluirDoacao(id);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
