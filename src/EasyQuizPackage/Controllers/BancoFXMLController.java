/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers;

import EasyQuizPackage.Controllers.SubControllers.AddCursoFXMLController;
import EasyQuizPackage.Controllers.SubControllers.AddMateriaFXMLController;
import EasyQuizPackage.Controllers.SubControllers.AddQuestaoFXMLController;
import EasyQuizPackage.Controllers.SubControllers.AddTopicoFXMLController;
import EasyQuizPackage.Controllers.SubControllers.GerenCurFXMLController;
import EasyQuizPackage.Controllers.SubControllers.GerenMatFXMLController;
import EasyQuizPackage.Controllers.SubControllers.GerenQuestFXMLController;
import EasyQuizPackage.Controllers.SubControllers.GerenTopFXMLController;
import EasyQuizPackage.Controllers.SubControllers.SelectTopicFXMLController;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Arthur
 */
public class BancoFXMLController extends Stage implements Initializable {
    
    @FXML
    FlowPane MainFlowPane;
    
    public BancoFXMLController(Parent parent){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Questions Database"));
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/Banco.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        this.setWidth(750);
        this.setHeight(550);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(MessageDialogFXMLController.class.getResourceAsStream(Main.LOGO)));   
        }
        catch (Exception e){
            MessageDialogFXMLController a = new MessageDialogFXMLController("Banco de Dados","Erro: "+e.getMessage(),null);
             a.showAndWait();
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CreateMetroButtonOnPane(rb.getString("ADD") + "\n" + rb.getString("COURSE"),"/Resources/Images/Icons/addcur.fw.png", this::AddCurMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("MANAGE") + "\n" + rb.getString("COURSES"), "/Resources/Images/Icons/gerencur.fw.png", this::GerenCurMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("ADD") + "\n" + rb.getString("SUBJECT"),"/Resources/Images/Icons/addmat.fw.png", this::AddMatMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("MANAGE") + "\n" + rb.getString("SUBJECTS"), "/Resources/Images/Icons/gerenmat.fw.png", this::GerenMatMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("ADD") + "\n" + rb.getString("TOPIC"),"/Resources/Images/Icons/addtop.fw.png", this::AddTopicoMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("MANAGE") + "\n" + rb.getString("TOPICS"), "/Resources/Images/Icons/gerentop.fw.png", this::GerenTopicoMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("ADD") + "\n" + rb.getString("QUESTION"),"/Resources/Images/Icons/addquest.fw.png", this::AddQuestMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("MANAGE") + "\n" + rb.getString("QUESTIONS"), "/Resources/Images/Icons/gerenquest.fw.png", this::GerenQuestMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("MANAGE") + "\n" + rb.getString("ALTERNATIVES"), "/Resources/Images/Icons/gerenalt.fw.png", this::GerenAltMouseClick, this.MainFlowPane);
        CreateMetroButtonOnPane(rb.getString("BACK"),"/Resources/Images/Icons/ic_arrow_back_white_48dp_2x.png",this::VoltarMouseClick,this.MainFlowPane);
    }
    public FlowPane getMainFlowPane() {
        return MainFlowPane;
    }
    //método de criar botões na tela principal
    public void CreateMetroButtonOnPane(String Text,String Path, EventHandler<ActionEvent> evt, FlowPane pane){
        JFXButton a = new JFXButton();
        a.setText(Text);
        a.getStyleClass().add("Button2");
        a.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Path))));
        a.setContentDisplay(ContentDisplay.TOP);
        a.setOnAction(evt);
        a.setFocusTraversable(false);
        pane.getChildren().add(a);
    }
    //Funcionalidade do Botão Voltar
    public void VoltarMouseClick(ActionEvent evt){
        PrincipalFXMLController prin = new PrincipalFXMLController(null);
        this.hide();
        prin.show();
    }
    //Funcionalidade para mudar a View para a View de Adição de Matéria
    public void AddMatMouseClick(ActionEvent evt){
        AddMateriaFXMLController mat = new AddMateriaFXMLController(((FlowPane)this.MainFlowPane));
    }
    //Funcionalidade para mudar a View para a View de Gerenciamento de Matérias
    public void AddCurMouseClick(ActionEvent evt){
        AddCursoFXMLController a = new AddCursoFXMLController((FlowPane)this.MainFlowPane);
    }
    public void GerenCurMouseClick(ActionEvent evt){
        GerenCurFXMLController a = new GerenCurFXMLController((FlowPane)this.MainFlowPane);
    }
    public void GerenMatMouseClick(ActionEvent evt){
        GerenMatFXMLController gere = new GerenMatFXMLController(((FlowPane)this.MainFlowPane));
    }
    public void AddTopicoMouseClick(ActionEvent evt){
        AddTopicoFXMLController addt = new AddTopicoFXMLController(((FlowPane)this.MainFlowPane));
    }
    public void GerenTopicoMouseClick(ActionEvent evt){
        GerenTopFXMLController getop = new GerenTopFXMLController(((FlowPane)this.MainFlowPane));
    }
    public void AddQuestMouseClick(ActionEvent evt){
        AddQuestaoFXMLController addquest = new AddQuestaoFXMLController(((FlowPane)this.MainFlowPane));
    }
    public void GerenQuestMouseClick(ActionEvent evt){
        GerenQuestFXMLController gerenquest = new GerenQuestFXMLController(((FlowPane)this.MainFlowPane));
    }
    public void GerenAltMouseClick(ActionEvent evt){
        //GerenAltFXMLController gerenalt = new GerenAltFXMLController(((FlowPane)this.MainFlowPane));
        SelectTopicFXMLController selectTopic = new SelectTopicFXMLController(((FlowPane)this.MainFlowPane));
    }
}
