package br.com.fatec.controller;

import br.com.fatec.DAO.FornecedorDAO;
import br.com.fatec.model.Fornecedor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tab;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class FornecedorCadastroController {

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField txt_ddd, txt_telefone, txt_cod, txt_logradouro, txt_cep,
            txt_bairro, txt_cidade, txt_estado, txt_razao,
            txt_cnpj, txt_email, txt_filtrar_nome;

    @FXML
    private ComboBox<String> cmb_fornecedor, cmb_tributacao, cmb_frete;

    @FXML
    private RadioButton radio_devolucao, radio_cancelar;

    @FXML
    private Button btn_continuar, btn_voltar, btn_salvar, btn_editar, btn_excluir, btn_pesquisar;
    @FXML
    private Tab tab_endereco, tab_dados, tab_consultar;
    @FXML
    private VBox root;
    
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    @FXML
private void btn_continuar_click() {
    if (validarCampos()) {
        tab_dados.setDisable(false);
        tabPane.getSelectionModel().select(tab_dados);
    } else {
        showAlert("Erro de Validação", "Por favor, preencha todos os campos necessários corretamente antes de continuar.");
    }
}


    @FXML
    private void btn_voltar_click() {
        tabPane.getSelectionModel().selectPrevious();
    }

    @FXML
    private void btn_salvar_click() {
        // Implementar lógica de salvar
    }

    @FXML
    private void btn_editar_click() {
        // Implementar lógica de editar
    }

    @FXML
    private void btn_excluir_click() {
        // Implementar lógica de excluir
    }

    @FXML
    private void btn_pesquisar_click() {
        String cep = txt_cep.getText().replaceAll("[^\\d]", "");  // Remove qualquer caractere não numérico
        if (cep.length() == 8) {  // CEP brasileiro tem 8 dígitos
            String url = "https://opencep.com/v1/" + cep;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::processarResposta)
                    .join();  // Usado para esperar a resposta, pode ser removido se a chamada pode ser assíncrona
        } else {
            showAlert("Erro de Validação", "CEP inválido. Por favor, insira um CEP com 8 dígitos.");
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
            showAlert("Erro de Processamento", "Erro ao processar os dados do CEP: " + e.getMessage());
        });
    }
}

private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}
        

    private boolean isNumeric(TextField tf) {
        return Pattern.matches("\\d+", tf.getText());
    }
@FXML
    public void initialize() {  
       btn_salvar.setOnAction(event -> validarSalvamento());
       //  root.getStylesheets().add(getClass().getResource("@/br/com/fatec/css/Fornecedor.css").toExternalForm());
         tab_dados.setDisable(true);
        // Itens para cmb_fornecedor
        List<String> fornecedorItems = Arrays.asList("Distribuidor", "Atacado", "Varejo");
        fornecedorItems.forEach(item -> {
            if (!cmb_fornecedor.getItems().contains(item)) {
                cmb_fornecedor.getItems().add(item);
            }
        });

        // Itens para cmb_tributacao
        List<String> tributacaoItems = Arrays.asList("Especial", "Simples");
        tributacaoItems.forEach(item -> {
            if (!cmb_tributacao.getItems().contains(item)) {
                cmb_tributacao.getItems().add(item);
            }
        });

        // Itens para cmb_frete
        List<String> freteItems = Arrays.asList("Terceiros", "Sem frete", "Frete próprio");
        freteItems.forEach(item -> {
            if (!cmb_frete.getItems().contains(item)) {
                cmb_frete.getItems().add(item);
            }
        });
    }

    private boolean validarCampos() {
        boolean todosPreenchidos = true;

        // Lista de todos os TextField que devem ser validados
        TextField[] campos = {txt_ddd, txt_telefone, txt_cod, txt_logradouro, txt_cep,
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
     private void validarSalvamento() {
        boolean todosPreenchidos = true;

        // Lista de todos os TextField que devem ser validados
        TextField[] campos = {txt_ddd, txt_telefone, txt_cod, txt_logradouro, txt_cep,
            txt_bairro, txt_cidade, txt_estado, txt_razao, txt_cnpj, txt_email};

        for (TextField campo : campos) {
            if (campo.getText().isEmpty()) {
                campo.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-padding: 2;");
                todosPreenchidos = false;
            } else {
                // Resetar o estilo se o campo estiver preenchido
                campo.setStyle("");
            }
        }

        if (!todosPreenchidos) {
            showAlert("Erro de Validação", "Por favor, preencha todos os campos.");
        } else {
            String devolucao = radio_devolucao.isSelected() ? "S" : "N";
            String cancelamento = radio_cancelar.isSelected() ? "S" : "N";

               salvarDados(txt_ddd.getText(), txt_telefone.getText(), txt_cod.getText(), txt_logradouro.getText(), txt_cep.getText(), 
                        txt_bairro.getText(), txt_cidade.getText(), txt_estado.getText(), txt_razao.getText(), 
                        txt_cnpj.getText(), txt_email.getText(), devolucao, cancelamento,cmb_frete.);
        
               
    }
     }
        
          private void salvarDados(String ddd, String telefone, String cod, String logradouro, String cep, 
                             String bairro, String cidade, String estado, String razao, 
                             String cnpj, String email, String devolucao, String cancelamento, String frete) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setLogradouro(logradouro);
        fornecedor.setCep(cep);
        fornecedor.setTelefone(ddd + telefone);
        fornecedor.setRazao_social(razao);
        fornecedor.setEmail(email);
        fornecedor.setCnpj(cnpj);
        fornecedor.setTipo_frete(frete);
        
        fornecedor.setDevolucao(devolucao.charAt(0));
        fornecedor.setCancelamento(cancelamento.charAt(0));
        
        try {
            if (fornecedorDAO.insere(fornecedor)) {
                showAlert("Sucesso", "Dados salvos com sucesso!");
            } else {
                showAlert("Erro", "Erro ao salvar os dados.");
            }
        } catch (Exception e) {
            showAlert("Erro", "Erro ao salvar os dados: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
