/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Models.DAOs;

import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.Interfaces.DAOInterface;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.ProvaBean;
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
public class ProvaDAO implements DAOInterface<ProvaBean>{
    private Connection connection;
    
    public ProvaDAO(){
        this.connection = SingletonConnection.GetInstance();
    }
    @Override
    public void Insert(ProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "INSERT INTO prova(tipo, tamanho, nome_prova, nome_prof, nome_curso, nome_inst, data_prova, "
                + "nota, turma, materia_id) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getTipo());
            stm.setInt(2, Bean.getTamanho());
            stm.setString(3, Bean.getNome());
            stm.setString(4, Bean.getProf());
            stm.setString(5, Bean.getCurso());
            stm.setString(6, Bean.getInst());
            stm.setDate(7, Bean.getData());
            stm.setFloat(8, Bean.getNota());
            stm.setString(9, Bean.getTurma());
            stm.setInt(10, Bean.getMateria_id());
            stm.execute();
            this.connection.commit();
            stm.close();
        }catch (SQLException ex1) {
            try{
                this.connection.rollback(); //em caso de falhas tenta da rollback
                throw new AppSQLException(rb.getString("Add database error") + " - " + rb.getString("Exam"), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public void Edit(ProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "UPDATE prova SET tipo = ?, tamanho = ?, nome_prova = ?, nome_prof = ?, nome_curso = ?,"
                + "nome_inst = ?, data_prova = ?, nota = ?, turma = ?, materia_id = ? WHERE id = ?";
        PreparedStatement stm = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setString(1, Bean.getTipo());
            stm.setInt(2, Bean.getTamanho());
            stm.setString(3, Bean.getNome());
            stm.setString(4, Bean.getProf());
            stm.setString(5, Bean.getCurso());
            stm.setString(6, Bean.getInst());
            stm.setDate(7, Bean.getData());
            stm.setFloat(8, Bean.getNota());
            stm.setString(9, Bean.getTurma());
            stm.setInt(10, Bean.getMateria_id());
            stm.setInt(11, Bean.getID());
            stm.execute();
            this.connection.commit();
            stm.close();
        } catch (SQLException ex1) {
            try{
                this.connection.rollback();
                throw new AppSQLException(rb.getString("Modify database error") + " - " + rb.getString("Exam") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    @Override
    public ProvaBean Get_One(ProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM prova WHERE id = ?";
        PreparedStatement stm = null;
        ProvaBean novo = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.setInt(1, Bean.getID());
            stm.execute();
            ResultSet rs = stm.getResultSet();
            rs.next();
            novo = new ProvaBean();
            novo.setID(Bean.getID());
            novo.setTipo(rs.getString("tipo"));
            novo.setTamanho(rs.getInt("tamanho"));
            novo.setProf(rs.getString("nome_prof"));
            novo.setCurso(rs.getString("nome_curso"));
            novo.setInst(rs.getString("nome_inst"));
            novo.setData(rs.getDate("data_prova"));
            novo.setNota(rs.getFloat("nota"));
            novo.setTurma(rs.getString("turma"));
            novo.setMateria_id(rs.getInt("materia_id"));
            novo.setNome(rs.getString("nome_prova"));
            stm.close();
            return novo;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exam") + " ID: " + Bean.getID(), ex1);
        }
    }
    @Override
    public ArrayList<ProvaBean> Get_One_All(ProvaBean Bean) {
        return null; //nesse tipo de DAO não faz sentido o uso deste método
    }
    @Override
    public ArrayList<ProvaBean> Get_All() {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM prova ORDER BY id ASC";
        PreparedStatement stm = null;
        ArrayList<ProvaBean> results = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            results = new ArrayList<>();
            ProvaBean aux = null;
            while(rs.next()){
                aux = new ProvaBean();
                aux.setID(rs.getInt("id"));
                aux.setTipo(rs.getString("tipo"));
                aux.setTamanho(rs.getInt("tamanho"));
                aux.setProf(rs.getString("nome_prof"));
                aux.setCurso(rs.getString("nome_curso"));
                aux.setInst(rs.getString("nome_inst"));
                aux.setData(rs.getDate("data_prova"));
                aux.setNota(rs.getFloat("nota"));
                aux.setTurma(rs.getString("turma"));
                aux.setMateria_id(rs.getInt("materia_ID"));
                aux.setNome(rs.getString("nome_prova"));
                results.add(aux);
            }
            stm.close();
            return results;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Exams"), ex1);
        }
    }
    @Override
    public ArrayList<ProvaBean> Search_All(String Search) {
        return null;
    }
    @Override
    public void Delete(ProvaBean Bean) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "DELETE FROM prova WHERE id = ?";
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
                throw new AppSQLException(rb.getString("Delete database error") + " " + rb.getString("Exam") + " ID: " + Bean.getID(), ex1);
            }catch(SQLException ex2){
                throw new AppSQLException(rb.getString("Rollback error"), ex2);
            }
        }
    }
    public int Get_Last_ID(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String cmd = "SELECT * FROM prova ORDER BY id DESC LIMIT 1";
        PreparedStatement stm = null;
        ProvaBean novo = null;
        try {
            stm = this.connection.prepareStatement(cmd);
            stm.execute();
            ResultSet rs = stm.getResultSet();
            rs.next();
            int a = rs.getInt("id");
            stm.close();
            return a;
        } catch (SQLException ex1) {
            throw new AppSQLException(rb.getString("Retrieve data error") + " " + rb.getString("Last exam id"), ex1);
        }
    }
}
