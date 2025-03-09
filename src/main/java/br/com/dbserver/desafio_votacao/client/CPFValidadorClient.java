package br.com.dbserver.desafio_votacao.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CPFValidadorClient {

    private final WebClient.Builder webClientBuilder;
    @Value("${cpf.validator.url}")
    private String cpfValidatorUrl;

    public boolean isAbleToVote(String cpf) {
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");

        try {
            Map<String, String> response = webClientBuilder.build()
                    .get()
                    .uri(cpfValidatorUrl + "/" + cpfNumerico)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .block();

            return "ABLE_TO_VOTE".equals(response.get("status"));
        } catch (WebClientResponseException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPF inválido para votação.");
        }
        }
}
