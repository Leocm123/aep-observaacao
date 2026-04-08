package observaacao.repository;

import observaacao.domain.enums.Categoria;
import observaacao.domain.enums.Prioridade;
import observaacao.domain.enums.Status;
import observaacao.domain.model.Solicitacao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SolicitacaoRepository {

    private final Map<String, Solicitacao> dados = new HashMap<>();

    public void salvar(Solicitacao solicitacao) {
        dados.put(solicitacao.getProtocolo(), solicitacao);
    }

    public Optional<Solicitacao> buscarPorProtocolo(String protocolo) {
        return Optional.ofNullable(dados.get(protocolo.toUpperCase()));
    }

    public List<Solicitacao> listarTodas() {
        return new ArrayList<>(dados.values());
    }

    public List<Solicitacao> listarPorStatus(Status status) {
        List<Solicitacao> resultado = new ArrayList<>();
        for (Solicitacao s : dados.values()) {
            if (s.getStatus() == status) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    public List<Solicitacao> listarPorCategoria(Categoria categoria) {
        List<Solicitacao> resultado = new ArrayList<>();
        for (Solicitacao s : dados.values()) {
            if (s.getCategoria() == categoria) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    public List<Solicitacao> listarPorPrioridade(Prioridade prioridade) {
        List<Solicitacao> resultado = new ArrayList<>();
        for (Solicitacao s : dados.values()) {
            if (s.getPrioridade() == prioridade) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    public int contarTotal() {
        return dados.size();
    }
}
