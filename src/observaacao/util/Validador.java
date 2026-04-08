package observaacao.util;

public class Validador {

    private Validador() {}

    public static void validarNaoVazio(String valor, String nomeCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O campo '" + nomeCampo + "' é obrigatório.");
        }
    }

    public static void validarTamanhoMinimo(String valor, String nomeCampo, int minimo) {
        validarNaoVazio(valor, nomeCampo);
        if (valor.trim().length() < minimo) {
            throw new IllegalArgumentException(
                "O campo '" + nomeCampo + "' deve ter ao menos " + minimo + " caracteres."
            );
        }
    }

    public static void validarEmail(String email) {
        validarNaoVazio(email, "email");
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido: " + email);
        }
    }

    public static void validarOpcaoMenu(int opcao, int min, int max) {
        if (opcao < min || opcao > max) {
            throw new IllegalArgumentException(
                "Opção inválida. Digite um número entre " + min + " e " + max + "."
            );
        }
    }
}
