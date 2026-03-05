# TodoList

Um gerenciador de tarefas robusto e escalável construído com Spring Boot, oferecendo autenticação segura e gerenciamento eficiente de tarefas através de uma API RESTful.

## Descrição do Projeto

TodoList é uma aplicação backend que fornece uma API RESTful para gerenciar tarefas e usuários. O projeto implementa um sistema completo de autenticação básica com hashing de senhas usando BCrypt, validação de datas, e isolamento de dados por usuário.

## Problema que o Projeto Resolve

A aplicação endereça os seguintes desafios:

- **Gerenciamento centralizado de tarefas**: Permite que usuários criem, organizem e gerenciem suas tarefas de forma centralizada
- **Segurança de dados**: Implementa autenticação robusta com senhas criptografadas em BCrypt
- **Isolamento de dados**: Garante que cada usuário acesse apenas suas próprias tarefas
- **Validação de datas**: Previne a criação de tarefas com datas inválidas ou no passado
- **Persistência de dados**: Mantém os dados de forma segura em banco de dados MySQL

## Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 21 (LTS) | Linguagem de programação |
| Spring Boot | 4.0.0 | Framework web e injeção de dependência |
| MySQL | - | Banco de dados relacional |
| JPA/Hibernate | - | ORM para mapeamento objeto-relacional |
| Lombok | 1.18.42 | Redução de boilerplate (getters, setters, equals, etc) |
| BCrypt | 0.10.2 | Hash seguro de senhas |
| Maven | - | Gerenciador de dependências e build |
| Docker | - | Containerização da aplicação |

## Arquitetura do Projeto

A aplicação segue o padrão **MVC (Model-View-Controller)** com separação de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                    HTTP Requests                             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
    ┌────────────────────────────────────┐
    │    FilterTaskAuth (Autenticação)   │
    │   (Basic Auth + BCrypt Validation) │
    └────────────┬───────────────────────┘
                 │
                 ▼
  ┌──────────────────────────────────────────┐
  │         REST Controllers                 │
  │  ├─ TaskController                       │
  │  └─ UserController                       │
  └──────────────────┬───────────────────────┘
                     │
                     ▼
  ┌──────────────────────────────────────────┐
  │      Service Layer (Lógica Negócio)     │
  │  ├─ TaskService (Validações, Regras)   │
  │  └─ Operações diretas em Controllers   │
  └──────────────────┬───────────────────────┘
                     │
                     ▼
  ┌──────────────────────────────────────────┐
  │      Repository Layer (Persistência)     │
  │  ├─ ITaskRepository (extends JpaRepo)   │
  │  └─ IUserRepository (extends JpaRepo)   │
  └──────────────────┬───────────────────────┘
                     │
                     ▼
  ┌──────────────────────────────────────────┐
  │       ORM Layer (Hibernate/JPA)         │
  │    (Mapeamento Entity - Banco Dados)    │
  └──────────────────┬───────────────────────┘
                     │
                     ▼
  ┌──────────────────────────────────────────┐
  │         MySQL Database                   │
  │  ├─ Tabela: users                       │
  │  └─ Tabela: tasks                       │
  └──────────────────────────────────────────┘
