package br.com.dbserver.desafio_votacao.utils;

import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.dbserver.desafio_votacao.model.SessaoVotacao;

import java.time.LocalDateTime;

import static br.com.dbserver.desafio_votacao.utils.PautaHelper.gerarPauta1;
import static br.com.dbserver.desafio_votacao.utils.PautaHelper.gerarPauta2;

public class SessaoVotacaoHelper {

    public static SessaoVotacao gerarSessaoVotacao1(){
        return SessaoVotacao.builder()
                .id(1L)
                .aberturaSessao(LocalDateTime.now())
                .fechamentoSessao(LocalDateTime.now().plusMonths(1L))
                .pauta(gerarPauta1())
                .build();
    }
    public static SessaoVotacao gerarSessaoVotacao2(){
        return SessaoVotacao.builder()
                .id(2L)
                .id(1L)
                .aberturaSessao(LocalDateTime.now())
                .fechamentoSessao(LocalDateTime.now().plusMonths(1L))
                .pauta(gerarPauta2())
                .build();
    }

    public static SessaoVotacaoDTO gerarSessaoVotacaoDTO(SessaoVotacao sessaoVotacao){
        return new SessaoVotacaoDTO(sessaoVotacao.getId(),
                sessaoVotacao.getPauta().getId(),
                sessaoVotacao.getAberturaSessao(),
                sessaoVotacao.getFechamentoSessao());
    }
}
