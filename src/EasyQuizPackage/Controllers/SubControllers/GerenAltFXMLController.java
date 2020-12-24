/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.BancoFXMLController;
import EasyQuizPackage.Controllers.BlankFXMLController;
import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.Interfaces.TemplateMethod;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Models.Beans.AlternativaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.DAOs.AlternativaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.ArrayList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Arthur
 */
public class GerenAltFXMLController extends TemplateMethod{
    @FXML
    private Label TableQuestCount;
    @FXML
    private Label TableAltCount;
    @FXML
    private JFXTextField TableQuestSearch;
    @FXML
    private JFXTextField TableAltSearch;
    @FXML
    private JFXTreeTableView<QuestaoFechadaBean> TableQuest; 
    @FXML
    private JFXTreeTableView<AlternativaBean> TableAlt;
    @FXML
    private JFXButton AddBtn;
    @FXML
    private JFXButton EditBtn;
    @FXML
    private JFXButton DelBtn;
    @FXML
    private JFXButton VoltarBtn;
    @FXML
    private FlowPane MainPane;
    @FXML
    private SplitPane ppane;
    private String nomeMateria;
    private String nomeTopico;
    public GerenAltFXMLController(FlowPane MainPane, String nomeMateria, String nomeTopico){
        this.MainPane = MainPane;
        this.MainPane.getScene().getWindow().setWidth(1000);
        this.nomeMateria = nomeMateria;
        this.nomeTopico = nomeTopico;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/GerenAlt.fxml");
        this.ppane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.ppane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.MainPane.getScene().getWindow().centerOnScreen();
    }
    public GerenAltFXMLController(FlowPane MainPane, TreeItem<QuestaoFechadaBean> selectedItem, String nomeMateria, String nomeTopico){
        this.MainPane = MainPane;
        this.MainPane.getScene().getWindow().setWidth(1000);
        this.nomeMateria = nomeMateria;
        this.nomeTopico = nomeTopico;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/GerenAlt.fxml");
        this.ppane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.ppane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.MainPane.getScene().getWindow().centerOnScreen();
        SelectQuest(selectedItem);
    }
    @Override
    public void FocusTraversableFix(){
        this.TableQuestSearch.setFocusTraversable(false);
        this.TableAltSearch.setFocusTraversable(false);
    }
    @Override
    public void AddEvents(Node node){
        this.DelBtn.setOnAction(this::DelAltBtnClick);
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
        this.TableAlt.setOnKeyPressed(this::AltDelKey);
        this.EditBtn.setOnAction(this::EditBtnClick);
        this.TableQuest.setOnMouseClicked(this::SelectQuest);
        this.AddBtn.setOnAction(this::AddAltBtn);
    }
    @Override
    public void BuildSomething(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        
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
            //this.TableQuest.getColumns().setAll(IDF,EnunciadoF,MateriaF,TopicoF); // Removido ID da visualização
            this.TableQuest.getColumns().setAll(EnunciadoF,MateriaF,TopicoF);
            this.TableQuest.setRoot(root2);
            this.TableQuest.setShowRoot(false);
            this.TableQuestCount.textProperty().bind(Bindings.createStringBinding(()-> "( " + this.TableQuest.getCurrentItemsCount()+" )", this.TableQuest.currentItemsCountProperty()));
            ChangeListener listen2 = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    //QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
                    GerenAltFXMLController.this.TableQuest.setPredicate(//
                            (TreeItem<QuestaoFechadaBean> QuestaoFechadaBean) -> (!((String)newVal).isEmpty()) && 
                            (((((String)newVal).replaceAll("\\D+","").length() != 0 && QuestaoFechadaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    QuestaoFechadaBean.getValue().getEnunciado().contains(((String)newVal))) ||
                                    (((String)newVal).replaceAll("\\D+","").length() != 0 )) &&
                            QuestaoFechadaBean.getValue().getMateria().contains(nomeMateria) &&
                            QuestaoFechadaBean.getValue().getTopico().contains(nomeTopico)
                            );
                }
            };
            this.TableQuestSearch.textProperty().addListener(listen2);
            ((Stage)this.MainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    TableQuestSearch.textProperty().removeListener(listen2);
                    System.exit(0);
                }
            });
            
        //Tabela de Alternativas
        JFXTreeTableColumn<AlternativaBean,Integer> IDA = new JFXTreeTableColumn("ID");
            IDA.setCellValueFactory((TreeTableColumn.CellDataFeatures<AlternativaBean, Integer> param) ->{
			if(IDA.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return IDA.getComputedValue(param);
		});
            JFXTreeTableColumn<AlternativaBean,String> Texto = new JFXTreeTableColumn(rb.getString("Text"));
            Texto.setCellValueFactory((TreeTableColumn.CellDataFeatures<AlternativaBean,String> param) ->{
                        if(Texto.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getTexto());
                        else return Texto.getComputedValue(param);
            });
            Texto.setPrefWidth(300);
            JFXTreeTableColumn<AlternativaBean,String> Certa = new JFXTreeTableColumn(rb.getString("Correct alternative"));
            Certa.setCellValueFactory((TreeTableColumn.CellDataFeatures<AlternativaBean, String> param) ->{
			if(Certa.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getCerta());
			else return Certa.getComputedValue(param);
		});
            AlternativaDAO altdao = new AlternativaDAO();
            ObservableList<AlternativaBean> lista = FXCollections.observableArrayList();
            final TreeItem<AlternativaBean> rootalt = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            //this.TableAlt.getColumns().setAll(IDA,Texto,Certa); // Removido ID da visualização
            this.TableAlt.getColumns().setAll(Texto,Certa);
            this.TableAlt.setRoot(rootalt);
            this.TableAlt.setShowRoot(false);
            this.TableAltCount.textProperty().bind(Bindings.createStringBinding(()-> "( " + this.TableAlt.getCurrentItemsCount()+" )", this.TableAlt.currentItemsCountProperty()));
            ChangeListener listenalt = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    AlternativaDAO dao = new AlternativaDAO();
                    GerenAltFXMLController.this.TableAlt.setPredicate((TreeItem<AlternativaBean> AlternativaBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && AlternativaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    AlternativaBean.getValue().getTexto().contains(((String)newVal))) ||
                            AlternativaBean.getValue().getCerta().contains(((String)newVal))
                            );
                }
            };
            this.TableAltSearch.textProperty().addListener(listenalt);
            ((Stage)this.MainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    TableAltSearch.textProperty().removeListener(listenalt);
                    System.exit(0);
                }
            });
    }
    public void VoltarBtnClick(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
    public void AltDelKey(KeyEvent evt){
        final TreeItem<AlternativaBean> selectedItem = this.TableAlt.getSelectionModel().getSelectedItem();
        if ( selectedItem != null )
        {
            if ( evt.getCode().equals( KeyCode.DELETE ) )
            {
                try {
                    AlternativaDAO dao = new AlternativaDAO();
                    dao.Delete(selectedItem.getValue());
                    ObservableList<AlternativaBean> lista = FXCollections.observableArrayList();
                    ArrayList<AlternativaBean> arr = dao.Get_All();
                    if(arr != null)
                    lista.addAll(dao.Get_All());
                    final TreeItem<AlternativaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
                    this.TableAlt.setRoot(root);
                } catch (AppSQLException e) {
                    ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
                    MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Remove alternative error"), null);
                    a.showAndWait();
                }
             }
        }
    }
    public void DelAltBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        
        final TreeItem<QuestaoFechadaBean> selectedItem = this.TableQuest.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            this.TableAlt.fireEvent(new KeyEvent(this.DelBtn,this.TableAlt,KeyEvent.KEY_PRESSED,"","", KeyCode.DELETE,false,false,false,false));
            SelectQuest(selectedItem);
        }else{
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Unselected question alternative delete"), null);
            a.showAndWait();
        }       
    }
    public void EditBtnClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        
        final TreeItem<QuestaoFechadaBean> selectedQuestItem = this.TableQuest.getSelectionModel().getSelectedItem();
        final TreeItem<AlternativaBean> selectedAltItem = this.TableAlt.getSelectionModel().getSelectedItem();
        if(selectedQuestItem != null){
            if(selectedAltItem != null){
                BlankFXMLController janela = new BlankFXMLController(null);
                janela.show();
                EditAltFXMLController a = new EditAltFXMLController(this, janela.getMainFlowPane(), selectedAltItem.getValue(), selectedQuestItem, this.nomeMateria, this.nomeTopico);
            }else{
                MessageDialogFXMLController a = new MessageDialogFXMLController("", rb.getString("Select alternative edit"), null);
                a.showAndWait();
            }
        }else{
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Unselected question alternative edit"), null);
            a.showAndWait();
        }
    }
    public void SelectQuest(MouseEvent evt){
        final TreeItem<QuestaoFechadaBean> selectedItem = this.TableQuest.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            AlternativaDAO altdao = new AlternativaDAO();
            ObservableList<AlternativaBean> lista = FXCollections.observableArrayList();
            ArrayList<AlternativaBean> aux = altdao.Get_All_From_Quest(selectedItem.getValue());
            if( aux != null )
            lista.addAll(altdao.Get_All_From_Quest(selectedItem.getValue()));
            final TreeItem<AlternativaBean> rootalt = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            this.TableAlt.setRoot(rootalt);
        }
    }
    public void SelectQuest(TreeItem<QuestaoFechadaBean> selectedItem){
        if(selectedItem != null){
            AlternativaDAO altdao = new AlternativaDAO();
            ObservableList<AlternativaBean> lista = FXCollections.observableArrayList();
            ArrayList<AlternativaBean> aux = altdao.Get_All_From_Quest(selectedItem.getValue());
            if( aux != null )
            lista.addAll(altdao.Get_All_From_Quest(selectedItem.getValue()));
            final TreeItem<AlternativaBean> rootalt = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            this.TableAlt.setRoot(rootalt);
        }
    }
    public void AddAltBtn(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        
        final TreeItem<QuestaoFechadaBean> selectedItem = this.TableQuest.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            BlankFXMLController janela = new BlankFXMLController(null);
            janela.show();
            AddAltFXMLController a = new AddAltFXMLController(this, janela.getMainFlowPane(), selectedItem, nomeMateria, nomeTopico);
        }else{
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Unselected question alternative add"), null);
            a.showAndWait();
        }
    }
}
