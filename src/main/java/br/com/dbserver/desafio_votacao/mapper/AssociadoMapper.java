package br.com.dbserver.desafio_votacao.mapper;

import br.com.dbserver.desafio_votacao.dto.AssociadoDTO;
import br.com.dbserver.desafio_votacao.dto.AtualizarAssociadoDTO;
import br.com.dbserver.desafio_votacao.model.Associado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface AssociadoMapper {

    Associado toEntity (AssociadoDTO dto);
    Associado toEntity (AtualizarAssociadoDTO dto);
    AssociadoDTO toDto(Associado entity);
    @Mapping(target = "cpf", ignore = true)
    void updateFromDto(AssociadoDTO dto, @MappingTarget Associado entity);
    @Mapping(target = "cpf", ignore = true)
    void updateFromDto(AtualizarAssociadoDTO dto, @MappingTarget Associado entity);
}
