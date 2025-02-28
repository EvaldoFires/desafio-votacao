package br.com.dbserver.desafio_votacao.service;

import br.com.dbserver.desafio_votacao.dto.AssociadoDto;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDto;

import java.util.List;

public interface AssociadoService {

    public List<AssociadoDto> listarTodas();
    public AssociadoDto buscarPorId(String cpf);
    public AssociadoDto salvar(AssociadoDto associadoDto);
    public AssociadoDto atualizar(String cpf, AtualizarAssociadoDto associadoDto);
    public void deletarPorId(String cpf);
}
