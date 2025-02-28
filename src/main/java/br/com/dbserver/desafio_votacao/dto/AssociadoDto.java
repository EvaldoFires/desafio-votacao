package br.com.dbserver.desafio_votacao.dto;

import jakarta.validation.constraints.NotBlank;

public record AssociadoDto(

        @NotBlank(message = "CPF é obrigatório")
        String cpf,
        @NotBlank(message = "Nome é obrigatório")
        String nome
) {
}
