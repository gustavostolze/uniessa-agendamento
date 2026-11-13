# Especificação — interface e integração de agendamentos

**Estado:** aprovado pelo solicitante; implementação realizada. Consulte `integracao.md` e `ambientes-e-cors.md` para operação e configurações.

**Dinâmica de trabalho:** nesta etapa será criado somente este documento. Após sua leitura e aprovação pelo solicitante, implementaremos o escopo aprovado e verificaremos os critérios de aceite. Alterações de escopo deverão ser registradas antes da implementação correspondente.

## 1. Objetivo e limites

Criar uma interface simples no frontend existente, com dois botões principais: **Criar agendamento** e **Listar/atualizar agendamentos**. As ações acessarão a camada de serviço do frontend, que fará as requisições HTTP ao backend.

Neste documento, “atualizar” significa buscar novamente a lista no servidor por meio de GET. Não significa editar um agendamento persistido. Edição, cancelamento, filtros, paginação, autenticação e gestão de clientes ou profissionais não integram esta entrega. Caso a intenção seja editar registros, o contrato e o escopo precisarão ser ampliados antes da implementação.

O objetivo de segurança desta entrega é estabelecer validação, exposição mínima de dados, controle de origens e tratamento de falhas. Isso não equivale a tornar uma API sem autenticação adequada para exposição pública com dados pessoais reais.

## 2. Diagnóstico do repositório

| Elemento | Situação observada | Consequência para a entrega |
| --- | --- | --- |
| Frontend | React, TypeScript e Vite em `client/`; tela inicial simples | Evoluir a aplicação existente |
| Serviço do frontend | `client/src/services/AgendamentoService.ts` existe e está vazio | Usar esse ponto para centralizar a integração |
| Backend | Spring Boot, Spring MVC e Spring Data JPA em `back/` | Preservar a organização controller → service → repository |
| Rotas | Controller expõe GET e POST em `/agendamentos` | Alinhar com `/agendamento`, solicitado nesta demanda |
| Entrada e saída | Controller recebe e retorna a entidade JPA | Introduzir contratos explícitos para evitar exposição e alteração indevida de campos |
| Modelo | Agendamento possui `id`, `data`, `horario`, `status`, `cliente` e `profissional` | Definir como os relacionamentos entram no contrato |
| Status | Serviço usa `PENDENTE` quando o status não é informado | Preservar o estado inicial no servidor |
| Configuração | API na porta 8080; perfil `dev` aponta para H2 | Verificar inicialização, driver e dados de demonstração antes da integração |

Os READMEs contêm informações divergentes da configuração atual de banco; a implementação deverá corrigir os trechos afetados com base nos arquivos efetivamente usados. A existência de um arquivo de serviço ainda não representa uma integração implementada.

## 3. Decisões arquiteturais

O fluxo será:

```text
Interface React
    → AgendamentoService (frontend)
    → cliente HTTP
    → controller Spring
    → service de negócio
    → repository JPA
    → banco de dados
```

Essa divisão aplica o princípio de separação de responsabilidades. A interface organiza a interação; o serviço do frontend conhece as operações da API; o controller traduz HTTP; o serviço do backend valida as regras; o repositório realiza a persistência. Assim, mudanças no endereço da API ou no formato dos erros não se espalham pelos componentes visuais.

Usaremos a **Fetch API nativa do navegador**, sem adicionar Axios nesta entrega. Ela atende às duas operações propostas e evita uma dependência adicional. Como Fetch não transforma automaticamente respostas HTTP 4xx/5xx em exceções, a integração verificará explicitamente `response.ok`, tratará o corpo e normalizará as falhas.

O caminho canônico proposto será **`/agendamento`**, conforme a demanda. A implementação alterará o mapeamento atual e as referências locais correspondentes. A aprovação desta especificação inclui essa alteração de contrato; consumidores externos de `/agendamentos`, caso existam, deverão ser identificados antes da troca. Não será criado um alias sem necessidade de compatibilidade identificada.

## 4. Interface e comportamento

### 4.1 Criar agendamento

O botão principal abrirá ou exibirá um formulário com data, horário, identificador do cliente e identificador do profissional. O formulário terá uma ação própria de confirmação; os dois botões solicitados são as ações principais da tela, não uma proibição de controles auxiliares.

