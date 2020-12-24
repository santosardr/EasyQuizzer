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
import javafx.stage.Stage;

public class ChooseIdiomFXMLController extends Stage implements Initializable{

    @FXML
    private JFXButton EnglishButton;
    @FXML
    private JFXButton PortugueseButton;
    @FXML
    private JFXButton VoltarButton;
    @FXML
    private ImageView EasyQuizLogo;
    private static String idiom = "en";
    
    public ChooseIdiomFXMLController(Parent parent){
        this.setTitle("Easy Quizzer - Choose Idiom");
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/ChooseIdiom.fxml"));
        fxmlLoader.setController(this);
        try {
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(MessageDialogFXMLController.class.getResourceAsStream(Main.LOGO)));
            this.setResizable(false);
        } catch (IOException ex) {
            MessageDialogFXMLController a = new MessageDialogFXMLController("Choose Idiom", "Load Choose Idiom interface error: " + ex.getMessage(), null);
            a.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        this.EnglishButton.setOnAction(this::EnglishButtonAction);
        this.PortugueseButton.setOnAction(this::PortugueseButtonAction);
    }
    @FXML
    public void EnglishButtonAction(ActionEvent evt){
        setIdiom("en");
        this.hide();
    }
    @FXML
    public void PortugueseButtonAction(ActionEvent evt){
        setIdiom("pt");
        this.hide();
    }

    public static String getIdiom() {
        return idiom;
    }

    public static void setIdiom(String idiom) {
        ChooseIdiomFXMLController.idiom = idiom;
    }
}
