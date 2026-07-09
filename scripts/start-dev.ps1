#Requires -Version 5.1

$ErrorActionPreference = "Stop"

$RootDir = Split-Path -Parent $PSScriptRoot
$EnvFile = Join-Path $RootDir ".env"
$EnvExample = Join-Path $RootDir ".env.example"

function Write-Step {
    param([string]$Message)
    Write-Host "`n==> $Message" -ForegroundColor Cyan
}

function Import-EnvFile {
    param([string]$Path)

    Get-Content $Path | ForEach-Object {
        $line = $_.Trim()
        if ($line -eq "" -or $line.StartsWith("#")) {
            return
        }

        $parts = $line -split "=", 2
        if ($parts.Count -ne 2) {
            return
        }

        $name = $parts[0].Trim()
        $value = $parts[1].Trim()
        Set-Item -Path "Env:$name" -Value $value
    }
}

Write-Step "Verificando arquivo .env"

if (-not (Test-Path $EnvFile)) {
    Write-Host "Arquivo .env nao encontrado em: $EnvFile" -ForegroundColor Yellow
    Write-Host "Copie o template e defina suas credenciais locais:" -ForegroundColor Yellow
    Write-Host "  Copy-Item .env.example .env" -ForegroundColor White
    exit 1
}

Import-EnvFile -Path $EnvFile

if ($env:DB_PASSWORD -eq "defina_uma_senha_local") {
    Write-Host "Atualize DB_PASSWORD e POSTGRES_PASSWORD no arquivo .env antes de continuar." -ForegroundColor Yellow
    exit 1
}

Write-Step "Subindo PostgreSQL com Docker"
Push-Location $RootDir
try {
    docker compose up -d
    docker compose ps
}
finally {
    Pop-Location
}

Write-Step "Variaveis de ambiente carregadas para o Spring Boot"
Write-Host "DB_HOST=$($env:DB_HOST)"
Write-Host "DB_NAME=$($env:DB_NAME)"
Write-Host "DB_USER=$($env:DB_USER)"
Write-Host "DB_PASSWORD=********"

Write-Step "Proximos passos"
Write-Host "1. Em outro terminal, rode o back-end:" -ForegroundColor White
Write-Host "   cd screenmatch-back-end" -ForegroundColor Gray
Write-Host "   .\mvnw.cmd spring-boot:run" -ForegroundColor Gray
Write-Host ""
Write-Host "2. Abra o front-end com Live Server (index.html)" -ForegroundColor White
Write-Host ""
Write-Host "3. Teste a API:" -ForegroundColor White
Write-Host "   http://localhost:8080/series" -ForegroundColor Gray
Write-Host ""
Write-Host "Dica: execute '. .\scripts\load-env.ps1' antes do mvnw para carregar o .env no terminal." -ForegroundColor DarkGray
