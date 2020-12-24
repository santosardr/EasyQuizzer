/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.DAOs;

import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.Interfaces.DAOInterface;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaProvaBean;
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
public class QuestaoFechadaProvaDAO implements DAOInterface<QuestaoFechadaProvaBean>{
    private Connection connection;
    
    public QuestaoFechadaProvaDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    @Override
    public void Insert(QuestaoFechadaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO questao_fechada_prova(prova_id, questaofid) "
                + "VALUES (?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.setInt(2, Bean.getQuestaoFechada_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Closed exam question"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public void Edit(QuestaoFechadaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE questao_fechada_prova SET questaofid = ? WHERE prova_id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getQuestaoFechada_ID());
            stm.setInt(2, Bean.getProva_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " " + rb.getString("Closed exam question") + " ID: " + Bean.getQuestaoFechada_ID() + " " + rb.getString("and") + " " + rb.getString("Exam") + " ID: " + Bean.getProva_ID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public QuestaoFechadaProvaBean Get_One(QuestaoFechadaProvaBean Bean) {
        return null; //esse método não faz sentido nesse DAO
    }
    @Override
    public ArrayList<QuestaoFechadaProvaBean> Get_One_All(QuestaoFechadaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_fechada_prova WHERE prova_id = ?";
        PreparedStatement stm = null;
        ArrayList<QuestaoFechadaProvaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoFechadaProvaBean aux = null;
            while(rs.next()){
                aux = new QuestaoFechadaProvaBean();
                aux.setProva_ID(rs.getInt("prova_id"));
                aux.setQuestaoFechada_ID(rs.getInt("questaofid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Closed question"), ex1);
        }
    }
    public ArrayList<QuestaoFechadaBean> GetAllQuests(QuestaoFechadaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT t2.id AS id,t2.enunciado AS enunciado, t2.topicoid AS topicoid FROM "
                + "questao_fechada_prova AS t1 INNER JOIN questao_fechada AS t2 ON t1.questaofid = t2.id WHERE t1.prova_id = ? "
                + "ORDER BY t2.id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoFechadaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoFechadaBean aux = null;
            while(rs.next()){
                aux = new QuestaoFechadaBean();
                aux.setID(rs.getInt("id"));
                aux.setEnunciado(rs.getString("enunciado"));
                aux.setTopico_ID(rs.getInt("topicoid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Closed question"), ex1);
        }
    }
    @Override
    public ArrayList<QuestaoFechadaProvaBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_fechada_prova";
        PreparedStatement stm = null;
        ArrayList<QuestaoFechadaProvaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoFechadaProvaBean aux = null;
            while(rs.next()){
                aux = new QuestaoFechadaProvaBean();
                aux.setProva_ID(rs.getInt("prova_id"));
                aux.setQuestaoFechada_ID(rs.getInt("questaofid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Closed question"), ex1);
        }
    }
    @Override
    public ArrayList<QuestaoFechadaProvaBean> Search_All(String Search) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_fechada_prova WHERE cast(prova_id as varchar) LIKE ? OR "
                + "cast(questaofid as varchar) LIKE ?"
                + "ORDER BY prova_id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoFechadaProvaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, "%"+Search+"%");
            stm.setString(2, "%"+Search+"%");
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            QuestaoFechadaProvaBean aux = null;
            while(rs.next()){
                aux = new QuestaoFechadaProvaBean();
                aux.setProva_ID(rs.getInt("prova_id"));
                aux.setQuestaoFechada_ID(rs.getInt("questaofid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " " + rb.getString("and") + " " + rb.getString("Closed question"), ex1);
        }
    }
    @Override
    public void Delete(QuestaoFechadaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM questao_fechada_prova WHERE prova_id = ? AND questaofid = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getProva_ID());
            stm.setInt(2, Bean.getQuestaoFechada_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Closed exam question") + " ID: " + Bean.getQuestaoFechada_ID() + " " + rb.getString("and") + " " + rb.getString("Exam") + " ID: " + Bean.getProva_ID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    public void DeleteAll(QuestaoFechadaProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM questao_fechada_prova WHERE prova_id = ?";
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
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Closed exam question") + " ID: " + Bean.getQuestaoFechada_ID() + " " + rb.getString("and") + " " + rb.getString("Exam") + " ID: " + Bean.getProva_ID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
}
