# Slotly — Sistema de agendamentos

Projeto acadêmico de uma aplicação de agendamentos para pequenos negócios e profissionais independentes, como barbeiros, manicures, mecânicos e personal trainers. A proposta é centralizar a agenda e reduzir o trabalho de gerenciar horários por mensagens.

## Organização do repositório

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

A API possui rotas para verificar a inicialização, listar todos os agendamentos e criar um agendamento. O frontend contém a estrutura inicial em React e uma tela de apresentação; a integração com a API ainda está pendente.

Os requisitos previstos para a aplicação são:

- **RF1 — Cadastro:** registrar agendamentos com cliente, data, horário e serviço.
- **RF2 — Consulta:** consultar os agendamentos de um dia. A listagem atual da API ainda não filtra por data.
- **RF3 — Status:** cancelar ou atualizar o estado de um agendamento. As rotas de atualização ainda não foram implementadas.

## Tecnologias

| Parte | Tecnologias |
| --- | --- |
| Frontend | React, TypeScript, Vite e ESLint |
| Backend | Java 21, Spring Boot, Spring Data JPA e Maven |
| Banco de dados | PostgreSQL no perfil padrão; configuração H2 no perfil `dev` |

## Execução local

Requisitos: JDK 21, Maven 3.6.3 ou superior, Node.js compatível com o Vite instalado e npm. Para usar o perfil padrão do backend, também é necessário PostgreSQL. Consulte os READMEs específicos para os detalhes e limitações atuais de configuração.

Clone o repositório:

```bash
git clone https://github.com/gustavostolze/uniessa-agendamento.git
cd uniessa-agendamento
```

Em um terminal, configure o banco conforme o [README do backend](back/README.md) e execute:

```bash
cd back
mvn spring-boot:run
```

Em outro terminal, a partir da raiz:

```bash
cd client
npm ci
npm run dev
```

A API utiliza `http://localhost:8080`. O Vite normalmente disponibiliza o frontend em `http://localhost:5173`; confira a URL exibida no terminal. Os dois processos são executados separadamente.

## Integrantes

- Gustavo Stolze de Andrade
- Erick Henrique Borges Moura
- Lucas Nunes Lino
- Felipe Carlos Santos
