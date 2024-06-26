/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fatec.model;

/**
 *
 * @author Aluno
 */
public class Produto {

    /*
     Toda classe que é do tipo model deve:
     1) ter Getters e Setters
     2) Construtores default e paramétricos
     3) Reprogramar o equals() e hashCode() para informar como
        esse objeto deve ser comparado
     4) Reprogramas o toString() para informar o que deve ser
        retornado quando esse objeto é exibido
     */
    private int id, quantidade, localQuantidade;
    private String nome, embalagem, data_compra, codigo_barras;
    private double preco_venda, preco_custo;
    private Fornecedor fornecedor;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        return this.id == other.id;
    }

    public String getData_compra() {
        return data_compra;
    }

    public void setData_compra(String data_compra) {
        this.data_compra = data_compra;
    }

    public Produto(int id, int quantidade, String nome, String embalagem, float preco_venda, float preco_custo, Fornecedor fornecedor) {
        this.id = id;
        this.quantidade = quantidade;
        this.nome = nome;
        this.embalagem = embalagem;
        this.preco_venda = preco_venda;
        this.preco_custo = preco_custo;
        this.fornecedor = fornecedor;
    }

    public Produto() {
    }

    public Produto(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getLocalQuantidade() {
        return localQuantidade;
    }

    public void setLocalQuantidade(int localQuantidade) {
        this.localQuantidade = localQuantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public double getLucro() {
        return this.preco_venda - this.preco_custo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public double getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(double preco_venda) {
        this.preco_venda = preco_venda;
    }

    public double getPreco_custo() {
        return preco_custo;
    }

    public void setPreco_custo(double preco_custo) {
        this.preco_custo = preco_custo;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

}