Nesta entrega acadêmica, cliente e profissional serão informados por seus identificadores já cadastrados. O ambiente de demonstração deverá disponibilizar registros fictícios e documentar seus IDs. Isso permite integrar os relacionamentos sem criar novos endpoints de cadastro ou consulta. Um campo de serviço não será incluído, pois o modelo atual de agendamento não contém essa associação.

O frontend verificará campos obrigatórios, IDs inteiros positivos e coerência entre data e horário antes do envio. Durante a requisição, bloqueará nova submissão e apresentará carregamento. Em caso de sucesso, exibirá confirmação e o identificador retornado, limpará o formulário e recarregará a lista se ela estiver visível. Em caso de falha, preservará os valores para correção.

### 4.2 Listar/atualizar agendamentos

Cada acionamento fará um novo GET. A interface exibirá identificador, data, horário, status e IDs do cliente e do profissional em tabela ou lista legível. A primeira consulta poderá ser feita pelo próprio botão, sem carregamento automático obrigatório.

Serão representados separadamente os estados de carregamento, lista vazia, sucesso e erro. Se a atualização falhar e houver uma lista anterior, ela poderá continuar visível com aviso de que os dados não foram atualizados. Cliques concorrentes serão bloqueados ou a chamada anterior será cancelada, evitando que uma resposta antiga substitua a mais recente.

Os controles terão rótulos, navegação por teclado, foco visível e mensagens acessíveis. A apresentação funcionará em telas pequenas. Isso reduz erros de operação e torna o comportamento compreensível para diferentes usuários.

## 5. Contrato HTTP proposto

O contrato abaixo é uma proposta de implementação, não uma descrição do corpo atualmente aceito pelo controller.

| Operação | Método e rota | Resposta de sucesso |
| --- | --- | --- |
| Criar | `POST /agendamento` | `201 Created`, objeto criado |
| Consultar ou recarregar | `GET /agendamento` | `200 OK`, array de objetos; `[]` quando vazio |

POST utilizará `Content-Type: application/json`. As respostas terão representação JSON. GET não terá corpo nem enviará `Content-Type` desnecessariamente.

### 5.1 Entrada de criação

```json
{
  "data": "2026-11-20",
  "horario": "2026-11-20T14:30:00",
  "clienteId": 1,
  "profissionalId": 2
}
```

O cliente não poderá definir `id` nem `status` por esse contrato. O servidor gerará o identificador e atribuirá `PENDENTE`. Um DTO de entrada impedirá que a entidade de persistência seja usada como formulário aberto para alteração de propriedades internas.

`data` seguirá `YYYY-MM-DD`; `horario` seguirá uma data e hora ISO local, coerente com o `LocalDateTime` existente. Ambos representarão o horário civil do estabelecimento, adotando `America/Sao_Paulo` como referência funcional nesta entrega. Não converter esses valores implicitamente para UTC com `toISOString()`: o modelo atual não carrega offset. Suporte a múltiplos fusos exigirá uma revisão específica de contrato e persistência.

### 5.2 Saída

```json
{
  "id": 10,
  "data": "2026-11-20",
  "horario": "2026-11-20T14:30:00",
  "status": "PENDENTE",
  "clienteId": 1,
  "profissionalId": 2
}
```

GET retornará um array com esse formato, em ordem estável por identificador crescente. Os status reconhecidos serão `PENDENTE`, `CONFIRMADO`, `CANCELADO` e `CONCLUIDO`. DTOs de saída limitarão os dados expostos e evitarão serialização de relacionamentos JPA, ciclos entre entidades e divulgação de telefones ou outros atributos desnecessários.

### 5.3 Falhas

O backend retornará `400 Bad Request` para JSON inválido, campos ausentes, IDs inválidos ou divergência entre data e horário; `404 Not Found` quando cliente ou profissional informado não existir; e `500 Internal Server Error` para falhas inesperadas. O frontend também tratará indisponibilidade, timeout e respostas com formato inesperado.

