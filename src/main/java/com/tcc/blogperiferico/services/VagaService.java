package com.tcc.blogperiferico.services;

import com.tcc.blogperiferico.dto.VagaDTO;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.entities.Vaga;
import com.tcc.blogperiferico.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    // Criar nova vaga usando o usuário autenticado
    public VagaDTO criarVaga(VagaDTO dto, MultipartFile file, Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Usuário logado não pode ser nulo");
        }

        Vaga vaga = new Vaga();
        vaga.setTitulo(dto.getTitulo());
        vaga.setDescricao(dto.getDescricao());
        vaga.setZona(dto.getZona());

        if (file != null && !file.isEmpty()) {
            String urlImagem = azureBlobService.uploadFile(file);
            vaga.setImagem(urlImagem);
        }

        vaga.setDataHoraCriacao(LocalDateTime.now());
        vaga.setIDUsuario(usuarioLogado);

        vaga = vagaRepository.save(vaga);
        return new VagaDTO(vaga);
    }

    // Atualizar vaga usando o usuário autenticado
    public Optional<VagaDTO> atualizarVaga(Long id, VagaDTO dto, MultipartFile file, Usuario usuarioLogado) {
        Optional<Vaga> vagaOpt = vagaRepository.findById(id);
        if (vagaOpt.isPresent()) {
            Vaga vaga = vagaOpt.get();

            // Verifica se o usuário logado é dono da vaga
            if (!vaga.getIDUsuario().getId().equals(usuarioLogado.getId())) {
                throw new IllegalArgumentException("Você não tem permissão para editar esta vaga.");
            }

            vaga.setTitulo(dto.getTitulo());
            vaga.setDescricao(dto.getDescricao());
            vaga.setZona(dto.getZona());

            if (file != null && !file.isEmpty()) {
                String urlImagem = azureBlobService.uploadFile(file);
                vaga.setImagem(urlImagem);
            }

            vaga.setDataHoraCriacao(LocalDateTime.now());
            vaga = vagaRepository.save(vaga);

            return Optional.of(new VagaDTO(vaga));
        }
        return Optional.empty();
    }

    // Listar todas as vagas
    public List<VagaDTO> listarVagas() {
        return vagaRepository.findAll().stream().map(VagaDTO::new).collect(Collectors.toList());
    }

    // Buscar por ID
    public Optional<VagaDTO> buscarPorId(Long id) {
        return vagaRepository.findById(id).map(VagaDTO::new);
    }

    // Excluir vaga
    public boolean excluirVaga(Long id) {
        if (vagaRepository.existsById(id)) {
            vagaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
