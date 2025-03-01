package br.com.dbserver.desafio_votacao.model.enums;

import lombok.Getter;

@Getter
public enum EscolhaVoto {
    SIM(0),
    NAO(1);

    private final long valor;

    EscolhaVoto(long valor) {
        this.valor = valor;
    }

    public static EscolhaVoto fromValue(long valor) {
        for (EscolhaVoto e : values()) {
            if (e.valor == valor) {
                return e;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + valor);
    }
}
