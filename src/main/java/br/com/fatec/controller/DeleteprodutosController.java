package br.com.fatec.controller;

import br.com.fatec.model.Produto;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DeleteprodutosController {

    @FXML
    private TableView<Produto> table_produtos;

    @FXML
    private TableColumn<Produto, Integer> col_codigo;

    @FXML
    private TableColumn<Produto, String> col_nome;

    @FXML
    private TableColumn<Produto, Double> col_preco;

    @FXML
    private TableColumn<Produto, Integer> col_quantidade;

    private ObservableList<Produto> produtosList;

    @FXML
    public void initialize() {
        col_codigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_preco.setCellValueFactory(new PropertyValueFactory<>("preco_custo"));
        col_quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        produtosList = FXCollections.observableArrayList();
        table_produtos.setItems(produtosList);
    }

    public void setProdutos(List<Produto> produtos) {
        produtosList.setAll(produtos);
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) table_produtos.getScene().getWindow();
        stage.close();
    }
}
