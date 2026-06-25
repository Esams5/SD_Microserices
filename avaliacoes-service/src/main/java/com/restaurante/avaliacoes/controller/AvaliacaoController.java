package com.restaurante.avaliacoes.controller;

import com.restaurante.avaliacoes.model.Avaliacao;
import com.restaurante.avaliacoes.repository.AvaliacaoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoController(AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @GetMapping("/{pratoId}")
    public List<Avaliacao> buscarPorPrato(@PathVariable Long pratoId) {
        return avaliacaoRepository.findByPratoId(pratoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avaliacao criar(@Valid @RequestBody Avaliacao avaliacao) {
        avaliacao.setId(null);
        return avaliacaoRepository.save(avaliacao);
    }
}
