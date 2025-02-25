package com.tcc.blogPeriferico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.blogPeriferico.dto.ComercioDTO;
import com.tcc.blogPeriferico.entities.Comercio;
import com.tcc.blogPeriferico.repository.ComercioRepository;

@Service
public class ComercioService {

	@Autowired
    private ComercioRepository comercioRepository;

    // Criar novo anúncio
    public ComercioDTO criarComercio(ComercioDTO dto) {
    	Comercio comercio = new Comercio(dto.getId(), dto.getLocal(), dto.getTexto(), dto.getImagem());
    	comercio = comercioRepository.save(comercio);
        return new ComercioDTO(dto.getId(), comercio.getLocal(), comercio.getTexto(), comercio.getImagem());
    }

    // Listar todos os anúncios
    public List<ComercioDTO> listarComercios() {
        List<Comercio> comercios = comercioRepository.findAll();
        return comercios.stream()
                .map(a -> new ComercioDTO(a.getId(), a.getLocal(), a.getTexto(), a.getImagem()))
                .collect(Collectors.toList());
    }

    // Buscar anúncio por ID
    public Optional<ComercioDTO> buscarPorId(Long id) {
        Optional<Comercio> comercio = comercioRepository.findById(id);
        return comercio.map(a -> new ComercioDTO(a.getId(), a.getLocal(), a.getTexto(), a.getImagem()));
    }

    // Atualizar anúncio
    public Optional<ComercioDTO> atualizarComercio(Long id, ComercioDTO dto) {
        Optional<Comercio> comercioOpt = comercioRepository.findById(id);
        if (comercioOpt.isPresent()) {
            Comercio comercio = comercioOpt.get();
            comercio.setLocal(dto.getLocal());
            comercio.setTexto(dto.getTexto());
            comercio.setImagem(dto.getImagem());
            comercioRepository.save(comercio);
            return Optional.of(new ComercioDTO(comercio.getId(), comercio.getLocal(), comercio.getTexto(), comercio.getImagem()));
        }
        return Optional.empty();
    }

    // Excluir anúncio
    public boolean excluirComercio(Long id) {
        if (comercioRepository.existsById(id)) {
        	comercioRepository.deleteById(id);
            return true;
        }
        return false;
    }
	
}
