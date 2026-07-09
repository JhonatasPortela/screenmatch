# ScreenMatch

Aplicação full stack para consulta e catalogação de séries, desenvolvida no curso **Java Web** da Alura. O back-end expõe uma API REST com Spring Boot; o front-end consome esses dados em uma interface web estática.

---

## Visão geral

| Camada | Tecnologia | Porta padrão |
|--------|------------|--------------|
| Front-end | HTML, CSS, JavaScript (ES Modules) | Live Server (`5500`–`5502`) |
| Back-end | Java 17, Spring Boot 4, Spring Data JPA | `8080` |
| Banco de dados | PostgreSQL 16 (Docker) | `5432` |

---

## Estrutura do projeto

```text
screenmatch-full-stack/
├── docker-compose.yml          # PostgreSQL via Docker
├── .env.example                # Template de variáveis (seguro para o Git)
├── scripts/
│   ├── start-dev.ps1           # Sobe o banco e orienta os próximos passos
│   └── load-env.ps1            # Carrega variáveis do .env no terminal
├── docs/
│   └── AMBIENTE-TRABALHO.md    # Manual completo (casa e trabalho)
├── screenmatch-back-end/       # API REST — Spring Boot
│   ├── src/main/java/.../
│   │   ├── controller/         # Endpoints HTTP
│   │   ├── service/            # Regras e integrações
│   │   ├── repository/         # Acesso ao banco (JPA)
│   │   ├── model/              # Entidades JPA
│   │   ├── dto/                # Objetos de transferência
│   │   └── config/             # Configurações (CORS, etc.)
│   └── src/main/resources/
│       └── application.properties
└── screenmatch-front-end/      # Interface web estática
    ├── index.html
    ├── detalhes.html
    └── scripts/
```

---

## Pré-requisitos

- **Git**
- **JDK 17+**
- **Docker Desktop**
- **VS Code** com extensão **Live Server**

> Maven não precisa ser instalado — o projeto usa o Maven Wrapper (`mvnw.cmd`).

---

## Início rápido

### 1. Clonar e configurar variáveis

```powershell
git clone <url-do-repositorio>
cd screenmatch-full-stack
Copy-Item .env.example .env
```

Edite o `.env` e defina senhas locais (veja `.env.example` para referência).

### 2. Subir o banco de dados

```powershell
docker compose up -d
```

### 3. Rodar o back-end

```powershell
cd screenmatch-back-end
. ..\scripts\load-env.ps1
.\mvnw.cmd spring-boot:run
```

API disponível em: [http://localhost:8080/series](http://localhost:8080/series)

### 4. Rodar o front-end

Abra `screenmatch-front-end/index.html` com **Live Server** no VS Code.

---

## Variáveis de ambiente

O back-end lê credenciais do banco via variáveis de ambiente — **nunca** hardcoded no código.

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `DB_HOST` | Host do PostgreSQL | `localhost` |
| `DB_NAME` | Nome do banco | `portela_series` |
| `DB_USER` | Usuário do banco | `screenmatch` |
| `DB_PASSWORD` | Senha do banco | *(definida no `.env` local)* |

O Docker Compose usa variáveis complementares (`POSTGRES_*`) definidas no mesmo arquivo `.env`.

> O arquivo `.env` é ignorado pelo Git. Apenas `.env.example` (template) é versionado.

---

## API

### Endpoints disponíveis

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/series` | Lista todas as séries cadastradas |

**Exemplo de resposta:**

```json
[
  {
    "id": 1,
    "titulo": "Breaking Bad",
    "totalTemporadas": 5,
    "avaliacao": 9.5,
    "genero": "DRAMA",
    "atores": "Bryan Cranston, Aaron Paul",
    "poster": "https://...",
    "sinopse": "..."
  }
]
```

---

## Scripts auxiliares

| Script | Uso |
|--------|-----|
| `scripts/start-dev.ps1` | Sobe o PostgreSQL via Docker e exibe os próximos passos |
| `scripts/load-env.ps1` | Carrega as variáveis do `.env` no terminal atual |

```powershell
# Exemplo de uso completo
.\scripts\start-dev.ps1
cd screenmatch-back-end
. ..\scripts\load-env.ps1
.\mvnw.cmd spring-boot:run
```

---

## Trabalho em múltiplas máquinas

Para configurar o ambiente no PC do trabalho (ou em qualquer outra máquina), siga o manual detalhado:

**[docs/AMBIENTE-TRABALHO.md](docs/AMBIENTE-TRABALHO.md)**

Resumo: clone o repo, crie o `.env` local, rode `docker compose up -d` e pronto — sem instalar PostgreSQL manualmente.

---

## Solução de problemas

| Problema | Solução |
|----------|---------|
| Porta `5432` em uso | Altere `POSTGRES_PORT=5433` e `DB_HOST=localhost:5433` no `.env` |
| Erro de CORS no front-end | Ajuste `allowedOrigins` em `CorsConfiguration.java` para a porta do Live Server |
| Spring não conecta ao banco | Execute `. ..\scripts\load-env.ps1` antes do `mvnw.cmd` |
| Banco vazio | Normal em máquina nova — tabelas são criadas automaticamente pelo JPA |

---

## Segurança

- Credenciais ficam **apenas** no `.env` local
- `.env` está no `.gitignore` — não será enviado ao GitHub
- `.env.example` contém placeholders seguros para servir de template
- Em produção, use secrets managers — nunca commite senhas

---

## Licença

Projeto educacional desenvolvido no contexto dos cursos da [Alura](https://www.alura.com.br/).

Front-end original: [Monica Hillman](https://cursos.alura.com.br/user/monicahillman)
