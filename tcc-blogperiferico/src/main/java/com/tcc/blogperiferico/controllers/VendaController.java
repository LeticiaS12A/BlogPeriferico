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

import com.tcc.blogperiferico.dto.VendaDTO;
import com.tcc.blogperiferico.services.VendaService;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService anuncioService;

    // Criar uma nova venda
    @PostMapping
    public ResponseEntity<VendaDTO> criarVenda(@RequestBody VendaDTO dto) {
        VendaDTO novoVenda = anuncioService.criarVenda(dto);
        return ResponseEntity.ok(novoVenda);
    }

    // Listar todos as vendas
    @GetMapping
    public ResponseEntity<List<VendaDTO>> listarVendas() {
        List<VendaDTO> venda = anuncioService.listarVendas();
        return ResponseEntity.ok(venda);
    }

    // Buscar um venda por ID
    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> buscarPorId(@PathVariable Long id) {
        Optional<VendaDTO> venda = anuncioService.buscarPorId(id);
        return venda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar um venda por ID
    @PutMapping("/{id}")
    public ResponseEntity<VendaDTO> atualizarVenda(@PathVariable Long id, @RequestBody VendaDTO dto) {
        Optional<VendaDTO> vendaAtualizado = anuncioService.atualizarVenda(id, dto);
        return vendaAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir um venda por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        boolean excluido = anuncioService.excluirVenda(id);
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
	
}
