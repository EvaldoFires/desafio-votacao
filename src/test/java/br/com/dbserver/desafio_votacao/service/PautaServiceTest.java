package br.com.dbserver.desafio_votacao.service;

import br.com.dbserver.desafio_votacao.dto.PautaDTO;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.mapper.PautaMapper;
import br.com.dbserver.desafio_votacao.model.Pauta;
import br.com.dbserver.desafio_votacao.repository.PautaRepository;
import br.com.dbserver.desafio_votacao.service.impl.PautaServiceImpl;
import br.com.dbserver.desafio_votacao.utils.PautaHelper;
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
import static br.com.dbserver.desafio_votacao.utils.PautaHelper.gerarPauta2;
import static br.com.dbserver.desafio_votacao.utils.PautaHelper.gerarPautaDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaMapper pautaMapper;

    @InjectMocks
    private PautaServiceImpl pautaService;

    private Pauta pauta;
    private PautaDTO pautaDto;

    @BeforeEach
    void setup(){
        pauta = gerarPauta1();
        pautaDto = gerarPautaDTO(pauta);
    }

    @Nested
    class BuscarPauta{

        @Test
        void deveBuscarPautaPorid(){
            // Arrange
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.of(pauta));
            when(pautaMapper.toDto(any()))
                    .thenReturn(gerarPautaDTO(pauta));
            // Act

            var pautaRecebido = pautaService.buscarPorId(pauta.getId());

            // Assert
            verify(pautaRepository, times(1)).findById(pauta.getId());
            assertThat(pautaRecebido)
                    .usingRecursiveComparison()
                    .isEqualTo(pautaDto);

            verify(pautaRepository, times(1)).findById(pauta.getId());

            verify(pautaMapper, times(1)).toDto(pauta);
        }

        @Test
        void deveGerarExcecao_QuandoBuscarPauta_PorIdnexistente(){
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());
            // Act & Assert
            assertThatThrownBy(
                    () -> pautaService.buscarPorId(1L))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(pautaRepository, times(1)).findById(1L);
        }

        @Test
        void deveBuscarTodasAsPautas() {
            // Arrange
            var pautas = List.of(gerarPauta1(), gerarPauta2());
            var pautasDto = pautas.stream()
                    .map(PautaHelper::gerarPautaDTO)
                    .toList();
            when(pautaRepository.findAll())
                    .thenReturn(pautas);
            when(pautaMapper.toDto(any(Pauta.class)))
                    .thenAnswer(invocation -> {
                        Pauta pauta = invocation.getArgument(0);
                        return gerarPautaDTO(pauta);
                    });

            // Act
            List<PautaDTO> pautasRecebidos = pautaService.listarTodas();

            // Assert
            assertThat(pautasRecebidos)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactlyElementsOf(pautasDto);

            verify(pautaRepository, times(1)).findAll();

            verify(pautaMapper, times(2)).toDto(any(Pauta.class));
        }
    }

    @Nested
    class SalvarPauta{

        @Test
        void deveSalvarPauta(){
            // Arrange
            when(pautaRepository.save(any(Pauta.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(pautaMapper.toDto(any(Pauta.class)))
                    .thenReturn(pautaDto);
            when(pautaMapper.toEntity(any(PautaDTO.class)))
                    .thenReturn(pauta);

            // Act
            var pautaArmazenado = pautaService.salvar(pautaDto);

            // Assert
            assertThat(pautaArmazenado)
                    .isInstanceOf(PautaDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(pautaDto);

            verify(pautaRepository, times(1)).save(pauta);

            verify(pautaMapper, times(1)).toDto(pauta);
            verify(pautaMapper, times(1)).toEntity(pautaDto);
        }

    }

    @Nested
    class AlterarPauta{

        @Test
        void deveAlterarPauta(){
            // Arrange
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.of(pauta));
            when(pautaRepository.save(any(Pauta.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(pautaMapper.toDto(any(Pauta.class)))
                    .thenReturn(pautaDto);
            when(pautaMapper.toEntity(any(PautaDTO.class)))
                    .thenReturn(pauta);

            // Act
            var pautaArmazenado = pautaService.atualizar(pautaDto.id(), pautaDto);

            // Assert
            assertThat(pautaArmazenado)
                    .isInstanceOf(PautaDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(pautaDto);

            verify(pautaRepository, times(1)).findById(pauta.getId());
            verify(pautaRepository, times(1)).save(pauta);

            verify(pautaMapper, times(2)).toDto(pauta);
            verify(pautaMapper, times(1)).toEntity(pautaDto);
        }

        @Test
        void deveGerarException_QuandoAlterarPauta_PorIdInexistente(){
            // Arrange
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> pautaService.deletarPorId(pauta.getId()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(pautaRepository, times(1)).findById(pauta.getId());
            verify(pautaRepository, times(0)).save(any(Pauta.class));
        }
    }

    @Nested
    class DeletarPauta{

        @Test
        void deveDeletarPautaPorId(){
            // Arrange
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.of(pauta));
            doNothing().when(pautaRepository).deleteById(pauta.getId());

            // Act
            pautaService.deletarPorId(pauta.getId());

            // Assert
            verify(pautaRepository, times(1)).findById(pauta.getId());
            verify(pautaRepository, times(1)).deleteById(pauta.getId());
        }

        @Test
        void deveGerarExcecao_QuandoTentarDeletarPauta_PorIdInexistente(){
            // Arrange
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> pautaService.deletarPorId(pauta.getId()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(pautaRepository, times(1)).findById(pauta.getId());
            verify(pautaRepository, times(0)).deleteById(pauta.getId());
        }
    }
    
}
