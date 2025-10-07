package com.tcc.blogperiferico.repository;

import com.tcc.blogperiferico.entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByNoticia_Id(Long noticiaId);

    List<Comentario> findByVenda_Id(Long vendaId);

    List<Comentario> findByVaga_Id(Long vagaId);

    List<Comentario> findByDoacao_Id(Long doacaoId);
}
