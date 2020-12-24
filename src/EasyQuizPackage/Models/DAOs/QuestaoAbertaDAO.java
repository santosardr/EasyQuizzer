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
public class QuestaoAbertaDAO implements DAOInterface<QuestaoAbertaBean>{
    private Connection connection;
    
    public QuestaoAbertaDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    @Override
    public void Insert(QuestaoAbertaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO questao_aberta(enunciado, num_linhas, gabarito, topicoid) "
                + "VALUES (?,?,?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getEnunciado());
            stm.setInt(2, Bean.getNumLinhas());
            stm.setString(3, Bean.getGabarito());
            stm.setInt(4, Bean.getTopico_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Open question"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }

    @Override
    public void Edit(QuestaoAbertaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE questao_aberta SET enunciado = ?, num_linhas = ?, gabarito = ?, topicoid = ? WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getEnunciado());
            stm.setInt(2, Bean.getNumLinhas());
            stm.setString(3, Bean.getGabarito());
            stm.setInt(4, Bean.getTopico_ID());
            stm.setInt(5, Bean.getID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " - " + rb.getString("Open question") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }

    @Override
    public QuestaoAbertaBean Get_One(QuestaoAbertaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_aberta WHERE id = ?";
        PreparedStatement stm = null;
        QuestaoAbertaBean novo = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            rs.next();
            novo = new QuestaoAbertaBean();
            novo.setID(Bean.getID());
            novo.setEnunciado(rs.getString("enunciado"));
            novo.setNumLinhas(rs.getInt("num_linhas"));
            novo.setGabarito(rs.getString("Gabarito"));
            novo.setTopico_ID(rs.getInt("topicoid"));
            stm.close();
            return novo;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Open question") + " ID: " + Bean.getID(), ex1);
        }
    }

    @Override
    public ArrayList<QuestaoAbertaBean> Get_One_All(QuestaoAbertaBean Bean) {
        return null;
    }

    @Override
    public ArrayList<QuestaoAbertaBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_aberta ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoAbertaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
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
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Open question"), ex1);
        }
    }
    public ArrayList<QuestaoAbertaBean> GetAllTopico(QuestaoAbertaBean bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_aberta WHERE topicoid = ? ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoAbertaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, bean.getTopico_ID());
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
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Open question"), ex1);
        }
    }

    @Override
    public ArrayList<QuestaoAbertaBean> Search_All(String Search) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM questao_aberta WHERE cast(id as varchar) LIKE ? OR "
                + "enunciado LIKE ? OR "
                + "cast(num_linhas as varchar) LIKE ? OR "
                + "gabarito LIKE ? OR "
                + "cast(topicoid as varchar) LIKE ? "
                + "ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<QuestaoAbertaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, "%"+Search+"%");
            stm.setString(2, "%"+Search+"%");
            stm.setString(3, "%"+Search+"%");
            stm.setString(4, "%"+Search+"%");
            stm.setString(5, "%"+Search+"%");
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
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Open question"), ex1);
        }
    }

    @Override
    public void Delete(QuestaoAbertaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM questao_aberta WHERE id = ?";
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
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Open question") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
}
