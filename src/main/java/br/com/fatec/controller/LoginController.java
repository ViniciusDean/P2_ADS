package br.com.fatec.controller;

import br.com.fatec.DAO.FuncionarioDAO;
import br.com.fatec.model.Funcionario;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txt_usuario;

    @FXML
    private PasswordField txt_senha;

    private Funcionario operadorLogado;

    @FXML
    private void btn_entrar_click() throws IOException {
        String usuario = txt_usuario.getText();
        String senha = txt_senha.getText();

        if (usuario.isEmpty() || senha.isEmpty()) {
            showAlert("Erro", "Usuário ou senha não podem estar vazios!", Alert.AlertType.ERROR);
            return;
        }

        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        try {
            operadorLogado = funcionarioDAO.autenticar(usuario, senha);
            if (operadorLogado != null) {
                carregarTelaMenu();
            } else {
                showAlert("Erro", "Usuário ou senha incorretos!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Erro de SQL", "Erro ao autenticar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void carregarTelaMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/tela_menu.fxml"));
        Parent root = loader.load();

        MenuController menuController = loader.getController();
        menuController.setOperadorLogado(operadorLogado);

        Stage stage = (Stage) txt_usuario.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setOperadorLogado(Funcionario operadorLogado) {
        this.operadorLogado = operadorLogado;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void btn_sair_click() {
        System.exit(0);
    }
}
