package br.com.dbserver.desafio_votacao.controller;

import br.com.dbserver.desafio_votacao.dto.VotoDTO;
import br.com.dbserver.desafio_votacao.service.VotoService;
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
@RequestMapping("/voto")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService){
        this.votoService = votoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os votos", description = "Retorna uma lista de todos os votos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de votos retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VotoDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<VotoDTO>> listarTodas(){
        return ResponseEntity.ok(votoService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar voto por ID", description = "Busca um voto pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Voto encontrado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VotoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Voto nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<VotoDTO> buscar(@PathVariable Long id){
        return  ResponseEntity.ok(votoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Salvar novo voto", description = "Cadastra um novo voto")
    @ApiResponse(responseCode = "201", description = "Voto cadastrado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VotoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<VotoDTO> salvar(@Valid @RequestBody VotoDTO votoDto){
        return  ResponseEntity.status(HttpStatus.CREATED).body(votoService.salvar(votoDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar voto", description = "Atualiza um voto existente")
    @ApiResponse(responseCode = "201", description = "Voto atualizado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VotoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Voto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<VotoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody VotoDTO votoDto){
        return ResponseEntity.ok(votoService.atualizar(id, votoDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar voto por ID", description = "Exclui um voto pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Voto deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Voto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        votoService.deletarPorId(id);
        return ResponseEntity.noContent().build();

    }
}
