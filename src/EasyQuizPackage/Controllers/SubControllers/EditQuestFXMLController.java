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
public class EditQuestFXMLController extends TemplateMethod{
    @FXML
    private FlowPane MainPane;
    @FXML
    private JFXTextArea EditEnun;
    @FXML
    private JFXComboBox EditTipo;
    @FXML
    private JFXTextField EditLinhas;
    @FXML
    private JFXTextArea EditGabarito;
    @FXML
    private JFXComboBox EditMat;
    @FXML
    private JFXComboBox EditTop;
    @FXML
    private JFXButton SalvarBtn;
    @FXML
    private JFXButton VoltarBtn;
    private Object Questao;
    @FXML
    private AnchorPane apane;
    public EditQuestFXMLController(FlowPane MainPane, Object Questao){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.MainPane = MainPane;
        this.Questao = Questao;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/EditQuest.fxml");
        this.MainPane.setMinSize(879, 716);
        this.MainPane.getScene().getWindow().setWidth(900);
        this.MainPane.getScene().getWindow().setHeight(780);
        this.MainPane.getScene().getWindow().centerOnScreen();
        this.apane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.apane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.EditTipo.getItems().add(rb.getString("Open"));
        this.EditTipo.getItems().add(rb.getString("Closed"));
        int matid = 0, topicoid = 0;
        String mat = null, top = null;
        MateriaDAO matdao = new MateriaDAO();
        TopicoDAO topdao = new TopicoDAO();
        if(Questao instanceof QuestaoAbertaBean){
            this.EditTipo.getSelectionModel().select(0);
            QuestaoAbertaBean bean = ((QuestaoAbertaBean)Questao);
            this.EditEnun.setText( bean.getEnunciado() );
            this.EditGabarito.setText( bean.getGabarito() );
            this.EditLinhas.setText( Integer.toString(bean.getNumLinhas()));
            topicoid = bean.getTopico_ID();
            top = bean.getTopico();
            TopicoBean topbean = new TopicoBean();
            topbean.setID(topicoid);
            matid = topdao.Get_One(topbean).getMateria_ID();
            mat = bean.getMateria();
        }else if(Questao instanceof QuestaoFechadaBean){
            this.EditTipo.getSelectionModel().select(1);
            QuestaoFechadaBean bean = ((QuestaoFechadaBean)Questao);
            this.EditEnun.setText( bean.getEnunciado() );
            this.EditGabarito.setDisable(true);
            this.EditLinhas.setDisable(true);
            topicoid = bean.getTopico_ID();
            top = bean.getTopico();
            TopicoBean topbean = new TopicoBean();
            topbean.setID(topicoid);
            matid = topdao.Get_One(topbean).getMateria_ID();
            mat = bean.getMateria();
        }
        this.EditTipo.setDisable(true);
        
        ArrayList<MateriaBean> result = matdao.Get_All();
        ArrayList<String> Materias = new ArrayList<>();
        for(MateriaBean bean: result){
            Materias.add(bean.getID()+" : "+bean.getNome());
        }
        this.EditMat.getItems().addAll(Materias);
        this.EditMat.getSelectionModel().select(matid+" : "+mat);
        this.MateriaSelect(null);
        this.EditTop.getSelectionModel().select(topicoid+" : "+top);
    }
    
