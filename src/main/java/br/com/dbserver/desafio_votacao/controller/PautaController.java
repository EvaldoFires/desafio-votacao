package br.com.dbserver.desafio_votacao.controller;

import br.com.dbserver.desafio_votacao.dto.PautaDTO;
import br.com.dbserver.desafio_votacao.dto.ResultadoDTO;
import br.com.dbserver.desafio_votacao.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pauta")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService){
        this.pautaService = pautaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as pautas", description = "Retorna uma lista de todas as pautas cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de pautas retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PautaDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<PautaDTO>> listarTodos(){
        return ResponseEntity.ok(pautaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pauta por ID", description = "Busca uma pauta pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Pauta encontrada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PautaDTO.class)))
    @ApiResponse(responseCode = "404", description = "Pauta nao encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<PautaDTO> buscar(@PathVariable Long id){
        return  ResponseEntity.ok(pautaService.buscarPorId(id));
    }
    @GetMapping("/{id}/resultado")
    @Operation(summary = "Buscar o resultado da votação da pauta por ID", description = "Busca o resultado da ultima" +
            " votação da pauta pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Resultado obtido com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PautaDTO.class)))
    @ApiResponse(responseCode = "404", description = "resultado nao encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<ResultadoDTO> buscarResultado(@PathVariable Long id){
        return  ResponseEntity.ok(pautaService.buscarResultaVotacaoPautaId(id));
    }

    @PostMapping
    @Operation(summary = "Salvar nova pauta", description = "Cadastra uma nova pauta")
    @ApiResponse(responseCode = "201", description = "Pauta cadastrada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PautaDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<PautaDTO> salvar(@Valid @RequestBody PautaDTO pautaDto){
        return  ResponseEntity.status(HttpStatus.CREATED).body(pautaService.salvar(pautaDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pauta", description = "Atualiza uma pauta existente")
    @ApiResponse(responseCode = "201", description = "Pauta atualizada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PautaDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<PautaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PautaDTO pautaDto){
        return ResponseEntity.ok(pautaService.atualizar(id, pautaDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pauta por ID", description = "Exclui uma pauta pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Pauta deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        pautaService.deletarPorId(id);
        return ResponseEntity.noContent().build();

    }

}
