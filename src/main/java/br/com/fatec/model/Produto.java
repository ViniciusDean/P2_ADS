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
    private int id, fornecedor_id, quantidade;
    private String nome, embalagem;
    private double preco_venda, preco_custo;

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

    public Produto(int id, int fornecedor_id, int quantidade, String nome, String embalagem, float preco_venda, float preco_custo) {
        this.id = id;
        this.fornecedor_id = fornecedor_id;
        this.quantidade = quantidade;
        this.nome = nome;
        this.embalagem = embalagem;
        this.preco_venda = preco_venda;
        this.preco_custo = preco_custo;
    }

    public Produto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFornecedor_id() {
        return fornecedor_id;
    }

    public void setFornecedor_id(int fornecedor_id) {
        this.fornecedor_id = fornecedor_id;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
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

    public void setPreco_venda(float preco_venda) {
        this.preco_venda = preco_venda;
    }

    public void setPreco_custo(float preco_custo) {
        this.preco_custo = preco_custo;
    }

}
