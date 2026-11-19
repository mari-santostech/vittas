package com.vittas.instituicoes.util;

public final class DocumentoUtil {

    private static final int[] PESOS_CNPJ_DV1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_CNPJ_DV2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private DocumentoUtil() {
    }

    public static String somenteDigitos(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    /**
     * Valida um CNPJ (com ou sem mascara) pelos digitos verificadores.
     */
    public static boolean cnpjValido(String cnpj) {
        String digitos = somenteDigitos(cnpj);

        if (digitos == null || digitos.length() != 14 || digitos.chars().distinct().count() == 1) {
            return false;
        }

        int dv1 = calcularDigito(digitos, PESOS_CNPJ_DV1);
        int dv2 = calcularDigito(digitos, PESOS_CNPJ_DV2);

        return dv1 == Character.getNumericValue(digitos.charAt(12))
                && dv2 == Character.getNumericValue(digitos.charAt(13));
    }

    private static int calcularDigito(String digitos, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(digitos.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
