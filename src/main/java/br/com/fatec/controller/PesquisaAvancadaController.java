package br.com.fatec.controller;

import br.com.fatec.DAO.ProdutoDAO;
import br.com.fatec.model.Produto;
import java.sql.SQLException;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PesquisaAvancadaController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab_consulta;

    @FXML
    private TableView<Produto> table_produto;

    @FXML
    private DatePicker date_1, date_2;
    @FXML
    private CheckBox chk_desativar, chk_desativado;

    @FXML
    private Button btn_sair;

    @FXML
    private TextField txt_produto, txt_fornecedor;

    private ObservableList<Produto> produtos;
    ProdutoDAO produtoDAO = new ProdutoDAO();
    @FXML
    private TableColumn<Produto, String> col_nome, col_lucro, col_fornecedor;
    @FXML
    private TableColumn<Produto, Integer> col_qtd;
    @FXML
    private TableColumn<Produto, Double> col_preco;

    @FXML
    private void initialize() {
        col_preco.setCellValueFactory(new PropertyValueFactory<>("preco_venda"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_qtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

    }

    @FXML

    private void btn_sair_click() {
        // Lógica para o botão sair
        System.exit(0);
    }

    private void loadProdutoData() {
        produtos = FXCollections.observableArrayList();

        try {
            Collection<Produto> listaProdutos = produtoDAO.lista("");
            produtos.addAll(listaProdutos);
        } catch (SQLException e) {
            e.printStackTrace();
            // Mostrar alerta de erro, se necessário
            showAlert("Erro ao carregar dados", e.getMessage(), Alert.AlertType.ERROR);
        }
        table_produto.getItems().clear();
        table_produto.setItems(produtos);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
