
package com.vittas.instituicoes.service;

import com.vittas.instituicoes.dto.instituicao.InstituicaoCreateRequest;
import com.vittas.instituicoes.dto.instituicao.InstituicaoResponse;
import com.vittas.instituicoes.exception.BusinessException;
import com.vittas.instituicoes.model.Instituicao;
import com.vittas.instituicoes.repository.InstituicaoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

import java.util.Optional;

import com.vittas.instituicoes.dto.instituicao.InstituicaoUpdateRequest;

@ExtendWith(MockitoExtension.class)
class InstituicaoServiceTests {

    @Mock
    private InstituicaoRepository repository;

    @InjectMocks
    private InstituicaoService service;

    @Test
    void deveCriarInstituicaoComSucesso() {

        InstituicaoCreateRequest request =
                new InstituicaoCreateRequest(
                        "Hospital Teste LTDA",
                        "Hospital Teste",
                        "12345678000199",
                        "Rua Teste, 100",
                        "44999999999",
                        "ATIVA"
                );

        when(repository.existsByCnpj("12345678000199"))
                .thenReturn(false);

        when(repository.save(any(Instituicao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        InstituicaoResponse response = service.criar(request);

        assertNotNull(response);
        assertEquals("Hospital Teste LTDA", response.razaoSocial());
        assertEquals("Hospital Teste", response.nomeFantasia());
        assertEquals("12345678000199", response.cnpj());
        assertEquals("Rua Teste, 100", response.endereco());

        verify(repository).existsByCnpj("12345678000199");
        verify(repository).save(any(Instituicao.class));
    }

    @Test
    void naoDeveCriarInstituicaoComCnpjDuplicado() {

        InstituicaoCreateRequest request =
                new InstituicaoCreateRequest(
                        "Hospital Teste LTDA",
                        "Hospital Teste",
                        "12345678000199",
                        "Rua Teste, 100",
                        "44999999999",
                        "ATIVA"
                );

        when(repository.existsByCnpj("12345678000199"))
                .thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.criar(request)
        );

        assertEquals(
                "INSTITUICAO_JA_EXISTE",
                exception.getCodigo()
        );

        verify(repository).existsByCnpj("12345678000199");
        verify(repository, never()).save(any(Instituicao.class));
    }

    @Test
    void deveListarInstituicoesComFiltrosNormalizados() {

        Instituicao instituicao = new Instituicao();
        instituicao.setRazaoSocial("Hospital Teste LTDA");
        instituicao.setNomeFantasia("Hospital Teste");
        instituicao.setCnpj("12345678000199");
        instituicao.setEndereco("Rua Teste, 100");
        instituicao.setTelefone("44999999999");
        instituicao.setStatus("ATIVA");

        Page<Instituicao> pagina = new PageImpl<>(
                List.of(instituicao)
        );

        when(repository.buscarPorNomeOuRazaoSocialEStatus(
                "Hospital",
                "ATIVA",
                PageRequest.of(0, 10)
        )).thenReturn(pagina);

        Page<InstituicaoResponse> resultado = service.listar(
                " Hospital ",
                "ativa",
                PageRequest.of(0, 10)
        );

        assertEquals(1, resultado.getTotalElements());
        assertEquals(
                "Hospital Teste",
                resultado.getContent().get(0).nomeFantasia()
        );

        verify(repository).buscarPorNomeOuRazaoSocialEStatus(
                "Hospital",
                "ATIVA",
                PageRequest.of(0, 10)
        );
    }

    @Test
    void deveListarSemFiltrosQuandoNomeEStatusForemVazios() {

        Page<Instituicao> pagina = Page.empty();

        when(repository.buscarPorNomeOuRazaoSocialEStatus(
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(pagina);

        Page<InstituicaoResponse> resultado = service.listar(
                " ",
                null,
                PageRequest.of(0, 10)
        );

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).buscarPorNomeOuRazaoSocialEStatus(
                isNull(),
                isNull(),
                eq(PageRequest.of(0, 10))
        );
    }

    @Test
    void deveConsultarInstituicaoPorId() {
        Instituicao instituicao = new Instituicao();
        instituicao.setIdInstituicao(1L);
        instituicao.setRazaoSocial("Hospital Teste LTDA");
        instituicao.setNomeFantasia("Hospital Teste");
        instituicao.setCnpj("12345678000199");
        instituicao.setEndereco("Rua Teste, 100");
        instituicao.setTelefone("44999999999");
        instituicao.setStatus("ATIVA");

        when(repository.findById(1L))
                .thenReturn(Optional.of(instituicao));

        InstituicaoResponse response = service.consultar(1L);

        assertNotNull(response);
        assertEquals(1L, response.idInstituicao());
        assertEquals("Hospital Teste LTDA", response.razaoSocial());
        assertEquals("Hospital Teste", response.nomeFantasia());

        verify(repository).findById(1L);
    }

    @Test
    void naoDeveConsultarInstituicaoInexistente() {
        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.consultar(999L)
        );

        assertEquals(
                "INSTITUICAO_NAO_ENCONTRADA",
                exception.getCodigo()
        );

        verify(repository).findById(999L);
    }

    @Test
    void deveAtualizarInstituicaoComSucesso() {
        Instituicao instituicao = new Instituicao();
        instituicao.setIdInstituicao(1L);
        instituicao.setRazaoSocial("Hospital Antigo LTDA");
        instituicao.setNomeFantasia("Hospital Antigo");
        instituicao.setCnpj("12345678000199");
        instituicao.setEndereco("Rua Antiga, 100");
        instituicao.setTelefone("44999999999");
        instituicao.setStatus("ATIVA");

        InstituicaoUpdateRequest request = new InstituicaoUpdateRequest(
                "Hospital Novo LTDA",
                "Hospital Novo",
                "98765432000199",
                "Rua Nova, 200",
                "44988888888",
                "ATIVA"
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(instituicao));

        when(repository.existsByCnpjAndIdInstituicaoNot(
                "98765432000199", 1L
        )).thenReturn(false);

        when(repository.save(any(Instituicao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        InstituicaoResponse response = service.atualizar(1L, request);

        assertEquals("Hospital Novo LTDA", response.razaoSocial());
        assertEquals("Hospital Novo", response.nomeFantasia());
        assertEquals("98765432000199", response.cnpj());
        assertEquals("Rua Nova, 200", response.endereco());

        verify(repository).save(instituicao);
    }

    @Test
    void naoDeveAtualizarComCnpjDuplicado() {
        Instituicao instituicao = new Instituicao();
        instituicao.setIdInstituicao(1L);

        InstituicaoUpdateRequest request = new InstituicaoUpdateRequest(
                "Hospital Novo LTDA",
                "Hospital Novo",
                "98765432000199",
                "Rua Nova, 200",
                "44988888888",
                "ATIVA"
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(instituicao));

        when(repository.existsByCnpjAndIdInstituicaoNot(
                "98765432000199", 1L
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.atualizar(1L, request)
        );

        assertEquals("INSTITUICAO_JA_EXISTE", exception.getCodigo());

        verify(repository, never()).save(any(Instituicao.class));
    }

    @Test
    void naoDeveAtualizarInstituicaoInexistente() {
        InstituicaoUpdateRequest request = new InstituicaoUpdateRequest(
                "Hospital Novo LTDA",
                "Hospital Novo",
                "98765432000199",
                "Rua Nova, 200",
                "44988888888",
                "ATIVA"
        );

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.atualizar(999L, request)
        );

        assertEquals(
                "INSTITUICAO_NAO_ENCONTRADA",
                exception.getCodigo()
        );

        verify(repository, never()).save(any(Instituicao.class));
    }

    @Test
    void deveExcluirInstituicaoAlterandoStatusParaInativa() {
        Instituicao instituicao = new Instituicao();
        instituicao.setIdInstituicao(1L);
        instituicao.setRazaoSocial("Hospital Teste LTDA");
        instituicao.setNomeFantasia("Hospital Teste");
        instituicao.setCnpj("12345678000199");
        instituicao.setEndereco("Rua Teste, 100");
        instituicao.setStatus("ATIVA");

        when(repository.findById(1L))
                .thenReturn(Optional.of(instituicao));

        service.excluir(1L);

        assertEquals("INATIVA", instituicao.getStatus());

        verify(repository).findById(1L);
        verify(repository).save(instituicao);
        verify(repository, never()).delete(any(Instituicao.class));
    }

    @Test
    void naoDeveExcluirInstituicaoInexistente() {
        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.excluir(999L)
        );

        assertEquals(
                "INSTITUICAO_NAO_ENCONTRADA",
                exception.getCodigo()
        );

        verify(repository, never()).save(any(Instituicao.class));
        verify(repository, never()).delete(any(Instituicao.class));
    }
}
