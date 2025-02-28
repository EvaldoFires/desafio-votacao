package br.com.dbserver.desafio_votacao.service;


import br.com.dbserver.desafio_votacao.dto.PautaDto;

import java.util.List;

public interface PautaService {

    public List<PautaDto> listarTodas();
    public PautaDto buscarPorId(Long id);
    public PautaDto salvar(PautaDto pautaDto);
    public PautaDto atualizar(Long id, PautaDto pautaDto);
    public void deletarPorId(Long id);

}
