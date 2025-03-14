package br.com.dbserver.desafio_votacao.mapper;

import br.com.dbserver.desafio_votacao.dto.PautaDTO;
import br.com.dbserver.desafio_votacao.model.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    @Mapping(target = "dataCriacao", ignore = true)
    Pauta toEntity(PautaDTO dto);
    PautaDTO toDto(Pauta entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    void updateFromDto(PautaDTO dto, @MappingTarget Pauta entity);
}

