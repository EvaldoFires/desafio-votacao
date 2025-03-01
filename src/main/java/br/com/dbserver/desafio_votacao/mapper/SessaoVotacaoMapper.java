package br.com.dbserver.desafio_votacao.mapper;

import br.com.dbserver.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.dbserver.desafio_votacao.model.SessaoVotacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {

    @Mapping(target = "pauta.id", source = "idPauta")
    SessaoVotacao toEntity(SessaoVotacaoDTO dto);

    @Mapping(target = "idPauta", source = "pauta.id")
    SessaoVotacaoDTO toDto(SessaoVotacao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pauta.id", source = "idPauta")
    void updateFromDto(SessaoVotacaoDTO dto, @MappingTarget SessaoVotacao entity);
}
