# Attorney App

Plataforma web inovadora desenvolvida para conectar **Jurisdicionados (Clientes)** a **Advogados Especialistas**, simplificando o entendimento de termos técnico-jurídicos e humanizando a contratação de serviços advocatícios.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Funcionalidades Principais](#funcionalidades-principais)
- [Stack Tecnológica](#stack-tecnológica)
- [Requisitos de Sistema](#requisitos-de-sistema)
- [Como Rodar o Projeto](#como-rodar-o-projeto)
  - [Opção 1: Modo Padrão (H2 Database - Recomendado para Desenvolvimento)](#opção-1-modo-padrão-h2-database---recomendado-para-desenvolvimento)
  - [Opção 2: Modo PostgreSQL](#opção-2-modo-postgresql)
- [Usuários de Demonstração (Seed)](#usuários-de-demonstração-seed)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [Comandos Úteis de Build e Testes](#comandos-úteis-de-build-e-testes)

---

## Visão Geral

O **Attorney** tem como missão resolver uma das maiores dores de quem necessita de assistência legal: a barreira da linguagem jurídica e a dificuldade em encontrar advogados qualificados e transparentes.

A plataforma oferece:
1. **Conexão Direta:** Clientes encontram advogados por especialidade e localização, podendo iniciar contato e formalizar uma Relação de Prestação de Serviços (RPS).
2. **Glossário Jurídico Descomplicado:** Consulta rápida e busca por termos técnicos frequentemente utilizados em despachos, sentenças e andamentos processuais.
3. **Avaliações com Notas (0 a 10):** Clientes avaliam os serviços prestados, fomentando integridade, qualidade e transparência na advocacia.
4. **Integração com CNA OAB:** Verificação rápida da regularidade do registro profissional junto ao Cadastro Nacional dos Advogados.

---

## Funcionalidades Principais

- **Autenticação e Perfis Distintos:** Cadastro e login segregados para Clientes e Advogados.
- **Busca por Especialidade:** Filtro dinâmico na Home por áreas de atuação (*Direito do Trabalho, Civil, Família, Consumidor, Penal, etc.*).
- **Validação de Inscrição na OAB:** Redirecionamento e consulta ao Cadastro Nacional de Advogados (CNA).
- **Glossário Jurídico Integrado:** Carga automática com mais de 115 termos jurídicos explicados de forma clara, com mecanismo de busca por palavra-chave.
- **Canal de Contato & Mensagens:** Troca de mensagens entre cliente e advogado para alinhamento inicial.
- **RPS (Relação de Prestação de Serviços):** Registro formal de contratação com detalhamento de serviços e acompanhamento de status.
- **Sistema de Avaliações:** Notas de 0 a 10 com feedbacks públicos no perfil do profissional.
- **Upload de Foto de Perfil:** Personalização de foto para clientes e advogados.

---

## Stack Tecnológica

O projeto foi construído utilizando as seguintes tecnologias:

### Backend
- **Java 25** (OpenJDK / Temurin)
- **Spring Boot 4.1.1**
  - **Spring Web MVC:** Arquitetura model-view-controller para rotas e controle de requisições.
  - **Spring Data JPA:** Camada de persistência e repositórios automatizados.
  - **Hibernate ORM:** Mapeamento objeto-relacional com geração automática de tabelas (`ddl-auto=update`).
  - **Spring Validation:** Validação de formulários e integridade de dados (Bean Validation).

### Frontend (Server-Side Rendering)
- **Thymeleaf:** Motor de templates acoplado ao Spring Boot para renderização dinâmica no servidor.
- **HTML5 & CSS3:** Layout semântico, moderno, responsivo e adaptado para múltiplos dispositivos.
- **JavaScript Vanilla:** Scripts leves para interações dinâmicas e validações em tela.

### Banco de Dados
- **H2 Database:** Banco de dados relacional embarcado com persistência local em arquivo (`./data/attorneydb`), configurado como padrão para desenvolvimento ágil.
- **PostgreSQL:** Suporte completo com driver `org.postgresql.Driver` e profile dedicado (`postgres`).

### Build & Gerenciamento de Dependências
- **Apache Maven** (acompanha **Maven Wrapper** `mvnw` e `mvnw.cmd`).

---

## Requisitos de Sistema

Antes de iniciar, certifique-se de que sua máquina possui:

1. **JDK 25 (Java Development Kit)** instalado e configurado nas variáveis de ambiente (`JAVA_HOME` e `PATH`).
   - Para verificar no terminal:
     ```bash
     java -version
     ```
2. **Git** (opcional, para clonar o repositório).
3. *(Opcional)* **PostgreSQL 14+** ou **Docker**, apenas se optar por rodar com banco PostgreSQL.

> **Nota:** Não é necessário ter o Maven instalado globalmente, pois o projeto já inclui o **Maven Wrapper** (`mvnw` / `mvnw.cmd`).

---

## Como Rodar o Projeto

Abra o terminal na pasta raiz do projeto (`c:\Workspace\attorney`):

### Opção 1: Modo Padrão (H2 Database - Recomendado para Desenvolvimento)

Esta opção não exige nenhuma instalação de banco de dados externo. O banco criará automaticamente um arquivo de dados persistido na pasta `./data/attorneydb`.

#### No Windows (PowerShell ou Prompt de Comando):
```powershell
.\mvnw.cmd spring-boot:run
```

#### No Linux ou macOS:
```bash
./mvnw spring-boot:run
```

Ao iniciar, você verá a mensagem indicando que o Tomcat subiu na porta **8080**:
```text
Tomcat started on port 8080 (http) with context path '/'
Started AttorneyApplication in ... seconds
```

#### Endereços de Acesso:
- **Aplicação Web:** [http://localhost:8080/](http://localhost:8080/)
- **Console do Banco H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - **JDBC URL:** `jdbc:h2:file:./data/attorneydb`
  - **User Name:** `sa`
  - **Password:** *(deixe em branco)*

---

### Opção 2: Modo PostgreSQL

Caso queira utilizar o banco relacional PostgreSQL:

#### 1. Iniciar o PostgreSQL e Criar o Banco
Se tiver o PostgreSQL instalado localmente ou via Docker, crie a base de dados:
```sql
CREATE DATABASE attorney_db;
```

*(Ou via Docker CLI)*:
```bash
docker run --name attorney-postgres -e POSTGRES_DB=attorney_db -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
```

#### 2. Ajustar as Credenciais (se necessário)
As configurações do perfil PostgreSQL ficam em `src/main/resources/application-postgres.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/attorney_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
```

#### 3. Executar o Projeto com o Perfil `postgres`
Execute o Maven ativando o perfil do Spring:

- **Windows:**
  ```powershell
  .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
  ```
- **Linux/macOS:**
  ```bash
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
  ```

---

## Usuários de Demonstração (Seed)

Na primeira execução, o sistema executa o `DataInitializer` e popula a base automaticamente com dados de teste para facilitar a avaliação do sistema:

| Tipo | Nome | E-mail | Senha | Detalhes |
| :--- | :--- | :--- | :--- | :--- |
| **Cliente** | Carlos Eduardo Silva | `cliente@attorney.com` | `123456` | Possui conversas e RPS ativa |
| **Cliente** | Mariana Souza Ramos | `mariana@email.com` | `123456` | Avaliou advogados |
| **Advogado** | Dra. Camila Alencar | `advogado@attorney.com` | `123456` | OAB/SP 412.390 (Direito do Trabalho) |
| **Advogado** | Dr. Roberto Guimarães | `roberto@attorney.com` | `123456` | OAB/RJ 198.420 (Direito Civil) |
| **Advogado** | Dra. Beatriz Fontana | `beatriz@attorney.com` | `123456` | OAB/MG 305.112 (Direito de Família) |
| **Advogado** | Dr. Marcelo Siqueira | `marcelo@attorney.com` | `123456` | OAB/SP 289.771 (Direito do Consumidor) |
| **Advogado** | Dr. Fernando Nogueira | `fernando@attorney.com` | `123456` | OAB/DF 154.982 (Direito Penal) |

---

## Estrutura de Pastas

```text
attorney/
├── docs/                           # Documentação e especificações de requisitos
│   ├── spec-inicial.txt            # Escopo inicial e regras de negócio
│   └── glossario-juridico-bruto.txt # Base de termos jurídicos populada
├── src/
│   ├── main/
│   │   ├── java/com/attorney/
│   │   │   ├── AttorneyApplication.java
│   │   │   ├── config/             # Configurações Web MVC e beans
│   │   │   ├── controller/         # Controllers de Home, Auth, Advogado, Cliente, Chat, Glossário
│   │   │   ├── dto/                # Objetos de transferência de dados (formulários e registros)
│   │   │   ├── model/              # Entidades JPA (Cliente, Advogado, RPS, Avaliação, Termos, etc.)
│   │   │   ├── repository/         # Interfaces Spring Data JPA
│   │   │   └── service/            # Regras de negócio e inicializadores de dados
│   │   └── resources/
│   │       ├── application.properties           # Configuração padrão (H2)
│   │       ├── application-postgres.properties  # Configuração perfil PostgreSQL
│   │       ├── static/                          # Arquivos estáticos (CSS, imagens, JS)
│   │       └── templates/                       # Telas Thymeleaf (HTML)
│   └── test/                                    # Testes automatizados
├── uploads/                        # Diretório local para uploads (fotos de perfil)
├── mvnw / mvnw.cmd                 # Maven Wrapper (Linux/Windows)
└── pom.xml                         # Descritor Maven e dependências
```

---

## Comandos Úteis de Build e Testes

- **Executar Testes Automatizados:**
  ```powershell
  .\mvnw.cmd test
  ```

- **Limpar e Empacotar o Projeto (.jar):**
  ```powershell
  .\mvnw.cmd clean package
  ```

- **Executar o arquivo JAR gerado:**
  ```powershell
  java -jar target/attorney-0.0.1-SNAPSHOT.jar
  ```
