package br.com.dbserver.desafio_votacao.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizarAssociadoDto(

        @NotBlank(message = "Nome é obrigatório")
        String nome
) {
}
