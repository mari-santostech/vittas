# VITTAS 2.0 — Plataforma de Integração e Acesso à Saúde
Link Jira https://vittas.atlassian.net/jira/software/c/projects/VITTAS/boards/4/backlog

## Sobre o Projeto
O **VITTAS** é uma plataforma digital de integração, acesso e acompanhamento de serviços de saúde, com foco no ecossistema privado e suplementar. 

O objetivo do projeto é conectar pacientes, instituições de saúde (clínicas, hospitais e pronto-atendimentos particulares), profissionais e operadoras de planos de saúde, centralizando informações sobre disponibilidade e oferta de serviços. 

O VITTAS atua como uma camada intermediária de acesso, sem o objetivo de substituir os sistemas de gestão utilizados pelas instituições participantes.

---

## Público-Alvo do MVP
* **Pacientes:** Usuários que pesquisam serviços de saúde, realizam e acompanham agendamentos, e consultam receitas disponibilizadas.
* **Instituições de Saúde:** Clínicas, hospitais e unidades de pronto-atendimento privadas.
* **Administradores:** Responsáveis pelo gerenciamento da plataforma, instituições, permissões e usuários.

---

## Principais Funcionalidades (Escopo do MVP)

* **Busca e Consulta de Serviços:** Pesquisa de instituições, profissionais, especialidades, convênios aceitos e horários por localização ou necessidade.
* **Agendamento e Agenda:** Realização, solicitação, reagendamento e cancelamento de consultas/serviços pelo paciente, além da gestão de horários e bloqueios pela instituição.
* **Acompanhamento de Atendimentos:** Visualização do status dos agendamentos e históricos de atendimentos realizados.
* **Consulta de Receitas:** Visualização e histórico de receitas médicas disponibilizadas por instituições ou sistemas integrados (sem emissão ou prescrição direta no VITTAS).
* **Notificações:** Avisos sobre confirmação, cancelamento, alteração de horário e lembretes via e-mail e notificações internas.
* **Portal da Instituição & Painel Administrativo:** Áreas exclusivas para gestão cadastral, profissionais, agendas, permissões e operação do sistema.
* **Camada de Interoperabilidade:** API REST para intercâmbio de dados entre o VITTAS e sistemas externos/hospitalares.

---

## O que NÃO faz parte do MVP (Fora de Escopo)
Para manter o limite de desenvolvimento planejado (aprox. 1.000h), os seguintes recursos **não** estão incluídos nesta versão inicial:
* **Gestão Clínica:** Prontuário eletrônico completo, emissão/alteração de receitas, laudos ou prescrições.
* **Inteligência Artificial Clínica:** Nenhuma IA para diagnóstico, triagem, prescrição ou decisão médica.
* **Telemedicina:** Sem módulo de vídeo chamadas ou gestão de teleconsultas próprias.
* **Operações de Emergência:** Rastreamento ou roteamento de ambulâncias e integração em tempo real com equipes de resgate.
* **Financeiro:** Cobranças, faturamento, processamento de convênios ou pagamento online.

---

## Integração e Arquitetura
O sistema é composto por:
* Frontend Web
* Backend & API REST
* Banco de Dados
* Módulos específicos: Paciente, Instituição e Administração
* Camada de Integração de dados externos

**VITTAS**
# VITTAS 2.0 – CRUD de Instituições

## Como executar

### 1. Banco de dados (MySQL 8)
Execute `database/estrutura_vittas.sql` (cria o banco `vittas_db` e a tabela `instituicao`).
Opcional: `database/teste_crud_instituicao.sql` testa o CRUD direto no banco.

### 2. Backend
Requer JDK 21. Usuário e senha do banco vêm de variáveis de ambiente (padrão: `root` sem senha):

```powershell
cd backend
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "sua_senha"
.\mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080/api/instituicoes`.
Testes: `.\mvnw.cmd test` — 16 testes (unitários do service e do validador de CNPJ +
integração HTTP → JPA → banco). Não precisam do MySQL: os de integração usam H2 em
memória no modo MySQL, configurado só em `src/test/resources`.

### 3. Frontend
Sirva a pasta `front/` na porta **5500** (ex.: extensão *Live Server* do VS Code) e abra
`http://localhost:5500`. A URL da API fica na constante `API_URL` em `front/script.js`.
Para usar outra porta, inclua a origem em `vittas.cors.allowed-origins`
(`backend/src/main/resources/application.properties`) ou na variável `CORS_ORIGINS`.

## API

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/instituicoes?busca=&status=` | Lista; `busca` filtra por nome fantasia, razão social ou CNPJ; `status` = `ATIVA` ou `INATIVA` |
| GET | `/api/instituicoes/{id}` | Consulta uma instituição |
| POST | `/api/instituicoes` | Cadastra (201 + `Location`) |
| PUT | `/api/instituicoes/{id}` | Atualiza |
| DELETE | `/api/instituicoes/{id}` | **Exclusão lógica**: marca como `INATIVA` (204) |
| PATCH | `/api/instituicoes/{id}/ativar` | Reativa |

Corpo de POST/PUT:

```json
{
  "nomeFantasia": "Clinica Teste VITTAS",
  "razaoSocial": "Clinica Teste VITTAS Ltda",
  "cnpj": "11.222.333/0001-81",
  "telefone": "(44) 99999-0000",
  "endereco": "Rua Exemplo, 100 - Maringa/PR"
}
```

Erros seguem o formato `{ "codigo", "mensagem", "campos": { "<campo>": "<mensagem>" } }`
(400 validação/CNPJ inválido, 404 não encontrada, 409 CNPJ duplicado).

## Regras adotadas
- CNPJ e telefone são gravados **somente com dígitos**; a máscara é aplicada no front.
- O CNPJ é validado pelos dígitos verificadores e é único.
- Todos os campos do formulário são obrigatórios (telefone com DDD, 10 ou 11 dígitos).
- Status é um enum (`ATIVA`/`INATIVA`); excluir = desativar, conforme o texto da tela.
