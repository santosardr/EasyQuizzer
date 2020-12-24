/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Interfaces;

import java.util.ArrayList;

/**
 *
 * @author Arthur
 */
public interface DAOInterface <T>{
    public void Insert(T Bean); //Método de Inserção que recebe um Bean e o Insere no Banco.
    public void Edit(T Bean); //Método de Edição que recebe um Bean e aplica as modificações no Banco.
    public T Get_One(T Bean); //Método de Requisição que busca no banco o registro requisitado e o retorna em forma de Bean
    public ArrayList<T> Get_One_All(T Bean); //Método de REquisição para Beans que representam tabelas de relacionamentos N..N
    public ArrayList<T> Get_All(); //Método para retornar todos os registros do banco
    public ArrayList<T> Search_All(String Search); //Método para buscar em todos os campos
    public void Delete(T Bean); //Método para exclusão de registros no banco.
}
