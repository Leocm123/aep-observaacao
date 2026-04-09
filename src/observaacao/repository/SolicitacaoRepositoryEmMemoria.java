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
import java.util.function.Predicate;

public class SolicitacaoRepositoryEmMemoria implements SolicitacaoRepository {

    private final Map<String, Solicitacao> dados = new HashMap<>();

    @Override
    public void salvar(Solicitacao solicitacao) {
        dados.put(solicitacao.getProtocolo(), solicitacao);
    }

    @Override
    public Optional<Solicitacao> buscarPorProtocolo(String protocolo) {
        return Optional.ofNullable(dados.get(protocolo.toUpperCase()));
    }

    @Override
    public List<Solicitacao> listarTodas() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public List<Solicitacao> listarPorStatus(Status status) {
        return filtrar(solicitacao -> solicitacao.getStatus() == status);
    }

    @Override
    public List<Solicitacao> listarPorCategoria(Categoria categoria) {
        return filtrar(solicitacao -> solicitacao.getCategoria() == categoria);
    }

    @Override
    public List<Solicitacao> listarPorPrioridade(Prioridade prioridade) {
        return filtrar(solicitacao -> solicitacao.getPrioridade() == prioridade);
    }

    @Override
    public int contarTotal() {
        return dados.size();
    }

    private List<Solicitacao> filtrar(Predicate<Solicitacao> criterio) {
        List<Solicitacao> resultado = new ArrayList<>();
        for (Solicitacao solicitacao : dados.values()) {
            if (criterio.test(solicitacao)) resultado.add(solicitacao);
        }
        return resultado;
    }
}
