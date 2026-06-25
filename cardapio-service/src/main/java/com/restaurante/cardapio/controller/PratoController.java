package com.restaurante.cardapio.controller;

import com.restaurante.cardapio.model.Prato;
import com.restaurante.cardapio.repository.PratoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/pratos")
@CrossOrigin(origins = "http://localhost:4200")
public class PratoController {

    private final PratoRepository pratoRepository;

    public PratoController(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    @GetMapping
    public List<Prato> listar() {
        return pratoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prato criar(@Valid @RequestBody Prato prato) {
        prato.setId(null);
        return pratoRepository.save(prato);
    }

    @PutMapping("/{id}")
    public Prato atualizar(@PathVariable Long id, @Valid @RequestBody Prato pratoAtualizado) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato nao encontrado"));

        prato.setNome(pratoAtualizado.getNome());
        prato.setDescricao(pratoAtualizado.getDescricao());
        prato.setPreco(pratoAtualizado.getPreco());

        return pratoRepository.save(prato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!pratoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato nao encontrado");
        }
        pratoRepository.deleteById(id);
    }
}

