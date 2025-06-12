package com.tcc.blogperiferico.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.blogperiferico.dto.AuthRequest;
import com.tcc.blogperiferico.dto.AuthResponse;
import com.tcc.blogperiferico.entities.Usuario;
import com.tcc.blogperiferico.repository.UsuarioRepository;
import com.tcc.blogperiferico.security.JWTUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtUtil.generateToken(userDetails, usuario.getRoles().name());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciais inválidas.");
        }
    }
}
