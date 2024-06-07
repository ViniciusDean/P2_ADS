package br.com.fatec.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FuncionarioCadastroController {

    @FXML
    private TextField txt_telefone, txt_ddd, txt_id, txt_cep, txt_nome, txt_cpf, txt_email;

    @FXML
    private ComboBox<String> cmb_contrato;

    @FXML
    private DatePicker data_nasc;

    @FXML
    private Button btn_continuar, btn_voltar, btn_salvar, btn_editar, btn_excluir;

    @FXML
    private TableView<?> table_view;

    @FXML
    private TableColumn<?, ?> col_id, col_nome, col_email;

    @FXML
    private void initialize() {
        // Inicialização da ComboBox de contrato
        cmb_contrato.setItems(FXCollections.observableArrayList("CLT", "PJ", "Estágio", "Temporário"));

        // Adicionar listeners e lógica de inicialização conforme necessário
    }

    @FXML
    private void btn_voltar_click() {
        // Lógica para o botão Voltar
    }

    @FXML
    private void btn_salvar_click() {
        // Lógica para o botão Salvar
    }

    @FXML
    private void btn_editar_click() {
        // Lógica para o botão Editar
    }

    @FXML
    private void btn_excluir_click() {
        // Lógica para o botão Excluir
    }
}
