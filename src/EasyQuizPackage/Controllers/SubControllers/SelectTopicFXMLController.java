/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.BancoFXMLController;
import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.Interfaces.TemplateMethod;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.TopicoBean;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import EasyQuizPackage.Models.DAOs.TopicoDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Arthur
 */
public class SelectTopicFXMLController extends TemplateMethod{
    @FXML
    private FlowPane MainPane;
    @FXML
    private JFXComboBox Materia;
    @FXML
    private JFXComboBox Topico;
    @FXML
    private JFXButton VerAltBtn;
    @FXML
    private JFXButton VoltarBtn;
    
    public SelectTopicFXMLController(FlowPane MainPane){
        this.MainPane = MainPane;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/SelectTopic.fxml");
        this.MainPane.setMinSize(600, 0);
        MateriaDAO matdao = new MateriaDAO();
        ArrayList<MateriaBean> result = matdao.Get_All();
        ArrayList<String> Materias = new ArrayList<>();
        for(MateriaBean bean: result){
            Materias.add(bean.getID()+" : "+bean.getNome());
        }
        this.Materia.getItems().addAll(Materias);
    }
    
    @Override
    public void FocusTraversableFix(){
        
    }
    @Override
    public void AddEvents(Node node){
        AnchorPane AddQuest = ((AnchorPane)node);
        AddQuest.setPrefSize(this.MainPane.getWidth()-5, this.MainPane.getHeight()-5);
            this.MainPane.getScene().widthProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                    AddQuest.setPrefWidth((double) newSceneWidth-5);
                }
            });
            this.MainPane.getScene().heightProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                    AddQuest.setPrefHeight((double) newSceneHeight-5);
                }
        });
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
        this.VerAltBtn.setOnAction(this::VerAltBtnClick);
        this.Materia.setOnAction(this::MateriaSelect);
    }
    //funcionalidade da ComboBox de seleção de matéria
    public void MateriaSelect(Event evt){
        this.Topico.getItems().clear();
        MateriaBean bean = new MateriaBean();
        bean.setID(Integer.parseInt( ((String)this.Materia.getSelectionModel().getSelectedItem()).split(" : ")[0] ));
        TopicoDAO dao = new TopicoDAO();
        ArrayList<TopicoBean> topicos = dao.Get_From_Materia(bean);
        ArrayList<String> lista_topicos = new ArrayList<>();
        topicos.stream().forEach((topbean) -> {
            lista_topicos.add(topbean.getID()+" : "+topbean.getNome());
        });
        this.Topico.getItems().addAll(lista_topicos);
    }
    //funcionalidade do botão de ver alternativas
    public void VerAltBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        boolean errors = false;
        String err_msg = "";
        if(this.Topico.getSelectionModel().getSelectedItem() == null){
            errors=true;
            err_msg += rb.getString("Unselected question topic") + "\n";
        }
        if(errors){
            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), err_msg, null);
            msg.showAndWait();
        }else{
            String nomeMateria = ((String)this.Materia.getSelectionModel().getSelectedItem()).split(" : ")[1];
            String nomeTopico = ((String)this.Topico.getSelectionModel().getSelectedItem()).split(" : ")[0];
            new GerenAltFXMLController(this.MainPane, nomeMateria, nomeTopico);
        }
    }
    //funcionalidade do botão voltar
    public void VoltarBtnClick(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
}
