package com.restaurante.auth.controller;

import com.restaurante.auth.dto.AuthResponse;
import com.restaurante.auth.dto.AuthValidationResponse;
import com.restaurante.auth.dto.LoginRequest;
import com.restaurante.auth.dto.RegisterRequest;
import com.restaurante.auth.model.Sessao;
import com.restaurante.auth.model.Usuario;
import com.restaurante.auth.repository.SessaoRepository;
import com.restaurante.auth.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final SessaoRepository sessaoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(
            UsuarioRepository usuarioRepository,
            SessaoRepository sessaoRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoRepository = sessaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        Usuario salvo = usuarioRepository.save(usuario);

        return new AuthResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), null, "Cadastro realizado com sucesso");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
        }

        Sessao sessao = new Sessao();
        sessao.setToken(UUID.randomUUID().toString());
        sessao.setUsuario(usuario);
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        return new AuthResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                sessaoSalva.getToken(),
                "Login realizado com sucesso"
        );
    }

    @GetMapping("/validate")
    public AuthValidationResponse validate(@RequestHeader(name = "Authorization", required = false) String authorization) {
        String token = extractBearerToken(authorization);
        Sessao sessao = sessaoRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido"));

        Usuario usuario = sessao.getUsuario();
        return new AuthValidationResponse(true, usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token nao informado");
        }
        return authorization.substring(7);
    }
}
