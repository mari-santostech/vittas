package com.vittas.instituicoes.mapper;

import com.vittas.instituicoes.dto.instituicao.InstituicaoRequest;
import com.vittas.instituicoes.dto.instituicao.InstituicaoResponse;
import com.vittas.instituicoes.model.Instituicao;
import com.vittas.instituicoes.util.DocumentoUtil;

public final class InstituicaoMapper {

    private InstituicaoMapper() {
    }

    public static Instituicao toEntity(InstituicaoRequest request) {
        Instituicao entity = new Instituicao();
        updateEntity(entity, request);
        return entity;
    }

    public static void updateEntity(Instituicao entity, InstituicaoRequest request) {
        entity.setNomeFantasia(request.nomeFantasia().trim());
        entity.setRazaoSocial(request.razaoSocial().trim());
        entity.setCnpj(DocumentoUtil.somenteDigitos(request.cnpj()));
        entity.setTelefone(DocumentoUtil.somenteDigitos(request.telefone()));
        entity.setEndereco(request.endereco().trim());
    }

    public static InstituicaoResponse toResponse(Instituicao entity) {
        return new InstituicaoResponse(
                entity.getIdInstituicao(),
                entity.getNomeFantasia(),
                entity.getRazaoSocial(),
                entity.getCnpj(),
                entity.getTelefone(),
                entity.getEndereco(),
                entity.getStatus(),
                entity.getDataCadastro()
        );
    }
}