```

### Fluxo de Requisição

1. **Requisição HTTP** chega ao servidor
2. **FilterTaskAuth** intercepta a requisição
   - Extrai credenciais do header `Authorization`
   - Valida usuário no banco de dados
   - Verifica senha com BCrypt
   - Adiciona `idUser` ao contexto da requisição
3. **Controller** recebe a requisição autenticada
   - Extrai parâmetros e body
   - Chama o serviço apropriado
4. **Service** executa lógica de negócio
   - Valida dados (ex: datas inválidas)
   - Chama repository para persistência
5. **Repository** acessa o banco de dados via JPA/Hibernate
6. **Resposta** é retornada ao cliente com status HTTP apropriado

## Estrutura de Pastas

```
todolist/
├── src/
│   ├── main/
│   │   ├── java/br/com/marcosporto/todolist/
│   │   │   ├── TodolistApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── TaskController.java      # Endpoints para gerenciar tarefas
│   │   │   │   └── UserController.java      # Endpoints para usuários
│   │   │   ├── model/
│   │   │   │   ├── TaskModel.java           # Entidade JPA de Tarefas
│   │   │   │   └── UserModel.java           # Entidade JPA de Usuários
│   │   │   ├── service/
│   │   │   │   └── TaskService.java         # Lógica de negócio de tarefas
│   │   │   ├── repository/
│   │   │   │   ├── ITaskRepository.java     # Interface de acesso a tarefas
│   │   │   │   └── IUserRepository.java     # Interface de acesso a usuários
│   │   │   ├── filter/
│   │   │   │   └── FilterTaskAuth.java      # Filtro de autenticação básica
│   │   │   ├── errors/
│   │   │   │   └── ExceptionHandlerController.java # Handler global de exceções
│   │   │   └── utils/
│   │   │       └── Utils.java               # Utilitários (ex: cópia de props)
│   │   └── resources/
│   │       ├── application.properties       # Configuração da aplicação
│   │       ├── static/                      # Arquivos estáticos (CSS, JS)
│   │       └── templates/                   # Templates (Thymeleaf, etc)
│   └── test/
│       └── java/...                         # Testes unitários
├── pom.xml                                  # Configuração Maven
├── mvnw / mvnw.cmd                          # Maven wrapper (Unix/Windows)
├── Dockerfile                               # Configuração Docker
├── HELP.md                                  # Documentação auxiliar
└── README.md                                # Este arquivo
```

### Responsabilidades de Cada Camada

| Camada | Responsabilidade | Arquivos |
|--------|-----------------|----------|
| **Controllers** | Receber requisições HTTP, validar entrada, chamar serviços, retornar respostas | `TaskController`, `UserController` |
| **Services** | Implementar lógica de negócio, validações, regras de acesso | `TaskService` |
| **Models** | Definir estrutura das entidades, mapeamento JPA, validações de campo | `TaskModel`, `UserModel` |
| **Repositories** | Abstrair acesso aos dados, queries customizadas | `ITaskRepository`, `IUserRepository` |
| **Filters** | Interceptar requisições para autenticação e autorização | `FilterTaskAuth` |
| **Error Handlers** | Tratar exceções de forma centralizada e retornar respostas apropriadas | `ExceptionHandlerController` |
| **Utils** | Fornecer funções utilitárias reutilizáveis | `Utils` |

## Pré-requisitos

Para executar este projeto localmente, você precisará ter instalado:

- **Java 21 (JDK)**: [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.8+**: [Download](https://maven.apache.org/download.cgi)
- **MySQL 8.0+**: [Download](https://www.mysql.com/downloads/)
- **Git**: [Download](https://git-scm.com/downloads)
- **Docker** (opcional): [Download](https://www.docker.com/products/docker-desktop)

### Verificar Instalação

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar MySQL
mysql --version
```

## Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/marcosporto/todolist.git
cd todolist
```

### 2. Configurar o Banco de Dados

Crie um banco de dados MySQL e atualize as credenciais em `src/main/resources/application.properties`:

```sql
-- Criar banco de dados
CREATE DATABASE todolist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Arquivo: `src/main/resources/application.properties`**

```properties
# Configuração do banco de dados
spring.datasource.url=jdbc:mysql://localhost:3306/todolist?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
spring.datasource.username=seu_usuario_mysql
spring.datasource.password=sua_senha_mysql
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=false
```

### 3. Instalar Dependências

```bash
mvn clean install
```

### 4. Executar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### 5. Alternativa - Executar com Dockerfile

```bash
# Build da imagem Docker
docker build -t todolist:1.0.0 .

# Executar container
docker run -p 8080:8080 --name todolist todolist:1.0.0
```

## Scripts Disponíveis

