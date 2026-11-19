package com.vittas.instituicoes.dto.instituicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados de cadastro e edicao de uma instituicao.
 * CNPJ e telefone podem chegar com ou sem mascara; o service grava apenas os digitos.
 */
public record InstituicaoRequest(

        @NotBlank(message = "O nome fantasia é obrigatório")
        @Size(min = 3, max = 150, message = "O nome fantasia deve ter entre 3 e 150 caracteres")
        String nomeFantasia,

        @NotBlank(message = "A razão social é obrigatória")
        @Size(min = 3, max = 150, message = "A razão social deve ter entre 3 e 150 caracteres")
        String razaoSocial,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$",
                message = "O CNPJ deve estar no formato 00.000.000/0000-00")
        String cnpj,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$",
                message = "O telefone deve estar no formato (00) 0000-0000 ou (00) 00000-0000")
        String telefone,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
        String endereco
) {
}
