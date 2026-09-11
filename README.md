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
