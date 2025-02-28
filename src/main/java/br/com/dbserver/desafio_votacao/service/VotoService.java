package br.com.dbserver.desafio_votacao.service;


import br.com.dbserver.desafio_votacao.dto.VotoDto;

import java.util.List;

public interface VotoService {

    public List<VotoDto> listarTodas();
    public VotoDto buscarPorId(Long id);
    public VotoDto salvar(VotoDto votoDto);
    public VotoDto atualizar(Long id, VotoDto votoDto);
    public void deletarPorId(Long id);
}
