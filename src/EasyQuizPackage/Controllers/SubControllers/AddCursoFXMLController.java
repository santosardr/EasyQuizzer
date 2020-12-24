/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.BancoFXMLController;
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
import javafx.stage.Stage;

/**
 *
 * @author Arthur
 */
public class AddCursoFXMLController extends TemplateMethod{
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
    @FXML
    private FlowPane fpane;
    public AddCursoFXMLController(FlowPane MainPane){
        this.MainPane = MainPane;
        this.MainPane.setMinSize(363, 152);
        ((Stage)this.MainPane.getScene().getWindow()).setWidth(700);
        ((Stage)this.MainPane.getScene().getWindow()).setHeight(200);
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/AddCur.fxml");
        this.fpane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.fpane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.apane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.apane.prefWidthProperty().bind(this.MainPane.widthProperty());
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
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
    //funcionalidade do botão de adicionar novo curso
    public void AddCurBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        if(this.Nome.getText().equals("") || this.Nome.getText() == null){
            MessageDialogFXMLController mesg = new MessageDialogFXMLController(rb.getString("Invalid Data"), (rb.getString("Field") + " \"" + rb.getString("Name") + "\" " + rb.getString("can not be blank") + "\n"), null);
            mesg.showAndWait();
        }else{
            CursoBean bean = new CursoBean();
            bean.setNome(this.Nome.getText());
            CursoDAO dao = new CursoDAO();
            try{
                dao.Insert(bean);
                MessageDialogFXMLController success = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Success adding new course"), null);
                success.showAndWait();
                this.Nome.setText("");
            }catch(AppSQLException ex){
                MessageDialogFXMLController error = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Add database error") + " - " + rb.getString("Course") + ": " + ex.getBaseException().getMessage(), null);
                error.showAndWait();
            }
        }
    }
}
