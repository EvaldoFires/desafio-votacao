package br.com.dbserver.desafio_votacao.service;

import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.exception.CpfJaCadastradoException;
import br.com.dbserver.desafio_votacao.exception.RecursoNaoEncontradoException;
import br.com.dbserver.desafio_votacao.mapper.AssociadoMapper;
import br.com.dbserver.desafio_votacao.model.Associado;
import br.com.dbserver.desafio_votacao.repository.AssociadoRepository;
import br.com.dbserver.desafio_votacao.service.impl.AssociadoServiceImpl;
import br.com.dbserver.desafio_votacao.utils.AssociadoHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static br.com.dbserver.desafio_votacao.utils.AssociadoHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private AssociadoMapper associadoMapper;

    @InjectMocks
    private AssociadoServiceImpl associadoService;

    private Associado associado;
    private AssociadoDTO associadoDto;

    @BeforeEach
    void setup(){
        associado = gerarAssociado1();
        associadoDto = gerarAssociadoDTO(associado);
    }
    @Nested
    class BuscarAssociado{

        @Test
        void deveBuscarAssociadoPorid(){
            // Arrange
            when(associadoRepository.findById(associado.getCpf()))
                    .thenReturn(Optional.of(associado));
            when(associadoMapper.toDto(any()))
                    .thenReturn(gerarAssociadoDTO(associado));
            // Act

            var associadoRecebido = associadoService.buscarPorId(associado.getCpf());

            // Assert
            assertThat(associadoRecebido)
                    .usingRecursiveComparison()
                    .isEqualTo(associadoDto);

            verify(associadoRepository, times(1)).findById(associado.getCpf());

            verify(associadoMapper, times(1)).toDto(associado);
        }

        @Test
        void deveGerarExcecao_QuandoBuscarAssociado_PorCpfInexistente(){
            when(associadoRepository.findById(anyString()))
                    .thenReturn(Optional.empty());
            // Act & Assert
            assertThatThrownBy(
                    () -> associadoService.buscarPorId("111.222.333-44"))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(associadoRepository, times(1)).findById("111.222.333-44");
        }

        @Test
        void deveBuscarTodosOsAssociados() {
            // Arrange
            var associados = List.of(gerarAssociado1(), gerarAssociado2());
            var associadosDto = associados.stream()
                    .map(AssociadoHelper::gerarAssociadoDTO)
                    .toList();

            when(associadoRepository.findAll())
                    .thenReturn(associados);

            when(associadoMapper.toDto(any(Associado.class)))
                    .thenAnswer(invocation -> {
                        Associado associado = invocation.getArgument(0);
                        return gerarAssociadoDTO(associado);
                    });

            // Act
            List<AssociadoDTO> associadosRecebidos = associadoService.listarTodas();

            // Assert
            assertThat(associadosRecebidos)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(2)
                    .containsExactlyElementsOf(associadosDto);
            verify(associadoRepository, times(1)).findAll();

            verify(associadoMapper, times(2)).toDto(any(Associado.class));
        }
    }

    @Nested
    class SalvarAssociado{

        @Test
        void deveSalvarAssociado(){
            // Arrange
            when(associadoRepository.findById(anyString()))
                    .thenReturn(Optional.empty());

            when(associadoRepository.save(any(Associado.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(associadoMapper.toDto(any(Associado.class)))
                    .thenReturn(associadoDto);
            when(associadoMapper.toEntity(any(AssociadoDTO.class)))
                    .thenReturn(associado);

            // Act
            var associadoArmazenado = associadoService.salvar(associadoDto);

            // Assert
            assertThat(associadoArmazenado)
                    .isInstanceOf(AssociadoDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .isEqualTo(associadoDto);

            verify(associadoRepository, times(1)).findById(associado.getCpf());
            verify(associadoRepository, times(1)).save(associado);


            verify(associadoMapper, times(1)).toDto(associado);
            verify(associadoMapper, times(1)).toEntity(associadoDto);
        }

        @Test
        void deveGerarExcecao_QuandoSalvarAssociado_ComCpfExistente(){
            // Arrange
            when(associadoRepository.findById(anyString())).thenReturn
                    (Optional.of(associado));

            // Act & Assert
            assertThatThrownBy(
                    () -> associadoService.salvar(associadoDto))
                    .isInstanceOf(CpfJaCadastradoException.class);

            verify(associadoRepository, times(1)).findById(associado.getCpf());
            verify(associadoRepository, times(0)).save(associado);
        }
    }

    @Nested
    class AlterarAssociado{

        @Test
        void deveAlterarAssociado(){
            // Arrange
            var atualizarAssociadoDto = gerarAtualizarAssociadoDTO(associado);

            when(associadoRepository.findById(anyString()))
                    .thenReturn(Optional.of(associado));
            when(associadoRepository.save(any(Associado.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(associadoMapper.toDto(any(Associado.class)))
                    .thenReturn(associadoDto);
            when(associadoMapper.toEntity(any(AssociadoDTO.class)))
                    .thenReturn(associado);

            // Act
            var associadoArmazenado = associadoService.atualizar(associadoDto.cpf(), atualizarAssociadoDto);

            // Assert
            assertThat(associadoArmazenado)
                    .isInstanceOf(AssociadoDTO.class)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("cpf")
                    .isEqualTo(atualizarAssociadoDto);

            assertThat(associadoArmazenado.cpf())
                    .isEqualTo(associadoDto.cpf());

            verify(associadoRepository, times(1)).findById(associado.getCpf());
            verify(associadoRepository, times(1)).save(associado);

            verify(associadoMapper, times(2)).toDto(associado);
            verify(associadoMapper, times(1)).toEntity(associadoDto);
        }

        @Test
        void deveGerarException_QuandoAlterarAssociado_PorCpfInexistente(){
            // Arrange
            when(associadoRepository.findById(anyString()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> associadoService.deletarPorId(associado.getCpf()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);

            verify(associadoRepository, times(1)).findById(associado.getCpf());
            verify(associadoRepository, times(0)).save(any(Associado.class));
        }
    }

    @Nested
    class DeletarAssociado{

        @Test
        void deveDeletarAssociadoPorCpf(){
            // Arrange
            when(associadoRepository.findById(anyString()))
                    .thenReturn(Optional.of(associado));
            doNothing().when(associadoRepository).deleteById(associado.getCpf());

            // Act
            associadoService.deletarPorId(associado.getCpf());

            // Assert
            verify(associadoRepository, times(1)).findById(associado.getCpf());
            verify(associadoRepository, times(1)).deleteById(associado.getCpf());
        }

        @Test
        void deveGerarExcecao_QuandoTentarDeletarAssociado_PorCpfInexistente(){
            // Arrange
            when(associadoRepository.findById(anyString()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(
                    () -> associadoService.deletarPorId(associado.getCpf()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);
            verify(associadoRepository, times(1)).findById(associado.getCpf());
            verify(associadoRepository, times(0)).deleteById(associado.getCpf());
        }
    }


}
