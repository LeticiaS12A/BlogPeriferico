package com.tcc.blogperiferico.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.blogperiferico.dto.VagaDTO;
import com.tcc.blogperiferico.services.VagaService;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;

    // Criar um novo anúncio
    @PostMapping
    public ResponseEntity<VagaDTO> criarVaga(@RequestBody VagaDTO dto) {
    	VagaDTO novoVaga = vagaService.criarVaga(dto);
        return ResponseEntity.ok(novoVaga);
    }

    // Listar todos os anúncios
    @GetMapping
    public ResponseEntity<List<VagaDTO>> listarVagas() {
        List<VagaDTO> vagas = vagaService.listarVagas();
        return ResponseEntity.ok(vagas);
    }

    // Buscar um anúncio por ID
    @GetMapping("/{id}")
    public ResponseEntity<VagaDTO> buscarPorId(@PathVariable Long id) {
        Optional<VagaDTO> vaga = vagaService.buscarPorId(id);
        return vaga.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar um anúncio por ID
    @PutMapping("/{id}")
    public ResponseEntity<VagaDTO> atualizarVaga(@PathVariable Long id, @RequestBody VagaDTO dto) {
        Optional<VagaDTO> vagaAtualizado = vagaService.atualizarVaga(id, dto);
        return vagaAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir um anúncio por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVaga(@PathVariable Long id) {
        boolean excluido = vagaService.excluirVaga(id);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
