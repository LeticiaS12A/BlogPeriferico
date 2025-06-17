package com.tcc.blogperiferico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.blogperiferico.dto.VagaDTO;
import com.tcc.blogperiferico.entities.Vaga;
import com.tcc.blogperiferico.repository.VagaRepository;

@Service
public class VagaService {

	@Autowired
    private VagaRepository vagaRepository;

    // Criar novo anúncio
    public VagaDTO criarVaga(VagaDTO dto) {
    	Vaga vagas = new Vaga(dto.getId(), dto.getTitulo(), dto.getDescricao(), dto.getImagem(), dto.getZona(), dto.getDataHoraCriacao(), dto.getIdUsuario());
    	vagas = vagaRepository.save(vagas);
        return new VagaDTO(vagas.getId(), dto.getTitulo(), dto.getDescricao(), dto.getImagem(), dto.getZona(), dto.getDataHoraCriacao(), dto.getIdUsuario());
    }

    // Listar todos os anúncios
    public List<VagaDTO> listarVagas() {
        List<Vaga> vagas = vagaRepository.findAll();
        return vagas.stream()
                .map(v -> new VagaDTO(v.getId(), v.getTitulo(), v.getDescricao(), v.getImagem(), v.getZona(), v.getDataHoraCriacao(), v.getIDUsuario()))
                .collect(Collectors.toList());
    }

    // Buscar anúncio por ID
    public Optional<VagaDTO> buscarPorId(Long id) {
        Optional<Vaga> vaga = vagaRepository.findById(id);
        return vaga.map(v -> new VagaDTO(v.getId(), v.getTitulo(), v.getDescricao(), v.getImagem(), v.getZona(), v.getDataHoraCriacao(), v.getIDUsuario()));
    }

    // Atualizar anúncio
    public Optional<VagaDTO> atualizarVaga(Long id, VagaDTO dto) {
        Optional<Vaga> vagaOpt = vagaRepository.findById(id);
        if (vagaOpt.isPresent()) {
            Vaga vaga = vagaOpt.get();
            vaga.setTitulo(dto.getTitulo());
            vaga.setDescricao(dto.getDescricao());
            vaga.setImagem(dto.getImagem());
            vaga.setZona(dto.getZona());
            vaga.setDataHoraCriacao(dto.getDataHoraCriacao());
            vagaRepository.save(vaga);
            return Optional.of(new VagaDTO(vaga.getId(),  dto.getTitulo(), dto.getDescricao(), dto.getImagem(), dto.getZona(), dto.getDataHoraCriacao(), dto.getIdUsuario()));
        }
        return Optional.empty();
    }

    // Excluir anúncio
    public boolean excluirVaga(Long id) {
        if (vagaRepository.existsById(id)) {
        	vagaRepository.deleteById(id);
            return true;
        }
        return false;
    }
	
}
