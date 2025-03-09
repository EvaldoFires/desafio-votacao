package br.com.dbserver.desafio_votacao.service;

import br.com.dbserver.desafio_votacao.client.CPFValidadorClient;
import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.dbserver.desafio_votacao.dto.VotoDTO;
import br.com.dbserver.desafio_votacao.exception.CPFUnableToVoteException;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.exception.VotacaoForaDaSessaoException;
import br.com.dbserver.desafio_votacao.exception.VotacaoJaFeitaComEsseCpfException;
import br.com.dbserver.desafio_votacao.mapper.VotoMapper;
import br.com.dbserver.desafio_votacao.model.Voto;
import br.com.dbserver.desafio_votacao.repository.VotoRepository;
import br.com.dbserver.desafio_votacao.service.impl.VotoServiceImpl;
import br.com.dbserver.desafio_votacao.utils.VotoHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.dbserver.desafio_votacao.utils.AssociadoHelper.gerarAssociado1;
import static br.com.dbserver.desafio_votacao.utils.AssociadoHelper.gerarAssociadoDTO;
import static br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper.gerarSessaoVotacao1;
import static br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper.gerarSessaoVotacaoDTO;
import static br.com.dbserver.desafio_votacao.utils.VotoHelper.gerarVoto1;
import static br.com.dbserver.desafio_votacao.utils.VotoHelper.gerarVotoDTO;
import static br.com.dbserver.desafio_votacao.utils.VotoHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private VotoMapper votoMapper;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private CPFValidadorClient cpfValidadorClient;

    @InjectMocks
    private VotoServiceImpl votoService;

    private VotoDTO votoDto;
    private Voto voto;
    private AssociadoDTO associadoDto;
    private SessaoVotacaoDTO sessaoVotacaoDto;

    @BeforeEach
    void setup(){
        voto = gerarVoto1();
        votoDto = gerarVotoDTO(voto);
        associadoDto = gerarAssociadoDTO(gerarAssociado1());
        sessaoVotacaoDto = gerarSessaoVotacaoDTO(gerarSessaoVotacao1());
    }

    @Nested
    class BuscarVoto{

        @Test
        void deveBuscarVotoPorid(){
            // Arrange
            when(votoRepository.findById(anyLong()))
                    .thenReturn(Optional.of(voto));
            when(votoMapper.toDto(any()))
                    .thenReturn(gerarVotoDTO(voto));
            // Act

            var votoRecebido = votoService.buscarPorId(voto.getId());

            // Assert
            assertThat(votoRecebido)
                    .usingRecursiveComparison()
                    .isEqualTo(votoDto);

            verify(votoRepository, times(1)).findById(voto.getId());

            verify(votoMapper, times(1)).toDto(voto);
        }

        @Test
        void deveGerarExcecao_QuandoBuscarVoto_PorIdnexistente(){
            // Arrange
            when(votoRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());
            
            // Act & Assert
            assertThatThrownBy(
                    () -> votoService.buscarPorId(1L))
                    .isInstanceOf(RecursoNaoEncontradoException.class);
            
            verify(votoRepository, times(1)).findById(1L);
        }

        @Test
        void deveBuscarTodosOsVotos() {
            // Arrange
            var votos = List.of(gerarVoto1(), gerarVoto2());
            var votosDto = votos.stream()
                    .map(VotoHelper::gerarVotoDTO)
                    .toList();
            when(votoRepository.findAll())
                    .thenReturn(votos);
            when(votoMapper.toDto(any(Voto.class)))
                    .thenAnswer(invocation -> {
                        Voto voto = invocation.getArgument(0);
                        return gerarVotoDTO(voto);
                    });

            // Act
            List<VotoDTO> votosRecebidos = votoService.listarTodas();

            // Assert
            assertThat(votosRecebidos)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactlyElementsOf(votosDto);
            
            verify(votoRepository, times(1)).findAll();
            
            verify(votoMapper, times(2)).toDto(any(Voto.class));
        }
    }

    @Nested
    class SalvarVoto{

        @Test
        void deveSalvarVoto(){
            when(votoMapper.toEntity(any(VotoDTO.class)))
                    .thenReturn(voto);
            when(associadoService.buscarPorId(anyString()))
                    .thenReturn(associadoDto);
            when(sessaoVotacaoService.buscarPorId(anyLong()))
                    .thenReturn(sessaoVotacaoDto);
            when(cpfValidadorClient.isAbleToVote(anyString()))
                    .thenReturn(true);
            when(votoRepository.findByAssociado_CpfAndSessaoVotacao_Id(anyString(), anyLong()))
                    .thenReturn(Optional.empty());
            when(votoRepository.save(any(Voto.class)))
                    .thenReturn(voto);
            when(votoMapper.toDto(any(Voto.class)))
                    .thenReturn(votoDto);

            // Act
            var votoArmazenado = votoService.salvar(votoDto);

            // Assert
            assertThat(votoArmazenado)
                    .isInstanceOf(VotoDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(votoDto);

            verify(votoRepository, times(1)).save(voto);

            verify(votoMapper, times(1)).toDto(voto);
            verify(votoMapper, times(1)).toEntity(votoDto);
        }

        @Test
        void deveGerarExcecao_QuandoSalvarVoto_AssociadoJaVotou(){
            // Arrange
            when(associadoService.buscarPorId(anyString())).thenReturn(associadoDto);
            when(sessaoVotacaoService.buscarPorId(anyLong())).thenReturn(sessaoVotacaoDto);
            when(votoRepository.findByAssociado_CpfAndSessaoVotacao_Id(anyString(), anyLong()))
                    .thenReturn(Optional.of(voto));
            when(cpfValidadorClient.isAbleToVote(anyString())).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> votoService.salvar(votoDto))
                    .isInstanceOf(VotacaoJaFeitaComEsseCpfException.class);
        }

        @Test
        void deveGerarExcecao_QuandoSalvar_HorarioInvalido_AntesAbrirSessao() {
            SessaoVotacaoDTO sessaoVotacaoDto = new SessaoVotacaoDTO(1L, 1L,
                    LocalDateTime.now().plusMinutes(10),
                    LocalDateTime.now().plusMinutes(20));
            when(sessaoVotacaoService.buscarPorId(anyLong())).thenReturn(sessaoVotacaoDto);

            assertThatThrownBy(() -> votoService.salvar(votoDto))
                    .isInstanceOf(VotacaoForaDaSessaoException.class)
                    .hasMessageContaining("Voto não pôde ser efetuado. Sessão não iniciada");
        }

        @Test
        void deveGerarExcecao_QuandoSalvar_HorarioInvalido_DepoisFechamentoSessao() {
            SessaoVotacaoDTO sessaoVotacaoDto = new SessaoVotacaoDTO(1L, 1L,
                    LocalDateTime.now().minusMinutes(20),
                    LocalDateTime.now().minusMinutes(10));
            when(sessaoVotacaoService.buscarPorId(anyLong())).thenReturn(sessaoVotacaoDto);

            assertThatThrownBy(() -> votoService.salvar(votoDto))
                    .isInstanceOf(VotacaoForaDaSessaoException.class)
                    .hasMessageContaining("Voto não pôde ser efetuado. Sessão finalizada");
        }

        @Test
        void deveLancarExcecao_QuandoCPFInvalido() {
            when(associadoService.buscarPorId(anyString())).thenReturn(associadoDto);
            when(sessaoVotacaoService.buscarPorId(anyLong())).thenReturn(sessaoVotacaoDto);
            when(cpfValidadorClient.isAbleToVote(anyString())).thenReturn(false);

            assertThatThrownBy(() -> votoService.salvar(votoDto))
                    .isInstanceOf(CPFUnableToVoteException.class)
                    .hasMessageContaining("Associado não pode votar");
        }

    }

    @Nested
    class AlterarVoto{

        @Test
        void deveAlterarVoto(){
            // Arrange
            when(votoRepository.findById(anyLong()))
                    .thenReturn(Optional.of(voto));
            when(votoRepository.save(any(Voto.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(votoMapper.toDto(any(Voto.class)))
                    .thenReturn(votoDto);
            when(votoMapper.toEntity(any(VotoDTO.class)))
                    .thenReturn(voto);

            // Act
            var votoArmazenado = votoService.atualizar(votoDto.id(), votoDto);

            // Assert
            assertThat(votoArmazenado)
                    .isInstanceOf(VotoDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(votoDto);

            verify(votoRepository, times(1)).findById(voto.getId());
            verify(votoRepository, times(1)).save(voto);

            verify(votoMapper, times(2)).toDto(voto);
            verify(votoMapper, times(1)).toEntity(votoDto);
        }

        @Test
        void deveGerarException_QuandoAlterarVoto_PorIdInexistente(){
            // Arrange
            when(votoRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> votoService.deletarPorId(voto.getId()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(votoRepository, times(1)).findById(voto.getId());
            verify(votoRepository, times(0)).save(any(Voto.class));
        }
    }

    @Nested
    class DeletarVoto{

        @Test
        void deveDeletarVotoPorId(){
            // Arrange
            when(votoRepository.findById(anyLong()))
                    .thenReturn(Optional.of(voto));
            doNothing().when(votoRepository).deleteById(voto.getId());

            // Act
            votoService.deletarPorId(voto.getId());

            // Assert
            verify(votoRepository, times(1)).findById(voto.getId());
            verify(votoRepository, times(1)).deleteById(voto.getId());
        }

        @Test
        void deveGerarExcecao_QuandoTentarDeletarVoto_PorIdInexistente(){
            // Arrange
            when(votoRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> votoService.deletarPorId(voto.getId()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);
            
            verify(votoRepository, times(1)).findById(voto.getId());
            verify(votoRepository, times(0)).deleteById(voto.getId());
        }
    }


}
