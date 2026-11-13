# Integração implementada

React chama `AgendamentoService.ts`, que centraliza Fetch, timeout de 10 segundos, JSON e erros. O controller Spring valida DTOs; o serviço resolve os IDs, aplica estado PENDENTE e persiste em transação; a resposta DTO evita expor entidades e telefones.

- `POST /agendamento`: `data`, `horario`, `clienteId`, `profissionalId`; retorna 201.
- `GET /agendamento`: lista ordenada por ID; retorna 200 e array.
- Data: `2026-11-20`; horário local de São Paulo: `2026-11-20T14:30:00`. Data e horário precisam corresponder.
- Falhas: 400 para entrada inválida, 404 para relacionamento ausente, 500 com mensagem pública para falha interna.

A rota anterior `/agendamentos` foi substituída conforme aprovação. Atualizar significa recarregar GET, sem edição de registros. Cliente e profissional precisam existir; no perfil local, IDs 1 e 2 são fictícios. Se POST sofrer timeout, consulte a lista antes de reenviar: a gravação pode ter ocorrido.

Verificação: `cd client && npm run build && npm run lint`; `cd back && ./mvnw verify`. Os testes de integração usam Spring MVC e H2, cobrindo criação/listagem, validação e origens CORS permitidas/rejeitadas. Para validação visual, inicie ambos os sistemas, crie um registro e recarregue a lista no navegador. Consulte [ambientes e CORS](ambientes-e-cors.md) para configurar cada implantação.
