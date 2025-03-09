package br.com.dbserver.desafio_votacao.exception;

public class CPFUnableToVoteException extends RuntimeException {
    public CPFUnableToVoteException(String message) {
        super(message);
    }
}
