/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fatec.DAO;

import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author Aluno
 * 
 */
public interface DAO <T> {
    public boolean insere(T model) throws SQLException;
    public boolean remove(T model) throws SQLException;
    public boolean altera(T model) throws SQLException;
    public T buscaID(T model) throws SQLException;
    public Collection<T> lista(String criterio) 
                throws SQLException;
}
