package br.com.fatec.controller;

import br.com.fatec.DAO.FornecedorDAO;
import br.com.fatec.DAO.ProdutoDAO;
import br.com.fatec.model.Fornecedor;
import br.com.fatec.model.Produto;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProdutoCadastroController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tab_produto, tab_consultar;

    @FXML
    private TextField txt_id, txt_nome, txt_precoVenda, txt_precoCusto, txt_codigoBarras,
            txt_filtrar_nome, txt_qtd;

    @FXML
    private ComboBox<String> cmb_embalagem, cmb_fornecedor;

    @FXML
    private DatePicker date_inclusao;

    @FXML
    private Button btn_salvar, btn_voltar, btn_editar, btn_excluir, btn_cancelar;

    @FXML
    private TableView<Produto> table_produto;
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private ObservableList<Produto> produtos;
    @FXML
    private TableColumn<Produto, String> col_id, col_nome, col_qtd;

    @FXML
    private void btn_salvar_click(ActionEvent event) throws SQLException {
        if (validarCampos()) {
            try {
                Fornecedor f = new Fornecedor();
                int fornecedorId = Integer.parseInt(String.valueOf(cmb_fornecedor.getValue().charAt(0)));
                f.setId(fornecedorId);
                f = fornecedorDAO.buscaID(f);
                Produto produto = new Produto(f);
                produto.setId(Integer.parseInt(txt_id.getText()));
                produto.setEmbalagem(cmb_embalagem.getValue());
                produto.setNome(txt_nome.getText());
                produto.setPreco_custo(Double.parseDouble(txt_precoCusto.getText()));
                produto.setCodigo_barras(txt_codigoBarras.getText());
                produto.setPreco_venda(Double.parseDouble(txt_precoVenda.getText()));
                produto.setQuantidade(Integer.parseInt(txt_qtd.getText()));
                LocalDate selectedDate = date_inclusao.getValue();
                if (selectedDate != null) {
                    String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    produto.setData_compra(formattedDate);

                    if (produtoDAO.idExiste(produto.getId())) {
                        if (produtoDAO.altera(produto)) {
                            showAlert("Sucesso", "Produto atualizado com sucesso!", Alert.AlertType.INFORMATION);

                        } else {
                            showAlert("Erro", "Erro ao atualizar o produto.", Alert.AlertType.ERROR);
                        }
                    } else {
                        if (produtoDAO.insere(produto)) {
                            showAlert("Sucesso", "Produto salvo com sucesso!", Alert.AlertType.INFORMATION);

                        } else {
                            showAlert("Erro", "Erro ao inserir o produto.", Alert.AlertType.ERROR);
                        }
                    }
                    limparCampos();
                    searchId();
                    loadProdutoData();

                }
            } catch (Exception e) {
                if (e instanceof NumberFormatException) {
                    showAlert("Erro", "ID, preço ou quantidade inválidos. Por favor, insira valores válidos.", Alert.AlertType.ERROR);
                } else if (e instanceof SQLException) {
                    showAlert("Erro ao salvar produto", e.getMessage(), Alert.AlertType.ERROR);
                } else {
                    showAlert("Erro", "Ocorreu um erro inesperado.", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Erro", "Por favor, preencha todos os campos", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void btn_voltar_click(ActionEvent event) {
        try {
            Stage stage = (Stage) btn_voltar.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/tela_menu.fxml"));
            Parent root = loader.load();

            Stage menuStage = new Stage();
            menuStage.setScene(new Scene(root));
            menuStage.show();
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela do menu: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_cancelar_click(ActionEvent event) {
        limparCampos();
        searchId();
        btn_cancelar.setVisible(false);
    }

    @FXML
    private void btn_editar_click(ActionEvent event) {
        Produto produto = table_produto.getSelectionModel().getSelectedItem();
        if (produto != null) {
            btn_cancelar.setVisible(true);

            try {
                produto = produtoDAO.buscaID(produto);
                if (produto != null) {
                    preencherCampos(produto);
                    tabPane.getSelectionModel().select(0);

                } else {
                    showAlert("Erro", "Fornecedor não encontrado.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                showAlert("Erro ao buscar fornecedor", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erro", "Nenhum fornecedor selecionado.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_excluir_click(ActionEvent event) {
        Produto selectedProduto = table_produto.getSelectionModel().getSelectedItem();
        if (selectedProduto != null) {
            // Mostrar o diálogo de confirmação
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmação de Exclusão");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Deseja realmente excluir o produto?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Usuário confirmou a exclusão
                try {
                    boolean success = produtoDAO.remove(selectedProduto);
                    if (success) {
                        showAlert("Sucesso", "Produto deletado com sucesso!", Alert.AlertType.INFORMATION);
                        loadProdutoData();  // Recarregar a lista de produtos
                    } else {
                        showAlert("Erro", "Erro ao deletar o produto.", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Erro ao deletar produto", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Aviso", "Por favor, selecione um produto para deletar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void preencherCampos(Produto produto) throws SQLException {
        txt_id.setText(String.valueOf(produto.getId()));
        txt_nome.setText(produto.getNome());
        txt_precoVenda.setText(String.valueOf(produto.getPreco_venda()));
        txt_qtd.setText(String.valueOf(produto.getQuantidade()));
        txt_precoCusto.setText(String.valueOf(produto.getPreco_custo()));
        txt_codigoBarras.setText(produto.getCodigo_barras());
        int id = produto.getFornecedor().getId();
        Fornecedor f = new Fornecedor();
        f.setId(id);
        f = fornecedorDAO.buscaID(f);
        // Pegar o ID do fornecedor e definir no ComboBox
        cmb_fornecedor.getSelectionModel().select(String.valueOf(id) + " - " + f.getRazao_social());

        // Definir a data de inclusão
        if (produto.getData_compra() != null) {
            date_inclusao.setValue(LocalDate.parse(produto.getData_compra()));
        }

        // Definir a embalagem
        cmb_embalagem.getSelectionModel().select(produto.getEmbalagem());
    }

    @FXML
    public void initialize() {
        txt_filtrar_nome.textProperty().addListener((observable, oldValue, newValue) -> filterProdutos(newValue));

        searchId();
        loadProdutoData();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_qtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        ObservableList<String> embalagens = FXCollections.observableArrayList("Vasilhame", "Lata", "Fardo", "Caixa", "Retornável");
        if (!cmb_embalagem.getItems().containsAll(embalagens)) {
            cmb_embalagem.setItems(embalagens);
        }
        date_inclusao.setValue(LocalDate.now());
        loadFornecedorData();
    }

    private void filterProdutos(String filtro) {
        if (produtos == null) {
            return; // Certifique-se de que a lista não é nula antes de aplicar o filtro
        }

        List<Produto> filtrados = produtos.stream()
                .filter(p -> p.getNome().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());

        table_produto.setItems(FXCollections.observableArrayList(filtrados));
    }

    public void limparCampos() {
        date_inclusao.setValue(LocalDate.now());
        txt_id.clear();
        txt_nome.clear();
        txt_precoVenda.clear();
        txt_precoCusto.clear();
        txt_codigoBarras.clear();
        txt_filtrar_nome.clear();
        txt_qtd.setText("1");
        cmb_embalagem.getSelectionModel().clearSelection();
        cmb_fornecedor.getSelectionModel().clearSelection();
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

    private void searchId() {
        try {
            int nextId = produtoDAO.getNextId();
            txt_id.setText(String.valueOf(nextId));
            txt_id.setEditable(false); // Torna o campo somente leitura
        } catch (SQLException e) {
            showAlert("Erro ao buscar próximo ID", e.getMessage(), Alert.AlertType.ERROR);
        }
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
            if (cmb_fornecedor.getValue() == null || cmb_fornecedor.getValue().isEmpty()) {
                cmb_fornecedor.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-padding: 2;");
                todosPreenchidos = false;
            } else {
                // Resetar o estilo se o campo estiver preenchido
                cmb_fornecedor.setStyle("");
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

    private void loadFornecedorData() {
        ObservableList<String> fornecedores = FXCollections.observableArrayList();

        try {
            Collection<Fornecedor> listaFornecedores = fornecedorDAO.lista("");
            for (Fornecedor fornecedor : listaFornecedores) {
                fornecedores.add(fornecedor.getId() + " - " + fornecedor.getRazao_social());
            }
            cmb_fornecedor.setItems(fornecedores);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro ao carregar dados", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
