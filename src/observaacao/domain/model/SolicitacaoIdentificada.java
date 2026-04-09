package observaacao.domain.model;

import observaacao.domain.enums.Categoria;
import observaacao.domain.enums.Prioridade;

import java.util.LinkedHashMap;
import java.util.Map;

public class SolicitacaoIdentificada extends Solicitacao {

    private final String nomeCompleto;
    private final String email;
    private final String telefone;

    public SolicitacaoIdentificada(String protocolo, Categoria categoria, String descricao,
                                   String localizacao, String anexo, Prioridade prioridade,
                                   String nomeCompleto, String email, String telefone) {
        super(protocolo, categoria, descricao, localizacao, anexo, prioridade);
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
    }

    @Override
    public boolean isAnonima() {
        return false;
    }

    @Override
    public Map<String, String> getDadosComplementares() {
        Map<String, String> dados = new LinkedHashMap<>();
        dados.put("CIDADÃO", nomeCompleto);
        dados.put("EMAIL", email);
        if (telefone != null && !telefone.isBlank()) {
            dados.put("TELEFONE", telefone);
        }
        return dados;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }
}
