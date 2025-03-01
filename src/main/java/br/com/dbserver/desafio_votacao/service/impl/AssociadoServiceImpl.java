package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDTO;
import br.com.dbserver.desafio_votacao.exception.CpfJaCadastradoException;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.mapper.AssociadoMapper;
import br.com.dbserver.desafio_votacao.model.Associado;
import br.com.dbserver.desafio_votacao.repository.AssociadoRepository;
import br.com.dbserver.desafio_votacao.service.AssociadoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository associadoRepository;
    private final AssociadoMapper associadoMapper;

    public AssociadoServiceImpl (AssociadoRepository associadoRepository, AssociadoMapper associadoMapper){
        this.associadoRepository = associadoRepository;
        this.associadoMapper = associadoMapper;
    }

    @Override
    public List<AssociadoDTO> listarTodas() {
        return associadoRepository.findAll()
                .stream()
                .map(associadoMapper::toDto)
                .toList();
    }

    @Override
    public AssociadoDTO buscarPorId(String cpf) {
        Associado associado = associadoRepository.findById(cpf)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Associado não encontrado com CPF: " + cpf));
        return associadoMapper.toDto(associado);
    }

    @Override
    public AssociadoDTO salvar(AssociadoDTO associadoDto) {
        associadoRepository.findById(associadoDto.cpf()).ifPresent(associado -> {
            throw new CpfJaCadastradoException("Associado já cadastrado com CPF: " + associado.getCpf());
        });
        Associado associado = associadoMapper.toEntity(associadoDto);
        associado = associadoRepository.save(associado);
        return associadoMapper.toDto(associado);
    }

    @Override
    public AssociadoDTO atualizar(String cpf, AtualizarAssociadoDTO atualizarAssociadoDto) {
        Associado associado = associadoMapper.toEntity(this.buscarPorId(cpf));
        associadoMapper.updateFromDto(atualizarAssociadoDto, associado);
        associado = associadoRepository.save(associado);
        return associadoMapper.toDto(associado);
    }

    @Override
    public void deletarPorId(String cpf) {
        this.buscarPorId(cpf);
        associadoRepository.deleteById(cpf);
    }

}
