package br.com.dbserver.desafio_votacao.dto;

import br.com.dbserver.desafio_votacao.model.Voto;
import br.com.dbserver.desafio_votacao.model.enums.EscolhaVoto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VotoDTO(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        @NotNull(message = "Favor definir a sessão.")
        Long idSessaoVotacao,
        @NotNull(message = "O voto é obrigatório")
        EscolhaVoto escolhaVoto,
        @NotBlank(message = "O CPF do associado é obrigatória")
        String cpfAssociado
) {
        public VotoDTO(Voto voto) {
                this(
                        voto.getId(),
                        voto.getSessaoVotacao().getId(),
                        voto.getEscolhaVoto(),
                        voto.getAssociado().getCpf()
                );
        }
}