```bash
# Build do projeto
mvn clean install

# Executar aplicação em desenvolvimento (com auto-reload)
mvn spring-boot:run

# Rodar apenas testes unitários
mvn test

# Build empacotado (gera .jar)
mvn clean package

# Limpar artifacts gerados
mvn clean

# Verificar dependências
mvn dependency:tree
```

## Exemplos de Uso da Aplicação

### 1. Criar um Novo Usuário

**Requisição:**
```bash
curl -X POST http://localhost:8080/users/ \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao_silva",
    "name": "Joao Silva",
    "password": "senha123"
  }'
```

**Resposta (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "username": "joao_silva",
  "name": "Joao Silva",
  "password": "$2a$12$...(hash bcrypt)...",
  "createdAt": "2024-03-05T10:30:00"
}
```

### 2. Criar uma Tarefa (Autenticado)

**Requisição:**
```bash
curl -X POST http://localhost:8080/tasks/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic am9hb19zaWx2YTpzZW5oYTEyMw==" \
  -d '{
    "title": "Estudar Spring Boot",
    "description": "Aprender sobre MVC e REST",
    "startAt": "2024-03-10T09:00:00",
    "endAt": "2024-03-10T11:00:00",
    "priority": "ALTA"
  }'
```

**Nota:** O header `Authorization` deve ter o formato: `Basic base64(username:password)`

Para codificar credenciais em Base64:
```bash
# Linux/Mac
echo -n "joao_silva:senha123" | base64

# Windows (PowerShell)
[System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes("joao_silva:senha123"))
```

**Resposta (200 OK):**
```json
{
  "id": "660f9510-f40c-52e5-b827-557766551111",
  "title": "Estudar Spring Boot",
  "description": "Aprender sobre MVC e REST",
  "startAt": "2024-03-10T09:00:00",
  "endAt": "2024-03-10T11:00:00",
  "priority": "ALTA",
  "idUser": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2024-03-05T10:35:00"
}
```

### 3. Tratamento de Erros

**Requisição com data inválida:**
```bash
curl -X POST http://localhost:8080/tasks/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic am9hb19zaWx2YTpzZW5oYTEyMw==" \
  -d '{
    "title": "Tarefa no passado",
    "startAt": "2020-01-01T09:00:00",
    "endAt": "2020-01-01T11:00:00"
  }'
```

**Resposta (400 Bad Request):**
```json
"A data de início / término deve ser maior ou igual à data atual."
```

**Requisição sem autenticação:**
```bash
curl -X POST http://localhost:8080/tasks/ \
  -H "Content-Type: application/json" \
  -d '{"title": "Tarefa"}'
```

**Resposta (401 Unauthorized):**
Erro de autenticação - credenciais inválidas ou faltantes

## Endpoints da API

### Base URL
```
http://localhost:8080
```

### Endpoints de Usuários

#### 1. Criar Novo Usuário

```http
POST /users/
Content-Type: application/json

{
  "username": "string - único",
  "name": "string",
  "password": "string - será hashada com BCrypt"
}
```

**Status Codes:**
- `201 Created` - Usuário criado com sucesso
- `400 Bad Request` - Usuário já existe

---

### Endpoints de Tarefas

**Autenticação Required:** Todas as requisições de tarefas requerem autenticação Basic Auth

```http
Authorization: Basic base64(username:password)
```

#### 1. Criar Nova Tarefa

```http
POST /tasks/
Content-Type: application/json
Authorization: Basic <credentials>

