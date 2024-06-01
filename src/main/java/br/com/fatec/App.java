package br.com.fatec;

import br.com.fatec.DAO.FornecedorDAO;
import br.com.fatec.model.Proprietario;
import br.com.fatec.persistencia.Banco;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
           try {
            Banco.conectar(); // Verifica a conexão com o banco de dados na inicialização
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Conexão com o Banco de Dados");
            alert.setHeaderText(null);
            alert.setContentText("Erro ao conectar ao banco de dados: " + e.getMessage());
            alert.showAndWait();
            System.exit(1); // Encerra o programa se a conexão com o banco de dados falhar
        }
        scene = new Scene(loadFXML("view/fornecedor_cadastro"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
