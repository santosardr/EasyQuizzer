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
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
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
public class GerenCurFXMLController extends TemplateMethod{
    @FXML
    private Label count;
    @FXML
    private JFXTextField search;
    @FXML
    private JFXTreeTableView<CursoBean> table;
    @FXML
    private JFXButton edit;
    @FXML
    private JFXButton del;
    @FXML
    private JFXButton voltar;
    @FXML
    private FlowPane MainPane;
    @FXML
    private AnchorPane apane;
    public GerenCurFXMLController(FlowPane MainPane){
        this.MainPane = MainPane;
        this.templateMethod(MainPane, "/EasyQuizPackage/Views/GerenCur.fxml");
        this.MainPane.setMinSize(600, 300);
        this.apane.prefWidthProperty().bind(this.MainPane.widthProperty());
        this.apane.prefHeightProperty().bind(this.MainPane.heightProperty());
        this.MainPane.getScene().getWindow().centerOnScreen();
    }
    @Override
    public void FocusTraversableFix(){
    }
    @Override
    public void AddEvents(Node node){
        this.del.setOnAction(this::DelAltBtnClick);
        this.voltar.setOnAction(this::VoltarBtnClick);
        this.table.setOnKeyPressed(this::AltDelKey);
        this.edit.setOnAction(this::EditBtnClick);
    }
    @Override
    public void BuildSomething(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        
        JFXTreeTableColumn<CursoBean,Integer> id = new JFXTreeTableColumn("ID");
            id.setCellValueFactory((TreeTableColumn.CellDataFeatures<CursoBean, Integer> param) ->{
			if(id.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return id.getComputedValue(param);
		});
            JFXTreeTableColumn<CursoBean,String> nome = new JFXTreeTableColumn(rb.getString("Name"));
            nome.setCellValueFactory((TreeTableColumn.CellDataFeatures<CursoBean,String> param) ->{
                        if(nome.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getNome());
                        else return nome.getComputedValue(param);
            });
            CursoDAO altdao = new CursoDAO();
            ObservableList<CursoBean> lista = FXCollections.observableArrayList();
            lista.addAll(altdao.Get_All());
            final TreeItem<CursoBean> rootalt = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            //this.table.getColumns().setAll(id,nome); // Removido ID da visualização
            this.table.getColumns().setAll(nome);
            this.table.setRoot(rootalt);
            this.table.setShowRoot(false);
            this.count.textProperty().bind(Bindings.createStringBinding(()-> "( " + this.table.getCurrentItemsCount()+" )", this.table.currentItemsCountProperty()));
            ChangeListener listenalt = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    CursoDAO dao = new CursoDAO();
                    GerenCurFXMLController.this.table.setPredicate((TreeItem<CursoBean> CursoBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && CursoBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    CursoBean.getValue().getNome().contains(((String)newVal)))
                            );
                }
            };
            this.search.textProperty().addListener(listenalt);
            ((Stage)this.MainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    search.textProperty().removeListener(listenalt);
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
        final TreeItem<CursoBean> selectedItem = this.table.getSelectionModel().getSelectedItem();
        if ( selectedItem != null )
        {
            if ( evt.getCode().equals( KeyCode.DELETE ) )
            {
                try {
                    CursoDAO dao = new CursoDAO();
                    dao.Delete(selectedItem.getValue());
                    ObservableList<CursoBean> lista = FXCollections.observableArrayList();
                    ArrayList<CursoBean> arr = dao.Get_All();
                    if(arr != null)
                    lista.addAll(dao.Get_All());
                    final TreeItem<CursoBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
                    this.table.setRoot(root);
                } catch (AppSQLException e) {
                    ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
                    MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Remove course error"), null);
                    a.showAndWait();
                }
             }
        }
    }
    public void DelAltBtnClick(ActionEvent evt){
        this.table.fireEvent(new KeyEvent(this.del,this.table,KeyEvent.KEY_PRESSED,"","", KeyCode.DELETE,false,false,false,false));
    }
    public void EditBtnClick(ActionEvent evt){
        final TreeItem<CursoBean> selectedItem = this.table.getSelectionModel().getSelectedItem();
        EditCurFXMLController a = new EditCurFXMLController(this.MainPane,selectedItem.getValue());
    }
}