{
  "title": "string - máximo 50 caracteres",
  "description": "string",
  "startAt": "ISO 8601 - 2024-03-10T09:00:00",
  "endAt": "ISO 8601 - 2024-03-10T11:00:00",
  "priority": "string - ALTA|MEDIA|BAIXA"
}
```

**Status Codes:**
- `200 OK` - Tarefa criada com sucesso
- `400 Bad Request` - Dados inválidos ou datas no passado
- `401 Unauthorized` - Autenticação falhou

**Validações aplicadas:**
- Data de início deve ser >= data atual
- Data de término deve ser >= data atual
- Data de início deve ser < data de término
- Título máximo 50 caracteres
- Usuário só pode acessar suas próprias tarefas

---

#### 2. Endpoints Comentados (Implementação Futura)

Os seguintes endpoints foram prototipados mas ainda não estão ativos:

```http
GET /tasks/        # Listar tarefas do usuário autenticado
PUT /tasks/{id}    # Atualizar tarefa específica
```

## Boas Práticas Utilizadas no Projeto

### 1. Separação de Responsabilidades
- Controllers focam em HTTP
- Services encapsulam lógica de negócio
- Repositories abstraem acesso aos dados
- Filters cuidam de segurança

### 2. Injeção de Dependência
```java
@Service
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;
}
```
Facilita testes unitários e desacoplamento de código

### 3. Tratamento Global de Exceções
```java
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
```
Garante respostas consistentes e centralizadas

### 4. Segurança de Senhas
- Uso de BCrypt com salt cost 12
- Senhas nunca são retornadas na resposta
- Validação com algoritmo seguro

### 5. Use de Lombok
- Reduz boilerplate com `@Data`
- Elimina getters, setters, equals, hashCode, toString

### 6. Validação de Entrada
```java
if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
    throw new IllegalArgumentException("Data início deve ser menor que término");
}
```

### 7. Status HTTP Apropriados
- `201 Created` para criação
- `400 Bad Request` para dados inválidos
- `401 Unauthorized` para falha de autenticação

### 8. Isolamento de Dados
- Cada usuário acessa apenas suas tarefas
- ID do usuário é extraído da autenticação, não do cliente

### 9. Timestamps Automáticos
```java
@CreationTimestamp
private LocalDateTime createdAt;
```
Hibernate popula automaticamente

### 10. Interfaces para Repositories
```java
public interface ITaskRepository extends JpaRepository<TaskModel, String> {
    List<TaskModel> findByIdUser(String idUser);
}
```
Permite implementações alternativas e facilita testes

## Possíveis Melhorias Futuras

### Curto Prazo
- [ ] Implementar GET, PUT, DELETE de tarefas
- [ ] Adicionar paginação em listagens
- [ ] Validação com Bean Validation (`@Valid`, `@NotNull`)
- [ ] Logging estruturado com SLF4J + Logback
- [ ] Testes unitários e integração abrangentes

### Médio Prazo
- [ ] Migrar de Basic Auth para JWT (JSON Web Tokens)
- [ ] Refresh tokens para melhor segurança
- [ ] Autorização baseada em roles (Admin, User)
- [ ] Soft delete em tarefas e usuários
- [ ] Auditoria (quem criou/modificou quando)
- [ ] Cache com Redis para otimização
- [ ] Documentação com Swagger/OpenAPI

### Longo Prazo
- [ ] Integração com OAuth 2.0 (Google, GitHub)
- [ ] Notificações em tempo real com WebSocket
- [ ] Anexos em tarefas
- [ ] Colaboração em tarefas compartilhadas
- [ ] Integração com sistemas externos (Slack, email)
- [ ] Analytics e dashboard de produtividade
- [ ] Backup e disaster recovery
- [ ] Arquitetura de microsserviços se escalar
- [ ] Implementar CQRS para queries complexas

### Otimizações Técnicas
- [ ] Índices de banco de dados otimizados
- [ ] Connection pooling (HikariCP)
- [ ] Implementar specifications JPA para queries dinâmicas
- [ ] Usar DTOs (Data Transfer Objects)
- [ ] Versionamento de API
- [ ] Rate limiting e throttling

## Autor

**Marcos Porto**

- GitHub: [@marcosporto](https://github.com/marcosporto)
- Email: contato@marcosporto.dev

---

## Licença

Este projeto está sob licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

## Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JPA/Hibernate Guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [BCrypt Algorithm](https://auth0.com/blog/hashing-in-action-understanding-bcrypt/)
- [REST API Best Practices](https://restfulapi.net/)
- [12 Factor App](https://12factor.net/)

---

**Última atualização:** 5 de março de 2025
