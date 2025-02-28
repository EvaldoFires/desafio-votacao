package br.com.dbserver.desafio_votacao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SessaoVotacaoDto(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        @NotNull (message = "Id da pauta é obrigatório")
        Long idPauta,
        @NotNull (message = "Favor definir a data de abertura da sessão.")
        LocalDateTime aberturaSessao,
        LocalDateTime fechamentoSessao
) {
}
