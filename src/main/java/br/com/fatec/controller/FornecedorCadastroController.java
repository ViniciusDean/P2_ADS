package br.com.fatec.controller;

import br.com.fatec.DAO.FornecedorDAO;
import br.com.fatec.DAO.ProdutoDAO;
import br.com.fatec.model.Fornecedor;
import br.com.fatec.model.Produto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

public class FornecedorCadastroController {

    String devolucao = "", cancelamento = "";

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField txt_ddd, txt_telefone, txt_logradouro, txt_cep,
            txt_bairro, txt_cidade, txt_estado, txt_razao,
            txt_cnpj, txt_email, txt_filtrar_nome, txt_id;

    @FXML
    private ComboBox<String> cmb_fornecedor, cmb_tributacao, cmb_frete;
    @FXML
    private TableView<Fornecedor> table_fornecedor;

    @FXML
    private RadioButton radio_devolucao, radio_cancelar;

    @FXML
    private Button btn_continuar, btn_voltar, btn_salvar, btn_editar, btn_excluir, btn_pesquisar, btn_cancelarEdit;
    @FXML
    private Tab tab_endereco, tab_dados, tab_consultar;
    @FXML
    private VBox root;
    @FXML
    private TableColumn<Fornecedor, String> col_id, col_cnpj, col_razao;

    private FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    private ObservableList<Fornecedor> fornecedores;

