/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Exceptions;

import java.sql.SQLException;

/**
 *
 * @author Arthur
 */
public class AppSQLException extends RuntimeException{
    private SQLException base;
    public AppSQLException(){
        //Empty Constructor
    }
    public AppSQLException(String message, SQLException ex){
        super(message);
        this.base = ex;
    }
    public SQLException getBaseException(){
        return this.base;
    }
}
