/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;

import EasyQuizPackage.Interfaces.Questao;
import java.util.ArrayList;

/**
 *
 * @author Arthur
 */
public class PermutacaoBean {
    private ArrayList<Questao> questoes;
    public PermutacaoBean(){
        this.questoes = new ArrayList<>();
    }
    public ArrayList<Questao> getQuestoes(){
        return this.questoes;
    }
    public void addQuestao(Questao quest){
        this.questoes.add(quest);
    }
    public Questao getQuestao(int index){
        return this.questoes.get(index);
    }
    public int size(){
        return this.questoes.size();
    }
}
