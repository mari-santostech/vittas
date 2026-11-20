package com.vittas.instituicoes.dto.instituicao;

import com.vittas.instituicoes.model.StatusInstituicao;

import java.time.LocalDateTime;

public record InstituicaoResponse(
        Long id,
        String nomeFantasia,
        String razaoSocial,
        String cnpj,
        String telefone,
        String endereco,
        StatusInstituicao status,
        LocalDateTime dataCadastro
) {
}
