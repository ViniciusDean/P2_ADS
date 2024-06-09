package br.com.fatec.controller;

import br.com.fatec.DAO.FuncionarioDAO;
import br.com.fatec.model.Funcionario;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONObject;

public class FuncionarioCadastroController {

    @FXML
    private TextField txt_telefone, txt_filtrar_nome,
            txt_ddd, txt_id, txt_cep, txt_nome, txt_cpf, txt_email, txt_username, txt_logradouro, txt_bairro, txt_cidade, txt_estado;

    @FXML
    private PasswordField txt_senha;
    @FXML
    private ComboBox<String> cmb_contrato;

    @FXML
    private DatePicker data_nasc;

    @FXML
    private Button btn_continuar, btn_voltar, btn_salvar, btn_editar, btn_excluir, btn_pesquisar, btn_cancelar;

    @FXML
    private TableView<Funcionario> table_view;

    @FXML
    private TableColumn<Funcionario, Integer> col_id;

    @FXML
    private TableColumn<Funcionario, String> col_nome;

    @FXML
    private TableColumn<Funcionario, String> col_username;

    private ObservableList<Funcionario> funcionarios;
    private List<String> funcionarioCollection;
    @FXML
    private TabPane tabPane;
    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @FXML
    private void initialize() {
        searchId();
        cmb_contrato.setItems(FXCollections.observableArrayList("CLT", "PJ", "Temporário"));

        // Configurar as colunas da tabela
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        txt_filtrar_nome.textProperty().addListener((observable, oldValue, newValue) -> filterFuncionarios(newValue));

        loadFuncionarios();

    }

