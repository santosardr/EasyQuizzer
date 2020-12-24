/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
public class DBConfigDiagFXMLController extends Stage implements Initializable{
    @FXML
    private Label error_text;
    @FXML
    private JFXTextField endereco;
    @FXML
    private JFXTextField user;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton btn;
    private String end;
    private String usr;
    private String pass;
    public DBConfigDiagFXMLController(String end, String usr, String pass, ResourceBundle rb, Parent parent){
        this.setTitle(rb.getString("Easy Quizzer"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/DBConfigDiag.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(DBConfigDiagFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(DBConfigDiagFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(DBConfigDiagFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(DBConfigDiagFXMLController.class.getResourceAsStream(Main.LOGO)));
        }
        catch (IOException e){
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), e.getMessage(), rb, null);
            a.showAndWait();
        }
        this.end = end;
        this.usr = usr;
        this.pass = pass;
        this.endereco.setText(end);
        this.user.setText(usr);
        this.password.setText(pass);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.btn.setOnAction(this::btnClick);
    }
    private void btnClick(ActionEvent evt){
        SingletonConnection.endereco_adm = this.endereco.getText();
        SingletonConnection.user_adm = this.user.getText();
        SingletonConnection.pass_adm = this.password.getText();
        SingletonConnection.updateEndereco();
        this.close();
    }
}
