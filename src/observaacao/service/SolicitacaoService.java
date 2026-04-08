package observaacao.service;

import observaacao.domain.enums.Categoria;
import observaacao.domain.enums.Prioridade;
import observaacao.domain.enums.Status;
import observaacao.domain.model.Solicitacao;
import observaacao.domain.model.SolicitacaoAnonima;
import observaacao.domain.model.SolicitacaoIdentificada;
import observaacao.repository.SolicitacaoRepository;
import observaacao.util.GeradorProtocolo;
import observaacao.util.Validador;

import java.util.List;
import java.util.Optional;

public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final GeradorProtocolo geradorProtocolo;

    public SolicitacaoService(SolicitacaoRepository repository, GeradorProtocolo geradorProtocolo) {
        this.repository = repository;
        this.geradorProtocolo = geradorProtocolo;
    }

    public SolicitacaoIdentificada criarSolicitacaoIdentificada(
            Categoria categoria, String descricao, String localizacao,
            String anexo, Prioridade prioridade,
            String nomeCompleto, String email, String telefone) {

        validarCamposComuns(descricao, localizacao);
        Validador.validarTamanhoMinimo(nomeCompleto, "nome completo", 3);
        Validador.validarEmail(email);

        String protocolo = geradorProtocolo.gerar();
        SolicitacaoIdentificada solicitacao = new SolicitacaoIdentificada(
            protocolo, categoria, descricao.trim(), localizacao.trim(),
            anexo, prioridade, nomeCompleto.trim(), email.trim(), telefone
        );

        repository.salvar(solicitacao);
        return solicitacao;
    }

    public SolicitacaoAnonima criarSolicitacaoAnonima(
            Categoria categoria, String descricao, String localizacao,
            String anexo, Prioridade prioridade, String motivoAnonimato) {

        validarCamposComuns(descricao, localizacao);
        Validador.validarTamanhoMinimo(descricao, "descrição", 20);
        Validador.validarNaoVazio(motivoAnonimato, "motivo do anonimato");

        String protocolo = geradorProtocolo.gerar();
        SolicitacaoAnonima solicitacao = new SolicitacaoAnonima(
            protocolo, categoria, descricao.trim(), localizacao.trim(),
            anexo, prioridade, motivoAnonimato.trim()
        );

        repository.salvar(solicitacao);
        return solicitacao;
    }

    public void atualizarStatus(String protocolo, Status novoStatus,
                                String comentario, String responsavel) {
        Validador.validarNaoVazio(comentario, "comentário");
        Validador.validarNaoVazio(responsavel, "responsável");

        Solicitacao solicitacao = buscarOuLancarErro(protocolo);
        solicitacao.atualizarStatus(novoStatus, comentario.trim(), responsavel.trim());
    }

    public Optional<Solicitacao> buscarPorProtocolo(String protocolo) {
        Validador.validarNaoVazio(protocolo, "protocolo");
        return repository.buscarPorProtocolo(protocolo.trim());
    }

    public List<Solicitacao> listarTodas() {
        return repository.listarTodas();
    }

    public List<Solicitacao> listarPorStatus(Status status) {
        return repository.listarPorStatus(status);
    }

    public List<Solicitacao> listarPorCategoria(Categoria categoria) {
        return repository.listarPorCategoria(categoria);
    }

    public List<Solicitacao> listarPorPrioridade(Prioridade prioridade) {
        return repository.listarPorPrioridade(prioridade);
    }

    public int contarTotal() {
        return repository.contarTotal();
    }

    private void validarCamposComuns(String descricao, String localizacao) {
        Validador.validarTamanhoMinimo(descricao, "descrição", 10);
        Validador.validarNaoVazio(localizacao, "localização");
    }

    private Solicitacao buscarOuLancarErro(String protocolo) {
        return repository.buscarPorProtocolo(protocolo)
            .orElseThrow(() -> new IllegalArgumentException(
                "Protocolo não encontrado: " + protocolo
            ));
    }
}
