package com.vittas.instituicoes.dto.instituicao;

public record InstituicaoResponse(
        Long id,
        String nome,
        String cnpj,
        String endereco,
        String telefone,
        String email,
        Boolean status
) {}