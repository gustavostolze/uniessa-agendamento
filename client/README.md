# Slotly — Frontend

Interface web do sistema de agendamentos, desenvolvida com React, TypeScript e Vite. Atualmente, a aplicação exibe uma tela inicial com o título do sistema; as telas de agendamento e a integração com o backend ainda serão desenvolvidas.

[Visão geral do projeto](../README.md) · [Documentação do backend](../back/README.md)

## Requisitos e instalação

Instale Node.js compatível com a versão do Vite declarada em `package.json` e npm. Prefira uma versão LTS recente do Node.js e confira eventuais avisos de compatibilidade ao instalar as dependências.

A partir da raiz do repositório:

```bash
cd client
npm ci
npm run dev
```

Abra a URL exibida pelo Vite no terminal, normalmente `http://localhost:5173`. Alterações no código são refletidas automaticamente durante o desenvolvimento.

O `package-lock.json` deve permanecer versionado para reproduzir a instalação das dependências.

## Comandos

Execute os comandos dentro de `client/`:

| Comando | Finalidade |
| --- | --- |
| `npm run dev` | Iniciar o servidor de desenvolvimento |
| `npm run build` | Verificar os tipos com TypeScript e gerar o build em `dist/` |
| `npm run lint` | Analisar o código com ESLint |
| `npm run preview` | Servir localmente o build já gerado |

Para visualizar o build:

```bash
npm run build
npm run preview
```

## Estrutura

```text
client/
├── public/             # Arquivos estáticos
├── src/
│   ├── assets/         # Imagens e outros recursos importados
│   ├── App.tsx         # Componente principal
│   ├── App.css         # Estilos do componente principal
│   ├── index.css       # Estilos globais
│   └── main.tsx        # Inicialização do React
├── eslint.config.js
├── package.json
├── package-lock.json
├── tsconfig*.json
└── vite.config.ts
```

## Integração com a API

O backend utiliza `http://localhost:8080`. Ainda não há chamadas HTTP, URL de API configurada por variável de ambiente ou proxy no `vite.config.ts`.

Ao implementar a integração, será necessário configurar um proxy de desenvolvimento no Vite ou permitir a origem do frontend no backend via CORS. Arquivos `.env` locais são ignorados pelo Git; variáveis expostas ao navegador nunca devem conter senhas ou outros segredos.
