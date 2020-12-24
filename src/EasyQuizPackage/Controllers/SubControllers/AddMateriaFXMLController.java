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
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.DAOs.CursoDAO;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Arthur
 */
public class AddMateriaFXMLController extends TemplateMethod{
    @FXML
    private JFXTextField NomeMat;
    @FXML
    private JFXComboBox CurMat;
    @FXML
    private JFXButton AddMatBtn;
    @FXML
    private JFXButton VoltarBtn;
    @FXML
    private FlowPane MainPane;
    @FXML
    private AnchorPane apane;
    public AddMateriaFXMLController(FlowPane MainPane){
        this.MainPane = MainPane;
        this.MainPane.setMinSize(500, 160);
        ((Stage)this.MainPane.getScene().getWindow()).setWidth(650);
        ((Stage)this.MainPane.getScene().getWindow()).setHeight(250);
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/AddMat.fxml");
        this.apane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.apane.prefWidthProperty().bind(this.MainPane.widthProperty());
        CursoDAO dao = new CursoDAO();
        ArrayList<CursoBean> arr = dao.Get_All();
        arr.stream().forEach((a) -> {
            this.CurMat.getItems().add(a.getID()+" : "+a.getNome());
        });
    }
    
    @Override
    public void FocusTraversableFix(){
    }
    @Override
    public void AddEvents(Node node){
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
        this.AddMatBtn.setOnAction(this::AddMatBtnClick);
    }
    //funcionalidade do botão voltar
    public void VoltarBtnClick(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
    //funcionalidade do botão de adicionar nova matéria
    public void AddMatBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        boolean error = false;
        String erros = "";
        
        if(this.NomeMat.getText().equals("") || this.NomeMat.getText() == null){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Subject's Name") + "\" " + rb.getString("can not be blank") + "\n";
        }
        try { Integer.parseInt(this.CurMat.getSelectionModel().getSelectedItem().toString().split(" : ")[0]); }
        catch (Exception e) {
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Course") + "\" " + rb.getString("can not be blank") + "\n";
        }
        
        if(error){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Invalid Data"), rb.getString("The following errors were found") + ":\n" + erros, null);
            mes.showAndWait();
        }else{
            MateriaBean bean = new MateriaBean();
            bean.setNome(this.NomeMat.getText());
            bean.setCurso(Integer.parseInt(this.CurMat.getSelectionModel().getSelectedItem().toString().split(" : ")[0]));
            MateriaDAO dao = new MateriaDAO();
            try{
                dao.Insert(bean);
                MessageDialogFXMLController success = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Success adding new subject"), null);
                success.showAndWait();
                this.NomeMat.setText("");
                this.CurMat.getSelectionModel().clearSelection();
            }catch(AppSQLException ex){
                MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Add database error") + " - " + rb.getString("Subject") + ": " + ex.getBaseException().getMessage(), null);
                msg.showAndWait();
            }
        }
    }
}
