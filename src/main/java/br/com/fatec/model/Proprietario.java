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
public class Proprietario {
    /*
    Todo MODEL deve ser criado:
    Getters e Setters
    Construtores defaul e parametrico
    equals e hashCode
    toString
    */
    
    private int codProprietario;
    private String nome;

    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.codProprietario;
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
        final Proprietario other = (Proprietario) obj;
        if (this.codProprietario != other.codProprietario) {
            return false;
        }
        return true;
    }

    
    public Proprietario() {
    }

    public Proprietario(int codProprietario, String nome) {
        this.codProprietario = codProprietario;
        this.nome = nome;
    }
    
    public int getCodProprietario() {
        return codProprietario;
    }

    public void setCodProprietario(int codProprietario) {
        this.codProprietario = codProprietario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
}
