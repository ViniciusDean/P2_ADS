package br.com.fatec.utils;

import br.com.fatec.model.Funcionario;

public class SessionManager {

    private static Funcionario operadorLogado;

    public static Funcionario getOperadorLogado() {
        return operadorLogado;
    }

    public static void setOperadorLogado(Funcionario operador) {
        operadorLogado = operador;
    }
}
