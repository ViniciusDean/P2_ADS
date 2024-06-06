package br.com.fatec.DAO;

import br.com.fatec.model.Fornecedor;
import br.com.fatec.model.Produto;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ProdutoDAO implements DAO<Produto> {

    private Produto produto;
    private FornecedorDAO fornecedorDAO;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco_venda, preco_custo, embalagem, fornecedor_id, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, produto.getNome());
        pst.setDouble(2, produto.getPreco_venda());
        pst.setDouble(3, produto.getPreco_custo());
        pst.setString(4, produto.getEmbalagem());
        pst.setInt(5, produto.getFornecedor().getId());
        pst.setInt(6, produto.getQuantidade());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    @Override
    public boolean altera(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, preco_venda = ?, preco_custo = ?, embalagem = ?, fornecedor_id = ?, quantidade = ? WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, produto.getNome());
        pst.setDouble(2, produto.getPreco_venda());
        pst.setDouble(3, produto.getPreco_custo());
        pst.setString(4, produto.getEmbalagem());
        pst.setInt(5, produto.getFornecedor().getId());
        pst.setInt(6, produto.getQuantidade());
        pst.setInt(7, produto.getId());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    @Override
    public Produto buscaID(Produto model) throws SQLException {
        produto = null;
        String sql = "SELECT * FROM produto WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, model.getId());
        rs = pst.executeQuery();

        if (rs.next()) {
            Fornecedor f = new Fornecedor();
            f.setId(rs.getInt("fornecedor_id"));
            f = fornecedorDAO.buscaID(f);
            Produto p = new Produto(f);
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco_venda(rs.getDouble("preco_venda"));
            produto.setPreco_custo(rs.getDouble("preco_custo"));
            produto.setEmbalagem(rs.getString("embalagem"));
            produto.setQuantidade(rs.getInt("quantidade"));
        }

        Banco.desconectar();
        return produto;
    }

    @Override
    public boolean remove(Produto model) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";
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
    public Collection<Produto> lista(String criterio) throws SQLException {
        Collection<Produto> listagem = new ArrayList<>();
        String sql = "SELECT * FROM produto " + (criterio.isEmpty() ? "" : " WHERE " + criterio);
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            Fornecedor f = new Fornecedor();
            f.setId(rs.getInt("fornecedor_id"));
            f = fornecedorDAO.buscaID(f);
            Produto produto = new Produto(f);
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco_venda(rs.getDouble("preco_venda"));
            produto.setPreco_custo(rs.getDouble("preco_custo"));
            produto.setEmbalagem(rs.getString("embalagem"));
            produto.setQuantidade(rs.getInt("quantidade"));

            listagem.add(produto);
        }
        Banco.desconectar();
        return listagem;
    }
}