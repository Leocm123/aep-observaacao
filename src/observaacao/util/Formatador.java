package observaacao.util;

import observaacao.domain.model.Movimentacao;
import observaacao.domain.model.Solicitacao;
import observaacao.domain.model.SolicitacaoIdentificada;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatador {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String LINHA = "─".repeat(60);

    private Formatador() {}

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora.format(FORMATO);
    }

    public static void imprimirSolicitacaoResumida(Solicitacao s) {
        String prazoStatus = s.estaDentroDoPrazo() ? "✓ No prazo" : "⚠ Atrasada";
        System.out.printf("  [%s] %s | %s | %s | Prazo: %s (%s)%n",
            s.getProtocolo(),
            s.getCategoria(),
            s.getPrioridade(),
            s.getStatus(),
            formatarDataHora(s.calcularPrazoSLA()),
            prazoStatus
        );
    }

    public static void imprimirSolicitacaoDetalhada(Solicitacao s) {
        System.out.println(LINHA);
        System.out.println("  PROTOCOLO : " + s.getProtocolo());
        System.out.println("  TIPO      : " + (s.isAnonima() ? "Anônima" : "Identificada"));
        System.out.println("  CATEGORIA : " + s.getCategoria());
        System.out.println("  PRIORIDADE: " + s.getPrioridade());
        System.out.println("  STATUS    : " + s.getStatus());
        System.out.println("  ABERTURA  : " + formatarDataHora(s.getDataAbertura()));
        System.out.println("  PRAZO SLA : " + formatarDataHora(s.calcularPrazoSLA())
            + (s.estaDentroDoPrazo() ? " ✓" : " ⚠ ATRASADA"));
        System.out.println("  LOCAL     : " + s.getLocalizacao());
        System.out.println("  DESCRIÇÃO : " + s.getDescricao());

        if (s.getAnexo() != null && !s.getAnexo().isBlank()) {
            System.out.println("  ANEXO     : " + s.getAnexo());
        }

        if (!s.isAnonima()) {
            SolicitacaoIdentificada identificada = (SolicitacaoIdentificada) s;
            System.out.println("  CIDADÃO   : " + identificada.getNomeCompleto());
            System.out.println("  EMAIL     : " + identificada.getEmail());
            if (identificada.getTelefone() != null && !identificada.getTelefone().isBlank()) {
                System.out.println("  TELEFONE  : " + identificada.getTelefone());
            }
        }

        imprimirHistorico(s);
        System.out.println(LINHA);
    }

    private static void imprimirHistorico(Solicitacao s) {
        if (s.getHistorico().isEmpty()) {
            System.out.println("  HISTÓRICO : Nenhuma movimentação registrada.");
            return;
        }
        System.out.println("  HISTÓRICO :");
        for (Movimentacao m : s.getHistorico()) {
            System.out.printf("    [%s] %s → %s | Resp: %s%n",
                formatarDataHora(m.getDataHora()),
                m.getStatusAnterior(),
                m.getStatusNovo(),
                m.getResponsavel()
            );
            System.out.println("      Comentário: " + m.getComentario());
        }
    }

    public static void imprimirLinha() {
        System.out.println(LINHA);
    }
}
