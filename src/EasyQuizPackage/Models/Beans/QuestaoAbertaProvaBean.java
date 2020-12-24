/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;

import java.io.Serializable;

/**
 *
 * @author Arthur
 */
public class QuestaoAbertaProvaBean implements Serializable{
    private int QuestaoAberta_ID; //Indentificador de QuestaoAberta
    private int Prova_ID;         //Identificador de Prova
    
    public QuestaoAbertaProvaBean(){
        //Bean Constructor
    }
    public QuestaoAbertaProvaBean(int QuestaoAberta_ID, int Prova_ID){
        this.QuestaoAberta_ID = QuestaoAberta_ID;
        this.Prova_ID = Prova_ID;
    }
    public int getQuestaoAberta_ID() {
        return QuestaoAberta_ID;
    }
    public void setQuestaoAberta_ID(int QuestaoAberta_ID) {
        this.QuestaoAberta_ID = QuestaoAberta_ID;
    }
    public int getProva_ID() {
        return Prova_ID;
    }
    public void setProva_ID(int Prova_ID) {
        this.Prova_ID = Prova_ID;
    }
}
