/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;
import EasyQuizPackage.Interfaces.Questao;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaDAO;
import EasyQuizPackage.Models.DAOs.TopicoDAO;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Serializable;
import java.util.ArrayList;
/**
 *
 * @author Arthur
 */
public class QuestaoFechadaBean extends RecursiveTreeObject<QuestaoFechadaBean> implements Serializable, Questao{
    
    private int ID; //Indentificador de Questão Fechada
    private String Enunciado; // Texto referente ao enunciado da questão
    private int Topico_ID; // Identificador do tópico ao qual a questão se insere
    private ArrayList<AlternativaBean> selecionadas;
    
    public QuestaoFechadaBean() {
        //Bean constructor
        this.selecionadas = new ArrayList<>();
    }
    public QuestaoFechadaBean(int ID, int Topico_ID) {
        this.ID = ID;
        this.Topico_ID = Topico_ID;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public int getTopico_ID() {
        return Topico_ID;
    }
    public void setTopico_ID(int Topico_ID) {
        this.Topico_ID = Topico_ID;
    }
    public String getEnunciado(){
        return this.Enunciado;
    }
    public void setEnunciado(String Enunciado){
        this.Enunciado = Enunciado;
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
        QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
        return dao.GetAlts(this);
    }

    @Override
    public String getGabarito() {
        return null;
    }

    @Override
    public int getNumLinhas() {
        return 0;
    }

    @Override
    public ArrayList<AlternativaBean> getAlternativasCertas() {
        QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
        return dao.GetAltsCertas(this);
    }

    @Override
    public ArrayList<AlternativaBean> getAlternativasErradas() {
        QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
        return dao.GetAltsErradas(this);
    }
    public void addSelecionada(AlternativaBean alt){
        this.selecionadas.add(alt);
    }
    public ArrayList<AlternativaBean> getSelecionadas(){
        return this.selecionadas;
    }
    public void delSelecionada(AlternativaBean alt){
        this.selecionadas.remove(alt);
    }
}
