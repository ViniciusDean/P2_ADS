
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProdutoCadastroController {

    @FXML
    private Tab tab_produto, tab_consultar;

    @FXML
    private TextField txt_id, txt_nome, txt_precoVenda, txt_precoCusto, txt_codigoBarras, txt_lucro, txt_filtrar_nome;

    @FXML
    private ComboBox<String> cmb_embalagem, cmb_fornecedor;

    @FXML
    private Spinner<Integer> cmb_qtd;

    @FXML
    private DatePicker date_inclusao;

    @FXML
    private Button btn_salvar, btn_voltar, btn_editar, btn_excluir;

    @FXML
    private TableView<?> table_produto;

    @FXML
    private void btn_salvar_click(ActionEvent event) {
        // Lógica para salvar os dados do produto
    }

    @FXML
    private void btn_voltar_click(ActionEvent event) {
        // Lógica para voltar para a tela anterior
    }

    @FXML
    private void btn_editar_click(ActionEvent event) {
        // Lógica para editar um produto selecionado
    }

    @FXML
    private void btn_excluir_click(ActionEvent event) {
        // Lógica para excluir um produto selecionado
    }

    public void limparCampos() {
        txt_id.clear();
        txt_nome.clear();
        txt_precoVenda.clear();
        txt_precoCusto.clear();
        txt_codigoBarras.clear();
        txt_lucro.clear();
        txt_filtrar_nome.clear();
    }
}
