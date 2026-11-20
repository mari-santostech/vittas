
package com.vittas.instituicoes.dto.instituicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InstituicaoCreateRequest(

        @NotBlank(message = "A razão social é obrigatória")
        @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres")
        String razaoSocial,

        @NotBlank(message = "O nome fantasia é obrigatório")
        @Size(max = 150, message = "O nome fantasia deve ter no máximo 150 caracteres")
        String nomeFantasia,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter 14 dígitos numéricos")
        String cnpj,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
        String endereco,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Pattern(regexp = "ATIVA|INATIVA", message = "O status deve ser ATIVA ou INATIVA")
        String status
) {}
