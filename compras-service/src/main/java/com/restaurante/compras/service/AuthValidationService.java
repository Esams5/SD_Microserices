package com.restaurante.compras.service;

import com.restaurante.compras.dto.AuthValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthValidationService {

    private final RestClient restClient;
    private final String authServiceUrl;

    public AuthValidationService(RestClient.Builder restClientBuilder, @Value("${auth.service.url}") String authServiceUrl) {
        this.restClient = restClientBuilder.build();
        this.authServiceUrl = authServiceUrl;
    }

    public AuthValidationResponse validate(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login obrigatorio para comprar");
        }

        try {
            AuthValidationResponse response = restClient.get()
                    .uri(authServiceUrl + "/validate")
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .retrieve()
                    .body(AuthValidationResponse.class);

            if (response == null || !response.valido()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido");
            }

            return response;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Servico de autenticacao indisponivel");
        }
    }
}

