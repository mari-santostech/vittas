// VITTAS 2.0 - CRUD de Instituicoes
// Consome a API REST do backend Spring Boot (/api/instituicoes).

const API_URL = 'http://localhost:8080/api/instituicoes';

const STATUS_LABEL = { ATIVA: 'Ativa', INATIVA: 'Inativa' };

const elementos = {
    corpoTabela: document.getElementById('tabela-instituicoes-body'),
    mensagem: document.getElementById('mensagem-global'),
    formBusca: document.getElementById('form-busca'),
    busca: document.getElementById('busca'),
    situacao: document.getElementById('situacao'),

    modalCadastro: document.getElementById('modalInstituicao'),
    formCadastro: document.getElementById('form-instituicao'),
    btnSalvarCadastro: document.getElementById('btnSalvarInstituicao'),

    modalEdicao: document.getElementById('modalEditarInstituicao'),
    formEdicao: document.getElementById('form-editar-instituicao'),
    btnSalvarEdicao: document.getElementById('btnSalvarEdicao'),
    editId: document.getElementById('editInstituicaoId'),
    editIdDisplay: document.getElementById('edit-id-display'),
    editStatusDisplay: document.getElementById('edit-status-display'),
    textoSituacao: document.getElementById('texto-situacao'),
    btnDesativar: document.getElementById('btnDesativarInstituicao'),
    btnReativar: document.getElementById('btnReativarInstituicao'),
    btnConfirmarDesativacao: document.getElementById('btnConfirmarDesativacao'),
    confirmarDesativacao: document.getElementById('confirmar-desativacao'),
    desativarNome: document.getElementById('desativar-nome'),
};

// Campos de cada formulario: nome do campo na API -> id do input
const CAMPOS_CADASTRO = {
    nomeFantasia: 'nomeFantasia',
    razaoSocial: 'razaoSocial',
    cnpj: 'cnpj',
    telefone: 'telefone',
    endereco: 'endereco',
};

const CAMPOS_EDICAO = {
    nomeFantasia: 'editNomeFantasia',
    razaoSocial: 'editRazaoSocial',
    cnpj: 'editCnpj',
    telefone: 'editTelefone',
    endereco: 'editEndereco',
};

// ---------------------------------------------------------------------------
// Comunicacao com a API
// ---------------------------------------------------------------------------

class ApiError extends Error {
    constructor(mensagem, campos = {}) {
        super(mensagem);
        this.campos = campos;
    }
}

async function requisicao(caminho = '', opcoes = {}) {
    let resposta;
    try {
        resposta = await fetch(`${API_URL}${caminho}`, {
            headers: { 'Content-Type': 'application/json' },
            ...opcoes,
        });
    } catch {
        throw new ApiError('Não foi possível conectar à API. Verifique se o backend está em execução.');
    }

    if (resposta.status === 204) {
        return null;
    }

    const corpo = await resposta.json().catch(() => null);

    if (!resposta.ok) {
        throw new ApiError(corpo?.mensagem || 'Erro ao processar a requisição.', corpo?.campos || {});
    }
    return corpo;
}

const api = {
    listar: (busca, status) => {
        const params = new URLSearchParams();
        if (busca) params.set('busca', busca);
        if (status) params.set('status', status);
        const query = params.toString();
        return requisicao(query ? `?${query}` : '');
    },
    consultar: (id) => requisicao(`/${id}`),
    criar: (dados) => requisicao('', { method: 'POST', body: JSON.stringify(dados) }),
    atualizar: (id, dados) => requisicao(`/${id}`, { method: 'PUT', body: JSON.stringify(dados) }),
    desativar: (id) => requisicao(`/${id}`, { method: 'DELETE' }),
    ativar: (id) => requisicao(`/${id}/ativar`, { method: 'PATCH' }),
};

// ---------------------------------------------------------------------------
// Formatacao e mascaras
// ---------------------------------------------------------------------------

const somenteDigitos = (valor) => (valor || '').replace(/\D/g, '');

function formatarCnpj(valor) {
    const d = somenteDigitos(valor).slice(0, 14);
    return d
        .replace(/^(\d{2})(\d)/, '$1.$2')
        .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
        .replace(/\.(\d{3})(\d)/, '.$1/$2')
        .replace(/(\d{4})(\d)/, '$1-$2');
}

function formatarTelefone(valor) {
    const d = somenteDigitos(valor).slice(0, 11);
    if (d.length <= 10) {
        return d
            .replace(/^(\d{2})(\d)/, '($1) $2')
            .replace(/(\d{4})(\d)/, '$1-$2');
    }
    return d
        .replace(/^(\d{2})(\d)/, '($1) $2')
        .replace(/(\d{5})(\d)/, '$1-$2');
}

