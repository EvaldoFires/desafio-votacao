package br.com.dbserver.desafio_votacao.service;


import br.com.dbserver.desafio_votacao.dto.VotoDTO;

import java.util.List;

public interface VotoService {

    public List<VotoDTO> listarTodas();
    public VotoDTO buscarPorId(Long id);
    public VotoDTO salvar(VotoDTO votoDto);
    public VotoDTO atualizar(Long id, VotoDTO votoDto);
    public void deletarPorId(Long id);
}
