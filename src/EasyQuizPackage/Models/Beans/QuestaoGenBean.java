/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;

import EasyQuizPackage.JDBC.SingletonConnection;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 *
 * @author Arthur
 */
public class QuestaoGenBean extends RecursiveTreeObject<QuestaoGenBean> implements Serializable{
    private int ID;
    private String Enunciado;
    private String Topico;
    private String Materia;
    private boolean Aberta;
    public QuestaoGenBean(){
        //Bean Constructor
    }
    public QuestaoGenBean(int ID, String Enunciado, String Topico, String Materia, boolean Aberta){
        this.ID = ID;
        this.Enunciado = Enunciado;
        this.Topico = Topico;
        this.Materia = Materia;
        this.Aberta = Aberta;
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
    public String getTopico() {
        return Topico;
    }
    public void setTopico(String Topico) {
        this.Topico = Topico;
    }
    public String getMateria() {
        return Materia;
    }
    public void setMateria (String Materia) {
        this.Materia = Materia;
    }
    public boolean isAberta() {
        return Aberta;
    }
    public void setAberta(boolean Aberta) {
        this.Aberta = Aberta;
    }
    public String getAberta(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        if(this.Aberta){
            return rb.getString("Open");
        }else{
            return rb.getString("Close");
        }
    }
}
