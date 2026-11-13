# Slotly — Sistema de agendamentos

Projeto acadêmico de uma aplicação de agendamentos para pequenos negócios e profissionais independentes, como barbeiros, manicures, mecânicos e personal trainers. A proposta é centralizar a agenda e reduzir o trabalho de gerenciar horários por mensagens.

## Integrantes do grupo
- Gustavo Stolze de Andrade
- Erick Henrique Borges Moura
- Lucas Nunes Lino
- Felipe Carlos Santos

## 📝 O Problema & Público-Alvo
Pequenos prestadores de serviços (como barbeiros, manicures, mecânicos ou personal trainers) perdem muito tempo gerenciando marcações manualmente por mensagens, lidando frequentemente com conflitos de horários e esquecimentos por parte dos clientes.

```text
uniessa-agendamento/
├── client/     # Interface web com React, TypeScript e Vite
├── back/       # API REST com Java, Spring Boot e Spring Data JPA
├── .gitignore
└── README.md
```

A documentação de instalação, configuração e desenvolvimento de cada parte está em:

- [Frontend](client/README.md)
- [Backend](back/README.md)

## Estado atual

Interface integrada para criação e listagem/recarga em `/agendamento`. Edição de registros não está implementada. Consulte a documentação de integração e ambientes em `docs/`.

## Tecnologias

| Parte | Tecnologias |
| --- | --- |
| Frontend | React, TypeScript, Vite e ESLint |
| Backend | Java 21, Spring Boot, Spring Data JPA e Maven |
| Banco de dados | H2 em memória no perfil padrão `dev`; PostgreSQL em `homolog` e `prod` |

## Execução local

Requisitos: JDK 21, Maven 3.6.3 ou superior, Node.js compatível com o Vite instalado e npm. Para homologação e produção, também é necessário PostgreSQL. Consulte os READMEs específicos para os detalhes e limitações atuais de configuração.

Clone o repositório:

```bash
git clone https://github.com/gustavostolze/uniessa-agendamento.git
cd uniessa-agendamento
```

Em um terminal, configure o banco conforme o [README do backend](back/README.md) e execute:

```bash
cd back
./mvnw spring-boot:run
```

Em outro terminal, a partir da raiz:

```bash
cd client
npm ci
cp .env.development.example .env.development.local
npm run dev
```

A API utiliza `http://localhost:8080`. O Vite normalmente disponibiliza o frontend em `http://localhost:5173`; confira a URL exibida no terminal. Os dois processos são executados separadamente.

## Integrantes

- Gustavo Stolze de Andrade
- Erick Henrique Borges Moura
- Lucas Nunes Lino
- Felipe Carlos Santos

Documentação: [Integração](docs/integracao.md) · [Ambientes e CORS](docs/ambientes-e-cors.md).
