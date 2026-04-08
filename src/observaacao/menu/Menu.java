package observaacao.menu;

import observaacao.domain.enums.Categoria;
import observaacao.domain.enums.Prioridade;
import observaacao.domain.enums.Status;
import observaacao.domain.model.Solicitacao;
import observaacao.service.SolicitacaoService;
import observaacao.util.Formatador;
import observaacao.util.Validador;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private final SolicitacaoService service;
    private final Scanner scanner;

    public Menu(SolicitacaoService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("\n========================================");
        System.out.println("   ObservaAção — Ouvidoria Cidadã Digital");
        System.out.println("========================================\n");

        boolean executando = true;
        while (executando) {
            executando = exibirMenuPrincipal();
        }

        System.out.println("\nAté logo! Obrigado por usar o ObservaAção.");
        scanner.close();
    }

    private boolean exibirMenuPrincipal() {
        System.out.println("\nAcesse como:");
        System.out.println("  1. Cidadão");
        System.out.println("  2. Atendente / Gestor");
        System.out.println("  0. Sair");
        System.out.print("\nOpção: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1: exibirMenuCidadao(); return true;
            case 2: exibirMenuAtendente(); return true;
            case 0: return false;
            default:
                System.out.println("Opção inválida.");
                return true;
        }
    }

    // ─── MENU CIDADÃO ─────────────────────────────────────────────────

    private void exibirMenuCidadao() {
        boolean executando = true;
        while (executando) {
            System.out.println("\n[CIDADÃO]");
            System.out.println("  1. Registrar nova solicitação");
            System.out.println("  2. Consultar solicitação por protocolo");
            System.out.println("  0. Voltar");
            System.out.print("\nOpção: ");

            int opcao = lerInteiro();
            switch (opcao) {
                case 1: fluxoCriarSolicitacao(); break;
                case 2: fluxoBuscarPorProtocolo(); break;
                case 0: executando = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private void fluxoCriarSolicitacao() {
        System.out.println("\n[NOVA SOLICITAÇÃO]");

        Categoria categoria = selecionarCategoria();
        Prioridade prioridade = selecionarPrioridade();

        System.out.print("Descrição (mínimo 10 caracteres): ");
        String descricao = lerLinha();

        System.out.print("Localização (bairro ou endereço): ");
        String localizacao = lerLinha();

        System.out.print("Anexo (pressione Enter para pular): ");
        String anexo = lerLinha();
        if (anexo.isBlank()) anexo = null;

        System.out.println("\nDeseja se identificar?");
        System.out.println("  1. Sim (identificada)");
        System.out.println("  2. Não (anônima)");
        System.out.print("Opção: ");
        int tipoOpcao = lerInteiro();

        try {
            Solicitacao solicitacao;
            if (tipoOpcao == 1) {
                solicitacao = criarSolicitacaoIdentificada(categoria, descricao, localizacao, anexo, prioridade);
            } else {
                solicitacao = criarSolicitacaoAnonima(categoria, descricao, localizacao, anexo, prioridade);
            }

            System.out.println("\n✔ Solicitação registrada com sucesso!");
            System.out.println("  Protocolo: " + solicitacao.getProtocolo());
            System.out.println("  Prazo SLA: " + Formatador.formatarDataHora(solicitacao.calcularPrazoSLA()));
            System.out.println("  Guarde seu protocolo para acompanhar o andamento.");

        } catch (IllegalArgumentException e) {
            System.out.println("\n✘ Erro: " + e.getMessage());
        }
    }

    private Solicitacao criarSolicitacaoIdentificada(Categoria categoria, String descricao,
                                                     String localizacao, String anexo,
                                                     Prioridade prioridade) {
        System.out.print("Nome completo: ");
        String nome = lerLinha();

        System.out.print("E-mail: ");
        String email = lerLinha();

        System.out.print("Telefone (pressione Enter para pular): ");
        String telefone = lerLinha();
        if (telefone.isBlank()) telefone = null;

        return service.criarSolicitacaoIdentificada(
            categoria, descricao, localizacao, anexo, prioridade, nome, email, telefone
        );
    }

    private Solicitacao criarSolicitacaoAnonima(Categoria categoria, String descricao,
                                                String localizacao, String anexo,
                                                Prioridade prioridade) {
        System.out.println("\nSolicitação anônima: dados pessoais não serão armazenados.");
        System.out.println("Descreva o motivo do anonimato (ex.: receio de retaliação):");
        System.out.print("> ");
        String motivoAnonimato = lerLinha();

        return service.criarSolicitacaoAnonima(
            categoria, descricao, localizacao, anexo, prioridade, motivoAnonimato
        );
    }

    private void fluxoBuscarPorProtocolo() {
        System.out.print("\nDigite o protocolo (ex.: OBS-2026-00001): ");
        String protocolo = lerLinha();

        Optional<Solicitacao> resultado = service.buscarPorProtocolo(protocolo);
        if (resultado.isPresent()) {
            Formatador.imprimirSolicitacaoDetalhada(resultado.get());
        } else {
            System.out.println("✘ Protocolo não encontrado: " + protocolo);
        }
    }

    // ─── MENU ATENDENTE ───────────────────────────────────────────────

    private void exibirMenuAtendente() {
        boolean executando = true;
        while (executando) {
            int total = service.contarTotal();
            System.out.println("\n[ATENDENTE / GESTOR] — " + total + " solicitação(ões) no sistema");
            System.out.println("  1. Listar todas as solicitações");
            System.out.println("  2. Filtrar por status");
            System.out.println("  3. Filtrar por categoria");
            System.out.println("  4. Filtrar por prioridade");
            System.out.println("  5. Atualizar status de uma solicitação");
            System.out.println("  6. Ver detalhes de uma solicitação");
            System.out.println("  0. Voltar");
            System.out.print("\nOpção: ");

            int opcao = lerInteiro();
            switch (opcao) {
                case 1: listarSolicitacoes(service.listarTodas()); break;
                case 2: fluxoFiltrarPorStatus(); break;
                case 3: fluxoFiltrarPorCategoria(); break;
                case 4: fluxoFiltrarPorPrioridade(); break;
                case 5: fluxoAtualizarStatus(); break;
                case 6: fluxoBuscarPorProtocolo(); break;
                case 0: executando = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private void listarSolicitacoes(List<Solicitacao> lista) {
        if (lista.isEmpty()) {
            System.out.println("\nNenhuma solicitação encontrada.");
            return;
        }
        System.out.println("\n" + lista.size() + " solicitação(ões):");
        for (Solicitacao s : lista) {
            Formatador.imprimirSolicitacaoResumida(s);
        }
    }

    private void fluxoFiltrarPorStatus() {
        Status status = selecionarStatus();
        listarSolicitacoes(service.listarPorStatus(status));
    }

    private void fluxoFiltrarPorCategoria() {
        Categoria categoria = selecionarCategoria();
        listarSolicitacoes(service.listarPorCategoria(categoria));
    }

    private void fluxoFiltrarPorPrioridade() {
        Prioridade prioridade = selecionarPrioridade();
        listarSolicitacoes(service.listarPorPrioridade(prioridade));
    }

    private void fluxoAtualizarStatus() {
        System.out.print("\nProtocolo da solicitação: ");
        String protocolo = lerLinha();

        Optional<Solicitacao> resultado = service.buscarPorProtocolo(protocolo);
        if (resultado.isEmpty()) {
            System.out.println("✘ Protocolo não encontrado.");
            return;
        }

        Solicitacao solicitacao = resultado.get();
        System.out.println("Status atual: " + solicitacao.getStatus());

        Status novoStatus = selecionarStatus();

        System.out.print("Comentário obrigatório: ");
        String comentario = lerLinha();

        System.out.print("Seu nome (responsável): ");
        String responsavel = lerLinha();

        try {
            service.atualizarStatus(protocolo, novoStatus, comentario, responsavel);
            System.out.println("✔ Status atualizado para: " + novoStatus);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("✘ Erro: " + e.getMessage());
        }
    }

    // ─── SELETORES DE ENUM ────────────────────────────────────────────

    private Categoria selecionarCategoria() {
        Categoria[] categorias = Categoria.values();
        System.out.println("\nCategorias:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println("  " + (i + 1) + ". " + categorias[i].getDescricao());
        }
        System.out.print("Escolha: ");
        int opcao = lerInteiroNoIntervalo(1, categorias.length);
        return categorias[opcao - 1];
    }

    private Prioridade selecionarPrioridade() {
        Prioridade[] prioridades = Prioridade.values();
        System.out.println("\nPrioridade (define o prazo SLA):");
        for (int i = 0; i < prioridades.length; i++) {
            System.out.printf("  %d. %s — prazo: %d dia(s)%n",
                i + 1, prioridades[i], prioridades[i].getSlaDias());
        }
        System.out.print("Escolha: ");
        int opcao = lerInteiroNoIntervalo(1, prioridades.length);
        return prioridades[opcao - 1];
    }

    private Status selecionarStatus() {
        Status[] statuses = Status.values();
        System.out.println("\nStatus disponíveis:");
        for (int i = 0; i < statuses.length; i++) {
            System.out.println("  " + (i + 1) + ". " + statuses[i].getDescricao());
        }
        System.out.print("Escolha: ");
        int opcao = lerInteiroNoIntervalo(1, statuses.length);
        return statuses[opcao - 1];
    }

    // ─── LEITURA DE ENTRADA ───────────────────────────────────────────

    private String lerLinha() {
        return scanner.nextLine().trim();
    }

    private int lerInteiro() {
        try {
            String entrada = scanner.nextLine().trim();
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int lerInteiroNoIntervalo(int min, int max) {
        while (true) {
            int valor = lerInteiro();
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Digite um número entre %d e %d: ", min, max);
        }
    }
}
