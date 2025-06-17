package com.tcc.blogperiferico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.blogperiferico.dto.DoacaoDTO;
import com.tcc.blogperiferico.entities.Doacao;
import com.tcc.blogperiferico.repository.DoacaoRepository;

@Service
public class DoacaoService {

	@Autowired
    private DoacaoRepository doacaoRepository;
	
    // Criar nova Doacao
    public DoacaoDTO criarDoacao(DoacaoDTO dto) {
    	Doacao doacaos = new Doacao(dto.getId(), dto.getTitulo(), dto.getDescricao(), dto.getImagem(), dto.getZona(), dto.getTelefone(), dto.getDataHoraCriacao(), dto.getIdUsuario());
    	doacaos = doacaoRepository.save(doacaos);
        return new DoacaoDTO(doacaos.getId(), doacaos.getTitulo(), doacaos.getDescricao(), doacaos.getImagem(), doacaos.getZona(), doacaos.getTelefone(), doacaos.getDataHoraCriacao(), doacaos.getIdUsuario());
    }

    // Listar todos os anúncios
    public List<DoacaoDTO> listarDoacoes() {
        List<Doacao> doacoes = doacaoRepository.findAll();
        return doacoes.stream()
                .map(d -> new DoacaoDTO(d.getId(), d.getTitulo(), d.getDescricao(), d.getImagem(), d.getZona(), d.getTelefone(), d.getDataHoraCriacao(), d.getIdUsuario()))
                .collect(Collectors.toList());
    }

    // Buscar anúncio por ID
    public Optional<DoacaoDTO> buscarPorId(Long id) {
        Optional<Doacao> doacao = doacaoRepository.findById(id);
        return doacao.map(d -> new DoacaoDTO(d.getId(), d.getTitulo(), d.getDescricao(), d.getImagem(), d.getZona(), d.getTelefone(), d.getDataHoraCriacao(), d.getIdUsuario()));
    }

    // Atualizar anúncio
    public Optional<DoacaoDTO> atualizarDoacao(Long id, DoacaoDTO dto) {
        Optional<Doacao> doacaoOpt = doacaoRepository.findById(id);
        if (doacaoOpt.isPresent()) {
        	Doacao doacao = doacaoOpt.get();
        	doacao.setTitulo(dto.getTitulo());
        	doacao.setDescricao(dto.getDescricao());
        	doacao.setImagem(dto.getImagem());
        	doacao.setZona(dto.getZona());
        	doacao.setTelefone(dto.getTelefone());
        	doacao.setDataHoraCriacao(dto.getDataHoraCriacao());
        	doacaoRepository.save(doacao);
            return Optional.of(new DoacaoDTO(doacao.getId(), doacao.getTitulo(), doacao.getDescricao(), doacao.getImagem(), doacao.getZona(), doacao.getTelefone(), doacao.getDataHoraCriacao(), doacao.getIdUsuario()));
        }
        return Optional.empty();
    }

    // Excluir anúncio
    public boolean excluirDoacao(Long id) {
        if (doacaoRepository.existsById(id)) {
        	doacaoRepository.deleteById(id);
            return true;
        }
        return false;
    }	
	
}
