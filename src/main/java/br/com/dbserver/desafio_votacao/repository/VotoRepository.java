package br.com.dbserver.desafio_votacao.repository;

import br.com.dbserver.desafio_votacao.model.Voto;
import br.com.dbserver.desafio_votacao.model.enums.EscolhaVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    Optional<Voto> findByAssociado_CpfAndSessaoVotacao_Id(String cpfAssociado, Long idSessaoVotacao);

    @Query("SELECT COUNT(v) FROM Voto v " +
            "WHERE v.sessaoVotacao.id = :sessaoId " +
            "AND v.escolhaVoto = :escolhaVoto")
    Long countByEscolhaVotoAndSessaoVotacaoId(@Param("sessaoId") Long sessaoId,
                                              @Param("escolhaVoto") EscolhaVoto escolhaVoto);

}
