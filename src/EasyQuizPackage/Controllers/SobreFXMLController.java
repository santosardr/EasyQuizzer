/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers;

import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 *
 * @author Arthur
 */
public class SobreFXMLController extends Stage implements Initializable{

    @FXML
    private Text TituloText;
    @FXML
    private Text TextoText;
    @FXML
    private JFXButton VoltarButton;
    @FXML
    private ImageView EasyQuizLogo;
    
    public SobreFXMLController(Parent parent){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("About"));
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/Sobre.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try {
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(MessageDialogFXMLController.class.getResourceAsStream(Main.LOGO)));
            this.setResizable(false);
        } catch (IOException ex) {
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("About"), rb.getString("Load interface error") + " - " + rb.getString("About") + ": " + ex.getMessage(), null);
            a.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        this.VoltarButton.setOnAction(this::VoltarButtonAction);
    }
    @FXML
    public void VoltarButtonAction(ActionEvent evt){
        PrincipalFXMLController a = new PrincipalFXMLController(null);
        this.hide();
        a.show();
    }
}
