package br.com.dbserver.desafio_votacao.dto;

import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SessaoVotacaoDTO(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        @NotNull (message = "Id da pauta é obrigatório")
        Long idPauta,
        @NotNull (message = "Favor definir a data de abertura da sessão.")
        LocalDateTime aberturaSessao,
        LocalDateTime fechamentoSessao
) {
        public SessaoVotacaoDTO(SessaoVotacao sessao) {
                this(
                        sessao.getId(),
                        sessao.getPauta().getId(),
                        sessao.getAberturaSessao(),
                        sessao.getFechamentoSessao()
                );
        }
}
