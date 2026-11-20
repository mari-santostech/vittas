package com.vittas.instituicoes.service;

import com.vittas.instituicoes.dto.instituicao.InstituicaoRequest;
import com.vittas.instituicoes.dto.instituicao.InstituicaoResponse;
import com.vittas.instituicoes.exception.BusinessException;
import com.vittas.instituicoes.exception.RecursoNaoEncontradoException;
import com.vittas.instituicoes.model.Instituicao;
import com.vittas.instituicoes.model.StatusInstituicao;
import com.vittas.instituicoes.repository.InstituicaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstituicaoServiceTest {

    private static final String CNPJ_VALIDO = "11.222.333/0001-81";

    @Mock
    private InstituicaoRepository repository;

    @InjectMocks
    private InstituicaoService service;

    private InstituicaoRequest request(String cnpj) {
        return new InstituicaoRequest(
                " Clinica Teste ", "Clinica Teste Ltda", cnpj, "(44) 99999-0000", "Rua Exemplo, 100");
    }

    private Instituicao instituicaoSalva(Long id) {
        Instituicao entity = new Instituicao();
        entity.setIdInstituicao(id);
        entity.setNomeFantasia("Clinica Teste");
        entity.setRazaoSocial("Clinica Teste Ltda");
        entity.setCnpj("11222333000181");
        entity.setTelefone("44999990000");
        entity.setEndereco("Rua Exemplo, 100");
        entity.setStatus(StatusInstituicao.ATIVA);
        return entity;
    }

    @Test
    void criarGravaCnpjETelefoneSemMascara() {
        when(repository.existsByCnpj("11222333000181")).thenReturn(false);
        when(repository.save(any(Instituicao.class))).thenAnswer(inv -> inv.getArgument(0));

        service.criar(request(CNPJ_VALIDO));

        ArgumentCaptor<Instituicao> captor = ArgumentCaptor.forClass(Instituicao.class);
        verify(repository).save(captor.capture());
        Instituicao salva = captor.getValue();
        assertThat(salva.getCnpj()).isEqualTo("11222333000181");
        assertThat(salva.getTelefone()).isEqualTo("44999990000");
        assertThat(salva.getNomeFantasia()).isEqualTo("Clinica Teste");
    }

    @Test
    void criarRejeitaCnpjDuplicadoComConflito() {
        when(repository.existsByCnpj("11222333000181")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(request(CNPJ_VALIDO)))
                .isInstanceOf(BusinessException.class)
                .extracting(ex -> ((BusinessException) ex).getStatus())
                .isEqualTo(HttpStatus.CONFLICT);
        verify(repository, never()).save(any());
    }

    @Test
    void criarRejeitaCnpjInvalido() {
        assertThatThrownBy(() -> service.criar(request("11.222.333/0001-82")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("CNPJ inválido.");
        verify(repository, never()).save(any());
    }

    @Test
    void atualizarRejeitaCnpjDeOutraInstituicao() {
        when(repository.findById(1L)).thenReturn(Optional.of(instituicaoSalva(1L)));
        when(repository.existsByCnpjAndIdInstituicaoNot("11222333000181", 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.atualizar(1L, request(CNPJ_VALIDO)))
                .isInstanceOf(BusinessException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void consultarInexistenteLancaNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.consultar(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void desativarMarcaComoInativa() {
        when(repository.findById(1L)).thenReturn(Optional.of(instituicaoSalva(1L)));
        when(repository.save(any(Instituicao.class))).thenAnswer(inv -> inv.getArgument(0));

        InstituicaoResponse resposta = service.desativar(1L);

        assertThat(resposta.status()).isEqualTo(StatusInstituicao.INATIVA);
    }

    @Test
    void listarPorTextoNaoFiltraPorCnpj() {
        when(repository.pesquisar("Clinica", null, null)).thenReturn(List.of(instituicaoSalva(1L)));

        assertThat(service.listar("  Clinica ", null)).hasSize(1);
    }

    @Test
    void listarPorCnpjComMascaraBuscaPelosDigitos() {
        when(repository.pesquisar("11.222", "11222", StatusInstituicao.ATIVA))
                .thenReturn(List.of(instituicaoSalva(1L)));

        assertThat(service.listar("11.222", StatusInstituicao.ATIVA)).hasSize(1);
    }
}
