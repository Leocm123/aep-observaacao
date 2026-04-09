package observaacao.repository;

import observaacao.domain.enums.Categoria;
import observaacao.domain.enums.Prioridade;
import observaacao.domain.enums.Status;
import observaacao.domain.model.Solicitacao;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoRepository {
    void salvar(Solicitacao solicitacao);
    Optional<Solicitacao> buscarPorProtocolo(String protocolo);
    List<Solicitacao> listarTodas();
    List<Solicitacao> listarPorStatus(Status status);
    List<Solicitacao> listarPorCategoria(Categoria categoria);
    List<Solicitacao> listarPorPrioridade(Prioridade prioridade);
    int contarTotal();
}
