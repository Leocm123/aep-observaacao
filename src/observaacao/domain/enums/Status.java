package observaacao.domain.enums;

public enum Status {

    ABERTO("Aberto"),
    TRIAGEM("Em Triagem"),
    EM_EXECUCAO("Em Execução"),
    RESOLVIDO("Resolvido"),
    ENCERRADO("Encerrado");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean podeTransicionarPara(Status destino) {
        switch (this) {
            case ABERTO:      return destino == TRIAGEM;
            case TRIAGEM:     return destino == EM_EXECUCAO;
            case EM_EXECUCAO: return destino == RESOLVIDO;
            case RESOLVIDO:   return destino == ENCERRADO;
            default:          return false;
        }
    }

    @Override
    public String toString() {
        return descricao;
    }
}
