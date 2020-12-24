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
import EasyQuizPackage.Models.Beans.QuestaoAbertaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.DAOs.QuestaoAbertaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 * @author Arthur
 */
public class GerenQuestFXMLController extends TemplateMethod{
    @FXML
    private FlowPane MainPane;
    @FXML
    private JFXTextField SearchFieldAbt;
    @FXML
    private JFXTextField SearchFieldF;
    @FXML
    private Label TableCountAbt;
    @FXML
    private Label TableCountF;
    @FXML
    private JFXTreeTableView<QuestaoAbertaBean> TableAbt;
    @FXML
    private JFXTreeTableView<QuestaoFechadaBean> TableF;
    @FXML
    private JFXButton DelAbtBtn;
    @FXML
    private JFXButton DelFBtn;
    @FXML
    private JFXButton VoltarAbtBtn;
    @FXML
    private JFXButton VoltarFBtn;
    @FXML
    private JFXButton EditAbtBtn;
    @FXML
    private JFXButton EditFBtn;
    @FXML
    private SplitPane spane;
    public GerenQuestFXMLController(FlowPane MainPane){
        this.MainPane = MainPane;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/GerenQuest.fxml");
        Window frame = this.MainPane.getScene().getWindow();
        frame.setWidth(1250);
        frame.setHeight(700);
        this.MainPane.setMinSize(1200, 600);
        this.spane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.spane.prefHeightProperty().bind(this.MainPane.heightProperty());
        frame.centerOnScreen();
    }
    @Override
    public void FocusTraversableFix(){
        this.SearchFieldAbt.setFocusTraversable(false);
        this.SearchFieldF.setFocusTraversable(false);
    }
    @Override
    public void AddEvents(Node node){
        
        this.TableAbt.setOnKeyPressed(this::GerenQuestAbtDelKey);
        this.TableF.setOnKeyPressed(this::GerenQuestFDelKey);
        this.DelAbtBtn.setOnAction(this::DelAbtBtnClick);
        this.DelFBtn.setOnAction(this::DelFBtnClick);
        this.VoltarAbtBtn.setOnAction(this::VoltarAbtBtnClick);
        this.VoltarFBtn.setOnAction(this::VoltarFBtnClick);
        this.EditAbtBtn.setOnAction(this::EditAbtBtnClick);
        this.EditFBtn.setOnAction(this::EditFBtnClick);
    }
    @Override
    public void BuildSomething(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        
        //Tabela de Questões Abertas
        JFXTreeTableColumn<QuestaoAbertaBean,Integer> ID = new JFXTreeTableColumn("ID");
            ID.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean, Integer> param) ->{
			if(ID.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return ID.getComputedValue(param);
		});
            JFXTreeTableColumn<QuestaoAbertaBean,String> Enunciado = new JFXTreeTableColumn(rb.getString("Statement"));
            Enunciado.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean,String> param) ->{
                        if(Enunciado.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getEnunciado());
                        else return Enunciado.getComputedValue(param);
            });
            Enunciado.setCellFactory((TreeTableColumn<QuestaoAbertaBean,String>param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
                Enunciado.setOnEditCommit((TreeTableColumn.CellEditEvent<QuestaoAbertaBean,String> t)->{
                        ((QuestaoAbertaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).setEnunciado(t.getNewValue());
                        QuestaoAbertaDAO dao = new QuestaoAbertaDAO();
                        try{
                            dao.Edit(((QuestaoAbertaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()));
                        }catch(AppSQLException ex){
                            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Modify statement error") + ": " + ex.getBaseException().getMessage() + " " + ex.getBaseException().getErrorCode(), null);
                            msg.showAndWait();
                        }
                });
                Enunciado.setPrefWidth(300);
            JFXTreeTableColumn<QuestaoAbertaBean,String> NumLinhas = new JFXTreeTableColumn(rb.getString("Lines to skip"));
            NumLinhas.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean, String> param) ->{
			if(NumLinhas.validateValue(param)) return new SimpleStringProperty(Integer.toString(param.getValue().getValue().getNumLinhas()));
			else return NumLinhas.getComputedValue(param);
		});
            NumLinhas.setCellFactory((TreeTableColumn<QuestaoAbertaBean, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		NumLinhas.setOnEditCommit((TreeTableColumn.CellEditEvent<QuestaoAbertaBean, String> t)->{
                    ((QuestaoAbertaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).setNumLinhas(Integer.parseInt(t.getNewValue()));
                        QuestaoAbertaDAO dao = new QuestaoAbertaDAO();
                        dao.Edit(((QuestaoAbertaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()));
                });
            JFXTreeTableColumn<QuestaoAbertaBean,String> Gabarito = new JFXTreeTableColumn(rb.getString("Answer"));
            Gabarito.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean,String> param) ->{
                        if(Gabarito.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getGabarito());
                        else return Gabarito.getComputedValue(param);
            });
            Gabarito.setCellFactory((TreeTableColumn<QuestaoAbertaBean,String>param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
                Gabarito.setOnEditCommit((TreeTableColumn.CellEditEvent<QuestaoAbertaBean,String> t)->{
                        ((QuestaoAbertaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).setGabarito(t.getNewValue());
                        QuestaoAbertaDAO dao = new QuestaoAbertaDAO();
                        try{
                            dao.Edit(((QuestaoAbertaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()));
                        }catch(AppSQLException ex){
                            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Modify answer error") + ": " + ex.getBaseException().getMessage() + " " + ex.getBaseException().getErrorCode(), null);
                            msg.showAndWait();
                        }
                });
            Gabarito.setPrefWidth(300);
            JFXTreeTableColumn<QuestaoAbertaBean,String> Materia = new JFXTreeTableColumn(rb.getString("Subject"));
            Materia.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean,String> param) ->{
                        if(Materia.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getMateria());
                        else return Materia.getComputedValue(param);
            });
            JFXTreeTableColumn<QuestaoAbertaBean,String> Topico = new JFXTreeTableColumn(rb.getString("Topic"));
            Topico.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean,String> param) ->{
                        if(Topico.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getTopico());
                        else return Topico.getComputedValue(param);
            });
            QuestaoAbertaDAO dao = new QuestaoAbertaDAO();
            ObservableList<QuestaoAbertaBean> lista = FXCollections.observableArrayList();
            lista.addAll(dao.Get_All());
            final TreeItem<QuestaoAbertaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            //this.TableAbt.getColumns().setAll(ID,Enunciado,NumLinhas,Gabarito, Materia, Topico); // Removido ID da visualização
            this.TableAbt.getColumns().setAll(Enunciado,NumLinhas,Gabarito, Materia, Topico);
            this.TableAbt.setRoot(root);
            this.TableAbt.setShowRoot(false);
            this.TableCountAbt.textProperty().bind(Bindings.createStringBinding(()-> "( " + this.TableAbt.getCurrentItemsCount()+" )", this.TableAbt.currentItemsCountProperty()));
            ChangeListener listen = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    QuestaoAbertaDAO dao = new QuestaoAbertaDAO();
                    GerenQuestFXMLController.this.TableAbt.setPredicate((TreeItem<QuestaoAbertaBean> QuestaoAbertaBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && QuestaoAbertaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    QuestaoAbertaBean.getValue().getEnunciado().contains(((String)newVal))) ||
                                    QuestaoAbertaBean.getValue().getGabarito().contains(((String)newVal)) ||
                            (((String)newVal).replaceAll("\\D+","").length() != 0 && QuestaoAbertaBean.getValue().getNumLinhas() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) ||
                            QuestaoAbertaBean.getValue().getMateria().contains(((String)newVal))||
                            QuestaoAbertaBean.getValue().getTopico().contains(((String)newVal))
                            );
                }
            };
            this.SearchFieldAbt.textProperty().addListener(listen);
            ((Stage)this.MainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    SearchFieldAbt.textProperty().removeListener(listen);
                    System.exit(0);
                }
            });
            
        //Tabela de Questões Fechadas
        JFXTreeTableColumn<QuestaoFechadaBean,Integer> IDF = new JFXTreeTableColumn("ID");
            IDF.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoFechadaBean, Integer> param) ->{
			if(IDF.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return IDF.getComputedValue(param);
		});
            JFXTreeTableColumn<QuestaoFechadaBean,String> EnunciadoF = new JFXTreeTableColumn(rb.getString("Statement"));
            EnunciadoF.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoFechadaBean,String> param) ->{
                        if(EnunciadoF.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getEnunciado());
                        else return EnunciadoF.getComputedValue(param);
            });
            EnunciadoF.setCellFactory((TreeTableColumn<QuestaoFechadaBean,String>param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
                EnunciadoF.setOnEditCommit((TreeTableColumn.CellEditEvent<QuestaoFechadaBean,String> t)->{
                        ((QuestaoFechadaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).setEnunciado(t.getNewValue());
                        QuestaoFechadaDAO dao2 = new QuestaoFechadaDAO();
                        try{
                            dao2.Edit(((QuestaoFechadaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()));
                        }catch(AppSQLException ex){
                            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Modify statement error") + ": " + ex.getBaseException().getMessage() + " " + ex.getBaseException().getErrorCode(), null);
                            msg.showAndWait();
                        }
                });
            EnunciadoF.setPrefWidth(300);
            JFXTreeTableColumn<QuestaoFechadaBean,String> MateriaF = new JFXTreeTableColumn(rb.getString("Subject"));
            MateriaF.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoFechadaBean,String> param) ->{
                        if(MateriaF.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getMateria());
                        else return MateriaF.getComputedValue(param);
            });
            JFXTreeTableColumn<QuestaoFechadaBean,String> TopicoF = new JFXTreeTableColumn(rb.getString("Topic"));
            TopicoF.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoFechadaBean,String> param) ->{
                        if(TopicoF.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getTopico());
                        else return TopicoF.getComputedValue(param);
            });
            QuestaoFechadaDAO dao4 = new QuestaoFechadaDAO();
            ObservableList<QuestaoFechadaBean> lista2 = FXCollections.observableArrayList();
            lista2.addAll(dao4.Get_All());
            final TreeItem<QuestaoFechadaBean> root2 = new RecursiveTreeItem<>(lista2,RecursiveTreeObject::getChildren);
            //this.TableF.getColumns().setAll(IDF,EnunciadoF,MateriaF,TopicoF); // Removido ID da visualização
            this.TableF.getColumns().setAll(EnunciadoF,MateriaF,TopicoF);
            this.TableF.setRoot(root2);
            this.TableF.setShowRoot(false);
            this.TableCountF.textProperty().bind(Bindings.createStringBinding(()-> "( " + this.TableF.getCurrentItemsCount()+" )", this.TableF.currentItemsCountProperty()));
            ChangeListener listen2 = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
                    GerenQuestFXMLController.this.TableF.setPredicate((TreeItem<QuestaoFechadaBean> QuestaoFechadaBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && QuestaoFechadaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    QuestaoFechadaBean.getValue().getEnunciado().contains(((String)newVal))) ||
                                    (((String)newVal).replaceAll("\\D+","").length() != 0 ) ||
                            QuestaoFechadaBean.getValue().getMateria().contains(((String)newVal))||
                            QuestaoFechadaBean.getValue().getTopico().contains(((String)newVal))
                            );
                }
            };
            this.SearchFieldF.textProperty().addListener(listen2);
            ((Stage)this.MainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    SearchFieldF.textProperty().removeListener(listen2);
                    System.exit(0);
                }
            });
    }
    public void GerenQuestAbtDelKey(KeyEvent evt){
        
        final TreeItem<QuestaoAbertaBean> selectedItem = this.TableAbt.getSelectionModel().getSelectedItem();
        if ( selectedItem != null )
        {
            if ( evt.getCode().equals( KeyCode.DELETE ) )
            {
                try {
                    QuestaoAbertaDAO dao = new QuestaoAbertaDAO();
                    dao.Delete(selectedItem.getValue());
                    ObservableList<QuestaoAbertaBean> lista = FXCollections.observableArrayList();
                    lista.addAll(dao.Get_All());
                    final TreeItem<QuestaoAbertaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
                    this.TableAbt.setRoot(root);
                } catch (AppSQLException e) {
                    ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
                    MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Remove question error"), null);
                    a.showAndWait();
                }
            }
        }
    }
    public void GerenQuestFDelKey(KeyEvent evt){
        
        final TreeItem<QuestaoFechadaBean> selectedItem = this.TableF.getSelectionModel().getSelectedItem();
        if ( selectedItem != null )
        {
          if ( evt.getCode().equals( KeyCode.DELETE ) )
          {
              try {
                    QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
                    dao.Delete(selectedItem.getValue());
                    ObservableList<QuestaoFechadaBean> lista = FXCollections.observableArrayList();
                    lista.addAll(dao.Get_All());
                    final TreeItem<QuestaoFechadaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
                    this.TableF.setRoot(root);
              } catch (AppSQLException e) {
                  ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
                  MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Remove question error"), null);
                  a.showAndWait();
              }
           }
        }
    }
    public void VoltarAbtBtnClick(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
    public void VoltarFBtnClick(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
    public void DelAbtBtnClick(ActionEvent evt){
        this.TableAbt.fireEvent(new KeyEvent(this.DelAbtBtn,this.TableAbt,KeyEvent.KEY_PRESSED,"","", KeyCode.DELETE,false,false,false,false));
    }
    public void DelFBtnClick(ActionEvent evt){
        this.TableF.fireEvent(new KeyEvent(this.DelFBtn,this.TableF,KeyEvent.KEY_PRESSED,"","", KeyCode.DELETE,false,false,false,false));
    }
    public void EditAbtBtnClick(ActionEvent evt){
        EditQuestFXMLController edit = new EditQuestFXMLController(this.MainPane,this.TableAbt.getSelectionModel().getSelectedItem().getValue());
    }
    public void EditFBtnClick(ActionEvent evt){
        EditQuestFXMLController edit = new EditQuestFXMLController(this.MainPane,this.TableF.getSelectionModel().getSelectedItem().getValue());
    }
}
