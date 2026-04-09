package observaacao.domain.model;

import observaacao.domain.enums.Categoria;
import observaacao.domain.enums.Prioridade;

import java.util.Collections;
import java.util.Map;

public class SolicitacaoAnonima extends Solicitacao {

    private final String motivoAnonimato;

    public SolicitacaoAnonima(String protocolo, Categoria categoria, String descricao,
                              String localizacao, String anexo, Prioridade prioridade,
                              String motivoAnonimato) {
        super(protocolo, categoria, descricao, localizacao, anexo, prioridade);
        this.motivoAnonimato = motivoAnonimato;
    }

    @Override
    public boolean isAnonima() {
        return true;
    }

    @Override
    public Map<String, String> getDadosComplementares() {
        return Collections.emptyMap();
    }

    public String getMotivoAnonimato() {
        return motivoAnonimato;
    }
}
