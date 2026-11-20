
package com.vittas.instituicoes.mapper;

import com.vittas.instituicoes.dto.instituicao.InstituicaoCreateRequest;
import com.vittas.instituicoes.dto.instituicao.InstituicaoResponse;
import com.vittas.instituicoes.dto.instituicao.InstituicaoUpdateRequest;
import com.vittas.instituicoes.model.Instituicao;

public final class InstituicaoMapper {

    private InstituicaoMapper() {}

    public static Instituicao toEntity(InstituicaoCreateRequest request) {
        Instituicao entity = new Instituicao();

        entity.setRazaoSocial(request.razaoSocial().trim());
        entity.setNomeFantasia(request.nomeFantasia().trim());
        entity.setCnpj(request.cnpj().trim());
        entity.setEndereco(request.endereco().trim());
        entity.setTelefone(trimToNull(request.telefone()));

        if (request.status() != null) {
            entity.setStatus(request.status());
        }

        return entity;
    }

    public static void updateEntity(
            Instituicao entity,
            InstituicaoUpdateRequest request) {

        entity.setRazaoSocial(request.razaoSocial().trim());
        entity.setNomeFantasia(request.nomeFantasia().trim());
        entity.setCnpj(request.cnpj().trim());
        entity.setEndereco(request.endereco().trim());
        entity.setTelefone(trimToNull(request.telefone()));

        if (request.status() != null) {
            entity.setStatus(request.status());
        }
    }

    public static InstituicaoResponse toResponse(Instituicao entity) {
        return new InstituicaoResponse(
                entity.getIdInstituicao(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getCnpj(),
                entity.getEndereco(),
                entity.getTelefone(),
                entity.getStatus(),
                entity.getDataCadastro()
        );
    }

    private static String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
