package br.com.dbserver.desafio_votacao.service;


import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDto;

import java.util.List;

public interface SessaoVotacaoService {

    public List<SessaoVotacaoDto> listarTodas();
    public SessaoVotacaoDto buscarPorId(Long id);
    public SessaoVotacaoDto salvar(SessaoVotacaoDto sessaoVotacaoDto);
    public SessaoVotacaoDto atualizar(Long id, SessaoVotacaoDto sessaoVotacaoDto);
    public void deletarPorId(Long id);
}