Os erros terão estrutura estável, com código, mensagem pública e, quando aplicável, erros por campo. Um tratador central no backend converterá exceções nessa estrutura, sem retornar stack traces, SQL, credenciais ou detalhes internos. O frontend exibirá mensagens compreensíveis e não renderizará HTML vindo da API.

## 6. Etapas técnicas e justificativas

### Etapa 1 — Estabilizar contrato e ambiente

Alinhar a rota, criar tipos TypeScript e DTOs Java, verificar o banco configurado e disponibilizar dados fictícios para os relacionamentos. Ajustar somente as dependências necessárias, como validação e driver do perfil usado, caso ausentes.

**Por quê:** integração exige acordo sobre endereço, formatos, campos e significados. Tipos estáticos ajudam durante o desenvolvimento, mas não validam sozinhos o JSON recebido em execução; a integração também verificará a estrutura mínima das respostas.

### Etapa 2 — Validar e persistir no backend

Aplicar validação aos DTOs e regras no serviço: campos obrigatórios, IDs positivos, existência de cliente e profissional e correspondência de `data` com a parte de data de `horario`. Resolver os relacionamentos por consulta aos repositórios e persistir em transação. Criar o repositório de cliente caso necessário. O POST deverá sempre criar uma entidade nova, sem reaproveitar um ID enviado pelo navegador.

**Por quê:** o navegador é uma fronteira de confiança; suas validações podem ser contornadas. A validação autoritativa no servidor protege a integridade dos dados, enquanto a transação mantém a operação consistente. Restrições de banco complementam, sem substituir, essas verificações.

Bloqueio de horários sobrepostos, duração de atendimento e proibição de datas passadas não serão inventados nesta entrega: dependem de regras de negócio ainda não definidas. Desabilitar submissões repetidas reduz cliques duplicados, mas não garante idempotência de POST. Não haverá repetição automática de criação após falha de rede, pois o servidor pode já ter persistido o registro.

### Etapa 3 — Configurar CORS no backend

Centralizar a configuração Spring MVC para `/agendamento`, com origens explícitas por ambiente. No desenvolvimento, autorizar `http://localhost:5173`; se a porta mudar, atualizar a configuração. Liberar GET e POST e permitir o processamento de OPTIONS para o preflight, com o cabeçalho `Content-Type` necessário ao JSON. Manter credenciais desabilitadas, pois o fluxo não utiliza cookies de sessão.

**Por quê:** protocolo, host e porta compõem a origem. Frontend e backend locais estão em origens distintas; o navegador verifica a autorização de leitura da resposta e pode enviar um preflight antes do POST JSON. Autorizar apenas as origens necessárias reduz acesso por scripts de outras origens no navegador.

CORS não autentica usuários, não autoriza acesso a registros e não impede chamadas diretas por outros clientes. Não usar `mode: 'no-cors'`, curingas de origem ou reflexão irrestrita do cabeçalho `Origin` como solução. O teste deverá exercitar origens distintas de verdade, sem depender exclusivamente de proxy local.

### Etapa 4 — Implementar o serviço de integração no frontend

Criar uma configuração de URL base por `VITE_API_BASE_URL`, um cliente HTTP pequeno e as operações `criarAgendamento(dados)` e `listarAgendamentos()` no serviço existente. Centralizar construção de URLs, JSON, verificação de status, validação mínima de resposta, timeout de 10 segundos e normalização de erros. Distinguir erros HTTP de falhas de transporte; o navegador nem sempre permite distinguir CORS de outras falhas de rede.

**Por quê:** concentrar essas decisões elimina duplicação e permite testar a comunicação separadamente da interface. Variáveis `VITE_*` são públicas no bundle; conterão apenas endereço da API, nunca senha, token privado ou credencial de banco.

### Etapa 5 — Conectar a interface

Implementar formulário, botões principais, listagem e estados da seção 4. Os componentes chamarão somente o serviço do frontend para essas operações. Remover imports sem uso que impeçam a verificação do código alterado.

**Por quê:** desacoplar apresentação e transporte facilita manutenção e testes. Mensagens claras, validação imediata e bloqueio durante envio reduzem erros humanos sem substituir a proteção do servidor.

### Etapa 6 — Verificar e documentar

