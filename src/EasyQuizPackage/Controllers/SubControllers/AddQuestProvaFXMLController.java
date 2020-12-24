/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.CabecaFormFXMLController;
import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import EasyQuizPackage.Models.Beans.CursoBean;
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.ProvaBean;
import EasyQuizPackage.Models.Beans.QuestaoAbertaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.Beans.TopicoBean;
import EasyQuizPackage.Models.DAOs.CursoDAO;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoAbertaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaDAO;
import EasyQuizPackage.Models.DAOs.TopicoDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Arthur
 */
public class AddQuestProvaFXMLController extends Stage implements Initializable{
    @FXML
    private JFXComboBox curso;
    @FXML
    private JFXComboBox materia;
    @FXML
    private JFXComboBox topico;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXTextField searchField2;
    @FXML
    private JFXTreeTableView<QuestaoAbertaBean> tableQA;
    @FXML
    private JFXTreeTableView<QuestaoFechadaBean> tableQF;
    @FXML
    private JFXButton add;
    @FXML
    private JFXButton cancel;
    private ProvaBean bean;
    @FXML
    private VBox vbox;
    private ArrayList<QuestaoAbertaBean> questabt;
    private ArrayList<QuestaoFechadaBean> questfec;
    public AddQuestProvaFXMLController(Parent parent,ProvaBean bean, ArrayList<QuestaoAbertaBean> questabt, ArrayList<QuestaoFechadaBean> questfec, VBox vbox){
        this.bean = bean;
        this.questabt = questabt;
        this.questfec = questfec;
        this.vbox = vbox;
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Add Question to Exam"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/AddQuestProva.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(CabecaFormFXMLController.class.getResourceAsStream(Main.LOGO)));
            CursoDAO cdao = new CursoDAO();
            ArrayList<CursoBean>cbeans = cdao.Get_All();
            cbeans.forEach((cbean) ->{ this.curso.getItems().add(cbean.getID()+" : "+cbean.getNome());});
            this.curso.getSelectionModel().selectFirst();
            Event.fireEvent(this.curso, new ActionEvent());
            //Construção da Tabela QA
            JFXTreeTableColumn<QuestaoAbertaBean,Integer> ID = new JFXTreeTableColumn("ID");
            ID.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean, Integer> param) ->{
			if(ID.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return ID.getComputedValue(param);
		});
            JFXTreeTableColumn<QuestaoAbertaBean,String> Enum = new JFXTreeTableColumn(rb.getString("Statement"));
            Enum.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean,String> param) ->{
                        if(Enum.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getEnunciado());
                        else return Enum.getComputedValue(param);
            });
            JFXTreeTableColumn<QuestaoAbertaBean,String> Linhas = new JFXTreeTableColumn(rb.getString("Number of lines"));
            Linhas.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean, String> param) ->{
			if(Linhas.validateValue(param)) return new SimpleStringProperty(Integer.toString(param.getValue().getValue().getNumLinhas()));
			else return Linhas.getComputedValue(param);
		});
            JFXTreeTableColumn<QuestaoAbertaBean,String> Gab = new JFXTreeTableColumn(rb.getString("Answer"));
            Gab.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoAbertaBean, String> param) ->{
			if(Gab.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getGabarito());
			else return Gab.getComputedValue(param);
		});
            
            this.tableQA.getColumns().setAll(ID,Enum,Linhas,Gab);
            this.tableQA.setShowRoot(false);
            //Construção da Tabela QF
            JFXTreeTableColumn<QuestaoFechadaBean,Integer> ID2 = new JFXTreeTableColumn("ID");
            ID2.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoFechadaBean, Integer> param) ->{
			if(ID2.validateValue(param)) return new SimpleIntegerProperty(param.getValue().getValue().getID()).asObject();
			else return ID2.getComputedValue(param);
		});
            JFXTreeTableColumn<QuestaoFechadaBean,String> Enum2 = new JFXTreeTableColumn(rb.getString("Statement"));
            Enum2.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestaoFechadaBean,String> param) ->{
                        if(Enum2.validateValue(param)) return new SimpleStringProperty(param.getValue().getValue().getEnunciado());
                        else return Enum2.getComputedValue(param);
            });
            this.tableQF.getColumns().setAll(ID2,Enum2);
            this.tableQF.setShowRoot(false);
            ChangeListener listen = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    AddQuestProvaFXMLController.this.tableQF.setPredicate((TreeItem<QuestaoFechadaBean> QuestaoFechadaBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && QuestaoFechadaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    QuestaoFechadaBean.getValue().getEnunciado().contains(((String)newVal))) 
                            );
                }
            };
            ChangeListener listen2 = (ChangeListener) new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newVal) {
                    AddQuestProvaFXMLController.this.tableQA.setPredicate((TreeItem<QuestaoAbertaBean> QuestaoAbertaBean) -> (!((String)newVal).isEmpty()) && 
                            ((((String)newVal).replaceAll("\\D+","").length() != 0 && QuestaoAbertaBean.getValue().getID() == Integer.parseInt(((String)newVal).replaceAll("\\D+",""))) || 
                                    QuestaoAbertaBean.getValue().getEnunciado().contains(((String)newVal))) ||
                                    QuestaoAbertaBean.getValue().getGabarito().contains(((String)newVal)) ||
                                    String.valueOf(QuestaoAbertaBean.getValue().getNumLinhas()).contains(((String)newVal)) 
                            );
                }
            };
            this.searchField.textProperty().addListener(listen2);
            this.searchField2.textProperty().addListener(listen);
            ((Stage)this.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    searchField.textProperty().removeListener(listen2);
                    searchField2.textProperty().removeListener(listen);
                    System.exit(0);
                }
            });
        }
        catch (FileNotFoundException ex){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("File not found") + ": " + ex.getMessage(), null);
            mes.showAndWait();
        }
        catch (IOException e){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Open AddQuestProva error") + ": " + e.getMessage(), null);
            mes.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cancel.setOnAction(this::CancelClick);
        this.curso.setOnAction(this::CursoSelect);
        this.materia.setOnAction(this::MateriaSelect);
        this.topico.setOnAction(this::TopicoSelect);
        this.tableQA.setOnMouseClicked(this::QAClick);
        this.tableQF.setOnMouseClicked(this::QFClick);
        this.add.setOnAction(this::addClick);
    }
    public void CancelClick(ActionEvent evt){
        this.close();
    }
    public void CursoSelect(Event evt){
        this.topico.getItems().clear();
        this.materia.getItems().clear();
        MateriaDAO mdao = new MateriaDAO();
        ArrayList<MateriaBean>mbeans = mdao.Get_All();
        mbeans.forEach((mbean)->{ 
            if(mbean.getCurso() == Integer.parseInt(this.curso.getSelectionModel().getSelectedItem().toString().split(" : ")[0]))
                this.materia.getItems().add(mbean.getID()+" : "+mbean.getNome());
        });
    }
    public void MateriaSelect(Event evt){
        if(this.materia.getSelectionModel().getSelectedItem() != null){
            this.topico.getItems().clear();
            TopicoDAO tdao = new TopicoDAO();
            ArrayList<TopicoBean>tbeans = tdao.Get_All();
            tbeans.forEach((tbean)->{ 
                if(tbean.getMateria_ID() == Integer.parseInt(this.materia.getSelectionModel().getSelectedItem().toString().split(" : ")[0]))
                    this.topico.getItems().add(tbean.getID()+" : "+tbean.getNome());
            });
        } 
    }
    public void TopicoSelect(Event evt){
        if(this.topico.getSelectionModel().getSelectedItem() != null){
            QuestaoAbertaDAO dao = new QuestaoAbertaDAO();
            ObservableList<QuestaoAbertaBean> lista = FXCollections.observableArrayList();
            QuestaoAbertaBean b = new QuestaoAbertaBean();
            b.setTopico_ID(Integer.parseInt(this.topico.getSelectionModel().getSelectedItem().toString().split(" : ")[0]));
            lista.addAll(dao.GetAllTopico(b));
            final TreeItem<QuestaoAbertaBean> root = new RecursiveTreeItem<>(lista,RecursiveTreeObject::getChildren);
            this.tableQA.setRoot(root);
            
            QuestaoFechadaDAO dao2 = new QuestaoFechadaDAO();
            ObservableList<QuestaoFechadaBean> lista2 = FXCollections.observableArrayList();
            QuestaoFechadaBean c = new QuestaoFechadaBean();
            c.setTopico_ID(Integer.parseInt(this.topico.getSelectionModel().getSelectedItem().toString().split(" : ")[0]));
            lista2.addAll(dao2.GetAllTopico(c));
            final TreeItem<QuestaoFechadaBean> root2 = new RecursiveTreeItem<>(lista2,RecursiveTreeObject::getChildren);
            this.tableQF.setRoot(root2);
        }
    }
    public void QAClick(Event evt){
        this.tableQF.getSelectionModel().clearSelection();
    }
    public void QFClick(Event evt){
        this.tableQA.getSelectionModel().clearSelection();
    }
    public void addClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        final TreeItem<QuestaoAbertaBean> abt = this.tableQA.getSelectionModel().getSelectedItem();
        final TreeItem<QuestaoFechadaBean> fec = this.tableQF.getSelectionModel().getSelectedItem();
        if(abt != null){
            boolean pres = false;
            for(QuestaoAbertaBean a: this.questabt){
                if(a.getID() == abt.getValue().getID()){
                    pres = true;
                    break;
                }
            }
            if(!pres){
                this.questabt.add(abt.getValue());
                vbox.getChildren().add(this.createQuestAbtLabel(abt.getValue()));
                MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Add question success"), null);
                a.showAndWait();
            }else{
                MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Already selected question error"), null);
                a.showAndWait();
            }
        }else if(fec != null){
            boolean pres = false;
            for(QuestaoFechadaBean a: this.questfec){
                if(a.getID() == fec.getValue().getID()){
                    pres = true;
                    break;
                }
            }
            if(!pres){
                if(verificaQuestaoFec(fec.getValue())){
                    this.questfec.add(fec.getValue());
                    Label k = this.createQuestFecLabel(fec.getValue());
                    vbox.getChildren().add(k);
                    MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Success"), rb.getString("Add question success"), null);
                    a.showAndWait();
                }else{
                    MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Incompatible question error"), null);
                    a.showAndWait();
                }
            }else{
                MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Already selected question error"), null);
                a.showAndWait();
            }
        }else{
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Select question error"), null);
            a.showAndWait();
        }
    }
    public boolean verificaQuestaoFec(QuestaoFechadaBean fec){
        QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
        String result = dao.GetNumsAlts(fec);
        int c = Integer.parseInt(result.split(" : ")[0]);
        int e = Integer.parseInt(result.split(" : ")[1]);
        int pc = Integer.parseInt(this.bean.getTipo().split(":")[0]);
        int pe = Integer.parseInt(this.bean.getTipo().split(":")[1]);
        return c >= pc && e >= pe;
    }
    private Label createQuestLabel(){
        Label aux = new Label();
        aux.getStyleClass().add("QuestaoLabel");
        aux.setMaxWidth(Double.MAX_VALUE);
        aux.wrapTextProperty().set(true);
        aux.setTextAlignment(TextAlignment.JUSTIFY);
        aux.setOnMouseClicked((evt)->{
            if(aux.getBorder() == null){
                aux.setBorder(new Border(new BorderStroke(Color.ORANGE, 
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
            }else{
                aux.setBorder(null);
            }
        });
        return aux;
    }
    private Label createQuestAbtLabel(QuestaoAbertaBean abt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        Label aux = this.createQuestLabel();
        if(abt.getEnunciado().length() <= 100)
            aux.setText(rb.getString("Open question id") + ": " + abt.getID() + "\n" + rb.getString("Statement") + ": " + abt.getEnunciado());
        else
            aux.setText(rb.getString("Open question id") + ": " + abt.getID() + "\n" + rb.getString("Statement") + ": " + abt.getEnunciado().substring(0, 100) + "...");
        return aux;
    }
    private Label createQuestFecLabel(QuestaoFechadaBean fec){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        Label aux = this.createQuestLabel();
        if(fec.getEnunciado().length() <= 100)
            aux.setText(rb.getString("Closed question id") + ": " + fec.getID() + "\n" + rb.getString("Statement") + ": " + fec.getEnunciado());
        else
            aux.setText(rb.getString("Closed question id") + ": " + fec.getID() + "\n" + rb.getString("Statement") + ": " + fec.getEnunciado().substring(0, 100) + "...");
        return aux;
    }
}
