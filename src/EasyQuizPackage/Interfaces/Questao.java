/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Interfaces;

import EasyQuizPackage.Models.Beans.AlternativaBean;
import java.util.ArrayList;

/**
 *
 * @author Arthur
 */
public interface Questao {
    public String getEnunciado();
    public ArrayList<AlternativaBean> getAlternativasCertas();
    public ArrayList<AlternativaBean> getAlternativasErradas();
    public ArrayList<AlternativaBean> getAlternativas();
    public String getGabarito();
    public int getNumLinhas();
}
