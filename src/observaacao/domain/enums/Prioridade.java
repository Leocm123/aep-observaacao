package observaacao.domain.enums;

public enum Prioridade {

    VERDE(7, "Baixa", "🟢"),
    AMARELO(3, "Média", "🟡"),
    VERMELHO(1, "Alta", "🔴");

    private final int slaDias;
    private final String descricao;
    private final String icone;

    Prioridade(int slaDias, String descricao, String icone) {
        this.slaDias = slaDias;
        this.descricao = descricao;
        this.icone = icone;
    }

    public int getSlaDias() {
        return slaDias;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getIcone() {
        return icone;
    }

    @Override
    public String toString() {
        return icone + " " + descricao;
    }
}
