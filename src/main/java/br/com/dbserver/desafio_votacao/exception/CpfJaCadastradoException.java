package br.com.dbserver.desafio_votacao.exception;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String message) {
        super(message);
    }
}
