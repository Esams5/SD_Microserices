package com.restaurante.auth.config;

import com.restaurante.auth.model.Usuario;
import com.restaurante.auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthDataInitializer {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner seedUsuarioBase(UsuarioRepository usuarioRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            if (usuarioRepository.existsByEmail("admin@restaurante.com")) {
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNome("Administrador");
            usuario.setEmail("admin@restaurante.com");
            usuario.setSenhaHash(encoder.encode("1234"));
            usuarioRepository.save(usuario);
        };
    }
}

