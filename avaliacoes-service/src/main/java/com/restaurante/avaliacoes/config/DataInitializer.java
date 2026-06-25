package com.restaurante.avaliacoes.config;

import com.restaurante.avaliacoes.model.Avaliacao;
import com.restaurante.avaliacoes.repository.AvaliacaoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedAvaliacoes(AvaliacaoRepository avaliacaoRepository) {
        return args -> {
            if (avaliacaoRepository.count() > 0) {
                return;
            }

            avaliacaoRepository.save(buildAvaliacao(1L, 5, "Marina", "Excelente prato."));
            avaliacaoRepository.save(buildAvaliacao(1L, 4, "Carlos", "Muito saboroso."));
            avaliacaoRepository.save(buildAvaliacao(2L, 5, "Ana", "Camarao no ponto certo."));
            avaliacaoRepository.save(buildAvaliacao(2L, 4, "Joao", "Bem servido."));
        };
    }

    private Avaliacao buildAvaliacao(Long pratoId, int nota, String autor, String comentario) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setPratoId(pratoId);
        avaliacao.setNota(nota);
        avaliacao.setAutor(autor);
        avaliacao.setComentario(comentario);
        return avaliacao;
    }
}

