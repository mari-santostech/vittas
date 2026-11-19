package com.vittas.instituicoes.controller;

import com.vittas.instituicoes.repository.InstituicaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste de integracao do fluxo completo: HTTP -> controller -> service -> JPA -> banco (H2 modo MySQL).
 */
@SpringBootTest
@AutoConfigureMockMvc
class InstituicaoControllerIntegrationTest {

    private static final String URL = "/api/instituicoes";

    private static final String CORPO_VALIDO = """
            {
              "nomeFantasia": "Clinica Teste VITTAS",
              "razaoSocial": "Clinica Teste VITTAS Ltda",
              "cnpj": "11.222.333/0001-81",
              "telefone": "(44) 99999-0000",
              "endereco": "Rua Exemplo, 100 - Maringa/PR"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstituicaoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    private String criar() throws Exception {
        return mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(CORPO_VALIDO))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
    }

    @Test
    void cicloCompletoDoCrud() throws Exception {
        String location = criar();

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cnpj").value("11222333000181"))
                .andExpect(jsonPath("$.telefone").value("44999990000"))
                .andExpect(jsonPath("$.status").value("ATIVA"))
                .andExpect(jsonPath("$.dataCadastro").exists());

        mockMvc.perform(put(location).contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO.replace("Rua Exemplo, 100", "Avenida Brasil, 500")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endereco").value("Avenida Brasil, 500 - Maringa/PR"));

        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(jsonPath("$.status").value("INATIVA"));

        mockMvc.perform(patch(location + "/ativar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATIVA"));
    }

    @Test
    void pesquisaPorNomeCnpjEStatus() throws Exception {
        criar();

        mockMvc.perform(get(URL).param("busca", "teste vittas")).andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get(URL).param("busca", "11.222.333")).andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get(URL).param("busca", "inexistente")).andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get(URL).param("status", "ATIVA")).andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get(URL).param("status", "INATIVA")).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void retornaErrosPadronizados() throws Exception {
        criar();

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(CORPO_VALIDO))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("INSTITUICAO_JA_EXISTE"))
                .andExpect(jsonPath("$.campos.cnpj").exists());

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDACAO"))
                .andExpect(jsonPath("$.campos.nomeFantasia").exists())
                .andExpect(jsonPath("$.campos.cnpj").exists());

        mockMvc.perform(get(URL + "/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("NAO_ENCONTRADO"));

        mockMvc.perform(get(URL).param("status", "QUALQUER"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void liberaCorsParaOFrontend() throws Exception {
        mockMvc.perform(options(URL + "/1")
                        .header(HttpHeaders.ORIGIN, "http://localhost:5500")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5500"));
    }
}