function aplicarMascaras() {
    ['cnpj', 'editCnpj'].forEach((id) => {
        const input = document.getElementById(id);
        input.addEventListener('input', () => { input.value = formatarCnpj(input.value); });
    });
    ['telefone', 'editTelefone'].forEach((id) => {
        const input = document.getElementById(id);
        input.addEventListener('input', () => { input.value = formatarTelefone(input.value); });
    });
}

// ---------------------------------------------------------------------------
// Mensagens
// ---------------------------------------------------------------------------

let temporizadorMensagem;

function mostrarMensagem(texto, tipo = 'success') {
    const { mensagem } = elementos;
    mensagem.textContent = texto;
    mensagem.className = `alert alert-${tipo}`;
    clearTimeout(temporizadorMensagem);
    temporizadorMensagem = setTimeout(() => mensagem.classList.add('d-none'), 5000);
}

// ---------------------------------------------------------------------------
// Tabela
// ---------------------------------------------------------------------------

function criarCelula(texto, classe) {
    const td = document.createElement('td');
    td.textContent = texto;
    if (classe) td.className = classe;
    return td;
}

function criarLinha(instituicao) {
    const tr = document.createElement('tr');

    tr.append(
        criarCelula(instituicao.id, 'cell-id'),
        criarCelula(instituicao.nomeFantasia, 'cell-nome'),
        criarCelula(formatarCnpj(instituicao.cnpj)),
        criarCelula(instituicao.endereco),
        criarCelula(formatarTelefone(instituicao.telefone)),
    );

    const tdStatus = document.createElement('td');
    const badge = document.createElement('span');
    badge.className = `badge-status badge-status--${instituicao.status.toLowerCase()}`;
    badge.textContent = STATUS_LABEL[instituicao.status];
    tdStatus.append(badge);

    const tdAcoes = document.createElement('td');
    const btnEditar = document.createElement('button');
    btnEditar.type = 'button';
    btnEditar.className = 'btn btn-sm btn-outline-primary btn-com-icone';
    btnEditar.title = 'Editar';
    btnEditar.dataset.id = instituicao.id;
    btnEditar.innerHTML = '<i class="bi bi-pen"></i> Editar';
    tdAcoes.append(btnEditar);

    tr.append(tdStatus, tdAcoes);
    return tr;
}

function renderizarLinhaUnica(texto) {
    const tr = document.createElement('tr');
    const td = document.createElement('td');
    td.colSpan = 7;
    td.className = 'text-center text-muted py-4';
    td.textContent = texto;
    tr.append(td);
    elementos.corpoTabela.replaceChildren(tr);
}

async function carregarInstituicoes() {
    const busca = elementos.busca.value.trim();
    const status = elementos.situacao.value.toUpperCase();

    renderizarLinhaUnica('Carregando...');
    try {
        const instituicoes = await api.listar(busca, status);
        if (instituicoes.length === 0) {
            renderizarLinhaUnica('Nenhuma instituição encontrada.');
            return;
        }
        elementos.corpoTabela.replaceChildren(...instituicoes.map(criarLinha));
    } catch (erro) {
        renderizarLinhaUnica('Não foi possível carregar as instituições.');
        mostrarMensagem(erro.message, 'danger');
    }
}

// ---------------------------------------------------------------------------
// Formularios (validacao e envio)
// ---------------------------------------------------------------------------

function lerFormulario(mapaCampos) {
    const dados = {};
    Object.entries(mapaCampos).forEach(([campo, id]) => {
        dados[campo] = document.getElementById(id).value.trim();
    });
    return dados;
}

function preencherFormulario(mapaCampos, instituicao) {
    Object.entries(mapaCampos).forEach(([campo, id]) => {
        document.getElementById(id).value = instituicao[campo] ?? '';
    });
    document.getElementById(mapaCampos.cnpj).value = formatarCnpj(instituicao.cnpj);
    document.getElementById(mapaCampos.telefone).value = formatarTelefone(instituicao.telefone);
}

function limparErros(mapaCampos) {
    Object.values(mapaCampos).forEach((id) => {
        document.getElementById(id).classList.remove('is-invalid');
    });
}

function marcarErro(id, mensagem) {
    const input = document.getElementById(id);
    input.classList.add('is-invalid');
    const feedback = document.getElementById(`erro-${id}`);
    if (feedback && mensagem) feedback.textContent = mensagem;
}

/** Validacao no navegador; a API valida novamente (inclusive digitos do CNPJ). */
function validarDados(dados) {
    const erros = {};
    if (dados.nomeFantasia.length < 3) erros.nomeFantasia = 'Informe o nome fantasia (mínimo 3 caracteres).';
    if (dados.razaoSocial.length < 3) erros.razaoSocial = 'Informe a razão social (mínimo 3 caracteres).';
    if (somenteDigitos(dados.cnpj).length !== 14) erros.cnpj = 'CNPJ inválido. Use o formato 00.000.000/0000-00.';
    const tel = somenteDigitos(dados.telefone).length;
    if (tel < 10 || tel > 11) erros.telefone = 'Informe um telefone válido com DDD.';
    if (!dados.endereco) erros.endereco = 'Informe o endereço completo.';
    return erros;
}

