package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDto;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.mapper.SessaoVotacaoMapper;
import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import br.com.dbserver.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.dbserver.desafio_votacao.service.PautaService;
import br.com.dbserver.desafio_votacao.service.SessaoVotacaoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessaoVotacaoServiceImpl implements SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final SessaoVotacaoMapper sessaoVotacaoMapper;
    private final PautaService pautaService;

    public SessaoVotacaoServiceImpl (SessaoVotacaoRepository sessaoVotacaoRepository,
                                     SessaoVotacaoMapper sessaoVotacaoMapper,
                                     PautaService pautaService){
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.sessaoVotacaoMapper = sessaoVotacaoMapper;
        this.pautaService = pautaService;
    }

    @Override
    public List<SessaoVotacaoDto> listarTodas() {
        return sessaoVotacaoRepository.findAll()
                .stream()
                .map(sessaoVotacaoMapper::toDto)
                .toList();
    }

    @Override
    public SessaoVotacaoDto buscarPorId(Long id) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão de Votação não encontrada com Id: " + id));
        return sessaoVotacaoMapper.toDto(sessaoVotacao);
    }

    @Override
    public SessaoVotacaoDto salvar(SessaoVotacaoDto sessaoVotacaoDto) {

        pautaService.buscarPorId(sessaoVotacaoDto.idPauta());
        SessaoVotacao sessaoVotacao = sessaoVotacaoMapper.toEntity(sessaoVotacaoDto);
        sessaoVotacao = sessaoVotacaoRepository.save(sessaoVotacao);
        return sessaoVotacaoMapper.toDto(sessaoVotacao);
    }

    @Override
    public SessaoVotacaoDto atualizar(Long id, SessaoVotacaoDto sessaoVotacaoDto) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoMapper.toEntity(this.buscarPorId(id));
        pautaService.buscarPorId(sessaoVotacaoDto.idPauta());
        sessaoVotacaoMapper.updateFromDto(sessaoVotacaoDto, sessaoVotacao);
        sessaoVotacao = sessaoVotacaoRepository.save(sessaoVotacao);
        return sessaoVotacaoMapper.toDto(sessaoVotacao);
    }

    @Override
    public void deletarPorId(Long id) {
        this.buscarPorId(id);
        sessaoVotacaoRepository.deleteById(id);
    }
}
