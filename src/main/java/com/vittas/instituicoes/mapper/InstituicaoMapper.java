package com.vittas.instituicoes.mapper;

import com.vittas.dto.instituicao.InstituicaoCreateRequest;
import com.vittas.dto.instituicao.InstituicaoResponse;
import com.vittas.dto.instituicao.InstituicaoUpdateRequest;
import com.vittas.model.Instituicao;

public final class InstituicaoMapper {

    private InstituicaoMapper() {}

    public static Instituicao toEntity(InstituicaoCreateRequest request) {
        Instituicao entity = new Instituicao();
        entity.setNome(request.nome().trim());
        entity.setCnpj(request.cnpj().trim());
        entity.setEndereco(trimToNull(request.endereco()));
        entity.setTelefone(trimToNull(request.telefone()));
        entity.setEmail(trimToNull(request.email()));
        entity.setStatus(request.status() != null ? request.status() : true);
        return entity;
    }

    public static void updateEntity(Instituicao entity, InstituicaoUpdateRequest request) {
        entity.setNome(request.nome().trim());
        entity.setCnpj(request.cnpj().trim());
        entity.setEndereco(trimToNull(request.endereco()));
        entity.setTelefone(trimToNull(request.telefone()));
        entity.setEmail(trimToNull(request.email()));
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
    }

    public static InstituicaoResponse toResponse(Instituicao entity) {
        return new InstituicaoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getEndereco(),
                entity.getTelefone(),
                entity.getEmail(),
                entity.getStatus()
        );
    }

    private static String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}