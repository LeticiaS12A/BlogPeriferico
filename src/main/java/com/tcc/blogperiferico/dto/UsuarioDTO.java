package com.tcc.blogperiferico.dto;

import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.enums.UsuarioRole;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;
    
    private String biografia;

    @NotBlank(message = "Email é obrigatório.")
    @Email(message = "Email inválido.")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "E-mail inválido. Use um formato válido, ex: exemplo@email.com"
    )
    private String email;

    @NotBlank(message = "Senha é obrigatória.")
    private String senha;

    @Enumerated(EnumType.STRING)
    private UsuarioRole roles;

    private String fotoPerfil;

    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String nome, String biografia, String email, String senha, UsuarioRole roles) {
        this.id = id;
        this.nome = nome;
        this.biografia = biografia;
        this.email = email;
        this.senha = senha;
        this.roles = roles;
    }

    public UsuarioDTO(Usuario u) {
        id = u.getId();
        nome = u.getNome();
        biografia = u.getBiografia();
        email = u.getEmail();
        senha = u.getSenha();
        roles = u.getRoles();
        fotoPerfil = u.getFotoPerfil();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public UsuarioRole getRoles() { return roles; }
    public void setRoles(UsuarioRole roles) { this.roles = roles; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}
