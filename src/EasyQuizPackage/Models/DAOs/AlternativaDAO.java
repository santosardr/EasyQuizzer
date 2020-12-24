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
public class AlternativaDAO implements DAOInterface<AlternativaBean>{
    private Connection connection;
    
    public AlternativaDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    @Override
    public void Insert(AlternativaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO alternativa(texto, certa, questf_id) "
                + "VALUES (?,?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getTexto());
            stm.setBoolean(2, Bean.isCerta());
            stm.setInt(3, Bean.getQuestFID());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Alternative"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public void Edit(AlternativaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE alternativa SET texto = ?, certa = ?, questf_id = ? WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getTexto());
            stm.setBoolean(2, Bean.isCerta());
            stm.setInt(3, Bean.getQuestFID());
            stm.setInt(4, Bean.getID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " - " + rb.getString("Alternative") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public AlternativaBean Get_One(AlternativaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa WHERE id = ?";
        PreparedStatement stm = null;
        AlternativaBean novo = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            rs.next();
            novo = new AlternativaBean();
            novo.setID(Bean.getID());
            novo.setTexto(rs.getString("texto"));
            novo.setCerta(rs.getBoolean("certa"));
            novo.setQuestFID(rs.getInt("questf_id"));
            stm.close();
            return novo;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Alternative") + " ID: " + Bean.getID(), ex1);
        }
    }
    @Override
    public ArrayList<AlternativaBean> Get_One_All(AlternativaBean Bean) {
        return null;
    }
    @Override
    public ArrayList<AlternativaBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<AlternativaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
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
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Alternative"), ex1);
        }
    }
    public ArrayList<AlternativaBean> Get_All_From_Quest(QuestaoFechadaBean quest) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa WHERE questf_id = ?";
        PreparedStatement stm = null;
        ArrayList<AlternativaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, quest.getID());
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
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Alternative"), ex1);
        }
    }
    @Override
    public ArrayList<AlternativaBean> Search_All(String Search) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM alternativa WHERE cast(id as varchar) LIKE ? OR "
                + "texto LIKE ? OR "
                + "cast(certa as varchar) LIKE ? OR "
                + "cast(questf_id as varchar) LIKE ? "
                + "ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<AlternativaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, "%"+Search+"%");
            stm.setString(2, "%"+Search+"%");
            stm.setString(3, "%"+Search+"%");
            stm.setString(4, "%"+Search+"%");
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
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Alternative"), ex1);
        }
    }
    @Override
    public void Delete(AlternativaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM alternativa WHERE id = ?";
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
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Alternative") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
}
