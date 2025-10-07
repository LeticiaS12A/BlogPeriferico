package com.tcc.blogperiferico.services;

import com.tcc.blogperiferico.dto.NoticiaDTO;
import com.tcc.blogperiferico.entities.Noticia;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    // Criar notícia com usuário autenticado
    public NoticiaDTO criarNoticia(NoticiaDTO dto, MultipartFile file, Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Usuário logado não pode ser nulo");
        }

        Noticia noticia = new Noticia();
        noticia.setLocal(dto.getLocal());
        noticia.setZona(dto.getZona());
        noticia.setTitulo(dto.getTitulo());
        noticia.setTexto(dto.getTexto());

        if (file != null && !file.isEmpty()) {
            String urlImagem = azureBlobService.uploadFile(file);
            noticia.setImagem(urlImagem);
        }

        noticia.setDataHoraCriacao(LocalDateTime.now());
        noticia.setIdUsuario(usuarioLogado);

        noticia = noticiaRepository.save(noticia);
        return new NoticiaDTO(noticia);
    }

    // Atualizar notícia (só o dono pode editar, igual doação)
    public Optional<NoticiaDTO> atualizarNoticia(Long id, NoticiaDTO dto, MultipartFile file, Usuario usuarioLogado) {
        Optional<Noticia> noticiaOpt = noticiaRepository.findById(id);
        if (noticiaOpt.isPresent()) {
            Noticia noticia = noticiaOpt.get();

            // Valida se o usuário autenticado é o dono
            if (!noticia.getIdUsuario().getId().equals(usuarioLogado.getId())) {
                throw new IllegalArgumentException("Você não tem permissão para editar esta notícia.");
            }

            noticia.setLocal(dto.getLocal());
            noticia.setZona(dto.getZona());
            noticia.setTitulo(dto.getTitulo());
            noticia.setTexto(dto.getTexto());

            if (file != null && !file.isEmpty()) {
                String urlImagem = azureBlobService.uploadFile(file);
                noticia.setImagem(urlImagem);
            }

            noticia.setDataHoraCriacao(LocalDateTime.now());
            noticia = noticiaRepository.save(noticia);

            return Optional.of(new NoticiaDTO(noticia));
        }
        return Optional.empty();
    }

    // Listar todas as notícias
    public List<NoticiaDTO> listarNoticias() {
        return noticiaRepository.findAll().stream()
                .map(NoticiaDTO::new)
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public Optional<NoticiaDTO> buscarPorId(Long id) {
        return noticiaRepository.findById(id).map(NoticiaDTO::new);
    }

    // Excluir notícia
    public boolean excluirNoticia(Long id) {
        if (noticiaRepository.existsById(id)) {
            noticiaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
