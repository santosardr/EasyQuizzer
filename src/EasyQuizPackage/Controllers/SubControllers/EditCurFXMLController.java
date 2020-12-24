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
import EasyQuizPackage.Models.Beans.CursoBean;
import EasyQuizPackage.Models.DAOs.CursoDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Arthur
 */
public class EditCurFXMLController extends TemplateMethod{
    @FXML
    private JFXTextField Nome;
    @FXML
    private JFXButton AddCurBtn;
    @FXML
    private JFXButton VoltarBtn;
    @FXML
    private FlowPane MainPane;
    @FXML
    private AnchorPane apane;
    private CursoBean curso;
    public EditCurFXMLController(FlowPane MainPane, CursoBean bean){
        this.curso = bean;
        this.MainPane = MainPane;
        this.MainPane.setMinSize(500, 160);
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/EditCur.fxml");
        this.apane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.apane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.MainPane.getScene().getWindow().centerOnScreen();
        this.Nome.setText(this.curso.getNome());
    }
    
    @Override
    public void FocusTraversableFix(){
    }
    @Override
    public void AddEvents(Node node){
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
        this.AddCurBtn.setOnAction(this::AddCurBtnClick);
    }
    //funcionalidade do botão voltar
    public void VoltarBtnClick(ActionEvent evt){
        GerenCurFXMLController a = new GerenCurFXMLController(this.MainPane);
    }
    //funcionalidade do botão de adicionar nova matéria
    public void AddCurBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        if(this.Nome.getText().equals("") || this.Nome.getText() == null){
            String err_msg = rb.getString("Field") + " \"" + rb.getString("Name") + "\" " + rb.getString("can not be blank") + "\n";
            MessageDialogFXMLController mesg = new MessageDialogFXMLController(rb.getString("Invalid Data"), err_msg, null);
            mesg.showAndWait();
        }else{
            CursoBean bean = new CursoBean();
            bean.setNome(this.Nome.getText());
            bean.setID(this.curso.getID());
            CursoDAO dao = new CursoDAO();
            try{
                dao.Edit(bean);
                MessageDialogFXMLController success = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Successfully edited"), null);
                success.showAndWait();
                this.VoltarBtnClick(evt);
                this.Nome.setText("");
            }catch(AppSQLException e){
                MessageDialogFXMLController failure = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Edit error") + ": " + e.getBaseException().getMessage(), null);
                failure.showAndWait();
            }
        }
    }
}

