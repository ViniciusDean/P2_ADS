package br.com.fatec.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Banco {

    // criar atributos
    public static String bancoDados, usuario, senha, servidor;
    public static int porta;

    // variável de conexão
    public static Connection conexao = null;
    private static boolean mensagemConexaoMostrada = false;

    static {
        // mysql e mariaDB
        bancoDados = "Distribuidora";
        usuario = "root";
        senha = "";
        servidor = "localhost";
        porta = 3306;
    }

    public static void conectar() throws SQLException {
        String url = "jdbc:mysql://" + servidor + ":" + porta + "/" + bancoDados;
        conexao = DriverManager.getConnection(url, usuario, senha);

        if (!mensagemConexaoMostrada) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Status da Conexão com o Banco de Dados");
            alert.setHeaderText(null);
            alert.setContentText("Conectado ao banco de dados com sucesso!");
            alert.showAndWait();
            mensagemConexaoMostrada = true; // Define o indicador para evitar futuras mensagens
        }
    }

    public static void desconectar() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    public static Connection obterConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            throw new SQLException("Conexão está fechada.");
        } else {
            return conexao;
        }
    }
}
