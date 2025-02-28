package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.dto.PautaDto;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.mapper.PautaMapper;
import br.com.dbserver.desafio_votacao.model.Pauta;
import br.com.dbserver.desafio_votacao.repository.PautaRepository;
import br.com.dbserver.desafio_votacao.service.PautaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;

    public PautaServiceImpl(PautaRepository pautaRepository, PautaMapper pautaMapper) {
        this.pautaRepository = pautaRepository;
        this.pautaMapper = pautaMapper;
    }

    @Override
    public List<PautaDto> listarTodas() {
        return pautaRepository.findAll()
                .stream()
                .map(pautaMapper::toDto)
                .toList();
    }

    @Override
    public PautaDto buscarPorId(Long id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada com Id: " + id));
        return pautaMapper.toDto(pauta);
    }

    @Override
    public PautaDto salvar(PautaDto pautaDto) {
        Pauta pauta = pautaMapper.toEntity(pautaDto);
        pauta = pautaRepository.save(pauta);
        return pautaMapper.toDto(pauta);
    }

    @Override
    public PautaDto atualizar(Long id, PautaDto pautaDto) {
        Pauta pautaAtual = pautaMapper.toEntity(this.buscarPorId(id));
        pautaMapper.updateFromDto(pautaDto, pautaAtual);
        pautaAtual = pautaRepository.save(pautaAtual);
        return pautaMapper.toDto(pautaAtual);
    }

    @Override
    public void deletarPorId(Long id) {
        this.buscarPorId(id);
        pautaRepository.deleteById(id);
    }
}
