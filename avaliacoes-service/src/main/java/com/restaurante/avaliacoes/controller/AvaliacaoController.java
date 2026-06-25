package com.restaurante.avaliacoes.controller;

import com.restaurante.avaliacoes.dto.AuthValidationResponse;
import com.restaurante.avaliacoes.model.Avaliacao;
import com.restaurante.avaliacoes.repository.AvaliacaoRepository;
import com.restaurante.avaliacoes.service.AuthValidationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private final AuthValidationService authValidationService;

    public AvaliacaoController(AvaliacaoRepository avaliacaoRepository, AuthValidationService authValidationService) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.authValidationService = authValidationService;
    }

    @GetMapping("/{pratoId}")
    public List<Avaliacao> buscarPorPrato(@PathVariable Long pratoId) {
        return avaliacaoRepository.findByPratoId(pratoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avaliacao criar(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody Avaliacao avaliacao
    ) {
        AuthValidationResponse usuario = authValidationService.validate(authorization);
        avaliacao.setId(null);
        avaliacao.setAutor(usuario.nome());
        return avaliacaoRepository.save(avaliacao);
    }
}
