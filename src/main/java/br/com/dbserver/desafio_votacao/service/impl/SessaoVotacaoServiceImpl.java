package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.exception.SessaoVotacaoDaPautaJaExisteException;
import br.com.dbserver.desafio_votacao.mapper.SessaoVotacaoMapper;
import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import br.com.dbserver.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.dbserver.desafio_votacao.service.PautaService;
import br.com.dbserver.desafio_votacao.service.SessaoVotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessaoVotacaoServiceImpl implements SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final SessaoVotacaoMapper sessaoVotacaoMapper;
    private final PautaService pautaService;

    @Override
    public List<SessaoVotacaoDTO> listarTodas() {
        return sessaoVotacaoRepository.findAll()
                .stream()
                .map(sessaoVotacaoMapper::toDto)
                .toList();
    }

    @Override
    public SessaoVotacaoDTO buscarPorId(Long id) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão de Votação não encontrada com Id: " + id));
        return sessaoVotacaoMapper.toDto(sessaoVotacao);
    }

    @Override
    public SessaoVotacaoDTO salvar(SessaoVotacaoDTO sessaoVotacaoDto) {
        this.validarUnicaSessaoVotacaoPauta(sessaoVotacaoDto);
        pautaService.buscarPorId(sessaoVotacaoDto.idPauta());
        SessaoVotacao sessaoVotacao = sessaoVotacaoMapper.toEntity(sessaoVotacaoDto);
        sessaoVotacao.setFechamentoSessao(this.verificarFechamentoSessao(sessaoVotacaoDto));
        sessaoVotacao = sessaoVotacaoRepository.save(sessaoVotacao);
        return sessaoVotacaoMapper.toDto(sessaoVotacao);
    }

    @Override
    public SessaoVotacaoDTO atualizar(Long id, SessaoVotacaoDTO sessaoVotacaoDto) {
        SessaoVotacao sessaoVotacao = sessaoVotacaoMapper.toEntity(this.buscarPorId(id));
        pautaService.buscarPorId(sessaoVotacaoDto.idPauta());
        sessaoVotacaoMapper.updateFromDto(sessaoVotacaoDto, sessaoVotacao);
        sessaoVotacao.setFechamentoSessao(this.verificarFechamentoSessao(sessaoVotacaoDto));
        sessaoVotacao = sessaoVotacaoRepository.save(sessaoVotacao);
        return sessaoVotacaoMapper.toDto(sessaoVotacao);
    }

    @Override
    public void deletarPorId(Long id) {
        this.buscarPorId(id);
        sessaoVotacaoRepository.deleteById(id);
    }

    private LocalDateTime verificarFechamentoSessao (SessaoVotacaoDTO sessaoVotacaoDto){
        if (sessaoVotacaoDto.fechamentoSessao() == null){
            return sessaoVotacaoDto.aberturaSessao().plusMinutes(1);
        } else {
            return sessaoVotacaoDto.fechamentoSessao();
        }
    }
    private void validarUnicaSessaoVotacaoPauta(SessaoVotacaoDTO sessaoVotacaoDto){
        sessaoVotacaoRepository.findByPautaId(sessaoVotacaoDto.idPauta()).ifPresent(sessaoVotacao -> {
            throw new SessaoVotacaoDaPautaJaExisteException("Já existe uma sessão de votação cadastrada para a pauta de " +
                    "id: " + sessaoVotacao.getPauta().getId());
        });
    }
}
