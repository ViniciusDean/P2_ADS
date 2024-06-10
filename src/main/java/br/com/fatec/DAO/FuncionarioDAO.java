package br.com.fatec.DAO;

import br.com.fatec.model.Funcionario;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class FuncionarioDAO implements DAO<Funcionario> {

    private Funcionario funcionario;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionario (id, username, nome, email, telefone, senha, cpf, data_nasc, contrato, CEP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, funcionario.getId());
        pst.setString(2, funcionario.getUsername());
        pst.setString(3, funcionario.getNome());
        pst.setString(4, funcionario.getEmail());
        pst.setString(5, funcionario.getTelefone());
        pst.setString(6, funcionario.getSenha());
        pst.setString(7, funcionario.getCpf());
        pst.setString(8, funcionario.getDataNasc());
        pst.setString(9, funcionario.getContrato());
        pst.setString(10, funcionario.getCep());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    @Override
    public boolean altera(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionario SET username = ?, nome = ?, email = ?, telefone = ?, senha = ?, cpf = ?, data_nasc = ?, contrato = ?, CEP = ? WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, funcionario.getUsername());
        pst.setString(2, funcionario.getNome());
        pst.setString(3, funcionario.getEmail());
        pst.setString(4, funcionario.getTelefone());
        pst.setString(5, funcionario.getSenha());
        pst.setString(6, funcionario.getCpf());
        pst.setString(7, funcionario.getDataNasc());
        pst.setString(8, funcionario.getContrato());
        pst.setString(9, funcionario.getCep());
        pst.setInt(10, funcionario.getId());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    @Override
    public Funcionario buscaID(Funcionario model) throws SQLException {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, model.getId());
        rs = pst.executeQuery();

        if (rs.next()) {
            funcionario = new Funcionario();
            funcionario.setId(rs.getInt("id"));
            funcionario.setUsername(rs.getString("username"));
            funcionario.setNome(rs.getString("nome"));
            funcionario.setEmail(rs.getString("email"));
            funcionario.setTelefone(rs.getString("telefone"));
            funcionario.setSenha(rs.getString("senha"));
            funcionario.setCpf(rs.getString("cpf"));
            funcionario.setDataNasc(rs.getString("data_nasc"));
            funcionario.setContrato(rs.getString("contrato"));
            funcionario.setCep(rs.getString("CEP"));
        }

        Banco.desconectar();
        return funcionario;
    }

    @Override
    public boolean remove(Funcionario model) throws SQLException {
        String sqlDeleteVendaProduto = "DELETE FROM venda_produto WHERE venda_id IN (SELECT id FROM venda WHERE operador_id = ?)";
        String sqlDeleteVendas = "DELETE FROM venda WHERE operador_id = ?";
        String sqlDeleteFuncionario = "DELETE FROM funcionario WHERE id = ?";

        Banco.conectar();

        try {
            // Deletar os registros de venda_produto associados às vendas do funcionário
            pst = Banco.obterConexao().prepareStatement(sqlDeleteVendaProduto);
            pst.setInt(1, model.getId());
            pst.executeUpdate();

            // Deletar as vendas associadas ao funcionário
            pst = Banco.obterConexao().prepareStatement(sqlDeleteVendas);
            pst.setInt(1, model.getId());
            pst.executeUpdate();

            // Deletar o funcionário
            pst = Banco.obterConexao().prepareStatement(sqlDeleteFuncionario);
            pst.setInt(1, model.getId());
            int result = pst.executeUpdate();

            Banco.desconectar();
            return result > 0;
        } catch (SQLException e) {
            Banco.desconectar();
            throw e;
        }
    }

    @Override
    public Collection<Funcionario> lista(String criterio) throws SQLException {
        Collection<Funcionario> listagem = new ArrayList<>();
        String sql = "SELECT * FROM funcionario " + (criterio.isEmpty() ? "" : " WHERE " + criterio);
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            Funcionario funcionario = new Funcionario();
            funcionario.setId(rs.getInt("id"));
            funcionario.setUsername(rs.getString("username"));
            funcionario.setNome(rs.getString("nome"));
            funcionario.setEmail(rs.getString("email"));
            funcionario.setTelefone(rs.getString("telefone"));
            funcionario.setSenha(rs.getString("senha"));
            funcionario.setCpf(rs.getString("cpf"));
            funcionario.setDataNasc(rs.getString("data_nasc"));
            funcionario.setContrato(rs.getString("contrato"));
            funcionario.setCep(rs.getString("CEP"));

            listagem.add(funcionario);
        }
        Banco.desconectar();
        return listagem;
    }

    public int getNextId() throws SQLException {
        Banco.conectar();
        int nextId = 1;
        String sql = "SELECT MAX(id) FROM funcionario";
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

    public boolean idExiste(int id) throws SQLException {
        String sql = "SELECT count(id) FROM funcionario WHERE id = ?";
        Banco.conectar();
        try (PreparedStatement pst = Banco.obterConexao().prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } finally {
            Banco.desconectar();
        }
        return false;
    }

    public Funcionario autenticar(String username, String senha) throws SQLException {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE username = ? AND senha = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, username);
        pst.setString(2, senha);
        rs = pst.executeQuery();

        if (rs.next()) {
            funcionario = new Funcionario();
            funcionario.setId(rs.getInt("id"));
            funcionario.setUsername(rs.getString("username"));
            funcionario.setNome(rs.getString("nome"));
            funcionario.setEmail(rs.getString("email"));
            funcionario.setTelefone(rs.getString("telefone"));
            funcionario.setSenha(rs.getString("senha"));
            funcionario.setCpf(rs.getString("cpf"));
            funcionario.setDataNasc(rs.getString("data_nasc"));
            funcionario.setContrato(rs.getString("contrato"));
            funcionario.setCep(rs.getString("CEP"));
        }

        Banco.desconectar();
        return funcionario;
    }
}
