/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers;

import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import EasyQuizPackage.Models.Beans.CursoBean;
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.ProvaBean;
import EasyQuizPackage.Models.DAOs.CursoDAO;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Arthur
 */
public class CabecaFormFXMLController extends Stage implements Initializable{
    @FXML
    private JFXTextField Nome;
    @FXML
    private JFXTextField Prof;
    @FXML
    private JFXComboBox Curso;
    @FXML
    private JFXTextField Insti;
    @FXML
    private JFXDatePicker Data;
    @FXML
    private JFXTextField Nota;
    @FXML
    private JFXTextField Turma;
    @FXML
    private JFXComboBox modelo;
    @FXML
    private JFXComboBox fonte;
    @FXML
    private AnchorPane apane;
    @FXML
    private JFXSlider tamanho;
    @FXML
    private JFXComboBox Materia;
    @FXML
    private JFXButton Salvar;
    @FXML
    private JFXButton Cancelar;
    @FXML
    private JFXDatePicker datePicker;
    private ProvaBean prova;
    private boolean primeiroAviso = true;
    
    public CabecaFormFXMLController(Parent parent, ProvaBean prova){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Header and Exam Config"));
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/CabecaForm.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(CabecaFormFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(CabecaFormFXMLController.class.getResourceAsStream(Main.LOGO)));
            this.prova = prova;
            if(this.prova.getID() == -1){
                this.Salvar.setText(rb.getString("CREATE"));
                this.getScene().getWindow().setOnCloseRequest(evt -> {evt.consume();});
                this.prova.setID(-3);
            }else{
                this.Nome.setText(this.prova.getNome());
                this.Prof.setText(this.prova.getProf());
                this.Insti.setText(this.prova.getInst());
                this.Data.setValue(this.prova.getData().toLocalDate());
                this.Nota.setText(String.valueOf(this.prova.getNota()));
                this.Turma.setText(this.prova.getTurma());
            }
            this.modelo.getItems().add(rb.getString("Model1"));
            this.modelo.getItems().add(rb.getString("Model2"));
            this.modelo.getItems().add(rb.getString("Model3"));
            this.modelo.getItems().add(rb.getString("Model4"));
            if(this.prova.getTipo() != null){
                switch(this.prova.getTipo()){
                    case "2:3":
                        this.modelo.getSelectionModel().select(0);
                        break;
                    case "2:4":
                        this.modelo.getSelectionModel().select(1);
                        break;
                    case "3:4":
                        this.modelo.getSelectionModel().select(2);
                        break;
                    case "3:5":
                        this.modelo.getSelectionModel().select(3);
                        break;
                    default:
                        MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Invalid Exam Model"), null);
                        mes.showAndWait();
                        break;
                }
            }else{
                this.modelo.getSelectionModel().select(0);
            }
            if(this.prova.getTamanho() != 0){
                this.tamanho.setValue(this.prova.getTamanho());
            }
            CursoDAO cdao = new CursoDAO();
            ArrayList<CursoBean> arrayc = cdao.Get_All();
            arrayc.forEach((c) -> {
                this.Curso.getItems().add(c.getID()+" : "+c.getNome());
            });
            MateriaDAO mdao = new MateriaDAO();
            ArrayList<MateriaBean> arraym = mdao.Get_All();
            arraym.forEach((m) -> {
                this.Materia.getItems().add(m.getID()+" : "+m.getNome());
            });
            this.Materia.getSelectionModel().select(0);
            this.Curso.getSelectionModel().select(0);
            this.Cancelar.setOnAction(this::CancelarClick);
            this.Salvar.setOnAction(this::SalvarClick);
        }
        catch (FileNotFoundException ex){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("File not found") + ": " + ex.getMessage(), null);
            mes.showAndWait();
        }
        catch (IOException e){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Open header and config error") + ": " + e.getMessage(), null);
            mes.showAndWait();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.modelo.setOnAction(this::avisoModelo);
    }
    public void SalvarClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        boolean error = false;
        String erros = "";
        if(this.Nome.getText().equals("") || this.Nome.getText() == null){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Exam's Name") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(this.Prof.getText().equals("") || this.Prof.getText() == null){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Teacher") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(this.Insti.getText().equals("") || this.Insti.getText() == null){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Institution") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(this.Data.getValue() == null || this.Data.getValue().toString().equals("")){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Date") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(this.Nota.getText().equals("") || this.Nota.getText() == null){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Score/Grade") + "\" " + rb.getString("can not be blank") + "\n";
        }
        // REGEX expression to find non-float characters
	if(!this.Nota.getText().matches("[+]?\\d*\\.?\\d+")){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Score/Grade") + "\" " + rb.getString("can not contain letters") + "\n";
        }
        if(this.Turma.getText().equals("") || this.Turma.getText() == null){
            error = true;
            erros += rb.getString("Field") + " \"" + rb.getString("Class") + "\" " + rb.getString("can not be blank") + "\n";
        }
        if(!error){
            this.prova.setNome(this.Nome.getText());
            this.prova.setProf(this.Prof.getText());
            this.prova.setInst(this.Insti.getText());
            this.prova.setData(Date.valueOf(this.Data.getValue().toString()));
            this.prova.setNota(Float.parseFloat(this.Nota.getText()));
            this.prova.setTurma(this.Turma.getText().replace(".", ","));
            switch(this.modelo.getSelectionModel().getSelectedIndex()){
                case 0:
                    this.prova.setTipo("2:3");
                    break;
                case 1:
                    this.prova.setTipo("2:4");
                    break;
                case 2:
                    this.prova.setTipo("3:4");
                    break;
                case 3:
                    this.prova.setTipo("3:5");
                    break;
                default:
                    break;
            }
            CursoBean curso = new CursoBean();
            CursoDAO cdao = new CursoDAO();
            curso.setID(Integer.parseInt(this.Curso.getValue().toString().split(" : ")[0]));
            curso = cdao.Get_One(curso);
            this.prova.setCurso(curso.getNome());
            this.prova.setTamanho((int) this.tamanho.getValue());
            this.prova.setMateria_id(Integer.parseInt(this.Materia.getValue().toString().split(" : ")[0]));
            this.prova.setID(-2);
            this.close();
        }else{
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Invalid Data"), rb.getString("The following errors were found") + ":\n" + erros, null);
            mes.showAndWait();
        }
    }
    private void avisoModelo(Event evt){
        if(primeiroAviso){
            primeiroAviso = false;
            ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Easy Quizzer") + " - " + rb.getString("Header and Exam Config"), rb.getString("Change Model Warning"), null);
            a.showAndWait();
        }
    }
    public void CancelarClick(ActionEvent evt){
        this.close();
    }
}
