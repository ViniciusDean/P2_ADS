package br.com.fatec.controller;

import br.com.fatec.model.Funcionario;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    private Funcionario operadorLogado;
    @FXML
    private Button btn_fornecedor;

    @FXML
    private Button btn_produto;

    @FXML
    private Button btn_consulta;

    @FXML
    private Button btn_funcionario;

    @FXML
    private Button btn_sair;

    @FXML
    private Button btn_caixa;

    @FXML
    private void btn_fornecedor_click() {
        carregarTela("/br/com/fatec/view/fornecedor_cadastro.fxml");
    }

    @FXML
    private void btn_produto_click() {
        carregarTela("/br/com/fatec/view/produto_cadastro.fxml");
    }

    @FXML
    private void btn_consulta_click() {
        carregarTela("/br/com/fatec/view/pesquisa_avancada.fxml");
    }

    @FXML
    private void btn_funcionario_click() {
        carregarTela("/br/com/fatec/view/funcionario_cadastro.fxml");
    }

    @FXML
    private void btn_sair_click() {
        // Lógica para sair da aplicação
        System.exit(0);
    }

    @FXML
    private void btn_caixa_click() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/Caixa.fxml"));
        Parent root = loader.load();

        CaixaController caixaController = loader.getController();
        caixaController.setOperadorLogado(operadorLogado);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setOperadorLogado(Funcionario operadorLogado) {
        this.operadorLogado = operadorLogado;
    }

    private void carregarTela(String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btn_fornecedor.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Falha ao carregar a próxima tela: " + e.getMessage(), Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
