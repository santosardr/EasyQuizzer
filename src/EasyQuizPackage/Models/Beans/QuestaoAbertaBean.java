/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;

import EasyQuizPackage.Interfaces.Questao;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import EasyQuizPackage.Models.DAOs.TopicoDAO;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Arthur
 */
public class QuestaoAbertaBean extends RecursiveTreeObject<QuestaoAbertaBean> implements Serializable,Questao{
    private int ID; //Identificador de Questão Aberta
    private String Enunciado; // Texto referente ao enunciado da questão
    private int NumLinhas; // Numero de linhas a serem saltadas na impressão para resposta
    private String Gabarito; // Resposta da Questão
    private int Topico_ID; // Identificador do Tópico ao Qual a questão se insere
    
    public QuestaoAbertaBean(){
        //Bean Constructor
    }
    public QuestaoAbertaBean(int ID, String Enunciado,int NumLinhas, String Gabarito, int Topico_ID) {
        this.ID = ID;
        this.Enunciado = Enunciado;
        this.NumLinhas = NumLinhas;
        this.Gabarito = Gabarito;
        this.Topico_ID = Topico_ID;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getEnunciado() {
        return Enunciado;
    }
    public void setEnunciado(String Enunciado) {
        this.Enunciado = Enunciado;
    }
    public int getNumLinhas() {
        return NumLinhas;
    }
    public void setNumLinhas(int NumLinhas) {
        this.NumLinhas = NumLinhas;
    }
    public String getGabarito() {
        return Gabarito;
    }
    public void setGabarito(String Gabarito) {
        this.Gabarito = Gabarito;
    }
    public int getTopico_ID() {
        return Topico_ID;
    }
    public void setTopico_ID(int Topico_ID) {
        this.Topico_ID = Topico_ID;
    }
    public String getTopico(){
        TopicoDAO dao = new TopicoDAO();
        TopicoBean bean = new TopicoBean();
        bean.setID(this.Topico_ID);
        return dao.Get_One(bean).getNome();
    }
    public String getMateria(){
        TopicoDAO topdao = new TopicoDAO();
        MateriaDAO matdao = new MateriaDAO();
        TopicoBean topbean = new TopicoBean();
        topbean.setID(this.Topico_ID);
        topbean = topdao.Get_One(topbean);
        MateriaBean matbean = new MateriaBean();
        matbean.setID(topbean.getMateria_ID());
        matbean = matdao.Get_One(matbean);
        return matbean.getNome();
    }

    @Override
    public ArrayList<AlternativaBean> getAlternativas() {
        return null;
    }

    @Override
    public ArrayList<AlternativaBean> getAlternativasCertas() {
        return null;
    }

    @Override
    public ArrayList<AlternativaBean> getAlternativasErradas() {
        return null;
    }
}
