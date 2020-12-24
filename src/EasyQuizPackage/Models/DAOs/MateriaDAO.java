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
public class MateriaDAO implements DAOInterface<MateriaBean>{
    private Connection connection;
    
    public MateriaDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    @Override
    public void Insert(MateriaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO materia(nome, curso) "
                + "VALUES (?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getNome());
            stm.setInt(2, Bean.getCurso());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Subject"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public void Edit(MateriaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE materia SET nome = ?, curso = ? WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getNome());
            stm.setInt(2, Bean.getCurso());
            stm.setInt(3, Bean.getID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " - " + rb.getString("Subject") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public MateriaBean Get_One(MateriaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM materia WHERE id = ?";
        PreparedStatement stm = null;
        MateriaBean novo = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            rs.next();
            novo = new MateriaBean();
            novo.setID(Bean.getID());
            novo.setNome(rs.getString("nome"));
            novo.setCurso(rs.getInt("curso"));
            stm.close();
            return novo;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Subject") + " ID: " + Bean.getID(), ex1);
        }
    }
    @Override
    public ArrayList<MateriaBean> Get_One_All(MateriaBean Bean) {
        return null; //Nesse tipo de DAO não faz sentido esse método
    }
    @Override
    public ArrayList<MateriaBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT id,nome,curso FROM materia ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<MateriaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            MateriaBean aux = null;
            while(rs.next()){
                aux = new MateriaBean();
                aux.setID(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setCurso(rs.getInt("curso"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Subject"), ex1);
        }
    }
    @Override
    public ArrayList<MateriaBean> Search_All(String Search) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM materia WHERE cast(id as varchar) LIKE ? OR "
                + "nome LIKE ? OR "
                + "curso LIKE ?"
                + "ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<MateriaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, "%"+Search+"%");
            stm.setString(2, "%"+Search+"%");
            stm.setString(3, "%"+Search+"%");
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            MateriaBean aux = null;
            while(rs.next()){
                aux = new MateriaBean();
                aux.setID(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setCurso(rs.getInt("curso"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Subject"), ex1);
        }
    }
    @Override
    public void Delete(MateriaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM materia WHERE id = ?";
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
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Subject") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
}
