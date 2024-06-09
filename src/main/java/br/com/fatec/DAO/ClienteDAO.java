package br.com.fatec.DAO;

import br.com.fatec.model.Cliente;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    private PreparedStatement pst;
    private ResultSet rs;

    public Cliente buscaPorCPF(String cpf) throws SQLException {
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, cpf);
        rs = pst.executeQuery();

        if (rs.next()) {
            cliente = new Cliente();
            cliente.setId(rs.getInt("id"));
            cliente.setNome(rs.getString("nome"));
            cliente.setCpf(rs.getString("cpf"));
        }

        Banco.desconectar();
        return cliente;
    }
}
