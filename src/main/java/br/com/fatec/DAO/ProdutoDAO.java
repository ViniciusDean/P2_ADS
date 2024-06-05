package br.com.fatec.DAO;

import br.com.fatec.model.Produto;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private PreparedStatement pst;
    private ResultSet rs;

    // Inserir Produto
    public boolean insereProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco_venda, preco_custo, embalagem, fornecedor_id, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, produto.getNome());
        pst.setDouble(2, produto.getPreco_venda());
        pst.setDouble(3, produto.getPreco_custo());
        pst.setString(4, produto.getEmbalagem());
        pst.setInt(5, produto.getFornecedor_id());
        pst.setInt(6, produto.getQuantidade());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    public boolean removeProduto(int produtoId) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, produtoId);
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    // Atualizar Produto
    public boolean alteraProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, preco_venda = ?, preco_custo = ?, embalagem = ?, fornecedor_id = ?, quantidade = ? WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, produto.getNome());
        pst.setDouble(2, produto.getPreco_venda());
        pst.setDouble(3, produto.getPreco_custo());
        pst.setString(4, produto.getEmbalagem());
        pst.setInt(5, produto.getFornecedor_id());
        pst.setInt(6, produto.getQuantidade());
        pst.setInt(7, produto.getId());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    // Buscar Produto por ID
    public Produto buscaProdutoPorID(int id) throws SQLException {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, id);
        rs = pst.executeQuery();
        if (rs.next()) {
            produto = new Produto();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco_venda(rs.getDouble("preco_venda"));
            produto.setPreco_custo(rs.getDouble("preco_custo"));
            produto.setEmbalagem(rs.getString("embalagem"));
            produto.setFornecedor_id(rs.getInt("fornecedor_id"));
            produto.setQuantidade(rs.getInt("quantidade"));
        }

        Banco.desconectar();
        return produto;
    }

    // Listar todos os Produtos
    public List<Produto> listaProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            Produto produto = new Produto();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco_venda(rs.getDouble("preco_venda"));
            produto.setPreco_custo(rs.getDouble("preco_custo"));
            produto.setEmbalagem(rs.getString("embalagem"));
            produto.setFornecedor_id(rs.getInt("fornecedor_id"));
            produto.setQuantidade(rs.getInt("quantidade"));
            produtos.add(produto);
        }
        Banco.desconectar();
        return produtos;
    }
}
