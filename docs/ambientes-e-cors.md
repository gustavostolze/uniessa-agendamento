# Ambientes e CORS

O navegador chama a API; o Spring não precisa servir os arquivos React. Precisa de uma URL alcançável, contrato HTTP compatível, banco acessível e permissão CORS para a origem exata do frontend. Em HTTPS, a API também deve usar HTTPS para evitar bloqueio de conteúdo misto.

| Ambiente | Perfil Spring | Modo Vite | Banco | Origem autorizada |
| --- | --- | --- | --- | --- |
| Local | dev (padrão) | development | H2 em memória, descartável | http://localhost:5173 |
| Homologação | homolog | homolog | PostgreSQL exclusivo de testes | domínio real do frontend de homologação |
| Produção | prod | production | PostgreSQL exclusivo de produção | domínio real do frontend de produção |

## Execução local

Em `back/`: `./mvnw spring-boot:run`. Em `client/`: copie `.env.development.example` para `.env.development.local`, execute `npm ci` e `npm run dev`. O banco local é recriado pelo script de desenvolvimento: use IDs 1 ou 2 para cliente e profissional. Nunca ative `dev` em banco persistente.

## Homologação e produção

No backend, injete no processo (IDE, terminal, container ou serviço de hospedagem):

```sh
export SPRING_PROFILES_ACTIVE=homolog
export DB_URL=jdbc:postgresql://seu-host:5432/agendamento_homolog
export DB_USERNAME=usuario_do_ambiente
# Injete DB_PASSWORD pelo gerenciador de segredos da hospedagem.
export CORS_ALLOWED_ORIGINS=https://app-homolog.example.com
java -jar target/agendamento-0.0.1-SNAPSHOT.jar
```

Os domínios `.example.com` são ilustrativos e precisam ser substituídos. Para produção, use `SPRING_PROFILES_ACTIVE=prod`, banco/usuário/senha próprios e `CORS_ALLOWED_ORIGINS=https://app.example.com`. Não ative os perfis juntos. As variáveis são obrigatórias e o Spring lê seu valor na inicialização; reinicie após mudanças. Um `.env` isolado não é carregado automaticamente pelo Java.

Ambos os perfis persistentes usam `ddl-auto=validate`: o banco precisa existir antes da aplicação. Em banco vazio, o administrador pode executar uma vez [schema-postgresql.sql](schema-postgresql.sql), sem DROP e sem dados fictícios. Para bancos existentes, revise e aplique uma migração apropriada; não execute scripts às cegas. Cadastre clientes e profissionais previamente. O Spring não cria nem altera tabelas automaticamente nesses perfis. O acesso de rede ao PostgreSQL e as credenciais também precisam estar liberados. Use TLS do banco conforme a infraestrutura.

No frontend, copie `.env.homolog.example` para `.env.homolog.local` e substitua a URL. Execute `npm run build:homolog`. Para produção, use `.env.production.example` → `.env.production.local` e `npm run build:prod`. Publique o conteúdo de `client/dist` em hospedagem estática HTTPS; cada build substitui esse diretório, portanto guarde os artefatos separadamente. Alternativamente, forneça `VITE_API_BASE_URL` no processo de build via CI.

Variáveis Vite são incorporadas no build: alterar a variável depois de publicar não muda o JavaScript. Gere e publique um novo build. Elas são públicas; nenhuma senha deve ter prefixo `VITE_`. O endereço é a base da API, sem `/agendamento`. A origem CORS é a URL do FRONTEND, sem caminho ou barra final. Front homolog deve apontar à API homolog, e a API homolog deve permitir o front homolog; repita o mesmo par em produção. Não permita homologação em produção por conveniência.

## Funcionamento do CORS

`CorsConfig` implementa `WebMvcConfigurer` e configura `/agendamento`. `allowedOrigins` recebe a lista `CORS_ALLOWED_ORIGINS`, separada por vírgulas; `allowedMethods` limita GET/POST/OPTIONS, `allowedHeaders` permite JSON e `allowCredentials(false)` mantém cookies fora do fluxo. Não são necessários `@CrossOrigin` por controller nem `@EnableWebMvc`.

Origem é protocolo + host + porta. `localhost`, `127.0.0.1`, HTTP, HTTPS e portas diferentes são origens diferentes. Para POST JSON, o navegador pode enviar OPTIONS (preflight) antes da operação; o Spring responde se a origem, o método e os cabeçalhos forem autorizados. O GET com origem também precisa de autorização na resposta. O servidor trata preflight automaticamente, sem controller OPTIONS.

Para conferir, execute com a URL e a origem reais:

```sh
curl -i -X OPTIONS http://localhost:8080/agendamento \
  -H 'Origin: http://localhost:5173' \
  -H 'Access-Control-Request-Method: POST' \
  -H 'Access-Control-Request-Headers: content-type'
```

Confira `Access-Control-Allow-Origin` e depois teste os botões no navegador, na aba Network. Repita com origem não autorizada: não deve receber permissão. Um curl bem-sucedido sem Origin não prova CORS. Se houver proxy, permita OPTIONS e preserve os cabeçalhos; mantenha apenas uma camada responsável por CORS para evitar respostas duplicadas. Confira DNS, certificado e alcance da API antes de atribuir toda falha a CORS. Se Spring Security for adicionado, sua cadeia de filtros também deverá integrar CORS antes da autenticação.

CORS não autentica nem impede clientes fora do navegador. A aplicação ainda não possui autenticação/autorização: mantenha o acesso controlado e dados fictícios até implementar essa proteção para uso público real. Cookies futuros exigirão revisão de credenciais, origens e CSRF. Console H2 e carga inicial estão desabilitados nos ambientes persistentes.

Referências: [Spring MVC CORS](https://docs.spring.io/spring-framework/reference/web/webmvc-cors.html) e [Vite: modos e variáveis](https://vite.dev/guide/env-and-mode).
