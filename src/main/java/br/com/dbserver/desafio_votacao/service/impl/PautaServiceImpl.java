package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.dto.PautaDTO;
import br.com.dbserver.desafio_votacao.dto.ResultadoDTO;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.mapper.PautaMapper;
import br.com.dbserver.desafio_votacao.model.Pauta;
import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import br.com.dbserver.desafio_votacao.model.enums.EscolhaVoto;
import br.com.dbserver.desafio_votacao.repository.PautaRepository;
import br.com.dbserver.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.dbserver.desafio_votacao.repository.VotoRepository;
import br.com.dbserver.desafio_votacao.service.PautaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;
    private final VotoRepository votoRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public PautaServiceImpl(PautaRepository pautaRepository, PautaMapper pautaMapper,
                            VotoRepository votoRepository, SessaoVotacaoRepository sessaoVotacaoRepository) {
        this.pautaRepository = pautaRepository;
        this.pautaMapper = pautaMapper;
        this.votoRepository = votoRepository;
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
    }

    @Override
    public List<PautaDTO> listarTodas() {
        return pautaRepository.findAll()
                .stream()
                .map(pautaMapper::toDto)
                .toList();
    }

    @Override
    public PautaDTO buscarPorId(Long id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada com Id: " + id)
                );
        return pautaMapper.toDto(pauta);
    }

    @Override
    public ResultadoDTO buscarResultaVotacaoPautaId(Long id) {
        SessaoVotacao sessaoVotacao = getSessaoVotacao(id);
        Long votosNao = getTotalVotos(sessaoVotacao, EscolhaVoto.NAO);
        Long votosSim = getTotalVotos(sessaoVotacao, EscolhaVoto.SIM);
        return new ResultadoDTO(votosSim, votosNao, getResultadoVotacao(votosSim, votosNao));
    }

    @Override
    public PautaDTO salvar(PautaDTO pautaDto) {
        Pauta pauta = pautaMapper.toEntity(pautaDto);
        pauta = pautaRepository.save(pauta);
        return pautaMapper.toDto(pauta);
    }

    @Override
    public PautaDTO atualizar(Long id, PautaDTO pautaDto) {
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

    private Long getTotalVotos(SessaoVotacao sessaoVotacao, EscolhaVoto escolhaVoto) {
        return votoRepository.countByEscolhaVotoAndSessaoVotacaoId(sessaoVotacao.getId(), escolhaVoto);
    }

    private SessaoVotacao getSessaoVotacao(Long id) {
        return sessaoVotacaoRepository.findByPautaId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException
                        ("Sessão de votação não encontrada para Pauta de Id: " + id)
                );
    }

    private String getResultadoVotacao(Long votosSim, Long votosNao) {
        return switch (Long.compare(votosSim, votosNao)) {
            case 0 -> "Empate";
            case 1 -> "Resultado SIM com " + votosSim + " votos";
            case -1 -> "Resultado NAO com " + votosNao + " votos";
            default -> votosSim == 0 && votosNao == 0 ? "Nenhum voto computado" : null;
        };
    }
}
