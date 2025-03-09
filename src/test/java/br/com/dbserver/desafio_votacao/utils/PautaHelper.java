package br.com.dbserver.desafio_votacao.utils;

import br.com.dbserver.desafio_votacao.dto.PautaDTO;
import br.com.dbserver.desafio_votacao.model.Pauta;

import java.time.LocalDateTime;

public class PautaHelper {

    public static Pauta gerarPauta1(){
        return Pauta.builder()
                .id(1L)
                .titulo("Pauta Teste 1")
                .descricao("Pauta Teste 1")
                .dataCriacao(LocalDateTime.now())
                .build();
    }
    public static Pauta gerarPauta2(){
        return Pauta.builder()
                .id(2L)
                .titulo("Pauta Teste 2")
                .descricao("Pauta Teste 2")
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    public static PautaDTO gerarPautaDTO(Pauta pauta){
        return new PautaDTO(pauta.getId(), pauta.getTitulo(), pauta.getDescricao(), pauta.getDataCriacao());
    }
}