    @Override
    public void FocusTraversableFix(){
        this.EditEnun.setFocusTraversable(false);
        this.EditLinhas.setFocusTraversable(false);
        this.EditGabarito.setFocusTraversable(false);
    }
    @Override
    public void AddEvents(Node node){
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
        this.SalvarBtn.setOnAction(this::SalvarBtnClick);
        this.EditMat.setOnAction(this::MateriaSelect);
    }
    //funcionalidade do botão voltar
    public void VoltarBtnClick(ActionEvent evt){
        GerenQuestFXMLController gren = new GerenQuestFXMLController(this.MainPane);
    }
    //funcionalidade do botão de adicionar nova questão
    public void SalvarBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        boolean errors = false;
        String err_msg = "";
        if(this.EditEnun.getText().equals("")){
            errors = true;
            err_msg += rb.getString("Field") + " \"" + rb.getString("Statement") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(this.EditTipo.getSelectionModel().getSelectedItem() == null){
            errors = true;
            err_msg += rb.getString("Unselected question type") + "\n";
        }
        if(this.EditTop.getSelectionModel().getSelectedItem() == null){
            errors = true;
            err_msg += rb.getString("Unselected question topic") + "\n";
        }else{
            if(((String)this.EditTipo.getSelectionModel().getSelectedItem()).equals(rb.getString("Open"))){
                if(this.EditGabarito.getText().equals("")){
                    errors = true;
                    err_msg += rb.getString("Field") + " \"" + rb.getString("Answer") + "\" " + rb.getString("can not be blank") + "\n";
                }
                if(this.EditLinhas.getText().equals("")){
                    errors = true;
                    err_msg += rb.getString("Field") + " \"" + rb.getString("Number of lines to skip") + "\" " + rb.getString("can not be blank") + "\n";
                }
                if(this.EditLinhas.getText().replaceAll("\\d+","").length() != 0){
                    errors = true;
                    err_msg += rb.getString("Field") + " \"" + rb.getString("Number of lines to skip") + "\" " + rb.getString("can not contain letters") + "\n";
                }
            }
        }
        if(errors){
            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Invalid Data"), rb.getString("The following errors were found") + ":\n" + err_msg, null);
            msg.showAndWait();
        }else{
            if(((String)this.EditTipo.getSelectionModel().getSelectedItem()).equals(rb.getString("Open"))) {
                    QuestaoAbertaBean questabt = new QuestaoAbertaBean();
                    questabt.setID(((QuestaoAbertaBean)this.Questao).getID());
                    questabt.setEnunciado(this.EditEnun.getText());
                    questabt.setGabarito(this.EditGabarito.getText());
                    questabt.setNumLinhas(Integer.parseInt(((String)this.EditLinhas.getText())));
                    questabt.setTopico_ID(Integer.parseInt(((String)this.EditTop.getSelectionModel().getSelectedItem()).split(" : ")[0]));
                    QuestaoAbertaDAO questabtdao = new QuestaoAbertaDAO();
                    try {
                        questabtdao.Edit(questabt);
                        MessageDialogFXMLController suc = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Successfully edited"), null);
                        suc.showAndWait();
                        this.LimpaCampos();
                    } catch (AppSQLException e) {
                        MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Edit error") + ": " + e.getBaseException().getMessage(), null);
                        msg.showAndWait();
                    }
            } else {
                if(((String)this.EditTipo.getSelectionModel().getSelectedItem()).equals(rb.getString("Closed"))) {
                    QuestaoFechadaBean questf = new QuestaoFechadaBean();
                    questf.setID( ((QuestaoFechadaBean)this.Questao).getID() );
                    questf.setEnunciado(this.EditEnun.getText());
                    questf.setTopico_ID( Integer.parseInt( ((String)this.EditTop.getSelectionModel().getSelectedItem()).split(" : ")[0] ) );
                    QuestaoFechadaDAO questfdao = new QuestaoFechadaDAO();
                    try {
                        questfdao.Edit(questf);
                        MessageDialogFXMLController suc = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Successfully edited"), null);
                        suc.showAndWait();
                        this.LimpaCampos();
                    } catch (AppSQLException e) {
                        MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Edit error") + ": " + e.getBaseException().getMessage(), null);
                        msg.showAndWait();
                    }
                } else {
                    // Do nothing;
                }
            }
            this.VoltarBtnClick(null);
        }
    }
    //funcionalidade da ComboBox de seleção de matéria
    public void MateriaSelect(Event evt){
        this.EditTop.getItems().clear();
        MateriaBean bean = new MateriaBean();
        bean.setID(Integer.parseInt( ((String)this.EditMat.getSelectionModel().getSelectedItem()).split(" : ")[0] ));
        TopicoDAO dao = new TopicoDAO();
        ArrayList<TopicoBean> topicos = dao.Get_From_Materia(bean);
        ArrayList<String> lista_topicos = new ArrayList<>();
        topicos.stream().forEach((topbean) -> {
            lista_topicos.add(topbean.getID()+" : "+topbean.getNome());
        });
        this.EditTop.getItems().addAll(lista_topicos);
    }
    //Método para reiniciar o formulário
    public void LimpaCampos(){
        this.EditEnun.setText("");
        this.EditGabarito.setText("");
        this.EditLinhas.setText("");
    }
}
