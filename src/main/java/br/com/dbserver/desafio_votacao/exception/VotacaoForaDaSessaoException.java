package br.com.dbserver.desafio_votacao.exception;

public class VotacaoForaDaSessaoException extends RuntimeException {
    public VotacaoForaDaSessaoException(String message) {
        super(message);
    }
}
