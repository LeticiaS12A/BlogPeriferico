package com.tcc.blogperiferico.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.blogperiferico.dto.VagaDTO;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.services.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vagas")
@CrossOrigin(origins = "*", allowCredentials = "*")
public class VagaController {

    @Autowired
    private VagaService vagaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // VISITANTE pode visualizar todas as vagas
    @GetMapping
    public ResponseEntity<List<VagaDTO>> listarVagas() {
        return ResponseEntity.ok(vagaService.listarVagas());
    }

    // Buscar vaga por ID
    @GetMapping("/{id}")
    public ResponseEntity<VagaDTO> buscarPorId(@PathVariable Long id) {
        Optional<VagaDTO> vaga = vagaService.buscarPorId(id);
        return vaga.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar vaga com usuário logado
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<VagaDTO> criarVaga(
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        VagaDTO dto = objectMapper.readValue(dtoJson, VagaDTO.class);
        VagaDTO nova = vagaService.criarVaga(dto, file, usuarioLogado);

        return ResponseEntity.ok(nova);
    }

    // Atualizar vaga com usuário logado
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<VagaDTO> atualizarVaga(
            @PathVariable Long id,
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        VagaDTO dto = objectMapper.readValue(dtoJson, VagaDTO.class);
        Optional<VagaDTO> atualizado = vagaService.atualizarVaga(id, dto, file, usuarioLogado);

        return atualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir vaga
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVaga(@PathVariable Long id) {
        boolean excluido = vagaService.excluirVaga(id);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
