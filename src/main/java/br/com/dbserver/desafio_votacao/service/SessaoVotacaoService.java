package br.com.dbserver.desafio_votacao.service;


import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;

import java.util.List;

public interface SessaoVotacaoService {

    public List<SessaoVotacaoDTO> listarTodas();
    public SessaoVotacaoDTO buscarPorId(Long id);
    public SessaoVotacaoDTO salvar(SessaoVotacaoDTO sessaoVotacaoDto);
    public SessaoVotacaoDTO atualizar(Long id, SessaoVotacaoDTO sessaoVotacaoDto);
    public void deletarPorId(Long id);
}
