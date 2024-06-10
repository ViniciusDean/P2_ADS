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
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco_venda, preco_custo, embalagem, fornecedor_id, quantidade, id, codigo_barras, data_compra) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, produto.getNome());
        pst.setDouble(2, produto.getPreco_venda());
        pst.setDouble(3, produto.getPreco_custo());
        pst.setString(4, produto.getEmbalagem());
        pst.setInt(5, produto.getFornecedor().getId());
        pst.setInt(6, produto.getQuantidade());
        pst.setInt(7, produto.getId());
        pst.setString(8, produto.getCodigo_barras());
        pst.setString(9, produto.getData_compra());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    @Override
    public boolean altera(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, preco_venda = ?, preco_custo = ?, embalagem = ?, fornecedor_id = ?, quantidade = ?, codigo_barras = ?, data_compra = ?  WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, produto.getNome());
        pst.setDouble(2, produto.getPreco_venda());
        pst.setDouble(3, produto.getPreco_custo());
        pst.setString(4, produto.getEmbalagem());
        pst.setInt(5, produto.getFornecedor().getId());
        pst.setInt(6, produto.getQuantidade());
        pst.setString(7, produto.getCodigo_barras());
        pst.setString(8, produto.getData_compra());
        pst.setInt(9, produto.getId());
        int executed = pst.executeUpdate();
        Banco.desconectar();
        return executed > 0;
    }

    @Override
    public Produto buscaID(Produto model) throws SQLException {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE id = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setInt(1, model.getId());
        rs = pst.executeQuery();

        if (rs.next()) {
            Fornecedor f = new Fornecedor();
            f.setId(rs.getInt("fornecedor_id"));
            f = fornecedorDAO.buscaID(f);
            produto = new Produto(f);
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco_venda(rs.getDouble("preco_venda"));
            produto.setPreco_custo(rs.getDouble("preco_custo"));
            produto.setEmbalagem(rs.getString("embalagem"));
            produto.setQuantidade(rs.getInt("quantidade"));
            produto.setCodigo_barras(rs.getString("codigo_barras"));
            produto.setData_compra(rs.getString("data_compra"));
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

    public int getNextId() throws SQLException {
        Banco.conectar();
        int nextId = 1;
        String sql = "SELECT MAX(id) FROM produto";
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
        String sql = "SELECT count(id) FROM produto WHERE id = ?";
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

    public Produto buscaPorCodigoBarras(String codBarras) throws SQLException {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE codigo_barras = ?";
        Banco.conectar();
        pst = Banco.obterConexao().prepareStatement(sql);
        pst.setString(1, codBarras);
        rs = pst.executeQuery();

        if (rs.next()) {
            produto = new Produto();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco_venda(rs.getDouble("preco_venda"));
            produto.setQuantidade(rs.getInt("quantidade"));
        }

        Banco.desconectar();
        return produto;
    }

    public boolean removerProdutoDependencias(int produtoId) throws SQLException {
        Banco.conectar();
        String sqlRemoverDependencias = "DELETE FROM venda_produto WHERE produto_id = ?";
        pst = Banco.obterConexao().prepareStatement(sqlRemoverDependencias);
        pst.setInt(1, produtoId);
        pst.executeUpdate();

        String sqlRemoverProduto = "DELETE FROM produto WHERE id = ?";
        pst = Banco.obterConexao().prepareStatement(sqlRemoverProduto);
        pst.setInt(1, produtoId);
        int linhasAfetadas = pst.executeUpdate();

        Banco.desconectar();
        return linhasAfetadas > 0;
    }
}
