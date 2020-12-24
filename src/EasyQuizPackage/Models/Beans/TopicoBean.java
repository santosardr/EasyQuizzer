/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;

import EasyQuizPackage.Models.DAOs.MateriaDAO;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Serializable;

/**
 *
 * @author Arthur
 */
public class TopicoBean extends RecursiveTreeObject<TopicoBean> implements Serializable {
    private int ID; //Identificador de Topico
    private String Nome; //Nome do Tópico
    private int Materia_ID; //Indentificador da Materia à qual o topico pertence 
    
    public TopicoBean(){
        //Bean Constructor
    }
    public TopicoBean(int ID, String Nome, int Materia_ID){
        this.ID = ID;
        this.Nome= Nome;
        this.Materia_ID = Materia_ID;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getNome() {
        return Nome;
    }
    public void setNome(String Nome) {
        this.Nome = Nome;
    }
    public int getMateria_ID() {
        return Materia_ID;
    }
    public void setMateria_ID(int Materia_ID) {
        this.Materia_ID = Materia_ID;
    }
    public String getMateriaName(){
        MateriaDAO a = new MateriaDAO();
        MateriaBean mat = new MateriaBean();
        mat.setID(this.Materia_ID);
        mat = a.Get_One(mat);
        return mat.getNome();
    }
}
