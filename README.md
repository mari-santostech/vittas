# VITTAS 2.0 – CRUD de Instituições

Integração completa **MySQL ↔ Spring Boot ↔ HTML/CSS/JS (Bootstrap)**.

```
database/   scripts SQL (estrutura + teste do CRUD)          -> VITTAS-51
backend/    API REST Java 21 / Spring Boot 4                 -> VITTAS-52 / VITTAS-53
front/      tela de gerenciamento de instituições            -> VITTAS-55
```

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
