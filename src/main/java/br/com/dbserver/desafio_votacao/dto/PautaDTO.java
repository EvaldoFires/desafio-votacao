package br.com.dbserver.desafio_votacao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record PautaDTO(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank(message = "O título é obrigatorio")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime dataCriacao
) {
}
