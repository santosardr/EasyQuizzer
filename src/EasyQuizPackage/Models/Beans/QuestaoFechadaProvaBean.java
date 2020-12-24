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
public class QuestaoFechadaProvaBean implements Serializable{
    private int QuestaoFechada_ID; //Identificador de Questao Fechada
    private int Prova_ID;          //Identificador de Prova
    
    public QuestaoFechadaProvaBean(){
        //Bean Constructor
    }
    public QuestaoFechadaProvaBean(int QuestaoFechada_ID, int Prova_ID){
        this.QuestaoFechada_ID = QuestaoFechada_ID;
        this.Prova_ID = Prova_ID;
    }
    public int getQuestaoFechada_ID() {
        return QuestaoFechada_ID;
    }
    public void setQuestaoFechada_ID(int QuestaoFechada_ID) {
        this.QuestaoFechada_ID = QuestaoFechada_ID;
    }
    public int getProva_ID() {
        return Prova_ID;
    }
    public void setProva_ID(int Prova_ID) {
        this.Prova_ID = Prova_ID;
    }
}
