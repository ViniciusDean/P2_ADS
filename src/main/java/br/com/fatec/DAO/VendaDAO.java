package br.com.fatec.DAO;

import br.com.fatec.model.Venda;
import br.com.fatec.model.VendaProduto;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class VendaDAO implements DAO<Venda> {

    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Venda venda) throws SQLException {
        String sql = "INSERT INTO venda (cliente_id, data_hora, operador_id) VALUES (?, ?, ?)";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setInt(1, venda.getCliente().getId());
        pst.setString(2, venda.getDataHora());
        pst.setInt(3, venda.getOperador().getId());
        int executed = pst.executeUpdate();

        if (executed > 0) {
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int vendaId = rs.getInt(1);

                for (VendaProduto vp : venda.getProdutos()) {
                    String sqlProduto = "INSERT INTO venda_produto (venda_id, produto_id) VALUES (?, ?)";
                    pst = Banco.obterConexao().prepareStatement(sqlProduto);
                    pst.setInt(1, vendaId);
                    pst.setInt(2, vp.getProduto().getId());
                    pst.executeUpdate();
                }
            }
        }

        Banco.desconectar();
        return executed > 0;
    }

    @Override
    public boolean remove(Venda model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean altera(Venda model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Venda buscaID(Venda model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Venda> lista(String criterio) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