Executar build e lint do frontend, verificações Maven compatíveis com o ambiente e testes direcionados do contrato, validação e CORS. Realizar também um fluxo no navegador com ambos os sistemas ativos, criar um registro e confirmar sua presença após GET. Atualizar os READMEs com variáveis, origem permitida, dados de demonstração e execução.

**Por quê:** um cliente HTTP de terminal não aplica a política CORS do navegador; um teste isolado de backend não comprova a integração visual. As verificações se complementam e devem relatar dependências indisponíveis, sem apresentar uma execução parcial como validação completa.

## 7. Segurança e limites de operação

Para a demonstração, usar ambiente local/controlado e dados fictícios. Não incluir segredos no Git, no frontend, nas mensagens de erro ou nos logs. Em implantação fora da máquina local, utilizar HTTPS e configurar explicitamente a origem permitida e a URL pública da API.

A ausência de autenticação e autorização significa que qualquer cliente com acesso de rede à API poderá listar e criar registros. A entrega não deverá ser apresentada como pronta para uso público com dados reais. Antes desse uso, será necessária uma etapa própria de identificação de usuários, autorização por recurso, proteção contra abuso, política de dados pessoais e estratégia operacional de banco e recuperação. Caso se adotem cookies de autenticação, também será necessário revisar credenciais CORS e proteção CSRF.

Essas medidas futuras ficam registradas como condições para produção, sem ampliar silenciosamente a implementação acadêmica de dois fluxos. A configuração do console H2 e das credenciais de banco também deverá ser revisada antes de qualquer exposição pública.

## 8. Arquivos previstos para a implementação futura

| Área | Alterações previstas |
| --- | --- |
| `client/src/App.tsx` e estilos | Interface, formulário e estados de interação |
| `client/src/services/AgendamentoService.ts` | Operações de criação e consulta |
| `client/src/types/` e módulo HTTP/configuração | Contratos TypeScript, transporte e URL base |
| `client/.env.example` | Exemplo público de `VITE_API_BASE_URL` |
| Controller, service e repositories do backend | Rota canônica, regras e resolução dos relacionamentos |
| Pacotes de DTO, configuração e tratamento de erros | Contratos, validação, CORS e respostas de falha |
| Propriedades e `back/pom.xml`, se necessário | Origens por ambiente, validação e banco utilizado |
| Testes e READMEs | Evidências de integração e instruções reproduzíveis |

Os nomes dos novos módulos poderão ser ajustados à organização existente, preservando as responsabilidades descritas. Esta etapa documental não altera nenhum desses arquivos.

## 9. Critérios de aceite

1. A tela apresenta os dois botões principais e é utilizável por teclado e em tela pequena.
2. A criação passa pelo serviço do frontend e envia POST a `/agendamento`, com JSON conforme o contrato.
3. O backend retorna 201, gera o ID, define `PENDENTE` e persiste os relacionamentos existentes.
4. O botão de listagem faz GET a `/agendamento` e permite recarregar dados reais do backend.
5. Lista vazia, carregamento, falha HTTP, indisponibilidade e timeout têm feedback adequado; falha na recarga não apresenta dados antigos como atualizados.
6. Campos inválidos e relacionamentos inexistentes são rejeitados no servidor; o POST não permite escolher ID ou status.
7. O POST JSON funciona no navegador a partir da origem autorizada, incluindo preflight. Uma origem não autorizada não recebe permissão CORS de leitura.
8. A resposta contém somente os campos definidos, sem entidades relacionadas completas, stack traces ou segredos.
9. Não há Fetch nos componentes para essas operações, repetição automática de POST nem submissão concorrente pelo formulário.
10. Testes direcionados cobrem criação, consulta, validação e CORS; build/lint e verificação do backend são executados, com eventuais impedimentos registrados.
11. As instruções permitem iniciar os dois sistemas e reproduzir criação e listagem usando dados fictícios.
12. Nenhuma edição de agendamento será apresentada como implementada por uma operação GET.

## 10. Aprovação

A aprovação autorizará a implementação desta proposta, incluindo a rota singular `/agendamento`, o uso de Fetch, a atualização entendida como recarga da lista e o cadastro por IDs de cliente/profissional previamente existentes. Até essa aprovação, a entrega se limita a este `spec.md`.
