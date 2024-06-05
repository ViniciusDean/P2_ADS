/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model;

/**
 *
 * @author deanv
 */
public class Fornecedor {

    private int id;
    private String cep, telefone, razao_social, email, cnpj, regime_tributacao, tipo_frete, tipo_fornecedor;
    private char devolucao, cancelamento;

    public Fornecedor() {
    }

    public Fornecedor(int id, String logradouro, String cep, String telefone, String razao_social, String email, String cnpj, String regime_tributacao, String tipo_frete, String tipo_fornecedor, char devolucao, char cancelamento) {
        this.id = id;
        this.tipo_fornecedor = tipo_fornecedor;
        this.cep = cep;
        this.telefone = telefone;
        this.razao_social = razao_social;
        this.email = email;
        this.cnpj = cnpj;
        this.regime_tributacao = regime_tributacao;
        this.tipo_frete = tipo_frete;
        this.devolucao = devolucao;
        this.cancelamento = cancelamento;
    }

    public String getTipo_fornecedor() {
        return tipo_fornecedor;
    }

    public void setTipo_fornecedor(String tipo_fornecedor) {
        this.tipo_fornecedor = tipo_fornecedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRazao_social() {
        return razao_social;
    }

    public void setRazao_social(String razao_social) {
        this.razao_social = razao_social;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRegime_tributacao() {
        return regime_tributacao;
    }

    public void setRegime_tributacao(String regime_tributacao) {
        this.regime_tributacao = regime_tributacao;
    }

    public String getTipo_frete() {
        return tipo_frete;
    }

    public void setTipo_frete(String tipo_frete) {
        this.tipo_frete = tipo_frete;
    }

    public char getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(char devolucao) {
        this.devolucao = devolucao;
    }

    public char getCancelamento() {
        return cancelamento;
    }

    public void setCancelamento(char cancelamento) {
        this.cancelamento = cancelamento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.id;
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
        final Fornecedor other = (Fornecedor) obj;
        return this.id == other.id;
    }

}