function exibirErros(mapaCampos, erros) {
    Object.entries(erros).forEach(([campo, mensagem]) => {
        if (mapaCampos[campo]) marcarErro(mapaCampos[campo], mensagem);
    });
}

async function enviarFormulario({ mapaCampos, botao, acao, modal, sucesso }) {
    limparErros(mapaCampos);
    const dados = lerFormulario(mapaCampos);

    const erros = validarDados(dados);
    if (Object.keys(erros).length > 0) {
        exibirErros(mapaCampos, erros);
        return;
    }

    botao.disabled = true;
    try {
        await acao(dados);
        bootstrap.Modal.getOrCreateInstance(modal).hide();
        mostrarMensagem(sucesso);
        await carregarInstituicoes();
    } catch (erro) {
        if (erro.campos && Object.keys(erro.campos).length > 0) {
            exibirErros(mapaCampos, erro.campos);
        } else {
            mostrarMensagem(erro.message, 'danger');
        }
    } finally {
        botao.disabled = false;
    }
}

// ---------------------------------------------------------------------------
// Modal de edicao
// ---------------------------------------------------------------------------

function atualizarSituacaoNoModal(instituicao) {
    const ativa = instituicao.status === 'ATIVA';
    elementos.editStatusDisplay.textContent = STATUS_LABEL[instituicao.status];
    elementos.btnDesativar.classList.toggle('d-none', !ativa);
    elementos.btnReativar.classList.toggle('d-none', ativa);
    elementos.textoSituacao.textContent = ativa
        ? 'Ao desativar, a instituição continua cadastrada e passa a constar como Inativa.'
        : 'Instituição inativa. Ao reativar, ela volta a constar como Ativa.';
    bootstrap.Collapse.getOrCreateInstance(elementos.confirmarDesativacao, { toggle: false }).hide();
}

async function abrirEdicao(id) {
    try {
        const instituicao = await api.consultar(id);
        limparErros(CAMPOS_EDICAO);
        elementos.editId.value = instituicao.id;
        elementos.editIdDisplay.textContent = instituicao.id;
        elementos.desativarNome.textContent = instituicao.nomeFantasia;
        preencherFormulario(CAMPOS_EDICAO, instituicao);
        atualizarSituacaoNoModal(instituicao);
        bootstrap.Modal.getOrCreateInstance(elementos.modalEdicao).show();
    } catch (erro) {
        mostrarMensagem(erro.message, 'danger');
    }
}

async function alterarSituacao(acao, mensagemSucesso) {
    const id = elementos.editId.value;
    try {
        await acao(id);
        const instituicao = await api.consultar(id);
        atualizarSituacaoNoModal(instituicao);
        mostrarMensagem(mensagemSucesso);
        await carregarInstituicoes();
    } catch (erro) {
        mostrarMensagem(erro.message, 'danger');
    }
}

// ---------------------------------------------------------------------------
// Eventos
// ---------------------------------------------------------------------------

function registrarEventos() {
    elementos.formBusca.addEventListener('submit', (evento) => {
        evento.preventDefault();
        carregarInstituicoes();
    });

    // O reset limpa os campos depois do evento; por isso a recarga vai para a fila.
    elementos.formBusca.addEventListener('reset', () => setTimeout(carregarInstituicoes));

    elementos.modalCadastro.addEventListener('show.bs.modal', () => {
        elementos.formCadastro.reset();
        limparErros(CAMPOS_CADASTRO);
    });

    elementos.formCadastro.addEventListener('submit', (evento) => {
        evento.preventDefault();
        enviarFormulario({
            mapaCampos: CAMPOS_CADASTRO,
            botao: elementos.btnSalvarCadastro,
            acao: (dados) => api.criar(dados),
            modal: elementos.modalCadastro,
            sucesso: 'Instituição cadastrada com sucesso.',
        });
    });

    elementos.formEdicao.addEventListener('submit', (evento) => {
        evento.preventDefault();
        enviarFormulario({
            mapaCampos: CAMPOS_EDICAO,
            botao: elementos.btnSalvarEdicao,
            acao: (dados) => api.atualizar(elementos.editId.value, dados),
            modal: elementos.modalEdicao,
            sucesso: 'Instituição atualizada com sucesso.',
        });
    });

    elementos.corpoTabela.addEventListener('click', (evento) => {
        const botao = evento.target.closest('button[data-id]');
        if (botao) abrirEdicao(botao.dataset.id);
    });

    elementos.btnConfirmarDesativacao.addEventListener('click', () =>
        alterarSituacao(api.desativar, 'Instituição desativada com sucesso.'));

    elementos.btnReativar.addEventListener('click', () =>
        alterarSituacao(api.ativar, 'Instituição reativada com sucesso.'));
}

aplicarMascaras();
registrarEventos();
carregarInstituicoes();
