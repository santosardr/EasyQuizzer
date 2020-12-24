package EasyQuizPackage.Controllers;

import EasyQuizPackage.Controllers.SubControllers.DBConfigDiagFXMLController;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class IdiomFXMLController extends Stage implements Initializable{

    @FXML
    private JFXButton EnglishButton;
    @FXML
    private JFXButton PortugueseButton;
    @FXML
    private JFXButton DBConfigButton;
    @FXML
    private JFXButton VoltarButton;
    @FXML
    private ImageView EasyQuizLogo;
    
    public IdiomFXMLController(Parent parent){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Settings"));
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/Idiom.fxml"));
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
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Settings"), rb.getString("Load interface error") + " - " + rb.getString("Settings") + ": " + ex.getMessage(), null);
            a.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        this.EnglishButton.setOnAction(this::EnglishButtonAction);
        this.PortugueseButton.setOnAction(this::PortugueseButtonAction);
        this.DBConfigButton.setOnAction(this::DBConfigButtonAction);
        this.VoltarButton.setOnAction(this::VoltarButtonAction);
    }
    @FXML
    public void EnglishButtonAction(ActionEvent evt){
        SingletonConnection.setIdiom("en");
        PrincipalFXMLController a = new PrincipalFXMLController(null);
        this.hide();
        a.show();
    }
    @FXML
    public void PortugueseButtonAction(ActionEvent evt){
        SingletonConnection.setIdiom("pt");
        PrincipalFXMLController a = new PrincipalFXMLController(null);
        this.hide();
        a.show();
    }
    @FXML
    public void DBConfigButtonAction(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        DBConfigDiagFXMLController dbc = new DBConfigDiagFXMLController(SingletonConnection.endereco_adm, SingletonConnection.user_adm, SingletonConnection.pass_adm, rb, null);
        this.hide();
        try { SingletonConnection.GetInstance().close(); } catch(SQLException e) { /* Ignored */ }
        dbc.showAndWait();
        IdiomFXMLController idiom = new IdiomFXMLController(null);
        idiom.show();
    }
    @FXML
    public void VoltarButtonAction(ActionEvent evt){
        PrincipalFXMLController a = new PrincipalFXMLController(null);
        this.hide();
        a.show();
    }
}
