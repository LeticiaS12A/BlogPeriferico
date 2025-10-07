package com.tcc.blogperiferico.services;

import com.tcc.blogperiferico.dto.VendaDTO;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.entities.Venda;
import com.tcc.blogperiferico.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    // Criar nova venda usando o usuário logado
    public VendaDTO criarVenda(VendaDTO dto, MultipartFile file, Usuario usuarioLogado) {
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Usuário logado não pode ser nulo");
        }

        Venda venda = new Venda();
        venda.setTitulo(dto.getTitulo());
        venda.setDescricao(dto.getDescricao());
        venda.setValor(dto.getValor());
        venda.setTelefone(dto.getTelefone());
        venda.setCpf(dto.getCpf());
        venda.setZona(dto.getZona());

        if (file != null && !file.isEmpty()) {
            String urlImagem = azureBlobService.uploadFile(file);
            venda.setImagem(urlImagem);
        }

        venda.setDataHoraCriacao(LocalDateTime.now());
        venda.setIdUsuario(usuarioLogado);

        venda = vendaRepository.save(venda);
        return new VendaDTO(venda);
    }

    // Atualizar venda usando o usuário logado
    public Optional<VendaDTO> atualizarVenda(Long id, VendaDTO dto, MultipartFile file, Usuario usuarioLogado) {
        Optional<Venda> vendaOpt = vendaRepository.findById(id);
        if (vendaOpt.isPresent()) {
            Venda venda = vendaOpt.get();

            if (!venda.getIdUsuario().getId().equals(usuarioLogado.getId())) {
                throw new IllegalArgumentException("Você não tem permissão para editar esta venda.");
            }

            venda.setTitulo(dto.getTitulo());
            venda.setDescricao(dto.getDescricao());
            venda.setValor(dto.getValor());
            venda.setTelefone(dto.getTelefone());
            venda.setCpf(dto.getCpf());
            venda.setZona(dto.getZona());

            if (file != null && !file.isEmpty()) {
                String urlImagem = azureBlobService.uploadFile(file);
                venda.setImagem(urlImagem);
            }

            venda.setDataHoraCriacao(LocalDateTime.now());
            venda = vendaRepository.save(venda);

            return Optional.of(new VendaDTO(venda));
        }
        return Optional.empty();
    }

    // Excluir venda usando o usuário logado
    public boolean excluirVenda(Long id, Usuario usuarioLogado) {
        Optional<Venda> vendaOpt = vendaRepository.findById(id);
        if (vendaOpt.isPresent()) {
            Venda venda = vendaOpt.get();

            if (!venda.getIdUsuario().getId().equals(usuarioLogado.getId())) {
                throw new IllegalArgumentException("Você não tem permissão para excluir esta venda.");
            }

            vendaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Listar todas as vendas
    public List<VendaDTO> listarVendas() {
        return vendaRepository.findAll().stream()
                .map(VendaDTO::new)
                .collect(Collectors.toList());
    }

    // Buscar venda por ID
    public Optional<VendaDTO> buscarPorId(Long id) {
        return vendaRepository.findById(id).map(VendaDTO::new);
    }
}
