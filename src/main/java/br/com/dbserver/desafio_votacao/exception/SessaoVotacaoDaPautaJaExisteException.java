package br.com.dbserver.desafio_votacao.exception;

public class SessaoVotacaoDaPautaJaExisteException extends RuntimeException {
    public SessaoVotacaoDaPautaJaExisteException(String message) {
        super(message);
    }
}
