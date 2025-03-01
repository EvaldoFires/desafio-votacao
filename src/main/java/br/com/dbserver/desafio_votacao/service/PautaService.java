package br.com.dbserver.desafio_votacao.service;


import br.com.dbserver.desafio_votacao.dto.PautaDTO;
import br.com.dbserver.desafio_votacao.dto.ResultadoDTO;

import java.util.List;

public interface PautaService {

    public List<PautaDTO> listarTodas();
    public PautaDTO buscarPorId(Long id);
    public ResultadoDTO buscarResultaVotacaoPautaId(Long id);
    public PautaDTO salvar(PautaDTO pautaDto);
    public PautaDTO atualizar(Long id, PautaDTO pautaDto);
    public void deletarPorId(Long id);

}
