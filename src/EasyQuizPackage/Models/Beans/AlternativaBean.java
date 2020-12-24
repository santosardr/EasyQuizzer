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
public class AlternativaBean extends RecursiveTreeObject<AlternativaBean> implements Serializable{
    private int ID; //Número identificador da Alternativa
    private String Texto; //Texto que representa a alternativa
    private boolean Certa; //Se a alternativa é correta
    private int QuestFID; //Questão a qual a alternativa pertence
    
    public AlternativaBean(){
        /*Bean default constructor*/
    }
    public AlternativaBean(int ID, String Texto, boolean Certa, int QuestFID){
        this.ID = ID;
        this.Texto = Texto;
        this.Certa = Certa;
        this.QuestFID = QuestFID;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getTexto() {
        return Texto;
    }
    public void setTexto(String Texto) {
        this.Texto = Texto;
    }
    public boolean isCerta() {
        return Certa;
    }
    public void setCerta(boolean Certa) {
        this.Certa = Certa;
    }
    public int getQuestFID() {
        return QuestFID;
    }
    public void setQuestFID(int QuestFID) {
        this.QuestFID = QuestFID;
    }
    public String getCerta(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        return this.Certa ? rb.getString("Yes") : rb.getString("No");
    }
}
