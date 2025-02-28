package br.com.dbserver.desafio_votacao.mapper;

import br.com.dbserver.desafio_votacao.dto.AssociadoDto;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDto;
import br.com.dbserver.desafio_votacao.model.Associado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface AssociadoMapper {

    Associado toEntity (AssociadoDto dto);
    Associado toEntity (AtualizarAssociadoDto dto);
    AssociadoDto toDto(Associado entity);
    @Mapping(target = "cpf", ignore = true)
    void updateFromDto(AssociadoDto dto, @MappingTarget Associado entity);
    @Mapping(target = "cpf", ignore = true)
    void updateFromDto(AtualizarAssociadoDto dto, @MappingTarget Associado entity);
}
