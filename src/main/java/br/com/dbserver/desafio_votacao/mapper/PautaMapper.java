package br.com.dbserver.desafio_votacao.mapper;

import br.com.dbserver.desafio_votacao.dto.PautaDto;
import br.com.dbserver.desafio_votacao.model.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    Pauta toEntity(PautaDto dto);
    PautaDto toDto(Pauta entity);
    @Mapping(target = "id", ignore = true)
    void updateFromDto(PautaDto dto, @MappingTarget Pauta entity);
}

