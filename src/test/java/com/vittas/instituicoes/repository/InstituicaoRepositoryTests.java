
package com.vittas.instituicoes.repository;

import com.vittas.instituicoes.model.Instituicao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InstituicaoRepositoryTests {

    @Autowired
    private InstituicaoRepository repository;

    private Instituicao criarInstituicao(
            String razaoSocial,
            String nomeFantasia,
            String cnpj,
            String status) {

        Instituicao instituicao = new Instituicao();

        instituicao.setRazaoSocial(razaoSocial);
        instituicao.setNomeFantasia(nomeFantasia);
        instituicao.setCnpj(cnpj);
        instituicao.setEndereco("Rua Teste, 100");
        instituicao.setTelefone("44999999999");
        instituicao.setStatus(status);

        return instituicao;
    }

    @Test
    void deveSalvarInstituicaoComSucesso() {

        Instituicao instituicao = criarInstituicao(
                "Hospital Teste LTDA",
                "Hospital Teste",
                "12345678000199",
                null
        );

        Instituicao salva = repository.save(instituicao);

        assertNotNull(salva.getIdInstituicao());
        assertEquals("Hospital Teste LTDA", salva.getRazaoSocial());
        assertEquals("Hospital Teste", salva.getNomeFantasia());
        assertEquals("12345678000199", salva.getCnpj());
        assertEquals("ATIVA", salva.getStatus());
        assertNotNull(salva.getDataCadastro());
    }

    @Test
    void deveBuscarInstituicaoPorId() {

        Instituicao salva = repository.save(
                criarInstituicao(
                        "Hospital Central LTDA",
                        "Hospital Central",
                        "11111111000111",
                        "ATIVA"
                )
        );

        var encontrada = repository.findById(salva.getIdInstituicao());

        assertTrue(encontrada.isPresent());
        assertEquals(
                "Hospital Central",
                encontrada.get().getNomeFantasia()
        );
    }

    @Test
    void deveVerificarExistenciaPorCnpj() {

        repository.save(
                criarInstituicao(
                        "Hospital Norte LTDA",
                        "Hospital Norte",
                        "22222222000122",
                        "ATIVA"
                )
        );

        assertTrue(repository.existsByCnpj("22222222000122"));
        assertFalse(repository.existsByCnpj("33333333000133"));
    }

    @Test
    void deveIdentificarCnpjDuplicadoEmOutraInstituicao() {

        Instituicao primeira = repository.save(
                criarInstituicao(
                        "Hospital Sul LTDA",
                        "Hospital Sul",
                        "44444444000144",
                        "ATIVA"
                )
        );

        Instituicao segunda = repository.save(
                criarInstituicao(
                        "Hospital Leste LTDA",
                        "Hospital Leste",
                        "55555555000155",
                        "ATIVA"
                )
        );

        assertTrue(repository.existsByCnpjAndIdInstituicaoNot(
                primeira.getCnpj(),
                segunda.getIdInstituicao()
        ));

        assertFalse(repository.existsByCnpjAndIdInstituicaoNot(
                primeira.getCnpj(),
                primeira.getIdInstituicao()
        ));
    }

    @Test
    void deveBuscarPorRazaoSocial() {

        repository.save(
                criarInstituicao(
                        "Hospital Regional LTDA",
                        "Regional",
                        "66666666000166",
                        "ATIVA"
                )
        );

        Page<Instituicao> resultado =
                repository.findByRazaoSocialContainingIgnoreCase(
                        "regional",
                        PageRequest.of(0, 10)
                );

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveBuscarPorNomeFantasia() {

        repository.save(
                criarInstituicao(
                        "Saude LTDA",
                        "Clinica Vida",
                        "77777777000177",
                        "ATIVA"
                )
        );

        Page<Instituicao> resultado =
                repository.findByNomeFantasiaContainingIgnoreCase(
                        "vida",
                        PageRequest.of(0, 10)
                );

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveFiltrarPorStatus() {

        repository.save(
                criarInstituicao(
                        "Hospital Ativo LTDA",
                        "Hospital Ativo",
                        "88888888000188",
                        "ATIVA"
                )
        );

        repository.save(
                criarInstituicao(
                        "Hospital Inativo LTDA",
                        "Hospital Inativo",
                        "99999999000199",
                        "INATIVA"
                )
        );

        Page<Instituicao> resultado =
                repository.findByStatus(
                        "ATIVA",
                        PageRequest.of(0, 10)
                );

        assertEquals(1, resultado.getTotalElements());
        assertEquals(
                "Hospital Ativo",
                resultado.getContent().get(0).getNomeFantasia()
        );
    }

    @Test
    void deveCombinarFiltrosENaoPerderPaginacao() {

        repository.save(
                criarInstituicao(
                        "Hospital Central LTDA",
                        "Central",
                        "10101010000110",
                        "ATIVA"
                )
        );

        repository.save(
                criarInstituicao(
                        "Hospital Central Norte LTDA",
                        "Central Norte",
                        "20202020000120",
                        "ATIVA"
                )
        );

        repository.save(
                criarInstituicao(
                        "Hospital Central Sul LTDA",
                        "Central Sul",
                        "30303030000130",
                        "INATIVA"
                )
        );

        Page<Instituicao> resultado =
                repository.buscarPorNomeOuRazaoSocialEStatus(
                        "Central",
                        "ATIVA",
                        PageRequest.of(0, 1)
                );

        assertEquals(2, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        assertTrue(resultado.hasNext());
    }
}
