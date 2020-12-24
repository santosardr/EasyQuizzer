/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Arthur
 */
public class ConfirmDialogFXMLController {
    public static boolean Display(String header, String text){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        ButtonType sim = new ButtonType(rb.getString("YES"), ButtonBar.ButtonData.OK_DONE);
        ButtonType nao = new ButtonType(rb.getString("NO"), ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.CONFIRMATION,text,sim,nao);
        alert.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Confirm"));
        alert.setHeaderText(header);
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add(ConfirmDialogFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
        alert.getDialogPane().getStylesheets().add(ConfirmDialogFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
        alert.getDialogPane().getStylesheets().add(ConfirmDialogFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(ConfirmDialogFXMLController.class.getResourceAsStream(Main.LOGO)));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == sim;
    }
}
