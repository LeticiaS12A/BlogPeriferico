package com.tcc.blogperiferico.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.blogperiferico.dto.AuthRequest;
import com.tcc.blogperiferico.dto.AuthResponse;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.security.JWTUtil;
import com.tcc.blogperiferico.services.EmailService;
import com.tcc.blogperiferico.services.PasswordResetService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowCredentials = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @Autowired
    private EmailService emailService;

    @PostMapping("/login") // Define uma rota POST em /auth/login para autenticação de usuários
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        
        // Exibe no console os dados recebidos do front (somente para debug)
        // Em produção, nunca exiba a senha em logs
        System.out.println("AuthRequest recebido: email=" + request.getEmail() + ", senha=" + request.getSenha());
        
        try {
            // Tenta autenticar o usuário com o e-mail e senha informados
            // O AuthenticationManager verifica se as credenciais estão corretas
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            // Carrega os detalhes do usuário autenticado
            // Isso é necessário para gerar o token JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            System.out.println("UserDetails carregado: " + userDetails.getUsername());

            // Busca o usuário completo no banco de dados pelo e-mail
            // Caso não exista, lança exceção UsernameNotFoundException
            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + request.getEmail()));
            
            System.out.println("Usuario encontrado: " + usuario.getEmail() + ", role: " + usuario.getRoles());

            // Pega o nome da role (papel) do usuário — exemplo: ADMINISTRADOR, USUARIO, VISITANTE
            // O comentário indica que foi removido o prefixo "ROLE_"
            String role = usuario.getRoles().name(); 

            // Gera o token JWT com base nas informações do usuário e sua role
            String token = jwtUtil.generateToken(userDetails, role);
            System.out.println("Token gerado: " + token);

            // Retorna o token JWT dentro de um objeto AuthResponse com status HTTP 200 (OK)
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            // Caso a senha esteja incorreta, retorna erro 401 (não autorizado)
            System.out.println("Erro de autenticação: Credenciais inválidas - " + e.getMessage());
            return ResponseEntity.status(401).body("Credenciais inválidas: senha incorreta.");
            
        } catch (UsernameNotFoundException e) {
            // Caso o e-mail não exista no banco de dados, retorna erro 401 (não autorizado)
            System.out.println("Erro de autenticação: " + e.getMessage());
            return ResponseEntity.status(401).body("Credenciais inválidas: usuário não encontrado.");
            
        } catch (Exception e) {
            // Captura qualquer outro erro inesperado e retorna erro 401 com mensagem genérica
            System.out.println("Erro inesperado: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return ResponseEntity.status(401).body("Erro ao autenticar: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuario-logado")
    public ResponseEntity<?> usuarioLogado(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok("Você está autenticado como: " + usuario.getEmail() + " | Role: " + usuario.getRoles());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin-only")
    public ResponseEntity<?> adminOnly() {
        return ResponseEntity.ok("Você tem acesso ADMINISTRADOR.");
    }
    
    //Enviar token de redefinição
    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com esse email"));

        String codigo = passwordResetService.generateResetCode(email);

        emailService.enviarCodigoRedefinicao(email, codigo);

        return ResponseEntity.ok("Código enviado para o e-mail do usuário");
    }


    //Redefinir senha usando token
    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codigo = body.get("codigo");
        String novaSenha = body.get("novaSenha");

        if (email == null || codigo == null || novaSenha == null) {
            return ResponseEntity.badRequest().body("Email, código e nova senha são obrigatórios");
        }

        if (!passwordResetService.validateResetCode(email, codigo)) {
            return ResponseEntity.badRequest().body("Código inválido ou expirado");
        }

        var usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Senha redefinida com sucesso");
    }
}