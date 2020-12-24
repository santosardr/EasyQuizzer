/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;

import EasyQuizPackage.Models.DAOs.CursoDAO;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

/**
 *
 * @author Arthur
 */
public class MateriaBean extends RecursiveTreeObject<MateriaBean>{
    private int ID; //Identificador de Matéria
    private String Nome; // Nome da Matéria
    private int Curso; // Nome do Curso ao qual a matéria se insere
    
    public MateriaBean(){
        //Bean Constructor
    }
    public MateriaBean(int ID, String Nome, int Curso){
        this.ID = ID;
        this.Nome = Nome;
        this.Curso = Curso;
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
    public int getCurso() {
        return Curso;
    }
    public void setCurso(int Curso) {
        this.Curso = Curso;
    }
    public String getCursoNome(){
        CursoBean bean = new CursoBean();
        bean.setID(this.Curso);
        CursoDAO dao = new CursoDAO();
        return dao.Get_One(bean).getNome();
    }
}
