package br.com.dbserver.desafio_votacao.dto;

public record ResultadoDTO(

        Long qtdVotosSim,
        Long qtdVotosNao,
        String resultadoApurado
) {
}
