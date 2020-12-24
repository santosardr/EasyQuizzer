/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.Beans;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Serializable;
import java.sql.Date;
/**
 *
 * @author Arthur
 */
public class ProvaBean extends RecursiveTreeObject<ProvaBean> implements Serializable{
    private int ID; //Identificação de Prova
    private String tipo;
    private int tamanho;
    private String nome; // Nome identificador da prova
    private String prof; // Nome do Professor que será impresso nas provas
    private String curso; //Nome do curso que será impresso nas provas
    private String inst; //Nome da instituição de ensino 
    private Date data; //Data da prova também para impressão
    private float nota; //Valor Total da prova, para impressão
    private String turma; // Nome da Turma, para impressão
    private int materia_id;

    public ProvaBean(){
        //Bean Constructor
    }
    public ProvaBean(int ID) {
        this.ID = ID;
    }
    public int getTamanho() {
        return tamanho;
    }
    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getProf() {
        return prof;
    }
    public void setProf(String prof) {
        this.prof = prof;
    }
    public String getCurso() {
        return curso;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }
    public String getInst() {
        return inst;
    }
    public void setInst(String inst) {
        this.inst = inst;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public float getNota() {
        return nota;
    }
    public void setNota(float nota) {
        this.nota = nota;
    }
    public String getTurma() {
        return turma;
    }
    public void setTurma(String turma) {
        this.turma = turma;
    }
    public int getMateria_id() {
        return materia_id;
    }
    public void setMateria_id(int materia_id) {
        this.materia_id = materia_id;
    }
}
