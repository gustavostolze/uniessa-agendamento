# 🚀 Slotly - SaaS de Agendamentos Simples

O **Slotly** é um sistema simplificado de agendamentos no modelo SaaS (Software as a Service) projetado para resolver problemas de gestão de horários de pequenos negócios e profissionais independentes.

## 📝 O Problema & Público-Alvo
Pequenos prestadores de serviços (como barbeiros, manicures, mecânicos ou personal trainers) perdem muito tempo gerenciando marcações manualmente por mensagens, lidando frequentemente com conflitos de horários e esquecimentos por parte dos clientes.

O foco do projeto é oferecer a esses profissionais independentes uma agenda digital ágil, intuitiva e centralizada.

## ⚙️ Requisitos Funcionais
*   **RF1 (Cadastro):** O sistema deve permitir que o profissional cadastre um novo agendamento, informando o nome do cliente, data, horário e serviço.
*   **RF2 (Consulta):** O sistema deve permitir consultar (listar) todos os agendamentos marcados para um determinado dia *(Endpoint GET obrigatório)*.
*   **RF3 (Cancelamento/Status):** O sistema deve permitir que o profissional cancele ou altere o estado de um agendamento (ex: de "Pendente" para "Concluído").

## 🛠️ Tecnologias Utilizadas

### Backend
*   **Java 21**
*   **Spring Boot** (Web MVC & Data JPA)
*   **Banco de Dados H2** (Em memória para desenvolvimento)
*   **Maven** (Gerenciador de dependências)

### Frontend
*   **A ser decidido

---

## 📋 Requisitos para Execução

Antes de começar, certifique-se de ter instalado em sua máquina:
1. **Java Development Kit (JDK) 21** ou superior.
2. **Maven 3.x** (opcional, caso prefira rodar via terminal externo, ou use o wrapper incluso `./mvnw`).
3. Uma IDE de sua escolha (recomendado **IntelliJ IDEA**).

---

## 🚀 Como Rodar o Projeto

### 1. Clonar o Repositório
```bash
git clone https://github.com/gustavostolze/uniessa-agendamento.git
```

### 2. Executar pelo IntelliJ (Método Recomendado)
1. Abra o IntelliJ IDEA.
2. Vá em `File > Open` e selecione a pasta raiz do projeto.
3. Aguarde o download das dependências do Maven terminar.
4. Localize o arquivo `AgendamentoApplication.java` dentro de `src/main/java/com/uniessa/agendamento/`.
5. Clique no botão **Run (▶️)** ou use o atalho `Ctrl + Shift + F10`.

### 3. Executar via Linha de Comando (Terminal)
Caso prefira rodar diretamente pelo terminal na raiz do projeto, execute o wrapper do Maven:

**No Windows:**
```bash
mvnw.cmd spring-boot:run
```

**No Linux/macOS:**
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

A aplicação iniciará por padrão no endereço: **`http://localhost:8080`**

---

## 🔍 Endpoints Iniciais para Teste
*   **Verificação de Inicialização:** `GET http://localhost:8080/teste`
*   **Console do Banco de Dados H2:** `http://localhost:8080/h2-console` *(Acesso em memória para validação das tabelas)*
*   <img width="713" height="790" alt="image" src="https://github.com/user-attachments/assets/049a3d20-96b6-4583-b21a-427409088d40" />
