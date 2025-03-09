package br.com.dbserver.desafio_votacao.service;

import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.exception.SessaoVotacaoDaPautaJaExisteException;
import br.com.dbserver.desafio_votacao.mapper.SessaoVotacaoMapper;
import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import br.com.dbserver.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.dbserver.desafio_votacao.service.impl.SessaoVotacaoServiceImpl;
import br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static br.com.dbserver.desafio_votacao.utils.PautaHelper.gerarPauta1;
import static br.com.dbserver.desafio_votacao.utils.PautaHelper.gerarPautaDTO;
import static br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper.*;
import static br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper.gerarSessaoVotacao1;
import static br.com.dbserver.desafio_votacao.utils.SessaoVotacaoHelper.gerarSessaoVotacaoDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private SessaoVotacaoMapper sessaoVotacaoMapper;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoVotacaoServiceImpl sessaoVotacaoService;

    private SessaoVotacao sessaoVotacao;
    private SessaoVotacaoDTO sessaoVotacaoDto;

    @BeforeEach
    void setup(){
        sessaoVotacao = gerarSessaoVotacao1();
        sessaoVotacaoDto = gerarSessaoVotacaoDTO(sessaoVotacao);
    }

    @Nested
    class BuscarSessaoVotacao{

        @Test
        void deveBuscarSessaoVotacaoPorid(){
            // Arrange
            when(sessaoVotacaoRepository.findById(anyLong()))
                    .thenReturn(Optional.of(sessaoVotacao));
            when(sessaoVotacaoMapper.toDto(any()))
                    .thenReturn(gerarSessaoVotacaoDTO(sessaoVotacao));
            // Act

            var sessaoVotacaoRecebido = sessaoVotacaoService.buscarPorId(sessaoVotacao.getId());

            // Assert
            assertThat(sessaoVotacaoRecebido)
                    .usingRecursiveComparison()
                    .isEqualTo(sessaoVotacaoDto);
            
            verify(sessaoVotacaoRepository, times(1)).findById(sessaoVotacao.getId());

            verify(sessaoVotacaoMapper, times(1)).toDto(sessaoVotacao);
        }
        
        @Test
        void deveGerarExcecao_QuandoBuscarSessaoVotacao_PorIdnexistente(){
            // Arrange
            when(sessaoVotacaoRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());
            
            // Act & Assert
            assertThatThrownBy(
                    () -> sessaoVotacaoService.buscarPorId(1L))
                    .isInstanceOf(RecursoNaoEncontradoException.class);
            
            verify(sessaoVotacaoRepository, times(1)).findById(1L);
        }

        @Test
        void deveBuscarTodasAsSessaoVotacao() {
            // Arrange
            var sessaoVotacaos = List.of(gerarSessaoVotacao1(), gerarSessaoVotacao2());
            var sessaoVotacaosDto = sessaoVotacaos.stream()
                    .map(SessaoVotacaoHelper::gerarSessaoVotacaoDTO)
                    .toList();
            
            when(sessaoVotacaoRepository.findAll())
                    .thenReturn(sessaoVotacaos);
            when(sessaoVotacaoMapper.toDto(any(SessaoVotacao.class)))
                    .thenAnswer(invocation -> {
                        SessaoVotacao sessaoVotacao = invocation.getArgument(0);
                        return gerarSessaoVotacaoDTO(sessaoVotacao);
                    });

            // Act
            List<SessaoVotacaoDTO> sessaoVotacaosRecebidos = sessaoVotacaoService.listarTodas();

            // Assert
            assertThat(sessaoVotacaosRecebidos)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactlyElementsOf(sessaoVotacaosDto);
            
            verify(sessaoVotacaoRepository, times(1)).findAll();
            verify(sessaoVotacaoMapper, times(2)).toDto(any(SessaoVotacao.class));
        }
    }

    @Nested
    class SalvarSessaoVotacao{

        @Test
        void deveSalvarSessaoVotacao(){
            // Arrange
            when(sessaoVotacaoRepository.findByPautaId(anyLong()))
                    .thenReturn(Optional.empty());

            when(pautaService.buscarPorId(anyLong()))
                    .thenReturn(gerarPautaDTO(gerarPauta1()));

            when(sessaoVotacaoRepository.save(any(SessaoVotacao.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(sessaoVotacaoMapper.toDto(any(SessaoVotacao.class)))
                    .thenReturn(sessaoVotacaoDto);
            when(sessaoVotacaoMapper.toEntity(any(SessaoVotacaoDTO.class)))
                    .thenReturn(sessaoVotacao);

            // Act
            var sessaoVotacaoArmazenado = sessaoVotacaoService.salvar(sessaoVotacaoDto);

            // Assert
            assertThat(sessaoVotacaoArmazenado)
                    .isInstanceOf(SessaoVotacaoDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(sessaoVotacaoDto);

            verify(sessaoVotacaoRepository, times(1)).findByPautaId(sessaoVotacaoDto.idPauta());
            verify(sessaoVotacaoRepository, times(1)).save(sessaoVotacao);

            verify(pautaService, times(1)).buscarPorId(sessaoVotacaoDto.idPauta());

            verify(sessaoVotacaoMapper, times(1)).toDto(sessaoVotacao);
            verify(sessaoVotacaoMapper, times(1)).toEntity(sessaoVotacaoDto);
        }

        @Test
        void deveGerarExcecao_QuandoSalvarSessaoVotacao_QuandoExisteSessaoParaPauta(){
            // Arrange
            when(sessaoVotacaoRepository.findByPautaId(sessaoVotacaoDto.idPauta()))
                    .thenReturn(Optional.of(sessaoVotacao));

            // Act & Assert
            assertThatThrownBy(() -> sessaoVotacaoService.salvar(sessaoVotacaoDto))
                    .isInstanceOf(SessaoVotacaoDaPautaJaExisteException.class);

            verify(sessaoVotacaoRepository, times(1)).findByPautaId(sessaoVotacaoDto.idPauta());
        }
    }

    @Nested
    class AlterarSessaoVotacao{

        @Test
        void deveAlterarSessaoVotacao(){
            // Arrange
            when(sessaoVotacaoRepository.findById(anyLong()))
                    .thenReturn(Optional.of(sessaoVotacao));
            when(sessaoVotacaoRepository.save(any(SessaoVotacao.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(sessaoVotacaoMapper.toDto(any(SessaoVotacao.class)))
                    .thenReturn(sessaoVotacaoDto);
            when(sessaoVotacaoMapper.toEntity(any(SessaoVotacaoDTO.class)))
                    .thenReturn(sessaoVotacao);

            // Act
            var sessaoVotacaoArmazenado = sessaoVotacaoService.atualizar(sessaoVotacaoDto.id(), sessaoVotacaoDto);

            // Assert
            assertThat(sessaoVotacaoArmazenado)
                    .isInstanceOf(SessaoVotacaoDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(sessaoVotacaoDto);

            verify(sessaoVotacaoRepository, times(1)).findById(sessaoVotacao.getId());
            verify(sessaoVotacaoRepository, times(1)).save(sessaoVotacao);

            verify(sessaoVotacaoMapper, times(2)).toDto(sessaoVotacao);
            verify(sessaoVotacaoMapper, times(1)).toEntity(sessaoVotacaoDto);
        }

        @Test
        void deveGerarException_QuandoAlterarSessaoVotacao_PorIdInexistente(){
            // Arrange
            when(sessaoVotacaoRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> sessaoVotacaoService.deletarPorId(sessaoVotacao.getId()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(sessaoVotacaoRepository, times(1)).findById(sessaoVotacao.getId());
            verify(sessaoVotacaoRepository, times(0)).save(any(SessaoVotacao.class));
        }
    }

    @Nested
    class DeletarSessaoVotacao{

        @Test
        void deveDeletarSessaoVotacaoPorId(){
            // Arrange
            when(sessaoVotacaoRepository.findById(anyLong()))
                    .thenReturn(Optional.of(sessaoVotacao));
            doNothing().when(sessaoVotacaoRepository).deleteById(sessaoVotacao.getId());

            // Act
            sessaoVotacaoService.deletarPorId(sessaoVotacao.getId());

            // Assert
            verify(sessaoVotacaoRepository, times(1)).findById(sessaoVotacao.getId());
            verify(sessaoVotacaoRepository, times(1)).deleteById(sessaoVotacao.getId());
        }

        @Test
        void deveGerarExcecao_QuandoTentarDeletarSessaoVotacao_PorIdInexistente(){
            // Arrange
            when(sessaoVotacaoRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> sessaoVotacaoService.deletarPorId(sessaoVotacao.getId()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);
            
            verify(sessaoVotacaoRepository, times(1)).findById(sessaoVotacao.getId());
            verify(sessaoVotacaoRepository, times(0)).deleteById(sessaoVotacao.getId());
        }
    }


}
