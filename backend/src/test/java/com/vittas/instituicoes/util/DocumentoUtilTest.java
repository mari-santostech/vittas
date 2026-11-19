package com.vittas.instituicoes.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentoUtilTest {

    @Test
    void aceitaCnpjValidoComESemMascara() {
        assertThat(DocumentoUtil.cnpjValido("11.222.333/0001-81")).isTrue();
        assertThat(DocumentoUtil.cnpjValido("11222333000181")).isTrue();
    }

    @Test
    void rejeitaCnpjComDigitoVerificadorErrado() {
        assertThat(DocumentoUtil.cnpjValido("11.222.333/0001-82")).isFalse();
    }

    @Test
    void rejeitaCnpjComDigitosRepetidosOuTamanhoErrado() {
        assertThat(DocumentoUtil.cnpjValido("00000000000000")).isFalse();
        assertThat(DocumentoUtil.cnpjValido("1122233300018")).isFalse();
        assertThat(DocumentoUtil.cnpjValido(null)).isFalse();
    }

    @Test
    void removeMascara() {
        assertThat(DocumentoUtil.somenteDigitos("(44) 99999-0000")).isEqualTo("44999990000");
    }
}
