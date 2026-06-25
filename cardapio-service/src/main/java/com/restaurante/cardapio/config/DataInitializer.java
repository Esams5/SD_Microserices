package com.restaurante.cardapio.config;

import com.restaurante.cardapio.model.Prato;
import com.restaurante.cardapio.repository.PratoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedPratos(PratoRepository pratoRepository) {
        return args -> {
            if (pratoRepository.count() > 0) {
                return;
            }

            Prato prato1 = new Prato();
            prato1.setNome("Lasanha Bolonhesa");
            prato1.setDescricao("Lasanha artesanal com molho bolonhesa e queijo gratinado.");
            prato1.setPreco(new BigDecimal("42.90"));

            Prato prato2 = new Prato();
            prato2.setNome("Risoto de Camarao");
            prato2.setDescricao("Risoto cremoso com camarao salteado e raspas de limao.");
            prato2.setPreco(new BigDecimal("58.50"));

            pratoRepository.save(prato1);
            pratoRepository.save(prato2);
        };
    }
}

