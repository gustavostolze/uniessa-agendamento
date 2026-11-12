# Slotly — Backend

API REST do sistema de agendamentos, desenvolvida com Java 21, Spring Boot e Spring Data JPA. O código organiza clientes, profissionais, serviços e agendamentos.

[Visão geral do projeto](../README.md) · [Documentação do frontend](../client/README.md)

## Requisitos

- JDK 21.
- Maven 3.6.3 ou superior instalado e disponível como `mvn`.
- PostgreSQL para executar com o perfil padrão.

Os scripts `mvnw` e `mvnw.cmd` estão nesta pasta, mas a pasta `.mvn/wrapper/` não está presente na estrutura atual. Portanto, use o Maven instalado até que os arquivos do wrapper sejam restaurados.

## Configuração do banco

O arquivo `src/main/resources/application.properties` ativa o perfil `dev` e define a porta `8080`.

### Perfil padrão: `dev` (PostgreSQL)

Apesar do nome, o perfil `dev` está configurado em `application-dev.properties` para PostgreSQL:

| Configuração | Valor atual |
| --- | --- |
| URL JDBC | `jdbc:postgresql://localhost:5432/bancoDadosSistemaAgendamento` |
| Usuário | `postgres` |
| Senha local | `postgres` |
| Hibernate | `ddl-auto=update` |

Crie o banco correspondente no PostgreSQL antes de iniciar a API. Para usar outra instância ou credenciais, sobrescreva as propriedades com variáveis de ambiente. Exemplo no Linux/macOS:

```bash
export SPRING_DATASOURCE_URL='jdbc:postgresql://localhost:5432/bancoDadosSistemaAgendamento'
export SPRING_DATASOURCE_USERNAME='seu_usuario'
read -rs -p 'Senha do banco: ' SPRING_DATASOURCE_PASSWORD
export SPRING_DATASOURCE_PASSWORD
```

As credenciais presentes no arquivo são valores locais de desenvolvimento. Use variáveis de ambiente para credenciais reais. O Spring Boot não carrega arquivos `.env` automaticamente; as variáveis devem estar disponíveis no processo ou configuradas na IDE.

### Perfil `prod` (configuração H2)

Atualmente, `application-prod.properties` contém a configuração de H2 em memória, console em `/h2-console` e execução de `schema.sql` e `data.sql`. Esse perfil é de demonstração e não representa uma configuração adequada de produção: os dados em memória são descartados ao encerrar o processo.

O `pom.xml` atual não declara explicitamente a dependência do banco `com.h2database:h2`. Antes de utilizar esse perfil, é necessário garantir a presença do driver H2 no classpath. A dependência de console, isoladamente, não substitui o driver do banco.

## Execução

A partir da raiz do repositório:

```bash
cd back
mvn spring-boot:run
```

A API utiliza `http://localhost:8080`. Para verificar a inicialização:

```bash
curl http://localhost:8080/teste
```

Na IDE, importe `back/pom.xml` como projeto Maven, selecione o JDK 21 e execute `AgendamentoApplication`, em `src/main/java/com/uniessa/agendamento/`.

## Rotas disponíveis

| Método | Rota | Comportamento |
| --- | --- | --- |
| `GET` | `/teste` | Retorna uma mensagem de verificação |
| `GET` | `/agendamentos` | Lista todos os agendamentos, sem filtro por dia |
| `POST` | `/agendamentos` | Salva um agendamento e retorna HTTP 201 |

O cadastro recebe um objeto `Agendamento` no corpo JSON. Quando o status não é informado, o serviço atribui `PENDENTE`. Os estados definidos são `PENDENTE`, `CONFIRMADO`, `CANCELADO` e `CONCLUIDO`.

Ainda não existem rotas para atualizar status ou cancelar agendamentos. O console H2 só está configurado no perfil `prod`, sujeito à disponibilidade do driver descrita acima.

## Estrutura

```text
back/
├── src/main/java/com/uniessa/agendamento/
│   ├── controller/     # Rotas HTTP
│   ├── model/          # Entidades e enum de status
│   ├── repository/     # Acesso a dados com Spring Data JPA
│   ├── service/        # Regras de negócio
│   └── AgendamentoApplication.java
├── src/main/resources/
│   ├── application*.properties
│   ├── schema.sql
│   └── data.sql
├── src/test/           # Testes do backend
└── pom.xml
```

## Testes e build

Dentro de `back/`:

```bash
mvn test
mvn verify
```

Os testes atuais carregam o contexto Spring e usam a configuração de banco ativa; mantenha o PostgreSQL configurado e acessível ao executá-los. Os artefatos de build são gerados em `target/`, ignorado pelo Git.
