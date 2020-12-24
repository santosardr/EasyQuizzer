/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers;

import EasyQuizPackage.JDBC.SingletonConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Arthur
 */
public class SpinnerDialogFXMLController extends Stage implements Initializable{
    @FXML
    private Label TextLabel;
    @FXML
    private Label TituloLabel;
    private String text;
    private String titulo;
    public SpinnerDialogFXMLController(String titulo, String text, Parent parent){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer"));
        this.titulo = titulo;
        this.text = text;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/SpinnerDialog.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(SpinnerDialogFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(SpinnerDialogFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(SpinnerDialogFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            this.getIcons().add(new Image(SpinnerDialogFXMLController.class.getResourceAsStream("/Resources/Images/logo_normal.png")));
            setScene(cena);
        }
        catch (Exception e){
            System.out.println(e.getCause()+" "+e.getMessage());
        }
    }
    public SpinnerDialogFXMLController(String titulo, String text, ResourceBundle rb, Parent parent){
        this.setTitle(rb.getString("Easy Quizzer"));
        this.titulo = titulo;
        this.text = text;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/SpinnerDialog.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(SpinnerDialogFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(SpinnerDialogFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(SpinnerDialogFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            this.getIcons().add(new Image(SpinnerDialogFXMLController.class.getResourceAsStream("/Resources/Images/logo_normal.png")));
            setScene(cena);
        }
        catch (Exception e){
            System.out.println(e.getCause()+" "+e.getMessage());
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.TituloLabel.setText(this.titulo);
        this.TextLabel.setText(this.text);
    }
}