    @FXML
    private void btn_voltar_click() {
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
    private void btn_continuar_click() {
        tabPane.getSelectionModel().select(1);
    }

    @FXML
    private void btn_salvar_click() {
        try {
            if (funcionarioCollection == null) {
                funcionarioCollection = new ArrayList<>();
            }

            funcionarioCollection.clear();
            funcionarioCollection.add(txt_id.getText());         // Índice 0
            funcionarioCollection.add(txt_username.getText());   // Índice 1
            funcionarioCollection.add(txt_nome.getText());       // Índice 2
            funcionarioCollection.add(txt_email.getText());      // Índice 3
            funcionarioCollection.add(txt_ddd.getText() + txt_telefone.getText()); // Índice 4
            funcionarioCollection.add(txt_cpf.getText());        // Índice 5

            // Formatar a data para o formato YYYY-MM-DD
            LocalDate dataNasc = data_nasc.getValue();
            if (dataNasc == null) {
                showAlert("Erro de Validação", "Data de nascimento é obrigatória.", Alert.AlertType.ERROR);
                return;
            }
            String dataNascFormatada = dataNasc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            funcionarioCollection.add(dataNascFormatada);        // Índice 6
            funcionarioCollection.add(cmb_contrato.getValue());  // Índice 7
            funcionarioCollection.add(txt_cep.getText());        // Índice 8
            funcionarioCollection.add(txt_senha.getText());      // Índice 9

            if (funcionarioCollection.get(5).length() > 20) {
                showAlert("Erro de Validação", "CPF não pode exceder 20 caracteres.", Alert.AlertType.ERROR);
                return;
            }

            int id = Integer.parseInt(funcionarioCollection.get(0));
            Funcionario funcionario = new Funcionario(id, funcionarioCollection.get(2), funcionarioCollection.get(3), funcionarioCollection.get(5),
                    funcionarioCollection.get(6), funcionarioCollection.get(7), funcionarioCollection.get(8), funcionarioCollection.get(4), funcionarioCollection.get(1), funcionarioCollection.get(9));

            try {
                if (funcionarioDAO.idExiste(id)) {
                    if (funcionarioDAO.altera(funcionario)) {
                        limparCampos();
                        loadFuncionarios();  // Recarregar a lista de funcionários
                        showAlert("Sucesso", "Dados atualizados com sucesso.", Alert.AlertType.INFORMATION);
                        searchId();
                        tabPane.getSelectionModel().select(2);
                    } else {
                        showAlert("Erro", "Falha ao atualizar no banco de dados.", Alert.AlertType.ERROR);
                    }
                } else {
                    if (funcionarioDAO.insere(funcionario)) {
                        limparCampos();
                        loadFuncionarios();  // Recarregar a lista de funcionários
                        showAlert("Sucesso", "Dados inseridos com sucesso.", Alert.AlertType.INFORMATION);
                        searchId();
                        tabPane.getSelectionModel().select(0);
                    } else {
                        showAlert("Erro", "Falha ao inserir no banco de dados.", Alert.AlertType.ERROR);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erro de SQL", "Erro ao inserir no banco de dados: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            // Handle the exception if txt_id contains invalid integer
            showAlert("Erro de Formato", "ID deve ser um número.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_pesquisar_click() {
        String cep = txt_cep.getText().replaceAll("[^\\d]", "");  // Remove qualquer caractere não numérico
        buscarCep(cep);
    }

    @FXML
    private void btn_cancelar_click() {
        limparCampos();
        searchId();
        btn_cancelar.setVisible(false);
    }

    private void buscarCep(String cep) {
        if (cep.length() == 8) {
            String url = "https://opencep.com/v1/" + cep;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::processarResposta)
                    .join();
        } else {
            showAlert("Erro", "CEP Invalido!!", Alert.AlertType.ERROR);
        }
    }

    private void filterFuncionarios(String filtro) {
        if (funcionarios == null) {
            return; // Certifique-se de que a lista não é nula antes de aplicar o filtro
        }

        List<Funcionario> filtrados = funcionarios.stream()
                .filter(f -> f.getNome().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());

        table_view.setItems(FXCollections.observableArrayList(filtrados));
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

    private void loadFuncionarios() {
        try {
            if (funcionarios == null) {
                funcionarios = FXCollections.observableArrayList();
                table_view.setItems(funcionarios);
            } else {
                funcionarios.clear();
            }

            List<Funcionario> listaFuncionarios = (List<Funcionario>) funcionarioDAO.lista("");
            funcionarios.addAll(listaFuncionarios);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro de SQL", "Erro ao carregar funcionários: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        txt_logradouro.clear();
        txt_estado.clear();
        txt_bairro.clear();
        txt_cidade.clear();
        txt_id.clear();
        txt_username.clear();
        txt_nome.clear();
        txt_email.clear();
        txt_cpf.clear();
        data_nasc.setValue(null);
        cmb_contrato.setValue(null);
        txt_cep.clear();
        txt_ddd.clear();
        txt_telefone.clear();
        txt_senha.clear();
    }

    @FXML
    private void btn_editar_click() {
        Funcionario funcionarioSelecionado = table_view.getSelectionModel().getSelectedItem();

        if (funcionarioSelecionado == null) {
            showAlert("Erro de Validação", "Nenhum funcionário selecionado para edição.", Alert.AlertType.ERROR);
            return;
        }
        try {
            Funcionario funcionario = funcionarioDAO.buscaID(funcionarioSelecionado);
            if (funcionario == null) {
                showAlert("Erro de Validação", "Funcionário não encontrado no banco de dados.", Alert.AlertType.ERROR);
                return;
            }
            // Adicionar os dados do funcionário a uma coleção
            List<String> dadosFuncionario = new ArrayList<>();
            dadosFuncionario.add(String.valueOf(funcionario.getId())); // Índice 0
            dadosFuncionario.add(funcionario.getUsername());           // Índice 1
            dadosFuncionario.add(funcionario.getNome());               // Índice 2
            dadosFuncionario.add(funcionario.getEmail());              // Índice 3
            // Separar DDD e telefone
            String telefoneCompleto = funcionario.getTelefone();
            String ddd = telefoneCompleto.substring(0, 2);
            String telefone = telefoneCompleto.substring(2);
            dadosFuncionario.add(ddd);                                 // Índice 4
            dadosFuncionario.add(telefone);                            // Índice 5
            dadosFuncionario.add(funcionario.getCpf());                // Índice 6
            dadosFuncionario.add(funcionario.getDataNasc());          // Índice 7
            dadosFuncionario.add(funcionario.getContrato());           // Índice 8
            dadosFuncionario.add(funcionario.getCep());                // Índice 9
            dadosFuncionario.add(funcionario.getSenha());              // Índice 10
            // Distribuir os dados da coleção para os respectivos TextField
            txt_id.setText(dadosFuncionario.get(0));
            txt_username.setText(dadosFuncionario.get(1));
            txt_nome.setText(dadosFuncionario.get(2));
            txt_email.setText(dadosFuncionario.get(3));
            txt_ddd.setText(dadosFuncionario.get(4));
            txt_telefone.setText(dadosFuncionario.get(5));
            txt_cpf.setText(dadosFuncionario.get(6));
            data_nasc.setValue(LocalDate.parse(dadosFuncionario.get(7)));
            cmb_contrato.setValue(dadosFuncionario.get(8));
            txt_cep.setText(dadosFuncionario.get(9));
            txt_senha.setText(dadosFuncionario.get(10));
            btn_cancelar.setVisible(true);

            tabPane.getSelectionModel().select(0);
            buscarCep(dadosFuncionario.get(9));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro de SQL", "Erro ao buscar dados do banco de dados: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_excluir_click() {
        Funcionario funcionarioSelecionado = table_view.getSelectionModel().getSelectedItem();

        if (funcionarioSelecionado == null) {
            showAlert("Erro de Validação", "Nenhum funcionário selecionado para exclusão.", Alert.AlertType.ERROR);
            return;
        }

        // Adicionar o funcionário a ser excluído em uma coleção temporária
        List<Funcionario> funcionariosParaRemover = new ArrayList<>();
        funcionariosParaRemover.add(funcionarioSelecionado);

        try {
            // Remover os funcionários identificados da lista principal
            for (Funcionario funcionario : funcionariosParaRemover) {
                if (funcionarios.contains(funcionario)) {
                    funcionarios.remove(funcionario);
                    System.out.println("to aqui");
                }
            }

            // Remover o funcionário do banco de dados
            if (funcionarioDAO.remove(funcionarioSelecionado)) {
                table_view.getItems().remove(funcionarioSelecionado); // Remover do TableView
                table_view.refresh();  // Atualiza a tabela para refletir a remoção
                limparCampos();
                showAlert("Sucesso", "Dados excluídos com sucesso.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erro", "Falha ao excluir no banco de dados.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erro de SQL", "Erro ao excluir no banco de dados: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        loadFuncionarios();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void searchId() {
        try {
            int nextId = funcionarioDAO.getNextId();
            txt_id.setText(String.valueOf(nextId));
            txt_id.setEditable(false); // Torna o campo somente leitura
        } catch (SQLException e) {
            showAlert("Erro ao buscar próximo ID", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
