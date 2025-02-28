package br.com.dbserver.desafio_votacao.mapper;

import br.com.dbserver.desafio_votacao.dto.VotoDto;
import br.com.dbserver.desafio_votacao.model.Voto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VotoMapper {

    @Mapping(target = "associado.cpf", source = "cpfAssociado")
    @Mapping(target = "sessaoVotacao.id", source = "idSessaoVotacao")
    Voto toEntity(VotoDto dto);

    @Mapping(target = "cpfAssociado", source = "associado.cpf")
    @Mapping(target = "idSessaoVotacao", source = "sessaoVotacao.id")
    VotoDto toDto(Voto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "associado.cpf", source = "cpfAssociado")
    @Mapping(target = "sessaoVotacao.id", source = "idSessaoVotacao")
    void updateFromDto(VotoDto dto, @MappingTarget Voto entity);
}
