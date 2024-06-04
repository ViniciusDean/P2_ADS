package br.com.fatec.DAO;

import br.com.fatec.model.Fornecedor;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class FornecedorDAO implements DAO<Fornecedor> {

    private Fornecedor fornecedor;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Fornecedor model) throws SQLException {
        String sql = "INSERT INTO fornecedor (logradouro, cep, telefone, razao_social, email, cnpj, regime_tributacao, tipo_frete, devolucao, cancelamento, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, model.getLogradouro());
        pst.setString(2, model.getCep());
        pst.setString(3, model.getTelefone());
        pst.setString(4, model.getRazao_social());
        pst.setString(5, model.getEmail());
        pst.setString(6, model.getCnpj());
        pst.setString(7, model.getRegime_tributacao());
        pst.setString(8, model.getTipo_frete());
        pst.setString(9, String.valueOf(model.getDevolucao()));
        pst.setString(10, String.valueOf(model.getCancelamento()));
        pst.setInt(11, model.getId());

        if (pst.executeUpdate() >= 1) {
            Banco.desconectar();
            return true;
        }
        Banco.desconectar();
        return false;
    }

    @Override
    public boolean remove(Fornecedor model) throws SQLException {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, model.getId());
        if (pst.executeUpdate() >= 1) {
            Banco.desconectar();
            return true;
        }
        Banco.desconectar();
        return false;
    }

    @Override
    public boolean altera(Fornecedor model) throws SQLException {
        String sql = "UPDATE fornecedor SET logradouro = ?, cep = ?, telefone = ?, razao_social = ?, email = ?, cnpj = ?, regime_tributacao = ?, tipo_frete = ?, devolucao = ?, cancelamento = ? WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(11, model.getId());
        pst.setString(1, model.getLogradouro());
        pst.setString(2, model.getCep());
        pst.setString(3, model.getTelefone());
        pst.setString(4, model.getRazao_social());
        pst.setString(5, model.getEmail());
        pst.setString(6, model.getCnpj());
        pst.setString(7, model.getRegime_tributacao());
        pst.setString(8, model.getTipo_frete());
        pst.setString(9, String.valueOf(model.getDevolucao()));
        pst.setString(10, String.valueOf(model.getCancelamento()));

        if (pst.executeUpdate() >= 1) {
            Banco.desconectar();
            return true;
        }
        Banco.desconectar();
        return false;
    }

    @Override
    public Fornecedor buscaID(Fornecedor model) throws SQLException {
        Fornecedor fornecedor = null;
        String sql = "SELECT * FROM fornecedor WHERE id = ?;";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, model.getId());
        rs = pst.executeQuery();
        if (rs.next()) {
            fornecedor = new Fornecedor();
            fornecedor.setId(rs.getInt("id"));
            fornecedor.setLogradouro(rs.getString("logradouro"));
            fornecedor.setCep(rs.getString("cep"));
            fornecedor.setTelefone(rs.getString("telefone"));
            fornecedor.setRazao_social(rs.getString("razao_social"));
            fornecedor.setEmail(rs.getString("email"));
            fornecedor.setCnpj(rs.getString("cnpj"));
            fornecedor.setRegime_tributacao(rs.getString("regime_tributacao"));
            fornecedor.setTipo_frete(rs.getString("tipo_frete"));
            fornecedor.setDevolucao(rs.getString("devolucao").charAt(0));
            fornecedor.setCancelamento(rs.getString("cancelamento").charAt(0));
        }
        Banco.desconectar();
        return fornecedor;
    }

    @Override
    public Collection<Fornecedor> lista(String criterio) throws SQLException {
        Collection<Fornecedor> listagem = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor " + (criterio.isEmpty() ? "" : " WHERE " + criterio);
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setId(rs.getInt("id"));
            fornecedor.setLogradouro(rs.getString("logradouro"));
            fornecedor.setCep(rs.getString("cep"));
            fornecedor.setTelefone(rs.getString("telefone"));
            fornecedor.setRazao_social(rs.getString("razao_social"));
            fornecedor.setEmail(rs.getString("email"));
            fornecedor.setCnpj(rs.getString("cnpj"));
            fornecedor.setRegime_tributacao(rs.getString("regime_tributacao"));
            fornecedor.setTipo_frete(rs.getString("tipo_frete"));
            fornecedor.setDevolucao(rs.getString("devolucao").charAt(0));
            fornecedor.setCancelamento(rs.getString("cancelamento").charAt(0));
            listagem.add(fornecedor);
        }
        Banco.desconectar();
        return listagem;
    }

    public int getNextId() throws SQLException {
        Banco.conectar();
        int nextId = 1;
        String sql = "SELECT MAX(id) FROM fornecedor";
        pst = Banco.obterConexao().prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            nextId = rs.getInt(1) + 1;
        }

        rs.close();
        pst.close();
        Banco.desconectar();

        return nextId;
    }

}
