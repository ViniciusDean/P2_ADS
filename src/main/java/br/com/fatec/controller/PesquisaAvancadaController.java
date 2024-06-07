package br.com.fatec.controller;

import br.com.fatec.DAO.ProdutoDAO;
import br.com.fatec.model.Produto;
import java.sql.SQLException;
import java.util.Collection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
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
    private CheckBox chk_desativar, chk_desativado;
    @FXML
    private ComboBox<String> cmb_filtrar;
    @FXML
    private Button btn_sair, btn_limpar, btn_desativar;

    @FXML
    private TextField txt_produto, txt_fornecedor, txt_preco_min, txt_preco_max;

    private ObservableList<Produto> produtos;
    ProdutoDAO produtoDAO = new ProdutoDAO();
    @FXML
    private TableColumn<Produto, String> col_nome, col_fornecedor;
    @FXML
    private TableColumn<Produto, Integer> col_qtd;
    @FXML
    private TableColumn<Produto, Double> col_preco, col_lucro;
    @FXML
    private ComboBox<String> cmb_embalagem;

    @FXML
    private void btn_desativar_click() {
    }

    @FXML
    private void initialize() {
        cmb_embalagem.setItems(FXCollections.observableArrayList("Vasilhame", "Lata", "Fardo", "Caixa", "Retornável"));
        txt_preco_min.textProperty().addListener((observable, oldValue, newValue) -> filterProducts());
        txt_preco_max.textProperty().addListener((observable, oldValue, newValue) -> filterProducts());
        cmb_embalagem.valueProperty().addListener((observable, oldValue, newValue) -> filterProducts());
        InicializarColunas();
        loadProdutoData();
        txt_fornecedor.textProperty().addListener((observable, oldValue, newValue) -> filterProducts());
        txt_produto.textProperty().addListener((observable, oldValue, newValue) -> filterProducts());
        ObservableList<String> embalagens = FXCollections.observableArrayList("Vasilhame", "Lata", "Fardo", "Caixa", "Retornável");
        if (cmb_embalagem.getItems().isEmpty()) {
            cmb_embalagem.setItems(embalagens);
        }
    }

    private void filterProducts() {
        String fornecedorFilter = txt_fornecedor.getText().toLowerCase();
        String produtoFilter = txt_produto.getText().toLowerCase();
        String embalagemFilter = cmb_embalagem.getValue() == null ? "" : cmb_embalagem.getValue().toLowerCase();
        String precoMinStr = txt_preco_min.getText();
        String precoMaxStr = txt_preco_max.getText();

        double precoMin = precoMinStr.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(precoMinStr);
        double precoMax = precoMaxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(precoMaxStr);

        ObservableList<Produto> filteredList = FXCollections.observableArrayList();

        for (Produto produto : produtos) {
            boolean matchesFornecedor = produto.getFornecedor().getRazao_social().toLowerCase().contains(fornecedorFilter);
            boolean matchesProduto = produto.getNome().toLowerCase().contains(produtoFilter);
            boolean matchesEmbalagem = produto.getEmbalagem().toLowerCase().contains(embalagemFilter);
            boolean matchesPreco = produto.getPreco_venda() >= precoMin && produto.getPreco_venda() <= precoMax;

            if (matchesFornecedor && matchesProduto && matchesEmbalagem && matchesPreco) {
                filteredList.add(produto);
            }
        }

        table_produto.setItems(filteredList);
    }

    private void InicializarColunas() {
        col_fornecedor.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFornecedor().getRazao_social()));
        col_preco.setCellValueFactory(new PropertyValueFactory<>("preco_venda"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_qtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        col_lucro.setCellValueFactory(cellData -> {
            Produto produto = cellData.getValue();
            double lucro = produto.getPreco_venda() - produto.getPreco_custo();
            return new SimpleDoubleProperty(lucro).asObject();
        });
        col_lucro.setCellFactory(column -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f", item));
                    setStyle(item < 0 ? "-fx-text-fill: red;" : "-fx-text-fill: black;");
                }
            }
        });
    }

    @FXML
    private void btn_limpar_click() {
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
