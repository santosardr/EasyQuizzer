/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers;

import EasyQuizPackage.Controllers.SubControllers.DBConfigDiagFXMLController;
import EasyQuizPackage.Controllers.SubControllers.OpenProvaFXMLController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.image.Image;
/**
 *
 * @author Arthur
 */
public class PrincipalFXMLController extends Stage implements Initializable {
    
    @FXML
    private JFXButton NovaProvaButton;
    @FXML
    private JFXButton CarregarProvaButton;
    @FXML
    private JFXButton BancoButton;
    @FXML
    private JFXButton ConfigButton;
    @FXML
    private JFXButton SobreButton;
    @FXML
    private JFXButton SairButton;
    @FXML
    private ImageView EasyQuizLogo;
    public PrincipalFXMLController(Parent parent){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Main"));
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/Principal.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(MessageDialogFXMLController.class.getResourceAsStream(Main.LOGO)));
            this.setResizable(false);
        } catch (IOException e){
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Main"), rb.getString("Error") + ": " + e.getMessage(), null);
             a.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle rb){
        this.NovaProvaButton.setOnAction(this::NovaProvaButtonAction);
        if(!SingletonConnection.populatedDB())
            this.NovaProvaButton.setDisable(true);
        this.CarregarProvaButton.setOnAction(this::CarregarProvaButtonAction);
        this.ConfigButton.setOnAction(this::ConfigButtonAction);
        this.SobreButton.setOnAction(this::SobreButtonAction);
        this.BancoButton.setOnAction(this::BancoButtonAction);
        this.SairButton.setOnAction(this::SairButtonAction);
    }
    @FXML
    public void ConfigButtonAction(ActionEvent evt){
        IdiomFXMLController config = new IdiomFXMLController(null);
        this.hide();
        config.show();
    }
    @FXML
    public void SobreButtonAction(ActionEvent evt){
        SobreFXMLController sobre = new SobreFXMLController(null);
        this.hide();
        sobre.show();
    }
    @FXML
    public void BancoButtonAction(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.hide();
        banco.show();
    }
    @FXML
    public void SairButtonAction(ActionEvent evt){
        System.exit(0);
    }
    @FXML
    public void NovaProvaButtonAction(ActionEvent evt){
        NovaProvaFXMLController prova = new NovaProvaFXMLController(null,null);
        this.hide();
        prova.show();
    }
    @FXML
    public void CarregarProvaButtonAction(ActionEvent evt){
        OpenProvaFXMLController a = new OpenProvaFXMLController(null);
        this.hide();
        a.show();
    }
}
