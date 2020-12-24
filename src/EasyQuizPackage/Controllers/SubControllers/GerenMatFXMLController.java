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
import EasyQuizPackage.Models.DAOs.MateriaDAO;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Arthur
 */
public class GerenMatFXMLController extends TemplateMethod{
    @FXML
    private FlowPane MainPane;
    @FXML
    private Label TableCount;
    @FXML
    private JFXTextField SearchField;
    @FXML
    private JFXTreeTableView<MateriaBean> Table;
    @FXML
    private JFXButton DelBtn;
    @FXML
    private JFXButton VoltarBtn;
    @FXML
    private AnchorPane apane;
    public GerenMatFXMLController(FlowPane MainPane){
        this.MainPane = MainPane;
        this.MainPane.setMinSize(600, 300);
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/GerenMat.fxml");
        this.apane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.apane.prefWidthProperty().bind(this.MainPane.widthProperty());
    }
    @Override
    public void FocusTraversableFix(){
        this.SearchField.setFocusTraversable(false);
    }
    @Override
    public void AddEvents(Node node){
        this.Table.setOnKeyPressed(this::GerenMatDelKey);
        this.DelBtn.setOnAction(this::DelBtnClick);
        this.VoltarBtn.setOnAction(this::VoltarBtnClick);
    }
    @Override
    public void BuildSomething(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        
        JFXTreeTableColumn<MateriaBean,Integer> ID = new JFXTreeTableColumn("ID");
            ID.setCellValueFactory((TreeTableColumn.CellDataFeatures<MateriaBean, Integer> param) ->{
			if(ID.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return ID.getComputedValue(param);
		});
            JFXTreeTableColumn<MateriaBean,String> Nome = new JFXTreeTableColumn(rb.getString("Name"));
            Nome.setCellValueFactory((TreeTableColumn.CellDataFeatures<MateriaBean,String> param) ->{
                        if(Nome.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getNome());
                        else return Nome.getComputedValue(param);
            });
            Nome.setCellFactory((TreeTableColumn<MateriaBean,String>param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
                Nome.setOnEditCommit((TreeTableColumn.CellEditEvent<MateriaBean,String> t)->{
                        ((MateriaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()).setNome(t.getNewValue());
                        MateriaDAO dao = new MateriaDAO();
                        try{
                            dao.Edit(((MateriaBean) t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue()));
                        }catch(AppSQLException ex){
                            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Modify name error") + ": " + ex.getBaseException().getMessage() + " " + ex.getBaseException().getErrorCode(), null);
                            msg.showAndWait();
                        }
                });
            JFXTreeTableColumn<MateriaBean,String> Cur = new JFXTreeTableColumn(rb.getString("Course"));
            Cur.setCellValueFactory((TreeTableColumn.CellDataFeatures<MateriaBean, String> param) ->{
			if(Cur.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getCursoNome());
			else return Cur.getComputedValue(param);
		});
            MateriaDAO dao = new MateriaDAO();
            ObservableList<MateriaBean> lista = FXCollections.observableArrayList();
            lista.addAll(dao.Get_All());
            final TreeItem<MateriaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            //this.Table.getColumns().setAll(ID,Nome,Cur); // Removido ID da visualização
            this.Table.getColumns().setAll(Nome,Cur);
            this.Table.setRoot(root);
            this.Table.setShowRoot(false);
            this.TableCount.textProperty().bind(Bindings.createStringBinding(()-> "( " + this.Table.getCurrentItemsCount()+" )", this.Table.currentItemsCountProperty()));
            ChangeListener listen = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    MateriaDAO dao = new MateriaDAO();
                    GerenMatFXMLController.this.Table.setPredicate((TreeItem<MateriaBean> MateriaBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && MateriaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    MateriaBean.getValue().getNome().contains(((String)newVal))) ||
                                    String.valueOf(MateriaBean.getValue().getCurso()).contains(((String)newVal))
                            );
                }
            };
            this.SearchField.textProperty().addListener(listen);
            ((Stage)this.MainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    SearchField.textProperty().removeListener(listen);
                    System.exit(0);
                }
            }); 
    }
    public void GerenMatDelKey(KeyEvent evt){
        final TreeItem<MateriaBean> selectedItem = this.Table.getSelectionModel().getSelectedItem();
        if ( selectedItem != null )
        {
            if ( evt.getCode().equals( KeyCode.DELETE ) )
            {
                try {
                    MateriaDAO dao = new MateriaDAO();
                    dao.Delete(selectedItem.getValue());
                    ObservableList<MateriaBean> lista = FXCollections.observableArrayList();
                    lista.addAll(dao.Get_All());
                    final TreeItem<MateriaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
                    this.Table.setRoot(root);
                } catch (AppSQLException e) {
                    ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
                    MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Remove subject error"), null);
                    a.showAndWait();
                }
             }
        }
    }
    public void VoltarBtnClick(ActionEvent evt){
        BancoFXMLController banco = new BancoFXMLController(null);
        this.MainPane.getScene().getWindow().hide();
        banco.show();
    }
    public void DelBtnClick(ActionEvent evt){
        this.Table.fireEvent(new KeyEvent(this.DelBtn,this.Table,KeyEvent.KEY_PRESSED,"","", KeyCode.DELETE,false,false,false,false));
    }
}
