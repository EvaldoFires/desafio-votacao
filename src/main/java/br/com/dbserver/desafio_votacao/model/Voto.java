package br.com.dbserver.desafio_votacao.model;

import br.com.dbserver.desafio_votacao.model.enums.EscolhaVoto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessao_votacao_id")
    private SessaoVotacao sessaoVotacao;
    private EscolhaVoto escolhaVoto;

    @ManyToOne
    @JoinColumn(name = "associado_cpf")
    private Associado associado;
}
