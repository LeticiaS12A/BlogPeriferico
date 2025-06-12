package com.tcc.blogperiferico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.blogperiferico.dto.VendaDTO;
import com.tcc.blogperiferico.entities.Venda;
import com.tcc.blogperiferico.repository.VendaRepository;

@Service
public class VendaService {


	@Autowired
    private VendaRepository vendaRepository;

    // Criar novo anúncio
    public VendaDTO criarVenda(VendaDTO dto) {
        Venda vendas = new Venda(dto.getId(), dto.getTitulo(), dto.getDescricao(), dto.getImagem(), dto.getValor(), dto.getTelefone(), dto.getCpf(), dto.getZona(), dto.getDataHoraCriacao(), dto.getIdUsuario());
        vendas = vendaRepository.save(vendas);
        return new VendaDTO(vendas.getId(), dto.getTitulo(), dto.getDescricao(), dto.getImagem(), dto.getValor(), dto.getTelefone(), dto.getCpf(), dto.getZona(), dto.getDataHoraCriacao(), dto.getIdUsuario());
    }

    // Listar todos os anúncios
    public List<VendaDTO> listarVendas() {
        List<Venda> vendas = vendaRepository.findAll();
        return vendas.stream()
                .map(v -> new VendaDTO(v.getId(), v.getTitulo(), v.getDescricao(), v.getImagem(), v.getValor(), v.getTelefone(), v.getCpf(), v.getZona(), v.getDataHoraCriacao(), v.getIdUsuario()))
                .collect(Collectors.toList());
    }

    // Buscar anúncio por ID
    public Optional<VendaDTO> buscarPorId(Long id) {
        Optional<Venda> vendas = vendaRepository.findById(id);
        return vendas.map(v -> new VendaDTO(v.getId(), v.getTitulo(), v.getDescricao(), v.getImagem(), v.getValor(), v.getTelefone(), v.getCpf(), v.getZona(), v.getDataHoraCriacao(), v.getIdUsuario()));
    }

    // Atualizar anúncio
    public Optional<VendaDTO> atualizarVenda(Long id, VendaDTO dto) {
        Optional<Venda> vendaOpt = vendaRepository.findById(id);
        if (vendaOpt.isPresent()) {
            Venda venda = vendaOpt.get();
            venda.setTitulo(dto.getTitulo());
            venda.setDescricao(dto.getDescricao());
            venda.setImagem(dto.getImagem());
            venda.setValor(dto.getValor());
            venda.setTelefone(dto.getTelefone());
            venda.setCpf(dto.getCpf());
            venda.setZona(dto.getZona());
            venda.setDataHoraCriacao(dto.getDataHoraCriacao());
            vendaRepository.save(venda);
            return Optional.of(new VendaDTO(venda.getId(), venda.getTitulo(), venda.getDescricao(), venda.getImagem(), venda.getValor(), venda.getTelefone(), venda.getCpf(), venda.getZona(), venda.getDataHoraCriacao(), venda.getIdUsuario()));
        }
        return Optional.empty();
    }

    // Excluir anúncio
    public boolean excluirVenda(Long id) {
        if (vendaRepository.existsById(id)) {
        	vendaRepository.deleteById(id);
            return true;
        }
        return false;
    }
	
}
