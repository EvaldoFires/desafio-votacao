package br.com.dbserver.desafio_votacao.repository;

import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
}
