package br.com.dbserver.desafio_votacao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false)
    @JsonIgnore
    private Pauta pauta;

    private LocalDateTime aberturaSessao;
    private LocalDateTime fechamentoSessao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sessaoVotacao")
    @JsonIgnore
    private List<Voto> votos = new ArrayList<>();

}
