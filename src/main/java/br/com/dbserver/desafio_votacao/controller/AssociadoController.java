package br.com.dbserver.desafio_votacao.controller;

import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDTO;
import br.com.dbserver.desafio_votacao.service.AssociadoService;
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
@RequestMapping("/associado")
public class AssociadoController {
    
    private final AssociadoService associadoService;
    
    public AssociadoController (AssociadoService associadoService){
        this.associadoService = associadoService;
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os associados", description = "Retorna uma lista de todos os associados cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de associados retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AssociadoDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<AssociadoDTO>> listarTodos(){
        return ResponseEntity.ok(associadoService.listarTodas());    
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "Buscar associado por CPF", description = "Busca uma associado pelo seu CPF")
    @ApiResponse(responseCode = "200", description = "Associado encontrado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AssociadoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Associado nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<AssociadoDTO> buscar(@PathVariable String cpf){
        return  ResponseEntity.ok(associadoService.buscarPorId(cpf));
    }

    @PostMapping
    @Operation(summary = "Salvar novo associado", description = "Cadastra um novo associado")
    @ApiResponse(responseCode = "201", description = "Associado cadastrado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AssociadoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<AssociadoDTO> salvar(@Valid @RequestBody AssociadoDTO associadoDto){
        return  ResponseEntity.status(HttpStatus.CREATED).body(associadoService.salvar(associadoDto));
    }

    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar associado", description = "Atualiza uma associado existente")
    @ApiResponse(responseCode = "201", description = "Associado atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AssociadoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Associado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<AssociadoDTO> atualizar(@PathVariable String cpf, @Valid @RequestBody AtualizarAssociadoDTO atualizarAssociadoDto){
        return ResponseEntity.ok(associadoService.atualizar(cpf, atualizarAssociadoDto));
    }

    @DeleteMapping("/{cpf}")
    @Operation(summary = "Deletar associado por CPF", description = "Exclui uma associado pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Associado deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Associado não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deletar(@PathVariable String cpf){
        associadoService.deletarPorId(cpf);
        return ResponseEntity.noContent().build();

    }
}
