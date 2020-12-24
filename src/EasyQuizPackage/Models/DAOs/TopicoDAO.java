/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.DAOs;

import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.Interfaces.DAOInterface;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.TopicoBean;
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
public class TopicoDAO implements DAOInterface<TopicoBean>{
    private Connection connection;
    
    public TopicoDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    @Override
    public void Insert(TopicoBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO topico(nome, matid) "
                + "VALUES (?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getNome());
            stm.setInt(2, Bean.getMateria_ID());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Topic"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public void Edit(TopicoBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE topico SET nome = ?, matid = ? WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getNome());
            stm.setInt(2, Bean.getMateria_ID());
            stm.setInt(3, Bean.getID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " - " + rb.getString("Topic") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public TopicoBean Get_One(TopicoBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM topico WHERE id = ?";
        PreparedStatement stm = null;
        TopicoBean novo = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            rs.next();
            novo = new TopicoBean();
            novo.setID(Bean.getID());
            novo.setNome(rs.getString("nome"));
            novo.setMateria_ID(rs.getInt("matid"));
            stm.close();
            return novo;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Topic") + " ID: " + Bean.getID(), ex1);
        }
    }
    @Override
    public ArrayList<TopicoBean> Get_One_All(TopicoBean Bean) {
        return null; //Neste tipo de DAO não faz sentido o uso deste método
    }
    @Override
    public ArrayList<TopicoBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM topico ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<TopicoBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            TopicoBean aux = null;
            while(rs.next()){
                aux = new TopicoBean();
                aux.setID(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setMateria_ID(rs.getInt("matid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Topic"), ex1);
        }
    }
    @Override
    public ArrayList<TopicoBean> Search_All(String Search) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM topico WHERE cast(id as varchar) LIKE ? OR "
                + "nome LIKE ? OR "
                + "cast(matid as varchar) LIKE ?"
                + "ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<TopicoBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, "%"+Search+"%");
            stm.setString(2, "%"+Search+"%");
            stm.setString(3, "%"+Search+"%");
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            TopicoBean aux = null;
            while(rs.next()){
                aux = new TopicoBean();
                aux.setID(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setMateria_ID(rs.getInt("matid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Topic"), ex1);
        }
    }
    @Override
    public void Delete(TopicoBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM topico WHERE id = ?";
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
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Topic") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    
    public ArrayList<TopicoBean> Get_From_Materia(MateriaBean bean){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM topico WHERE matid = ?";
        PreparedStatement stm = null;
        ArrayList<TopicoBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            TopicoBean aux = null;
            while(rs.next()){
                aux = new TopicoBean();
                aux.setID(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setMateria_ID(rs.getInt("matid"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Topic"), ex1);
        }
    }
}
