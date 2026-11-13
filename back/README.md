# Backend de agendamentos

Java 21 e Spring Boot. Execute `./mvnw spring-boot:run`; verifique com `./mvnw verify`.

GET e POST em `/agendamento`. O perfil padrão `dev` usa H2 em memória e dados fictícios, descartados na reinicialização. Os perfis `homolog` e `prod` usam PostgreSQL por variáveis obrigatórias, sem recriação automática de tabelas.

Consulte [integração](../docs/integracao.md) para o contrato e [ambientes e CORS](../docs/ambientes-e-cors.md) para configuração, banco e implantação.
