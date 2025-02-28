package br.com.dbserver.desafio_votacao.repository;

import br.com.dbserver.desafio_votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    Optional<Voto> findByAssociado_CpfAndSessaoVotacao_Id(String cpfAssociado, Long idSessaoVotacao);
}
