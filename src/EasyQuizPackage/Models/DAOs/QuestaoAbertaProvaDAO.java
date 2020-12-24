/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.DAOs;

import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.Interfaces.DAOInterface;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.QuestaoAbertaBean;
import EasyQuizPackage.Models.Beans.QuestaoAbertaProvaBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *
 * @author Arthur
 */
public class QuestaoAbertaProvaDAO implements DAOInterface<QuestaoAbertaProvaBean>{
    private Connection connection;
    public QuestaoAbertaProvaDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    @Override
    public void Insert(QuestaoAbertaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO questao_aberta_prova(prova_id, questaid) "
                + "VALUES (?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.setInt(2, Bean.getQuestaoAberta_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Open exam question"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public void Edit(QuestaoAbertaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE questao_aberta_prova SET questaid = ? WHERE prova_id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getQuestaoAberta_ID());
            stm.setInt(2, Bean.getProva_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " " + rb.getString("Closed exam question") + " ID: " + Bean.getQuestaoAberta_ID() + " " + rb.getString("and") + " " + rb.getString("Exam") + " ID: " + Bean.getProva_ID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public QuestaoAbertaProvaBean Get_One(QuestaoAbertaProvaBean Bean) {
        return null; //esse método não faz sentido nesse DAO
    }
    @Override
    public ArrayList<QuestaoAbertaProvaBean> Get_One_All(QuestaoAbertaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_aberta_prova WHERE prova_id = ?";
        PreparedStatement stm = null;
        ArrayList<QuestaoAbertaProvaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoAbertaProvaBean aux = null;
            while(rs.next()){
                aux = new QuestaoAbertaProvaBean();
                aux.setProva_ID(rs.getInt("prova_id"));
                aux.setQuestaoAberta_ID(rs.getInt("questaid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Open question"), ex1);
        }
    }
    public ArrayList<QuestaoAbertaBean> GetAllQuests(QuestaoAbertaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT t2.id AS id,t2.enunciado AS enunciado, t2.num_linhas AS num_linhas, t2.gabarito AS gabarito, t2.topicoid AS topicoid FROM "
                + "questao_aberta_prova AS t1 INNER JOIN questao_aberta AS t2 ON t1.questaid = t2.id WHERE t1.prova_id = ? "
                + "ORDER BY t2.id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoAbertaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoAbertaBean aux = null;
            while(rs.next()){
                aux = new QuestaoAbertaBean();
                aux.setID(rs.getInt("id"));
                aux.setEnunciado(rs.getString("enunciado"));
                aux.setNumLinhas(rs.getInt("num_linhas"));
                aux.setGabarito(rs.getString("gabarito"));
                aux.setTopico_ID(rs.getInt("topicoid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Open question"), ex1);
        }
    }
    @Override
    public ArrayList<QuestaoAbertaProvaBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_aberta_prova";
        PreparedStatement stm = null;
        ArrayList<QuestaoAbertaProvaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoAbertaProvaBean aux = null;
            while(rs.next()){
                aux = new QuestaoAbertaProvaBean();
                aux.setProva_ID(rs.getInt("prova_id"));
                aux.setQuestaoAberta_ID(rs.getInt("questaid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Open question"), ex1);
        }
    }
    @Override
    public ArrayList<QuestaoAbertaProvaBean> Search_All(String Search) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_aberta_prova WHERE cast(prova_id as varchar) LIKE ? OR "
                + "cast(questaid as varchar) LIKE ?"
                + "ORDER BY prova_id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoAbertaProvaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, "%"+Search+"%");
            stm.setString(2, "%"+Search+"%");
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoAbertaProvaBean aux = null;
            while(rs.next()){
                aux = new QuestaoAbertaProvaBean();
                aux.setProva_ID(rs.getInt("prova_id"));
                aux.setQuestaoAberta_ID(rs.getInt("questaid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Open question"), ex1);
        }
    }
    @Override
    public void Delete(QuestaoAbertaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM questao_aberta_prova WHERE prova_id = ? AND questaid = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.setInt(2, Bean.getQuestaoAberta_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Open exam question") + " ID: " + Bean.getQuestaoAberta_ID() + " " + rb.getString("and") + " " + rb.getString("Exam") + " ID: " + Bean.getProva_ID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    public void DeleteAll(QuestaoAbertaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM questao_aberta_prova WHERE prova_id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Open exam question") + " ID: " + Bean.getQuestaoAberta_ID() + " " + rb.getString("and") + " " + rb.getString("Exam") + " ID: " + Bean.getProva_ID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
}
