/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.DAOs;

import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.Interfaces.DAOInterface;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.AlternativaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
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
public class QuestaoFechadaDAO implements DAOInterface<QuestaoFechadaBean>{
    private Connection connection;
    
    public QuestaoFechadaDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    
    @Override
    public void Insert(QuestaoFechadaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO questao_fechada(enunciado, topicoid) "
                + "VALUES (?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getEnunciado());
            stm.setInt(2, Bean.getTopico_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Closed question"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }

    @Override
    public void Edit(QuestaoFechadaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE questao_fechada SET enunciado = ?, topicoid = ? WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getEnunciado());
            stm.setInt(2, Bean.getTopico_ID());
            stm.setInt(3, Bean.getID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " - " + rb.getString("Closed question") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }

    @Override
    public QuestaoFechadaBean Get_One(QuestaoFechadaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_fechada WHERE id = ?";
        PreparedStatement stm = null;
        QuestaoFechadaBean novo = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            rs.next();
            novo = new QuestaoFechadaBean();
            novo.setID(Bean.getID());
            novo.setEnunciado(rs.getString("enunciado"));
            novo.setTopico_ID(rs.getInt("topicoid"));
            stm.close();
            return novo;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question") + " ID: " + Bean.getID(), ex1);
        }
    }

    @Override
    public ArrayList<QuestaoFechadaBean> Get_One_All(QuestaoFechadaBean Bean) {
        return null; //nesse DAO esse método não possui sentido
    }

    @Override
    public ArrayList<QuestaoFechadaBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_fechada ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoFechadaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
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
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question"), ex1);
        }
    }
    public ArrayList<QuestaoFechadaBean> GetAllTopico(QuestaoFechadaBean bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_fechada WHERE topicoid = ? ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoFechadaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, bean.getTopico_ID());
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
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question"), ex1);
        }
    }
    @Override
    public ArrayList<QuestaoFechadaBean> Search_All(String Search) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_fechada WHERE cast(id as varchar) LIKE ? OR "
                + "enunciado LIKE ? OR "
                + "cast(topicoid as varchar) LIKE ? "
                + "ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoFechadaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, "%"+Search+"%");
            stm.setString(2, "%"+Search+"%");
            stm.setString(3, "%"+Search+"%");
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
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question"), ex1);
        }
    }

    @Override
    public void Delete(QuestaoFechadaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM questao_fechada WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Closed question") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    public String GetNumsAlts(QuestaoFechadaBean bean){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa WHERE questf_id = ?";
        PreparedStatement stm = null;
        ArrayList<AlternativaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            AlternativaBean aux = null;
            while(rs.next()){
                aux = new AlternativaBean();
                aux.setID(rs.getInt("id"));
                aux.setTexto(rs.getString("texto"));
                aux.setCerta(rs.getBoolean("certa"));
                aux.setQuestFID(rs.getInt("questf_id"));
                results.add(aux);
            }
            stm.close();
            int c = 0;
            int e = 0;
            for(AlternativaBean a : results){
                if(a.isCerta())
                    c++;
                else
                    e++;
            }
            return c+" : "+e;
        } catch (SQLException e) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question") + " " + rb.getString("and") + " " + rb.getString("Alternative"), e);
        }
    }
    public ArrayList<AlternativaBean> GetAltsCertas(QuestaoFechadaBean bean){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa WHERE questf_id = ? AND certa = ?";
        PreparedStatement stm = null;
        ArrayList<AlternativaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, bean.getID());
            stm.setBoolean(2, true);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            AlternativaBean aux = null;
            while(rs.next()){
                aux = new AlternativaBean();
                aux.setID(rs.getInt("id"));
                aux.setTexto(rs.getString("texto"));
                aux.setCerta(rs.getBoolean("certa"));
                aux.setQuestFID(rs.getInt("questf_id"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException e) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question") + " " + rb.getString("and") + " " + rb.getString("Alternative"), e);
        }
    }
    public ArrayList<AlternativaBean> GetAltsErradas(QuestaoFechadaBean bean){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa WHERE questf_id = ? AND certa = ?";
        PreparedStatement stm = null;
        ArrayList<AlternativaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, bean.getID());
            stm.setBoolean(2, false);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            AlternativaBean aux = null;
            while(rs.next()){
                aux = new AlternativaBean();
                aux.setID(rs.getInt("id"));
                aux.setTexto(rs.getString("texto"));
                aux.setCerta(rs.getBoolean("certa"));
                aux.setQuestFID(rs.getInt("questf_id"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException e) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question") + " " + rb.getString("and") + " " + rb.getString("Alternative"), e);
        }
    }
    public ArrayList<AlternativaBean> GetAlts(QuestaoFechadaBean bean){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa WHERE questf_id = ? ";
        PreparedStatement stm = null;
        ArrayList<AlternativaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, bean.getID());
            stm.setBoolean(2, true);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            AlternativaBean aux = null;
            while(rs.next()){
                aux = new AlternativaBean();
                aux.setID(rs.getInt("id"));
                aux.setTexto(rs.getString("texto"));
                aux.setCerta(rs.getBoolean("certa"));
                aux.setQuestFID(rs.getInt("questf_id"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException e) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Closed question") + " " + rb.getString("and") + " " + rb.getString("Alternative"), e);
        }
    }
}
