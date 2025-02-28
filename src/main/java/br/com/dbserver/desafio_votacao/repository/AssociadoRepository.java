package br.com.dbserver.desafio_votacao.repository;

import br.com.dbserver.desafio_votacao.model.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado,String> {
}
