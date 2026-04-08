package observaacao.util;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class GeradorProtocolo {

    private final AtomicInteger contador;

    public GeradorProtocolo() {
        this.contador = new AtomicInteger(1);
    }

    public String gerar() {
        int ano = LocalDate.now().getYear();
        int numero = contador.getAndIncrement();
        return String.format("OBS-%d-%05d", ano, numero);
    }
}
