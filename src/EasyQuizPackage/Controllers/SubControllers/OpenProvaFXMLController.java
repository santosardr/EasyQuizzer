/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.CabecaFormFXMLController;
import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.Controllers.NovaProvaFXMLController;
import EasyQuizPackage.Controllers.PrincipalFXMLController;
import EasyQuizPackage.Controllers.SpinnerDialogFXMLController;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.ProvaBean;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import EasyQuizPackage.Models.DAOs.ProvaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoAbertaProvaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaProvaDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Arthur
 */
public class OpenProvaFXMLController extends Stage implements Initializable{
    @FXML
    private Label tableCount;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXTreeTableView<ProvaBean> table;
    @FXML
    private JFXButton excluir;
    @FXML
    private JFXButton abrir;
    @FXML
    private JFXButton cancelar;
    public OpenProvaFXMLController(Parent parent){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Load Exam"));
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/OpenProva.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(CabecaFormFXMLController.class.getResourceAsStream(Main.LOGO)));
            //Construção da Tabela
            JFXTreeTableColumn<ProvaBean,Integer> ID = new JFXTreeTableColumn("ID");
            ID.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, Integer> param) ->{
			if(ID.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return ID.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Nome = new JFXTreeTableColumn(rb.getString("Name"));
            Nome.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Nome.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getNome());
			else return Nome.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Tipo = new JFXTreeTableColumn(rb.getString("Type"));
            Tipo.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean,String> param) ->{
                        if(Tipo.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getTipo());
                        else return Tipo.getComputedValue(param);
            });
            JFXTreeTableColumn<ProvaBean,String> Tam = new JFXTreeTableColumn(rb.getString("Size"));
            Tam.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Tam.validateValue(param)) return new SimpleStringProperty(Integer.toString(param.getValue().getValue().getTamanho()));
			else return Tam.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Prof = new JFXTreeTableColumn(rb.getString("Teacher"));
            Prof.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Prof.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getProf());
			else return Prof.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Curso = new JFXTreeTableColumn(rb.getString("Course"));
            Curso.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Curso.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getCurso());
			else return Curso.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Inst = new JFXTreeTableColumn(rb.getString("Institution"));
            Inst.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Inst.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getInst());
			else return Inst.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Data = new JFXTreeTableColumn(rb.getString("Date"));
            Data.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Data.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getData().toLocalDate().toString());
			else return Data.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Nota = new JFXTreeTableColumn(rb.getString("Score"));
            Nota.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Nota.validateValue(param)) return new SimpleStringProperty(Float.toString(param.getValue().getValue().getNota()));
			else return Nota.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Turma = new JFXTreeTableColumn(rb.getString("Class"));
            Turma.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
			if(Turma.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getTurma());
			else return Turma.getComputedValue(param);
		});
            JFXTreeTableColumn<ProvaBean,String> Materia = new JFXTreeTableColumn(rb.getString("Subject"));
            Materia.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProvaBean, String> param) ->{
                        MateriaDAO mdao = new MateriaDAO();
                        MateriaBean mbean = new MateriaBean();
                        mbean.setID(param.getValue().getValue().getMateria_id());
                        mbean = mdao.Get_One(mbean);
			if(Materia.validateValue(param)) return new SimpleStringProperty(mbean.getNome());
			else return Materia.getComputedValue(param);
		});
            ProvaDAO dao = new ProvaDAO();
            ObservableList<ProvaBean> lista = FXCollections.observableArrayList();
            lista.addAll(dao.Get_All());
            final TreeItem<ProvaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            this.table.getColumns().setAll(Nome,Tipo,Tam,Prof,Curso,Inst,Data,Nota,Turma,Materia);
            this.table.setRoot(root);
            this.table.setShowRoot(false);
            this.tableCount.textProperty().bind(Bindings.createStringBinding(()-> "( " + this.table.getCurrentItemsCount()+" )", this.table.currentItemsCountProperty()));
            ChangeListener listen = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    ProvaDAO dao = new ProvaDAO();
                    OpenProvaFXMLController.this.table.setPredicate((TreeItem<ProvaBean> ProvaBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && ProvaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    ProvaBean.getValue().getTipo().contains(((String)newVal))) ||
                                    String.valueOf(ProvaBean.getValue().getTamanho()).contains(((String)newVal)) ||
                                    ProvaBean.getValue().getProf().contains(((String)newVal)) ||
                                    ProvaBean.getValue().getCurso().contains(((String)newVal)) ||
                                    ProvaBean.getValue().getInst().contains(((String)newVal)) ||
                                    ProvaBean.getValue().getData().toLocalDate().toString().contains(((String)newVal)) ||
                                    String.valueOf(ProvaBean.getValue().getNota()).contains(((String)newVal)) ||
                                    ProvaBean.getValue().getTurma().contains(((String)newVal))
                            );
                }
            };
            this.searchField.textProperty().addListener(listen);
            ((Stage)this.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    searchField.textProperty().removeListener(listen);
                    System.exit(0);
                }
            }); 
        }
        catch (FileNotFoundException ex){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("File not found") + ": " + ex.getMessage(), null);
            mes.showAndWait();
        }
        catch (IOException e){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Open load exam error") + ": " + e.getMessage(), null);
            mes.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cancelar.setOnAction(this::CancelarClick);
        this.abrir.setOnAction(this::AbrirClick);
        this.excluir.setOnAction(this::DelClick);
        this.table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                   AbrirClick(new ActionEvent());
                }
            }
        });
    }
    public void CancelarClick(ActionEvent evt){
        PrincipalFXMLController p = new PrincipalFXMLController(null);
        this.close();
        p.show();
    }
    public void AbrirClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        final TreeItem<ProvaBean> selectedItem = this.table.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            NovaProvaFXMLController n = new NovaProvaFXMLController(null, selectedItem.getValue());
            this.hide();
            n.show();
        }else{
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("No exam selected"), null);
            a.showAndWait();
        }
    }
    public void DelClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        boolean result = ConfirmDialogFXMLController.Display("Excluir Prova","Deseja realmente excluir a prova?");
        if(result){
            QuestaoAbertaProvaDAO qabtdao = new QuestaoAbertaProvaDAO();
            QuestaoFechadaProvaDAO qfecdao = new QuestaoFechadaProvaDAO();
            ProvaDAO pdao = new ProvaDAO();
            ProvaBean pbean = this.table.getSelectionModel().getSelectedItem().getValue();
            try {
                pdao.Delete(pbean);
                MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Delete exam success"), null);
                a.showAndWait();
            } catch (Exception e) {
                MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Delete database error") + " - " + rb.getString("Exam") + ": " + e.getMessage(), null);
                a.showAndWait();
            }
            this.table.getRoot().getChildren().clear();
            ObservableList<ProvaBean> lista = FXCollections.observableArrayList();
            lista.addAll(pdao.Get_All());
            final TreeItem<ProvaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            this.table.setRoot(root);
        }
    }
}
