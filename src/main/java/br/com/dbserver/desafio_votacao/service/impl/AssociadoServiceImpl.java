package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.dto.AssociadoDto;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDto;
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
    public List<AssociadoDto> listarTodas() {
        return associadoRepository.findAll()
                .stream()
                .map(associadoMapper::toDto)
                .toList();
    }

    @Override
    public AssociadoDto buscarPorId(String cpf) {
        Associado associado = associadoRepository.findById(cpf)
                .orElseThrow(()-> new RecursoNaoEncontradoException("Associado não encontrado com CPF: " + cpf));
        return associadoMapper.toDto(associado);
    }

    @Override
    public AssociadoDto salvar(AssociadoDto associadoDto) {
        associadoRepository.findById(associadoDto.cpf()).ifPresent(associado -> {
            throw new CpfJaCadastradoException("Associado já cadastrado com CPF: " + associado.getCpf());
        });
        Associado associado = associadoMapper.toEntity(associadoDto);
        associado = associadoRepository.save(associado);
        return associadoMapper.toDto(associado);
    }

    @Override
    public AssociadoDto atualizar(String cpf, AtualizarAssociadoDto atualizarAssociadoDto) {
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
