package com.restaurante.compras.controller;

import com.restaurante.compras.dto.AuthValidationResponse;
import com.restaurante.compras.dto.CompraRequest;
import com.restaurante.compras.model.Compra;
import com.restaurante.compras.repository.CompraRepository;
import com.restaurante.compras.service.AuthValidationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:4200")
public class CompraController {

    private final CompraRepository compraRepository;
    private final AuthValidationService authValidationService;

    public CompraController(CompraRepository compraRepository, AuthValidationService authValidationService) {
        this.compraRepository = compraRepository;
        this.authValidationService = authValidationService;
    }

    @GetMapping
    public List<Compra> listar() {
        return compraRepository.findAll();
    }

    @GetMapping("/minhas")
    public List<Compra> listarMinhas(
            @RequestHeader(name = "Authorization", required = false) String authorization
    ) {
        AuthValidationResponse usuario = authValidationService.validate(authorization);
        return compraRepository.findByUsuarioIdOrderByIdDesc(usuario.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Compra criar(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody CompraRequest request
    ) {
        AuthValidationResponse usuario = authValidationService.validate(authorization);
        Compra compra = new Compra();
        compra.setId(null);
        compra.setPratoId(request.pratoId());
        compra.setPratoNome(request.pratoNome());
        compra.setQuantidade(request.quantidade());
        compra.setPrecoUnitario(request.precoUnitario());
        compra.setUsuarioId(usuario.id());
        compra.setUsuarioNome(usuario.nome());
        compra.setValorTotal(compra.getPrecoUnitario().multiply(BigDecimal.valueOf(compra.getQuantidade())));
        return compraRepository.save(compra);
    }
}
