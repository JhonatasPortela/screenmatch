# Guia de Ambiente — PC de Casa e Trabalho

Este manual descreve como configurar e rodar o **ScreenMatch** em qualquer máquina (casa ou trabalho) usando **Docker para o PostgreSQL**, sem instalar o banco diretamente no Windows.

---

## Pré-requisitos

Instale uma única vez em cada máquina:

| Ferramenta | Versão mínima | Para quê |
|------------|---------------|----------|
| [Git](https://git-scm.com/) | qualquer recente | Clonar e sincronizar o código |
| [JDK](https://adoptium.net/) | 17 | Compilar e rodar o back-end |
| [Docker Desktop](https://www.docker.com/products/docker-desktop/) | recente | Subir o PostgreSQL em container |
| [VS Code](https://code.visualstudio.com/) | recente | Editar código e rodar o front-end |
| Extensão **Live Server** | — | Servir o front-end estático |

> **Maven não é obrigatório.** O projeto inclui o Maven Wrapper (`mvnw.cmd`).

---

## Primeira configuração (faça em cada PC)

### 1. Clonar o repositório

```powershell
git clone <url-do-seu-repositorio>
cd screenmatch-full-stack
```

### 2. Criar o arquivo `.env` local

```powershell
Copy-Item .env.example .env
```

Abra o `.env` e defina uma senha local:

```env
POSTGRES_PASSWORD=sua_senha_aqui
DB_PASSWORD=sua_senha_aqui
```

> **Importante:** `POSTGRES_PASSWORD` e `DB_PASSWORD` devem ser **iguais**.  
> O arquivo `.env` **nunca** vai para o Git — ele já está no `.gitignore`.

### 3. Subir o banco de dados

```powershell
docker compose up -d
```

Verifique se o container está saudável:

```powershell
docker compose ps
```

Saída esperada: status `healthy` ou `running` no serviço `postgres`.

---

## Rotina diária de desenvolvimento

### Passo 1 — Abrir o Docker Desktop

Certifique-se de que o Docker está rodando antes de subir o banco.

### Passo 2 — Subir o PostgreSQL

Na raiz do projeto:

```powershell
docker compose up -d
```

Ou use o script auxiliar:

```powershell
.\scripts\start-dev.ps1
```

### Passo 3 — Carregar variáveis e rodar o back-end

Em um terminal:

```powershell
cd screenmatch-back-end
. ..\scripts\load-env.ps1
.\mvnw.cmd spring-boot:run
```

Aguarde a mensagem:

```
Tomcat started on port 8080 (http) with context path '/'
```

Teste no navegador: [http://localhost:8080/series](http://localhost:8080/series)

### Passo 4 — Rodar o front-end

1. Abra a pasta `screenmatch-front-end` no VS Code
2. Clique com o botão direito em `index.html`
3. Escolha **Open with Live Server**

O front-end abrirá em uma porta como `5500`, `5501` ou `5502`.

---

## Sincronizando casa ↔ trabalho

### O que vai pelo Git (seguro)

- Código Java e front-end
- `docker-compose.yml`
- `.env.example` (template **sem** senha real)

### O que fica só na sua máquina (nunca commitar)

- `.env` (senhas e credenciais)
- Volume Docker com dados do banco (`screenmatch_pg_data`)

### Fluxo recomendado

```text
Casa                          Trabalho
  │                               │
  ├─ git push ──────────────────► ├─ git pull
  ├─ .env (local)                 ├─ .env (local, criado uma vez)
  └─ docker compose up -d         └─ docker compose up -d
```

Cada máquina terá **seu próprio banco local**. Isso é normal para estudo — o Spring cria as tabelas automaticamente com `ddl-auto=update`.

Se quiser copiar dados de casa para o trabalho (opcional):

```powershell
# Exportar (casa)
docker exec screenmatch-postgres pg_dump -U screenmatch portela_series > backup.sql

# Importar (trabalho)
Get-Content backup.sql | docker exec -i screenmatch-postgres psql -U screenmatch -d portela_series
```

---

## Comandos Docker úteis

```powershell
# Subir o banco
docker compose up -d

# Parar o banco
docker compose down

# Parar e apagar todos os dados (banco zerado)
docker compose down -v

# Ver logs do PostgreSQL
docker compose logs -f postgres

# Entrar no banco via terminal
docker exec -it screenmatch-postgres psql -U screenmatch -d portela_series
```

---

## Solução de problemas

### Porta 5432 já em uso

Se você tiver PostgreSQL instalado localmente, pode haver conflito. No `.env`, altere:

```env
POSTGRES_PORT=5433
DB_HOST=localhost:5433
```

Depois reinicie o container:

```powershell
docker compose down
docker compose up -d
```

### Erro de conexão com o banco

1. Confirme que o Docker está rodando
2. Confirme que `DB_USER`, `DB_PASSWORD` e `DB_NAME` batem com `POSTGRES_*` no `.env`
3. Rode `docker compose ps` e verifique se o container está `healthy`

### Front-end não carrega dados (erro de CORS)

O back-end aceita requisições apenas da origem configurada em `CorsConfiguration.java`.  
Se o Live Server abrir em porta diferente de `5501`, ajuste o `allowedOrigins` para a porta correta (ex.: `http://127.0.0.1:5500`).

### Variáveis de ambiente não carregadas

Sempre execute `. ..\scripts\load-env.ps1` **no mesmo terminal** antes de rodar o `mvnw.cmd`.  
Sem isso, o Spring não encontra `DB_HOST`, `DB_USER`, etc.

### `mvn` não reconhecido

Use o wrapper do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

---

## Checklist rápido — primeiro boot no PC novo

- [ ] Git instalado
- [ ] JDK 17 instalado
- [ ] Docker Desktop instalado e rodando
- [ ] Repositório clonado
- [ ] `.env` criado a partir de `.env.example`
- [ ] Senhas definidas no `.env`
- [ ] `docker compose up -d` executado com sucesso
- [ ] Back-end sobe em `http://localhost:8080/series`
- [ ] Front-end aberto com Live Server
- [ ] CORS ajustado se a porta do Live Server for diferente

---

## Segurança

- **Nunca** commite o arquivo `.env`
- **Nunca** coloque senhas no `application.properties`
- Use senhas diferentes em ambientes reais de produção
- O `.env.example` contém apenas placeholders — isso é seguro para o Git
