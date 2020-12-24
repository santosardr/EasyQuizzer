/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Interfaces;

import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.JDBC.SingletonConnection;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @author Arthur
 */
public abstract class TemplateMethod {
    @FXML
    protected Pane MainPane;
    
    public final void templateMethod(Pane MainPane, String FXMLPath){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.MainPane = MainPane;
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource(FXMLPath));
        loader.setController(this);
        loader.setResources(rb);
        try {
            Node a = loader.load();
            this.MainPane.getChildren().clear();
            this.MainPane.getChildren().add(a);
            FocusTraversableFix();
            AddEvents(a);
            BuildSomething();
        } catch (IOException ex) {
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), ex.getMessage() + " " + ex.getCause(), null);
            a.showAndWait();
        }
    }
    public void FocusTraversableFix(){};
    public void AddEvents(Node node){};
    public void BuildSomething(){};
}
