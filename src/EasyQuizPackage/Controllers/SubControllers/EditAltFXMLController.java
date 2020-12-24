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
public class EditAltFXMLController extends TemplateMethod{
    @FXML
    private FlowPane MainPane;
    @FXML
    private JFXTextArea TextoAlt;
    @FXML
    private JFXCheckBox Certa;
    @FXML
    private JFXButton VoltarBtn;
    @FXML
    private JFXButton SalvarBtn;
    @FXML
    private AnchorPane apane;
    private AlternativaBean Alternativa;
    TreeItem<QuestaoFechadaBean> selectedItem;
    GerenAltFXMLController janelaAnterior;
    private String nomeMateria;
    private String nomeTopico;
    public EditAltFXMLController(GerenAltFXMLController janela, FlowPane MainPane, Object Alternativa, TreeItem<QuestaoFechadaBean> selectedItem, String nomeMateria, String nomeTopico){
        this.MainPane = MainPane;
        this.Alternativa = ((AlternativaBean)Alternativa);
        this.selectedItem = selectedItem;
        this.janelaAnterior = janela;
        this.nomeMateria = nomeMateria;
        this.nomeTopico = nomeTopico;
        this.MainPane.setMinSize(550, 248);
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/EditAlt.fxml");
        this.apane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.apane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.MainPane.getScene().getWindow().centerOnScreen();
        this.TextoAlt.setText(this.Alternativa.getTexto());
        this.Certa.setSelected(this.Alternativa.isCerta());
    }
    @Override
    public void AddEvents(Node node){
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
        this.SalvarBtn.setOnAction(this::SalvarBtnClick);
    }
    public void VoltarBtnClick(ActionEvent evt){
        GerenAltFXMLController gerenalt = new GerenAltFXMLController(this.MainPane, this.selectedItem, this.nomeMateria, this.nomeTopico);
        this.MainPane.getScene().getWindow().hide();
    }
    public void SalvarBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.Alternativa.setTexto(this.TextoAlt.getText());
        this.Alternativa.setCerta(this.Certa.isSelected());
        AlternativaDAO dao = new AlternativaDAO();
        try {
            dao.Edit(this.Alternativa);
            MessageDialogFXMLController success = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Successfully edited"), null);
            success.showAndWait();
            this.janelaAnterior.SelectQuest(selectedItem);
            this.MainPane.getScene().getWindow().hide();
        } catch (AppSQLException e) {
            MessageDialogFXMLController failure = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Edit error") + ": " + e.getBaseException().getMessage(), null);
            failure.showAndWait();
        }
        
    }
}
