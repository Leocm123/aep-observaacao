# ObservaAção — Ouvidoria Cidadã Digital

Sistema de ouvidoria cidadã desenvolvido em Java 17 puro (sem frameworks).

**AEP — Análise e Desenvolvimento de Sistemas · 1º Semestre 2026**

---

## Como executar

### Pré-requisito
JDK 17 instalado e no PATH.

### Windows
```bat
compile.bat
run.bat
```

### Linux / macOS
```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out -Dfile.encoding=UTF-8 observaacao.Main
```

---

## Estrutura do projeto

```
src/
  observaacao/
    Main.java                         # Ponto de entrada
    menu/
      Menu.java                       # Interface CLI e fluxos de navegação
    service/
      SolicitacaoService.java         # Lógica de negócio
    repository/
      SolicitacaoRepository.java      # Armazenamento em memória (HashMap)
    domain/
      model/
        Solicitacao.java              # Classe abstrata base
        SolicitacaoIdentificada.java  # Solicitação com dados do cidadão
        SolicitacaoAnonima.java       # Solicitação sem dados pessoais
        Movimentacao.java             # Registro de histórico de status
      enums/
        Status.java                   # ABERTO → TRIAGEM → EM_EXECUCAO → RESOLVIDO → ENCERRADO
        Categoria.java                # Iluminação, Buraco, Limpeza, Saúde, etc.
        Prioridade.java               # VERDE (7d) / AMARELO (3d) / VERMELHO (1d)
    util/
      GeradorProtocolo.java           # Gera protocolos no formato OBS-2026-00001
      Validador.java                  # Validações de entrada
      Formatador.java                 # Formatação de saída no terminal
```

---

## Funcionalidades (Beta)

### Cidadão
- Registrar solicitação identificada ou anônima
- Escolher categoria e prioridade
- Consultar solicitação por protocolo
- Visualizar histórico completo de movimentações e prazo SLA

### Atendente / Gestor
- Listar todas as solicitações
- Filtrar por status, categoria ou prioridade
- Atualizar status (com comentário obrigatório)
- Ver detalhes completos de qualquer solicitação

---

## Regras de negócio

| Regra | Detalhe |
|-------|---------|
| Protocolo | Gerado automaticamente: `OBS-AAAA-NNNNN` |
| Fluxo de status | ABERTO → TRIAGEM → EM_EXECUCAO → RESOLVIDO → ENCERRADO |
| SLA VERDE | 7 dias a partir da abertura |
| SLA AMARELO | 3 dias a partir da abertura |
| SLA VERMELHO | 1 dia a partir da abertura |
| Anônima | Exige descrição mínima de 20 chars e motivo do anonimato |
| Atualização de status | Comentário obrigatório em toda movimentação |