    @FXML
    private void btn_continuar_click() {
        if (validarCampos()) {
            tab_dados.setDisable(false);
            tabPane.getSelectionModel().select(tab_dados);
        } else {
            showAlert("Erro", "Por favor, preencha todos os campos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_cancelarEdit_click() {

    }

    @FXML
    private void btn_voltar_click() {
        tabPane.getSelectionModel().selectPrevious();
    }

    @FXML
    private void btn_salvar_click() {
        validarSalvamento();
    }

    @FXML
    private void btn_editar_click() {
        Fornecedor fornecedorSelecionado = table_fornecedor.getSelectionModel().getSelectedItem();
        if (fornecedorSelecionado != null) {
            try {
                Fornecedor fornecedor = fornecedorDAO.buscaID(fornecedorSelecionado);
                if (fornecedor != null) {
                    preencherCampos(fornecedor);
                    buscarCep(fornecedor.getCep());
                    btn_cancelarEdit.setDisable(false); // Habilita o botão de cancelar edição
                    tabPane.getSelectionModel().select(0);
                    tab_dados.setDisable(false);
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

    private void deleteFornecedorAndProdutos(Fornecedor fornecedor) throws SQLException {
        List<Produto> produtos = fornecedorDAO.getProdutosDoFornecedor(fornecedor);
        if (!produtos.isEmpty()) {
            for (Produto produto : produtos) {
                try {
                    produtoDAO.remove(produto);
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Erro ao Excluir", "Não foi possível excluir um dos produtos: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            fornecedorDAO.remove(fornecedor);

        }
    }

    @FXML
    private void btn_excluir_click() {
        Fornecedor fornecedorSelecionado = table_fornecedor.getSelectionModel().getSelectedItem();
        if (fornecedorSelecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText(null);

            VBox content = new VBox();
            Label label = new Label("Tem certeza que deseja deletar o fornecedor e seus ");
            Hyperlink link = new Hyperlink("produtos?");
            link.setOnAction(e -> {
                try {
                    showProducts(fornecedorSelecionado);
                } catch (SQLException ex) {
                    showAlert("ERRO", "NÃO FOI POSSIVEL DELETAR", Alert.AlertType.ERROR);
                }
            });

            content.getChildren().addAll(label, link);
            alert.getDialogPane().setContent(content);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        deleteFornecedorAndProdutos(fornecedorSelecionado);
                        loadFornecedorData();
                    } catch (SQLException ex) {
                        showAlert("ERRO", "NÃO FOI POSSIVEL DELETAR", Alert.AlertType.ERROR);
                    }
                }
            });

        }
    }

    private void showProducts(Fornecedor fornecedor) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/Deleteprodutos.fxml"));
            Parent root = loader.load();
            DeleteprodutosController controller = loader.getController();
            controller.setProdutos(fornecedorDAO.getProdutosDoFornecedor(fornecedor));

            Stage stage = new Stage();
            stage.setTitle("Produtos Associados");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erro", "Não foi possível abrir a janela de produtos", AlertType.ERROR);
        }
    }

    @FXML
    private void btn_pesquisar_click() {
        String cep = txt_cep.getText().replaceAll("[^\\d]", "");  // Remove qualquer caractere não numérico
        buscarCep(cep);
    }

    private void buscarCep(String cep) {
        if (cep.length() == 8) {
            String url = "https://opencep.com/v1/" + cep;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::processarResposta)
                    .join();
        } else {
            showAlert("Erro", "CEP Invalido!!", Alert.AlertType.ERROR);
        }
    }

    private void processarResposta(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);

            // Assegurar que todos os campos necessários estão presentes
            if (jsonObject.has("logradouro") && jsonObject.has("bairro") && jsonObject.has("localidade") && jsonObject.has("uf")) {
                String logradouro = jsonObject.getString("logradouro");
                String bairro = jsonObject.getString("bairro");
                String cidade = jsonObject.getString("localidade");
                String estado = jsonObject.getString("uf");

                // Atualiza campos na thread da GUI
                Platform.runLater(() -> {
                    txt_logradouro.setText(logradouro);
                    txt_bairro.setText(bairro);
                    txt_cidade.setText(cidade);
                    txt_estado.setText(estado);
                });
            } else {
                throw new Exception("Dados incompletos recebidos da API.");
            }
        } catch (Exception e) {
            // Trata qualquer JSONException ou outra exceção aqui
            Platform.runLater(() -> {
                showAlert("Erro", "Houve um problema ao processar os dados", Alert.AlertType.ERROR);
            });
        }
    }

    private void showAlert(String title, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isNumeric(TextField tf) {
        return Pattern.matches("\\d+", tf.getText());
    }

    private void searchId() {
        try {
            int nextId = fornecedorDAO.getNextId();
            txt_id.setText(String.valueOf(nextId));
            txt_id.setEditable(false); // Torna o campo somente leitura
        } catch (SQLException e) {
            showAlert("Erro ao buscar próximo ID", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void initialize() {
        searchId();

        loadFornecedorData();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_cnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        col_razao.setCellValueFactory(new PropertyValueFactory<>("razao_social"));

        tab_dados.setDisable(true);
        // Itens para cmb_fornecedor
        List<String> fornecedorItems = Arrays.asList("DISTRIBUIDOR", "ATACADO", "VAREJO");
        fornecedorItems.forEach(item -> {
            if (!cmb_fornecedor.getItems().contains(item)) {
                cmb_fornecedor.getItems().add(item);
            }
        });

        // Itens para cmb_tributacao
        List<String> tributacaoItems = Arrays.asList("ESPECIAL", "SIMPLES");
        tributacaoItems.forEach(item -> {
            if (!cmb_tributacao.getItems().contains(item)) {
                cmb_tributacao.getItems().add(item);
            }
        });

        // Itens para cmb_frete
        List<String> freteItems = Arrays.asList("TERCEIROS", "SEM FRETE", "FRETE PRÓPRIO");
        freteItems.forEach(item -> {
            if (!cmb_frete.getItems().contains(item)) {
                cmb_frete.getItems().add(item);
            }
        });
        txt_filtrar_nome.textProperty().addListener((observable, oldValue, newValue) -> filterFornecedores(newValue));
    }

    private void filterFornecedores(String filtro) {
        if (fornecedores == null) {
            return; // Certifique-se de que a lista não é nula antes de aplicar o filtro
        }

        List<Fornecedor> filtrados = fornecedores.stream()
                .filter(f -> f.getRazao_social().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());

        table_fornecedor.setItems(FXCollections.observableArrayList(filtrados));
    }

    private boolean validarCampos() {
        boolean todosPreenchidos = true;

        // Lista de todos os TextField que devem ser validados
        TextField[] campos = {txt_ddd, txt_telefone,
            txt_logradouro, txt_cep,
            txt_bairro, txt_cidade, txt_estado};

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

    private void loadFornecedorData() {
        fornecedores = FXCollections.observableArrayList();

        try {
            Collection<Fornecedor> listaFornecedores = fornecedorDAO.lista("");
            fornecedores.addAll(listaFornecedores);
        } catch (SQLException e) {
            e.printStackTrace();
            // Mostrar alerta de erro, se necessário
            showAlert("Erro ao carregar dados", e.getMessage(), Alert.AlertType.ERROR);
        }
        table_fornecedor.getItems().clear();
        table_fornecedor.setItems(fornecedores);
    }

    private void validarSalvamento() {
        boolean todosPreenchidos = true;

        // Lista de todos os TextField que devem ser validados
        TextField[] campos = {txt_ddd, txt_telefone, txt_logradouro, txt_cep,
            txt_bairro, txt_cidade, txt_estado, txt_razao, txt_cnpj, txt_email};

        for (TextField campo : campos) {
            if (campo.getText().isEmpty()) {
                campo.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-padding: 2;");
                todosPreenchidos = false;
            } else {

                campo.setStyle("");
            }
        }

        if (!todosPreenchidos) {
            showAlert("Erro", "Preencha todos os campos", Alert.AlertType.ERROR);
        } else {
            devolucao = radio_devolucao.isSelected() ? "S" : "N";
            cancelamento = radio_cancelar.isSelected() ? "S" : "N";
            salvarDados();
            limparCampos();
            searchId();
            tab_dados.setDisable(true);

            tabPane.getSelectionModel().select(tab_endereco);

        }
    }

    @FXML
    private void limparCampos() {
        TextField[] campos = {
            txt_id, txt_ddd, txt_telefone, txt_logradouro, txt_cep,
            txt_bairro, txt_cidade, txt_estado, txt_razao,
            txt_cnpj, txt_email
        };

        for (TextField campo : campos) {
            campo.clear();
            campo.setStyle(""); // Reseta o estilo dos campos
        }

        cmb_fornecedor.getSelectionModel().clearSelection();
        cmb_tributacao.getSelectionModel().clearSelection();
        cmb_frete.getSelectionModel().clearSelection();

        radio_devolucao.setSelected(false);
        radio_cancelar.setSelected(false);

        try {
            int nextId = fornecedorDAO.getNextId();
            txt_id.setText(String.valueOf(nextId));
            txt_id.setEditable(false); // Torna o campo somente leitura
        } catch (SQLException e) {
            showAlert("Erro ao buscar próximo ID", e.getMessage(), Alert.AlertType.ERROR);
        }

        // Reseta o TabPane para a primeira aba
        tabPane.getSelectionModel().select(0);
        btn_cancelarEdit.setDisable(true); // Desativa o botão de cancelar edição
    }

    private void salvarDados() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(Integer.parseInt(txt_id.getText()));
        fornecedor.setTipo_fornecedor(cmb_fornecedor.getValue());
        fornecedor.setRegime_tributacao(cmb_tributacao.getValue());
        fornecedor.setTipo_frete(cmb_frete.getValue());
        fornecedor.setCep(txt_cep.getText());
        fornecedor.setTelefone(txt_ddd.getText() + " " + txt_telefone.getText());
        fornecedor.setRazao_social(txt_razao.getText());
        fornecedor.setEmail(txt_email.getText());
        fornecedor.setCnpj(txt_cnpj.getText());
        fornecedor.setDevolucao(devolucao.charAt(0));
        fornecedor.setCancelamento(cancelamento.charAt(0));
        fornecedor.setDevolucao(devolucao.charAt(0));
        fornecedor.setCancelamento(cancelamento.charAt(0));

        try {
            if (fornecedorDAO.idExiste(fornecedor.getId())) {
                if (fornecedorDAO.altera(fornecedor)) {
                    showAlert("Sucesso", "Fornecedor atualizado com sucesso!", Alert.AlertType.INFORMATION);
                    loadFornecedorData();
                } else {
                    showAlert("Erro", "Erro ao atualizar o fornecedor.", Alert.AlertType.ERROR);
                }
            } else {
                if (fornecedorDAO.insere(fornecedor)) {
                    showAlert("Sucesso", "Fornecedor salvo com sucesso!", Alert.AlertType.CONFIRMATION);
                    loadFornecedorData();

                } else {
                    showAlert("Erro", "Erro ao inserir o fornecedor.", Alert.AlertType.ERROR);
                }
            }

            limparCampos();
        } catch (NumberFormatException e) {
            showAlert("Erro", "ID inválido. Por favor, insira um número válido.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erro ao salvar fornecedor", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void preencherCampos(Fornecedor fornecedor) {
        txt_id.setText(String.valueOf(fornecedor.getId()));
        txt_cep.setText(fornecedor.getCep());
        txt_razao.setText(fornecedor.getRazao_social());
        txt_cnpj.setText(fornecedor.getCnpj());
        txt_email.setText(fornecedor.getEmail());
        cmb_fornecedor.getSelectionModel().select(fornecedor.getTipo_fornecedor());
        cmb_tributacao.getSelectionModel().select(fornecedor.getRegime_tributacao());
        cmb_frete.getSelectionModel().select(fornecedor.getTipo_frete());
        radio_devolucao.setSelected(fornecedor.getDevolucao() == 'S');
        radio_cancelar.setSelected(fornecedor.getCancelamento() == 'S');
        String telefoneCompleto = fornecedor.getTelefone();
        if (telefoneCompleto != null && telefoneCompleto.length() >= 2) {
            txt_ddd.setText(telefoneCompleto.substring(0, 2)); // Os primeiros 2 dígitos são o DDD
            txt_telefone.setText(telefoneCompleto.substring(3)); // O restante é o telefone
        } else {
            txt_ddd.setText("");
            txt_telefone.setText(telefoneCompleto); // Caso o telefone esteja incorreto, ele será exibido inteiro
        }
    }

}
