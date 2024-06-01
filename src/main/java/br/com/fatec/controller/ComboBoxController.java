/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fatec.controller;

import br.com.fatec.model.Produto;
import br.com.fatec.persistencia.Banco;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class ComboBoxController implements Initializable {

    @FXML
    private ComboBox<Produto> cbProduto;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtDescricao;
    @FXML
    private TextField txtPreco;
    @FXML
    private Button btnPreencher;
    @FXML
    private Button btnExibir;

    //variaveis auxiliares
    //collection para os produtos aparecerem na comboBox
    private ObservableList<Produto> listaProdutos
            = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //interliga a coleção junto com a comboBox
        cbProduto.setItems(listaProdutos);
        //programa o evento change da combo
        configuraChangeValueComboProduto();

        try {
            Banco.conectar();
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            
            alerta.setTitle("Banco De dados");
            alerta.setHeaderText("Conexão OK");
            alerta.setContentText("MY SQL WORKBENCH");
            alerta.showAndWait(); //exibe a mensage
            Banco.desconectar();
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Banco De dados");
            alerta.setHeaderText("Conexão FALHOU");
            alerta.setContentText("MY SQL WORKBENCH");
            alerta.showAndWait(); 

        }

    }

    @FXML
    private void btnPreencher_Click(ActionEvent event) {
        //limpando a comboBox antes
        listaProdutos.clear();

        listaProdutos.add(new Produto(1, "Caneta", 5.87));
        listaProdutos.add(new Produto(2, "Regua", 10.20));
        listaProdutos.add(new Produto(3, "Compasso", 15.80));
    }

    /**
     * Exibe os dados do elemento selecionado na comboBox dentro de cada
     * textField correspondente
     *
     * @param event
     */
    @FXML
    private void btnExibir_Click(ActionEvent event) {
        //verifica se algum item está selecionado
        if (cbProduto.getValue() == null) {
            //exibe mensagem
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Mensagem");
            alerta.setHeaderText("Favor selecionar um Item");
            alerta.setContentText("");

            alerta.showAndWait(); //exibe a mensage
        } else {
            //alguem está selecionado
            Produto p = cbProduto.getValue();
            txtCodigo.setText(Integer.toString(p.getCodigo()));
            txtDescricao.setText(p.getDescricao());
            txtPreco.setText(String.valueOf(p.getPreco()));
        }
    }

    private void configuraChangeValueComboProduto() {
        //programando o evento change da combo para
        //exibir seu conteudo nos texts
        cbProduto.valueProperty().addListener((value, velho, novo) -> {
            if (novo != null) {
                txtCodigo.setText(Integer.toString(novo.getCodigo()));
                txtDescricao.setText(novo.getDescricao());
                txtPreco.setText(String.valueOf(novo.getPreco()));
            }
        });
    }

}
