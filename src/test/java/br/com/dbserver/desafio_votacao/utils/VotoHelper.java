package br.com.dbserver.desafio_votacao.utils;

import br.com.dbserver.desafio_votacao.dto.VotoDTO;
import br.com.dbserver.desafio_votacao.model.Voto;
import br.com.dbserver.desafio_votacao.model.enums.EscolhaVoto;

import java.time.LocalDateTime;

import static br.com.dbserver.desafio_votacao.utils.AssociadoHelper.gerarAssociado1;
import static br.com.dbserver.desafio_votacao.utils.AssociadoHelper.gerarAssociado2;
import static br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper.gerarSessaoVotacao1;
import static br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper.gerarSessaoVotacao2;

public class VotoHelper {

    public static Voto gerarVoto1(){
        return Voto.builder()
                .id(1L)
                .sessaoVotacao(gerarSessaoVotacao1())
                .escolhaVoto(EscolhaVoto.SIM)
                .associado(gerarAssociado1())
                .build();
    }
    public static Voto gerarVoto2(){
        return Voto.builder()
                .id(2L)
                .sessaoVotacao(gerarSessaoVotacao2())
                .escolhaVoto(EscolhaVoto.SIM)
                .associado(gerarAssociado2())
                .build();
    }

    public static VotoDTO gerarVotoDTO(Voto voto){
        return new VotoDTO(voto.getId(),
                voto.getSessaoVotacao().getId(),
                voto.getEscolhaVoto(),
                voto.getAssociado().getCpf());
    }
}
