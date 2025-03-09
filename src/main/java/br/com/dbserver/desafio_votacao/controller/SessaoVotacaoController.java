package br.com.dbserver.desafio_votacao.controller;

import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.dbserver.desafio_votacao.service.SessaoVotacaoService;
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
@RequestMapping("/v1/sessaoVotacao")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService){
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as sessões de votação",
            description = "Retorna uma lista de todos as sessões de votação cadastrada")
    @ApiResponse(responseCode = "200", description = "Lista de sessões de votação retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SessaoVotacaoDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<SessaoVotacaoDTO>> listarTodas(){
        return ResponseEntity.ok(sessaoVotacaoService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sessão de votação por ID", description = "Busca uma sessão de votação pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Sessão de votação encontrado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SessaoVotacaoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Sessão de votação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<SessaoVotacaoDTO> buscar(@PathVariable Long id){
        return  ResponseEntity.ok(sessaoVotacaoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Salvar nova sessão de votação", description = "Cadastra uma nova sessão de votação")
    @ApiResponse(responseCode = "201", description = "Sessão de votação cadastrada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SessaoVotacaoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<SessaoVotacaoDTO> salvar(@Valid @RequestBody SessaoVotacaoDTO sessaoVotacaoDto){
        return  ResponseEntity.status(HttpStatus.CREATED).body(sessaoVotacaoService.salvar(sessaoVotacaoDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sessão de votação", description = "Atualiza uma sessão de votação existente")
    @ApiResponse(responseCode = "201", description = "Sessão de votação atualizada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SessaoVotacaoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Sessão de votação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<SessaoVotacaoDTO> atualizar(@PathVariable Long id,
                                                      @Valid @RequestBody SessaoVotacaoDTO sessaoVotacaoDto){
        return ResponseEntity.ok(sessaoVotacaoService.atualizar(id, sessaoVotacaoDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar sessão de votação por ID", description = "Exclui uma sessão de votação pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Sessão de votação deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Sessão de votação nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        sessaoVotacaoService.deletarPorId(id);
        return ResponseEntity.noContent().build();

    }
}
