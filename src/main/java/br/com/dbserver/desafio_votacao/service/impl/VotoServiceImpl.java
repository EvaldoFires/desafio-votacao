package br.com.dbserver.desafio_votacao.service.impl;

import br.com.dbserver.desafio_votacao.client.CPFValidadorClient;
import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.dbserver.desafio_votacao.dto.VotoDTO;
import br.com.dbserver.desafio_votacao.exception.CPFUnableToVoteException;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.exception.VotacaoForaDaSessaoException;
import br.com.dbserver.desafio_votacao.exception.VotacaoJaFeitaComEsseCpfException;
import br.com.dbserver.desafio_votacao.mapper.VotoMapper;
import br.com.dbserver.desafio_votacao.model.Associado;
import br.com.dbserver.desafio_votacao.model.Voto;
import br.com.dbserver.desafio_votacao.repository.VotoRepository;
import br.com.dbserver.desafio_votacao.service.AssociadoService;
import br.com.dbserver.desafio_votacao.service.SessaoVotacaoService;
import br.com.dbserver.desafio_votacao.service.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepository;
    private final VotoMapper votoMapper;
    private final AssociadoService associadoService;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final CPFValidadorClient cpfValidadorClient;

    @Override
    public List<VotoDTO> listarTodas() {
        return votoRepository.findAll()
                .stream()
                .map(votoMapper::toDto)
                .toList();
    }

    @Override
    public VotoDTO buscarPorId(Long id) {
        Voto voto = votoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException ("\"Voto não encontrada com Id: \" + id)"));
        return votoMapper.toDto(voto);
    }

    @Override
    public VotoDTO salvar(VotoDTO votoDto) {

        Voto voto = votoMapper.toEntity(votoDto);
        this.validarVoto(votoDto);
        voto = votoRepository.save(voto);
        return votoMapper.toDto(voto);
    }

    @Override
    public VotoDTO atualizar(Long id, VotoDTO votoDto) {
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

    private void validarVoto (VotoDTO votoDto){
        AssociadoDTO associadoDto = associadoService.buscarPorId(votoDto.cpfAssociado());
        SessaoVotacaoDTO sessaoVotacaoDto = sessaoVotacaoService.buscarPorId(votoDto.idSessaoVotacao());
        this.verificarHorarioDeVotacao(sessaoVotacaoDto);
        this.verificarCPFValido(associadoDto.cpf());
        this.verificarVotoUnico(associadoDto, sessaoVotacaoDto);

    }
    private void verificarVotoUnico(AssociadoDTO associadoDto, SessaoVotacaoDTO sessaoVotacaoDto){
        votoRepository.findByAssociado_CpfAndSessaoVotacao_Id(associadoDto.cpf(), sessaoVotacaoDto.id())
                .ifPresent(v -> {
                    throw new VotacaoJaFeitaComEsseCpfException("Voto não permitido. Já foi feito uma votação para a" +
                            " sessão de id: " + v.getSessaoVotacao().getId() +
                            " pelo associado de cpf: " + v.getAssociado().getCpf());
                } );
    }
    private void verificarHorarioDeVotacao(SessaoVotacaoDTO sessaoVotacaoDto){
        LocalDateTime agora = LocalDateTime.now();
        if(agora.isBefore(sessaoVotacaoDto.aberturaSessao())){
            throw new VotacaoForaDaSessaoException("Voto não pôde ser efetuado. Sessão não iniciada");
        }
        if(agora.isAfter(sessaoVotacaoDto.fechamentoSessao())){
            throw new VotacaoForaDaSessaoException("Voto não pôde ser efetuado. Sessão finalizada");
        }
    }
    private void verificarCPFValido(String cpf){
        if (!cpfValidadorClient.isAbleToVote(cpf)) {
            throw new CPFUnableToVoteException("Associado não pode votar.");
        }
    }
}
