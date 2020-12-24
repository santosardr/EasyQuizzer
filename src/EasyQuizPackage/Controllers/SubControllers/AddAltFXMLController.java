/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.Interfaces.TemplateMethod;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.AlternativaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.DAOs.AlternativaDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Arthur
 */
public class AddAltFXMLController extends TemplateMethod{
    @FXML
    private FlowPane MainPane;
    @FXML
    private JFXTextArea TextoAlt;
    @FXML
    private JFXCheckBox Certa;
    @FXML
    private JFXButton AddBtn;
    @FXML
    private JFXButton VoltarBtn;
    private QuestaoFechadaBean questf;
    private TreeItem<QuestaoFechadaBean> selectedItem;
    private GerenAltFXMLController janelaAnterior;
    private String nomeMateria;
    private String nomeTopico;
    public AddAltFXMLController(GerenAltFXMLController janela, FlowPane MainPane, TreeItem<QuestaoFechadaBean> selectedItem, String nomeMateria, String nomeTopico){
        this.questf = selectedItem.getValue();
        this.selectedItem = selectedItem;
        this.janelaAnterior = janela;
        this.nomeMateria = nomeMateria;
        this.nomeTopico = nomeTopico;
        this.MainPane = MainPane;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/AddAlt.fxml");
    }
    @Override
    public void FocusTraversableFix(){
    }
    @Override
    public void AddEvents(Node node){
        AnchorPane AddAlt = ((AnchorPane)node);
        AddAlt.setPrefSize(this.MainPane.getWidth()-5, this.MainPane.getHeight()-5);
            this.MainPane.getScene().widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                    AddAlt.setPrefWidth((double) newSceneWidth-5);
                }
            });
            this.MainPane.getScene().heightProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                    AddAlt.setPrefHeight((double) newSceneHeight-5);
                }
        });
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
        this.AddBtn.setOnAction(this::AddBtnClick);
    }
    @Override
    public void BuildSomething(){
    }
    public void VoltarBtnClick(ActionEvent evt){
        //GerenAltFXMLController alt = new GerenAltFXMLController(this.MainPane, this.selectedItem, this.nomeMateria, this.nomeTopico);
        this.MainPane.getScene().getWindow().hide();
    }
    public void AddBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        boolean error = false;
        String msg = "";
        if("".equals(this.TextoAlt.getText())){
            error = true;
            msg += rb.getString("Field") + " \"" + rb.getString("Alternative Text") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(error){
            MessageDialogFXMLController md = new MessageDialogFXMLController(rb.getString("Invalid Data"), msg, null);
            md.showAndWait();
        }else{
            AlternativaBean altbean = new AlternativaBean();
            AlternativaDAO dao = new AlternativaDAO();
            altbean.setTexto(this.TextoAlt.getText());
            altbean.setCerta(this.Certa.isSelected());
            altbean.setQuestFID(questf.getID());
            try{
                dao.Insert(altbean);
                this.janelaAnterior.SelectQuest(selectedItem);
                this.MainPane.getScene().getWindow().hide();
            }catch(AppSQLException ex){
                MessageDialogFXMLController md = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Add database error") + " - " + rb.getString("Alternative") + ": " + ex.getBaseException().getMessage(), null);
                md.showAndWait();
            }
        }
    }
}
