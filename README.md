# ObservaAção — Ouvidoria Cidadã Digital

> Sistema de ouvidoria cidadã desenvolvido em Java 17 puro, sem frameworks externos.

**AEP — Análise e Desenvolvimento de Sistemas · 1º Semestre 2026**  
Disciplinas: Interação Humano-Computador · Programação Orientada a Objetos · Manutenção de Software  
ODS relacionados: [ODS 10](https://brasil.un.org/pt-br/sdgs/10) · [ODS 11](https://brasil.un.org/pt-br/sdgs/11) · [ODS 16](https://brasil.un.org/pt-br/sdgs/16)

---

## Sumário

- [Contexto e Problema](#contexto-e-problema)
- [O que é o ObservaAção](#o-que-é-o-observaação)
- [Funcionalidades](#funcionalidades)
- [Regras de Negócio](#regras-de-negócio)
- [Arquitetura e Organização](#arquitetura-e-organização)
- [Princípios Aplicados](#princípios-aplicados)
- [Stack Tecnológica](#stack-tecnológica)
- [Como Executar](#como-executar)
- [Estrutura de Arquivos](#estrutura-de-arquivos)
- [Entregas](#entregas)

---

## Contexto e Problema

Em muitas cidades brasileiras, a população enfrenta barreiras significativas para interagir com o poder público:

- Dificuldade em entender **como solicitar serviços** (iluminação, buracos, saúde, zeladoria)
- **Falta de retorno** após protocolos registrados — reclamações que "somem"
- Ausência de **transparência sobre prazos e prioridades**
- **Receio de se identificar** em denúncias, especialmente em áreas de vulnerabilidade

Esse cenário enfraquece a confiança nas instituições, amplia desigualdades sociais — quem tem "contatos" resolve mais rápido — e gera sensação de abandono.

---

## O que é o ObservaAção

O **ObservaAção** é um sistema de ouvidoria cidadã digital desenvolvido para uma govtech fictícia homônima que venceu um edital municipal. O sistema permite que:

- **Cidadãos** registrem solicitações de serviços públicos de forma identificada ou anônima, acompanhem o andamento por protocolo e visualizem prazos e histórico de atendimento
- **Atendentes e gestores** tratem as demandas com rastreabilidade, organização por prioridade e responsabilidade documentada em cada movimentação

O objetivo não é "fazer um app bonito", mas reduzir barreiras, aumentar transparência e melhorar a capacidade de resposta do poder público — com alinhamento direto aos ODS 10, 11 e 16 da ONU.

---

## Funcionalidades

### Cidadão

| Funcionalidade | Detalhe |
|---|---|
| Registrar solicitação identificada | Com nome, e-mail, telefone (opcional), categoria, prioridade, localização, descrição e anexo |
| Registrar solicitação anônima | Sem dados pessoais; exige descrição detalhada e motivo do anonimato |
| Consultar por protocolo | Exibe status atual, histórico completo de movimentações e prazo SLA |

### Atendente / Gestor

| Funcionalidade | Detalhe |
|---|---|
| Listar todas as solicitações | Visão resumida com protocolo, categoria, prioridade, status e indicador de prazo |
| Filtrar demandas | Por status, categoria ou prioridade |
| Atualizar status | Com comentário obrigatório e registro do responsável |
| Ver detalhes completos | Histórico cronológico de todas as movimentações |

---

## Regras de Negócio

| Regra | Detalhe |
|---|---|
| **Protocolo** | Gerado automaticamente no formato `OBS-AAAA-NNNNN` (ex.: `OBS-2026-00001`) |
| **Fluxo de status** | `ABERTO → TRIAGEM → EM_EXECUCAO → RESOLVIDO → ENCERRADO` — progressão linear, sem retrocesso |
| **SLA Verde** | Prioridade baixa — prazo de 7 dias |
| **SLA Amarelo** | Prioridade média — prazo de 3 dias |
| **SLA Vermelho** | Prioridade alta — prazo de 1 dia |
| **Solicitação anônima** | Exige mínimo de 20 caracteres na descrição e motivo do anonimato; nenhum dado pessoal é armazenado |
| **Solicitação identificada** | Exige nome completo (mín. 3 chars), e-mail válido; telefone é opcional |
| **Atualização de status** | Comentário e identificação do responsável são obrigatórios em toda movimentação |
| **Histórico imutável** | Cada movimentação registra status anterior, status novo, comentário, responsável e data/hora |

---

## Arquitetura e Organização

O sistema segue uma **arquitetura em camadas** definida manualmente, sem frameworks, garantindo separação de responsabilidades e baixo acoplamento:

```
┌─────────────────────────────────────────────┐
│                   Menu (CLI)                │  ← Apresentação: lê entrada, exibe saída
├─────────────────────────────────────────────┤
│              SolicitacaoService             │  ← Regras de negócio: validação, SLA, protocolo
├─────────────────────────────────────────────┤
│          SolicitacaoRepository (interface)  │  ← Contrato de acesso a dados
│       SolicitacaoRepositoryEmMemoria        │  ← Implementação: HashMap em memória (1º bim.)
├─────────────────────────────────────────────┤
│         Domain: Solicitacao (abstrata)      │
│   SolicitacaoIdentificada │ SolicitacaoAnonima │  ← Modelo de domínio
│   Movimentacao │ Status │ Categoria │ Prioridade │
└─────────────────────────────────────────────┘
```

**Hierarquia de classes (POO):**

```
Solicitacao (abstrata)
├── SolicitacaoIdentificada   → adiciona: nomeCompleto, email, telefone
└── SolicitacaoAnonima        → adiciona: motivoAnonimato
```

Cada `Solicitacao` mantém uma lista imutável de `Movimentacao`, registrando o histórico completo de transições de status.

---

## Princípios Aplicados

### SOLID

| Princípio | Onde foi aplicado |
|---|---|
| **SRP** — Responsabilidade Única | `Formatador` só formata; `Validador` só valida; `GeradorProtocolo` só gera protocolos; `Menu` só gerencia a UI |
| **OCP** — Aberto/Fechado | `Status`: cada constante do enum encapsula sua própria regra de transição — adicionar um novo status não exige modificar código existente |
| **LSP** — Substituição de Liskov | `getDadosComplementares()` abstrato em `Solicitacao`; cada subclasse implementa sem surpresas — `Formatador` nunca precisa de cast |
| **ISP** — Segregação de Interfaces | `SolicitacaoRepository` expõe apenas os métodos necessários, sem sobrecarregar os consumidores |
| **DIP** — Inversão de Dependência | `SolicitacaoService` depende da interface `SolicitacaoRepository`, não da implementação concreta; `Main` injeta `SolicitacaoRepositoryEmMemoria` |

### Clean Code

| Prática | Onde foi aplicado |
|---|---|
| Nomes que revelam intenção | Variáveis de loop `s`/`m` renomeadas para `solicitacao`/`movimentacao`; método `processarOpcaoPrincipal` no lugar de `exibirMenuPrincipal` |
| Sem comentários redundantes | Removido `// --- Getters ---` — o nome do método já comunica |
| Sem magic numbers | `LARGURA_LINHA = 60` em vez de `"─".repeat(60)` direto no código |
| Funções fazem uma coisa | `imprimirSolicitacaoDetalhada` decomposto em `imprimirDadosPrincipais`, `imprimirDadosComplementares` e `imprimirHistorico` |
| DRY | `filtrar(Predicate<Solicitacao>)` elimina triplicação dos métodos de filtro no repositório |

---

## Stack Tecnológica

| Camada | Tecnologia | Justificativa |
|---|---|---|
| Linguagem | Java 17 (sem frameworks) | Exigência da disciplina — aplica POO diretamente |
| Interface | CLI (linha de comando) | Sem dependência de bibliotecas externas |
| Persistência (1º bim.) | Em memória — `HashMap` | Coleções nativas do Java; sem banco ou ORM |
| Persistência (2º bim.) | Arquivos `.txt` / `.csv` | `java.io` nativo, sem libs externas |
| Compilação | `javac` / `java` (JDK padrão) | Sem Maven, Gradle ou build tools externos |
| Controle de versão | Git + GitHub | — |

---

## Como Executar

### Pré-requisito

- **JDK 17** instalado ([download Adoptium](https://adoptium.net/))
- `java` e `javac` disponíveis no PATH

Para verificar:
```bash
java -version
javac -version
```

Ambos devem retornar versão 17 ou superior.

---

### Windows

**1. Compilar:**
```bat
compile.bat
```

**2. Executar:**
```bat
run.bat
```

> Os scripts já criam a pasta `out/` automaticamente se ela não existir.

---

### Linux / macOS

**1. Compilar:**
```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
```

**2. Executar:**
```bash
java -cp out -Dfile.encoding=UTF-8 observaacao.Main
```

---

### Passo a passo manual (qualquer sistema)

```bash
# 1. Clone o repositório
git clone https://github.com/Leocm123/aep-observaacao.git
cd aep-observaacao

# 2. Crie a pasta de saída
mkdir out

# 3. Liste os fontes e compile
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt

# 4. Execute
java -cp out -Dfile.encoding=UTF-8 observaacao.Main
```

---

### Navegação no sistema

Ao iniciar, o sistema exibe o menu principal:

```
========================================
   ObservaAção — Ouvidoria Cidadã Digital
========================================

Acesse como:
  1. Cidadão
  2. Atendente / Gestor
  0. Sair
```

- **Opção 1 (Cidadão):** registrar nova solicitação ou consultar protocolo existente
- **Opção 2 (Atendente/Gestor):** listar, filtrar e atualizar demandas

---

## Estrutura de Arquivos

```
aep-observaacao/
├── src/
│   └── observaacao/
│       ├── Main.java                              # Ponto de entrada e injeção de dependências
│       ├── menu/
│       │   └── Menu.java                          # Interface CLI e fluxos de navegação
│       ├── service/
│       │   └── SolicitacaoService.java            # Lógica de negócio central
│       ├── repository/
│       │   ├── SolicitacaoRepository.java         # Interface de acesso a dados (DIP)
│       │   └── SolicitacaoRepositoryEmMemoria.java # Implementação em HashMap
│       ├── domain/
│       │   ├── model/
│       │   │   ├── Solicitacao.java               # Classe abstrata base
│       │   │   ├── SolicitacaoIdentificada.java   # Com dados do cidadão
│       │   │   ├── SolicitacaoAnonima.java        # Sem dados pessoais
│       │   │   └── Movimentacao.java              # Registro imutável de histórico
│       │   └── enums/
│       │       ├── Status.java                    # Fluxo de estados com OCP
│       │       ├── Categoria.java                 # Tipos de problema urbano
│       │       └── Prioridade.java                # Níveis de SLA (Verde/Amarelo/Vermelho)
│       └── util/
│           ├── GeradorProtocolo.java              # Formato OBS-AAAA-NNNNN (AtomicInteger)
│           ├── Validador.java                     # Validações de entrada do usuário
│           └── Formatador.java                    # Formatação de saída no terminal
├── docs/
│   ├── perfis_e_personas.docx                    # Perfis e personas (entrega IHC)
│   └── relatorio_clean_code.docx                 # Relatório Clean Code 3 funções
├── compile.bat                                    # Script de compilação (Windows)
├── run.bat                                        # Script de execução (Windows)
└── README.md
```

---

## Entregas

### 1º Bimestre ✅

- ✅ Versão Beta funcional em CLI (Java 17, sem framework)
- ✅ Perfis e personas (IHC) — `docs/perfis_e_personas.docx`
- ✅ Relatório Clean Code (3 funções) — `docs/relatorio_clean_code.docx`
- ⏳ Vídeo de apresentação (até 5 min)

### 2º Bimestre

- ⬜ Wireframes de todas as telas (IHC)
- ⬜ Migração para Spring Boot (controller / service / repository)
- ⬜ Persistência em arquivo `.txt` / `.csv` ou banco de dados
- ⬜ Relatório de métricas (SonarQube / Checkstyle / PMD)
