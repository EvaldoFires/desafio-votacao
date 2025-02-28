package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.dto.AssociadoDto;
import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDto;
import br.com.dbserver.desafio_votacao.dto.VotoDto;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.exception.VotacaoForaDaSessaoException;
import br.com.dbserver.desafio_votacao.exception.VotacaoJaFeitaComEsseCpfException;
import br.com.dbserver.desafio_votacao.mapper.VotoMapper;
import br.com.dbserver.desafio_votacao.model.Associado;
import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import br.com.dbserver.desafio_votacao.model.Voto;
import br.com.dbserver.desafio_votacao.repository.VotoRepository;
import br.com.dbserver.desafio_votacao.service.AssociadoService;
import br.com.dbserver.desafio_votacao.service.SessaoVotacaoService;
import br.com.dbserver.desafio_votacao.service.VotoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepository;
    private final VotoMapper votoMapper;
    private final AssociadoService associadoService;
    private final SessaoVotacaoService sessaoVotacaoService;

    public VotoServiceImpl (VotoRepository votoRepository, VotoMapper votoMapper,
                            AssociadoService associadoService, SessaoVotacaoService sessaoVotacaoService){
        this.votoRepository = votoRepository;
        this.votoMapper = votoMapper;
        this.associadoService = associadoService;
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @Override
    public List<VotoDto> listarTodas() {
        return votoRepository.findAll()
                .stream()
                .map(votoMapper::toDto)
                .toList();
    }

    @Override
    public VotoDto buscarPorId(Long id) {
        Voto voto = votoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException ("\"Voto não encontrada com Id: \" + id)"));
        return votoMapper.toDto(voto);
    }

    @Override
    public VotoDto salvar(VotoDto votoDto) {

        Voto voto = votoMapper.toEntity(votoDto);
        this.validarVoto(votoDto);
        voto = votoRepository.save(voto);
        return votoMapper.toDto(voto);
    }

    @Override
    public VotoDto atualizar(Long id, VotoDto votoDto) {
        associadoService.buscarPorId(votoDto.cpfAssociado());
        sessaoVotacaoService.buscarPorId(votoDto.idSessaoVotacao());

        Voto voto = votoMapper.toEntity(this.buscarPorId(id));
        votoMapper.updateFromDto(votoDto, voto);
        voto = votoRepository.save(voto);
        return votoMapper.toDto(voto);
    }

    @Override
    public void deletarPorId(Long id) {
        this.buscarPorId(id);
        votoRepository.deleteById(id);
    }

    private void validarVoto (VotoDto votoDto){
        AssociadoDto associadoDto = associadoService.buscarPorId(votoDto.cpfAssociado());
        SessaoVotacaoDto sessaoVotacaoDto = sessaoVotacaoService.buscarPorId(votoDto.idSessaoVotacao());
        this.verificarVotoUnico(associadoDto, sessaoVotacaoDto);
        this.verificarHorarioDeVotacao(sessaoVotacaoDto);

    }
    private void verificarVotoUnico(AssociadoDto associadoDto, SessaoVotacaoDto sessaoVotacaoDto){
        votoRepository.findByAssociado_CpfAndSessaoVotacao_Id(associadoDto.cpf(), sessaoVotacaoDto.id())
                .ifPresent(v -> {
                    throw new VotacaoJaFeitaComEsseCpfException("Voto não permitido. Já foi feito uma votação para a" +
                            " sessão de id: " + v.getSessaoVotacao().getId() +
                            " pelo associado de cpf: " + v.getAssociado().getCpf());
                } );
    }
    private void verificarHorarioDeVotacao(SessaoVotacaoDto sessaoVotacaoDto){
        LocalDateTime agora = LocalDateTime.now();
        if(agora.isBefore(sessaoVotacaoDto.aberturaSessao())){
            throw new VotacaoForaDaSessaoException("Voto não pôde ser efetuado. Sessão não iniciada");
        }
        if(agora.isAfter(sessaoVotacaoDto.fechamentoSessao())){
            throw new VotacaoForaDaSessaoException("Voto não pôde ser efetuado. Sessão finalizada");
        }
    }
}
