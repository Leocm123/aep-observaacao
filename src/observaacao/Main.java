package observaacao;

import observaacao.menu.Menu;
import observaacao.repository.SolicitacaoRepository;
import observaacao.repository.SolicitacaoRepositoryEmMemoria;
import observaacao.service.SolicitacaoService;
import observaacao.util.GeradorProtocolo;

public class Main {

    public static void main(String[] args) {
        SolicitacaoRepository repository = new SolicitacaoRepositoryEmMemoria();
        GeradorProtocolo geradorProtocolo = new GeradorProtocolo();
        SolicitacaoService service = new SolicitacaoService(repository, geradorProtocolo);
        Menu menu = new Menu(service);

        menu.iniciar();
    }
}
