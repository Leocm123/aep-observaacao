package observaacao.util;

import observaacao.domain.model.Movimentacao;
import observaacao.domain.model.Solicitacao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatador {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int LARGURA_LINHA = 60;
    private static final String LINHA = "─".repeat(LARGURA_LINHA);

    private Formatador() {}

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora.format(FORMATO);
    }

    public static void imprimirSolicitacaoResumida(Solicitacao solicitacao) {
        String prazoStatus = solicitacao.estaDentroDoPrazo() ? "✓ No prazo" : "⚠ Atrasada";
        System.out.printf("  [%s] %s | %s | %s | Prazo: %s (%s)%n",
            solicitacao.getProtocolo(),
            solicitacao.getCategoria(),
            solicitacao.getPrioridade(),
            solicitacao.getStatus(),
            formatarDataHora(solicitacao.calcularPrazoSLA()),
            prazoStatus
        );
    }

    public static void imprimirSolicitacaoDetalhada(Solicitacao solicitacao) {
        System.out.println(LINHA);
        imprimirDadosPrincipais(solicitacao);
        imprimirDadosComplementares(solicitacao);
        imprimirHistorico(solicitacao);
        System.out.println(LINHA);
    }

    private static void imprimirDadosPrincipais(Solicitacao solicitacao) {
        System.out.println("  PROTOCOLO : " + solicitacao.getProtocolo());
        System.out.println("  TIPO      : " + (solicitacao.isAnonima() ? "Anônima" : "Identificada"));
        System.out.println("  CATEGORIA : " + solicitacao.getCategoria());
        System.out.println("  PRIORIDADE: " + solicitacao.getPrioridade());
        System.out.println("  STATUS    : " + solicitacao.getStatus());
        System.out.println("  ABERTURA  : " + formatarDataHora(solicitacao.getDataAbertura()));
        System.out.println("  PRAZO SLA : " + formatarDataHora(solicitacao.calcularPrazoSLA())
            + (solicitacao.estaDentroDoPrazo() ? " ✓" : " ⚠ ATRASADA"));
        System.out.println("  LOCAL     : " + solicitacao.getLocalizacao());
        System.out.println("  DESCRIÇÃO : " + solicitacao.getDescricao());
        if (solicitacao.getAnexo() != null && !solicitacao.getAnexo().isBlank()) {
            System.out.println("  ANEXO     : " + solicitacao.getAnexo());
        }
    }

    private static void imprimirDadosComplementares(Solicitacao solicitacao) {
        solicitacao.getDadosComplementares().forEach((rotulo, valor) ->
            System.out.printf("  %-10s: %s%n", rotulo, valor)
        );
    }

    private static void imprimirHistorico(Solicitacao solicitacao) {
        if (solicitacao.getHistorico().isEmpty()) {
            System.out.println("  HISTÓRICO : Nenhuma movimentação registrada.");
            return;
        }
        System.out.println("  HISTÓRICO :");
        for (Movimentacao movimentacao : solicitacao.getHistorico()) {
            System.out.printf("    [%s] %s → %s | Resp: %s%n",
                formatarDataHora(movimentacao.getDataHora()),
                movimentacao.getStatusAnterior(),
                movimentacao.getStatusNovo(),
                movimentacao.getResponsavel()
            );
            System.out.println("      Comentário: " + movimentacao.getComentario());
        }
    }

    public static void imprimirLinha() {
        System.out.println(LINHA);
    }
}
