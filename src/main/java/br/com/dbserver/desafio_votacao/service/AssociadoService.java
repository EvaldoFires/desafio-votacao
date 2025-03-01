package br.com.dbserver.desafio_votacao.service;

import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDTO;

import java.util.List;

public interface AssociadoService {

    public List<AssociadoDTO> listarTodas();
    public AssociadoDTO buscarPorId(String cpf);
    public AssociadoDTO salvar(AssociadoDTO associadoDto);
    public AssociadoDTO atualizar(String cpf, AtualizarAssociadoDTO associadoDto);
    public void deletarPorId(String cpf);
}
