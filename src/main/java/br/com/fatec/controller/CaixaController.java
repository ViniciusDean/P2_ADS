package br.com.fatec.controller;

import br.com.fatec.DAO.ClienteDAO;
import br.com.fatec.DAO.ProdutoDAO;
import br.com.fatec.DAO.VendaDAO;
import br.com.fatec.model.Cliente;
import br.com.fatec.model.Funcionario;
import br.com.fatec.model.Produto;
import br.com.fatec.model.Venda;
import br.com.fatec.model.VendaProduto;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CaixaController {

    @FXML
    private TextField txt_cpf, txt_nome, txt_id, txt_codBarras;

    @FXML
    private TableView<Produto> table_produtos;

    @FXML
    private TableColumn<Produto, Integer> col_id;

    @FXML
    private TableColumn<Produto, String> col_nome;

    @FXML
    private TableColumn<Produto, Double> col_preco;

    @FXML
    private Button btn_confirmar, btn_voltar, btn_adicionar, btn_pesquisar;
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ObservableList<Produto> produtos;
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private Funcionario operadorLogado;
    private VendaDAO vendaDAO = new VendaDAO();

    @FXML
    private void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_preco.setCellValueFactory(new PropertyValueFactory<>("preco_venda"));

        produtos = FXCollections.observableArrayList();
        table_produtos.setItems(produtos);
    }

    @FXML
    private void btn_adicionar_click() {
        adicionarProduto(txt_codBarras.getText());
    }

    @FXML
    private void btn_pesquisar_click() {
        try {
            Cliente cliente = clienteDAO.buscaPorCPF(txt_cpf.getText());
            if (cliente != null) {
                txt_id.setText(String.valueOf(cliente.getId()));
                txt_nome.setText(cliente.getNome());
            } else {
                showAlert("Cliente não encontrado", "Cliente não encontrado, insira os dados manualmente.", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Erro de SQL", "Erro ao buscar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void adicionarProduto(String codBarras) {
        try {
            Produto produto = produtoDAO.buscaPorCodigoBarras(codBarras);
            if (produto != null) {
                boolean produtoExistente = false;
                for (Produto p : produtos) {
                    if (codBarras.equals(p.getCodigo_barras())) {
                        p.setQuantidade(p.getQuantidade() + 1);
                        table_produtos.refresh();
                        produtoExistente = true;
                        break;
                    }
                }
                if (!produtoExistente) {
                    produto.setQuantidade(1); // Define a quantidade inicial como 1
                    produtos.add(produto);
                }
            } else {
                showAlert("Erro", "Produto não encontrado!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Erro de SQL", "Erro ao buscar produto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_confirmar_click() {
        try {
            if (produtos.isEmpty()) {
                showAlert("Erro", "Nenhum produto adicionado!", Alert.AlertType.ERROR);
                return;
            }
            Cliente cliente = new Cliente();
            cliente.setId(Integer.parseInt(txt_id.getText()));

            List<VendaProduto> vendaProdutos = new ArrayList<>();
            for (Produto produto : produtos) {
                VendaProduto vendaProduto = new VendaProduto();
                vendaProduto.setProduto(produto);
                vendaProduto.setQuantidade(produto.getQuantidade());
                vendaProdutos.add(vendaProduto);
            }

            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setOperador(operadorLogado); // Adiciona o operador logado
            venda.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            venda.setProdutos(vendaProdutos);

            boolean vendaInserida = vendaDAO.insere(venda);
            if (vendaInserida) {
                showAlert("Sucesso", "Venda realizada com sucesso!", Alert.AlertType.INFORMATION);
                limparCampos();
            } else {
                showAlert("Erro", "Erro ao realizar a venda!", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Erro de SQL", "Erro ao realizar a venda: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Erro", "ID do cliente inválido!", Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        txt_cpf.clear();
        txt_nome.clear();
        txt_id.clear();
        txt_codBarras.clear();
        produtos.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    public void setOperadorLogado(Funcionario operadorLogado) {
        this.operadorLogado = operadorLogado;
    }
}
