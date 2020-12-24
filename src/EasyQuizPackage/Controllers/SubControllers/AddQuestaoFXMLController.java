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
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.QuestaoAbertaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.Beans.TopicoBean;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoAbertaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaDAO;
import EasyQuizPackage.Models.DAOs.TopicoDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
public class AddQuestaoFXMLController extends TemplateMethod{
    @FXML
    private FlowPane MainPane;
    @FXML
    private JFXTextArea Enunciado;
    @FXML
    private JFXComboBox Tipo;
    @FXML
    private JFXTextField Linhas;
    @FXML
    private JFXTextArea Gabarito;
    @FXML
    private JFXComboBox Materia;
    @FXML
    private JFXComboBox Topico;
    @FXML
    private JFXButton AddBtn;
    @FXML
    private JFXButton VoltarBtn;
    
    public AddQuestaoFXMLController(FlowPane MainPane){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.MainPane = MainPane;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/AddQuest.fxml");
        this.MainPane.setMinSize(600, 0);
        this.Linhas.setDisable(true);
        this.Gabarito.setDisable(true);
        this.Tipo.getItems().add(rb.getString("Open"));
        this.Tipo.getItems().add(rb.getString("Closed"));
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
        this.Enunciado.setFocusTraversable(false);
        this.Linhas.setFocusTraversable(false);
        this.Gabarito.setFocusTraversable(false);
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
        this.AddBtn.setOnAction(this::AddMatBtnClick);
        this.Tipo.setOnAction(this::TipoSelect);
        this.Materia.setOnAction(this::MateriaSelect);
    }
    //funcionalidade do botão voltar
    public void VoltarBtnClick(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
    //funcionalidade do botão de adicionar nova questão
    public void AddMatBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        boolean errors = false;
        String err_msg = "";
        if(this.Enunciado.getText().equals("")){
            errors = true;
            err_msg += rb.getString("Field") + " \"" + rb.getString("Statement") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(this.Tipo.getSelectionModel().getSelectedItem() == null){
            errors = true;
            err_msg += rb.getString("Unselected question type") + "\n";
        }
        if(this.Topico.getSelectionModel().getSelectedItem() == null){
            errors = true;
            err_msg += rb.getString("Unselected question topic") + "\n";
        }else{
           if(((String)this.Tipo.getSelectionModel().getSelectedItem()).equals(rb.getString("Open"))){
                if(this.Gabarito.getText().equals("")){
                    errors = true;
                    err_msg += rb.getString("Field") + " \"" + rb.getString("Answer") + "\" " + rb.getString("can not be blank") + "\n";
                }
                if(this.Linhas.getText().equals("")){
                    errors = true;
                    err_msg += rb.getString("Field") + " \"" + rb.getString("Number of lines to skip") + "\" " + rb.getString("can not be blank") + "\n";
                }
                if(this.Linhas.getText().replaceAll("\\d+","").length() != 0){
                    errors = true;
                    err_msg += rb.getString("Field") + " \"" + rb.getString("Number of lines to skip") + "\" " + rb.getString("can not contain letters") + "\n";
                }
            }
        }
        if(errors){
            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Invalid Data"), rb.getString("The following errors were found") + ":\n" + err_msg, null);
            msg.showAndWait();
        }else{
            if(((String)this.Tipo.getSelectionModel().getSelectedItem()).equals(rb.getString("Open"))) {
                    QuestaoAbertaBean questabt = new QuestaoAbertaBean();
                    questabt.setEnunciado(this.Enunciado.getText());
                    questabt.setGabarito(this.Gabarito.getText());
                    questabt.setNumLinhas(Integer.parseInt(((String)this.Linhas.getText())));
                    questabt.setTopico_ID(Integer.parseInt(((String)this.Topico.getSelectionModel().getSelectedItem()).split(" : ")[0]));
                    QuestaoAbertaDAO questabtdao = new QuestaoAbertaDAO();
                    try {
                        questabtdao.Insert(questabt);
                        MessageDialogFXMLController suc = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Success adding new question"), null);
                        suc.showAndWait();
                        this.LimpaCampos();
                    } catch (AppSQLException e) {
                        MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Add database error") + " - " + rb.getString("Open question") + ": " + e.getBaseException().getMessage(), null);
                        msg.showAndWait();
                    }
            } else {
                if(((String)this.Tipo.getSelectionModel().getSelectedItem()).equals(rb.getString("Closed"))) {
                    QuestaoFechadaBean questf = new QuestaoFechadaBean();
                    questf.setEnunciado(this.Enunciado.getText());
                    questf.setTopico_ID( Integer.parseInt( ((String)this.Topico.getSelectionModel().getSelectedItem()).split(" : ")[0] ) );
                    QuestaoFechadaDAO questfdao = new QuestaoFechadaDAO();
                    try {
                        questfdao.Insert(questf);
                        MessageDialogFXMLController suc = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Success adding new question"), null);
                        suc.showAndWait();
                        this.LimpaCampos();
                    } catch (AppSQLException e) {
                        MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Add database error") + " - " + rb.getString("Closed question") + ": " + e.getBaseException().getMessage(), null);
                        msg.showAndWait();
                    }
                } else {
                    // Do nothing;
                }
            }
        }
    }
    //funcionalidade da ComboBox Tipo
    public void TipoSelect(Event evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String selected = ((String)this.Tipo.getSelectionModel().getSelectedItem());
        if(selected.equals(rb.getString("Open"))){
            this.Linhas.setDisable(false);
            this.Gabarito.setDisable(false);
        }else{
            this.Linhas.setDisable(true);
            this.Gabarito.setDisable(true);
        }
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
    //Método para reiniciar o formulário
    public void LimpaCampos(){
        this.Enunciado.setText("");
        this.Gabarito.setText("");
        this.Linhas.setText("");
    }
}
