package com.tcc.blogperiferico.services;

import com.tcc.blogperiferico.dto.DoacaoDTO;
import com.tcc.blogperiferico.entities.Doacao;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.DoacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoacaoService {

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    // Criar nova doação usando o usuário autenticado
    public DoacaoDTO criarDoacao(DoacaoDTO dto, MultipartFile file, Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Usuário logado não pode ser nulo");
        }

        Doacao doacao = new Doacao();
        doacao.setTitulo(dto.getTitulo());
        doacao.setDescricao(dto.getDescricao());
        doacao.setZona(dto.getZona());
        doacao.setTelefone(dto.getTelefone());

        if (file != null && !file.isEmpty()) {
            String urlImagem = azureBlobService.uploadFile(file);
            doacao.setImagem(urlImagem);
        }

        doacao.setDataHoraCriacao(LocalDateTime.now());
        doacao.setIdUsuario(usuarioLogado);

        doacao = doacaoRepository.save(doacao);
        return new DoacaoDTO(doacao);
    }

    // Atualizar doação usando o usuário autenticado
    public Optional<DoacaoDTO> atualizarDoacao(Long id, DoacaoDTO dto, MultipartFile file, Usuario usuarioLogado) {
        Optional<Doacao> doacaoOpt = doacaoRepository.findById(id);
        if (doacaoOpt.isPresent()) {
            Doacao doacao = doacaoOpt.get();

            if (!doacao.getIdUsuario().getId().equals(usuarioLogado.getId())) {
                throw new IllegalArgumentException("Você não tem permissão para editar esta doação.");
            }

            doacao.setTitulo(dto.getTitulo());
            doacao.setDescricao(dto.getDescricao());
            doacao.setZona(dto.getZona());
            doacao.setTelefone(dto.getTelefone());

            if (file != null && !file.isEmpty()) {
                String urlImagem = azureBlobService.uploadFile(file);
                doacao.setImagem(urlImagem);
            }

            doacao.setDataHoraCriacao(LocalDateTime.now());
            doacao = doacaoRepository.save(doacao);

            return Optional.of(new DoacaoDTO(doacao));
        }
        return Optional.empty();
    }

    // Listar todas as doações
    public List<DoacaoDTO> listarDoacoes() {
        return doacaoRepository.findAll().stream().map(DoacaoDTO::new).collect(Collectors.toList());
    }

    // Buscar por ID
    public Optional<DoacaoDTO> buscarPorId(Long id) {
        return doacaoRepository.findById(id).map(DoacaoDTO::new);
    }

    // Excluir doação (mantém estrutura simples, mas permite controle via controller)
    public boolean excluirDoacao(Long id) {
        if (doacaoRepository.existsById(id)) {
            doacaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
