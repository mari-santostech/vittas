
package com.vittas.instituicoes.dto.instituicao;

import java.time.LocalDateTime;

public record InstituicaoResponse(
        Long idInstituicao,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String endereco,
        String telefone,
        String status,
        LocalDateTime dataCadastro
) {}
