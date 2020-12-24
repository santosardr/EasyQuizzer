package EasyQuizPackage.Main;


import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.Controllers.PrincipalFXMLController;
import EasyQuizPackage.JDBC.SingletonConnection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arthur
 */
public class Main extends Application{
    public static String LOGO = "/Resources/Images/NovaLogoFlat.png";
    
    @Override
    public void start(Stage stage) throws Exception {
        SingletonConnection.GetInstance(); // Testa conexao / cria o BD na primeira vez
        PrincipalFXMLController a = new PrincipalFXMLController(null);
        a.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void stop(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        try {
            SingletonConnection.GetInstance().close();
        } catch (SQLException ex) {
            MessageDialogFXMLController err = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Close database connection error"), null);
            err.showAndWait();
        }
    }
}
