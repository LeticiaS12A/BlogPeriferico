package com.tcc.blogperiferico.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.blogperiferico.dto.VendaDTO;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/vendas", "/anuncios"})
@CrossOrigin(origins = "*", allowCredentials = "*")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // VISITANTE pode visualizar
    @GetMapping
    public ResponseEntity<List<VendaDTO>> listarVendas() {
        return ResponseEntity.ok(vendaService.listarVendas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> buscarPorId(@PathVariable Long id) {
        Optional<VendaDTO> venda = vendaService.buscarPorId(id);
        return venda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar venda com imagem
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<VendaDTO> criarVenda(
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        VendaDTO dto = objectMapper.readValue(dtoJson, VendaDTO.class);
        VendaDTO novaVenda = vendaService.criarVenda(dto, file, usuarioLogado);

        return ResponseEntity.ok(novaVenda);
    }

    // Atualizar venda
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<VendaDTO> atualizarVenda(
            @PathVariable Long id,
            @RequestParam("dto") String dtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication) throws Exception {

        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        VendaDTO dto = objectMapper.readValue(dtoJson, VendaDTO.class);
        Optional<VendaDTO> atualizada = vendaService.atualizarVenda(id, dto, file, usuarioLogado);

        return atualizada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir venda
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean excluido = vendaService.excluirVenda(id, usuarioLogado);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
