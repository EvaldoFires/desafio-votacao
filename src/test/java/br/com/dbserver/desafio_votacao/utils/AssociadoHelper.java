package br.com.dbserver.desafio_votacao.utils;

import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDTO;
import br.com.dbserver.desafio_votacao.model.Associado;

public class AssociadoHelper {

    public static Associado gerarAssociado1(){
        return Associado.builder()
                .cpf("112.223.334-44")
                .nome("Associado Teste 1")
                .build();
    }
    public static Associado gerarAssociado2(){
        return Associado.builder()
                .cpf("223.334.445-55")
                .nome("Associado Teste 2")
                .build();
    }

    public static AssociadoDTO gerarAssociadoDTO(Associado associado){
        return new AssociadoDTO(associado.getCpf(), associado.getNome());
    }

    public static AtualizarAssociadoDTO gerarAtualizarAssociadoDTO(Associado associado){
        return new AtualizarAssociadoDTO(associado.getNome());
    }
}
