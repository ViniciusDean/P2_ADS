package br.com.fatec.controller;

import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProdutoCadastroController {

    @FXML
    private Tab tab_produto, tab_consultar;

    @FXML
    private TextField txt_id, txt_nome, txt_precoVenda, txt_precoCusto, txt_codigoBarras, txt_lucro, txt_filtrar_nome, txt_qtd;

    @FXML
    private ComboBox<String> cmb_embalagem, cmb_fornecedor;

    @FXML
    private DatePicker date_inclusao;

    @FXML
    private Button btn_salvar, btn_voltar, btn_editar, btn_excluir;

    @FXML
    private TableView<?> table_produto;

    @FXML
    private void btn_salvar_click(ActionEvent event) {
        if (validarCampos()) {
            showAlert("Erro", "Fun", Alert.AlertType.ERROR);
        } else {
            showAlert("Erro", "Por favor, preencha todos os campos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_voltar_click(ActionEvent event) {
        // Lógica para voltar para a tela anterior
    }

    @FXML
    private void btn_editar_click(ActionEvent event) {
        // Lógica para editar um produto selecionado
    }

    @FXML
    private void btn_excluir_click(ActionEvent event) {
        // Lógica para excluir um produto selecionado
    }

    public void limparCampos() {
        txt_id.clear();
        txt_nome.clear();
        txt_precoVenda.clear();
        txt_precoCusto.clear();
        txt_codigoBarras.clear();
        txt_lucro.clear();
        txt_filtrar_nome.clear();
    }

    private boolean validarCampos() {
        boolean todosPreenchidos = true;

        // Lista de todos os TextField que devem ser validados
        TextField[] campos = {txt_id, txt_nome, txt_precoVenda, txt_precoCusto, txt_codigoBarras, txt_qtd};

        for (TextField campo : campos) {
            if (campo.getText().isEmpty()) {
                campo.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-padding: 2;");
                todosPreenchidos = false;
            } else {
                // Resetar o estilo se o campo estiver preenchido
                campo.setStyle("");
            }
        }

        return todosPreenchidos;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        date_inclusao.setValue(LocalDate.now());

    }

}
