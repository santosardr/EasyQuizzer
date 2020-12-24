/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers;

import EasyQuizPackage.Controllers.SubControllers.AddQuestProvaFXMLController;
import EasyQuizPackage.Controllers.SubControllers.ConfirmDialogFXMLController;
import EasyQuizPackage.Controllers.SubControllers.GerarProvaFXMLController;
import EasyQuizPackage.Exceptions.AppSQLException;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import EasyQuizPackage.Models.Beans.AlternativaBean;
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.ProvaBean;
import EasyQuizPackage.Models.Beans.QuestaoAbertaBean;
import EasyQuizPackage.Models.Beans.QuestaoAbertaProvaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaProvaBean;
import EasyQuizPackage.Models.DAOs.AlternativaDAO;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import EasyQuizPackage.Models.DAOs.ProvaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoAbertaProvaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaDAO;
import EasyQuizPackage.Models.DAOs.QuestaoFechadaProvaDAO;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.FontPropertiesManager;
import org.icepdf.ri.util.PropertiesManager;

/**
 *
 * @author Arthur
 */
public class NovaProvaFXMLController extends Stage implements Initializable{
    @FXML
    private FlowPane MainFlowPane;
    @FXML
    private VBox vbox;
    @FXML
    private MenuBar MenuBar;
    @FXML
    private MenuItem Novo;
    @FXML
    private MenuItem Salvar;
    @FXML
    private MenuItem Config;
    @FXML
    private MenuItem Gerar;
    @FXML
    private MenuItem Voltar;
    @FXML 
    private MenuItem Sair;
    @FXML
    private MenuItem addquest;
    @FXML
    private MenuItem delquest;
    @FXML
    private MenuItem removeall;
    @FXML
    private BorderPane Preview;
    private SwingController swingController;
    private JComponent viewerPanel;
    private ProvaBean Prova;
    private ArrayList<QuestaoAbertaBean> questAbt;
    private ArrayList<QuestaoFechadaBean> questFec;
    private ByteArrayOutputStream temp_pdf;
    private static boolean save_flag = false;
    public NovaProvaFXMLController(Parent parent, ProvaBean prova){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("New Exam"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/NovaProva.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(PrincipalFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(MessageDialogFXMLController.class.getResourceAsStream(Main.LOGO)));
            this.setOnCloseRequest(event -> {
                String mensagem = "";
                if(save_flag)
                    mensagem = "Leave lost progress";
                else
                    mensagem = "Leave";
                
                if(ConfirmDialogFXMLController.Display(rb.getString("Confirm"), rb.getString(mensagem)))
                    SwingUtilities.invokeLater(() -> swingController.dispose());
                else
                    event.consume();
            });
            this.temp_pdf = new ByteArrayOutputStream();
            if(prova == null){
                this.Prova = new ProvaBean();
                this.Prova.setID(-1);
                CabecaFormFXMLController cabecalho = new CabecaFormFXMLController(null, this.Prova);
                cabecalho.showAndWait();
                this.questAbt = new ArrayList<>();
                this.questFec = new ArrayList<>();
            }else{
                this.Prova = prova;
                QuestaoAbertaProvaDAO qapDAO = new QuestaoAbertaProvaDAO();
                QuestaoAbertaProvaBean qapBean = new QuestaoAbertaProvaBean();
                qapBean.setProva_ID(this.Prova.getID());
                this.questAbt = qapDAO.GetAllQuests(qapBean);
                QuestaoFechadaProvaDAO qfpDAO = new QuestaoFechadaProvaDAO();
                QuestaoFechadaProvaBean qfpBean = new QuestaoFechadaProvaBean();
                qfpBean.setProva_ID(this.Prova.getID());
                this.questFec = qfpDAO.GetAllQuests(qfpBean);
                this.atualizaQuestoes();
            }
        } catch (IOException ex) {
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Load interface error"), rb.getString("Error") + ": " + ex.getMessage(), null);
            a.showAndWait();
        }
        if(this.Prova.getID() != -3){
            SpinnerDialogFXMLController spinner = new SpinnerDialogFXMLController(rb.getString("Exam"), rb.getString("Loading Exam"), null);
            Task<Boolean> tarefaCargaPg = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    try {
                    SwingUtilities.invokeAndWait(() -> {
                        // create the viewer ri components.
                        swingController = new SwingController();
                        swingController.setIsEmbeddedComponent(true);
                        PropertiesManager properties = new PropertiesManager(System.getProperties(),
                                ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));
                        // read/store the font cache.
                        ResourceBundle messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);
                        new FontPropertiesManager(properties, System.getProperties(), messageBundle);
                        properties.set(PropertiesManager.PROPERTY_HIDE_UTILITYPANE, "true");
                        properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION_TEXT, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_ANNOTATION_HIGHLIGHT, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_UPANE, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_SEARCH, "false");
                        properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "0.60");
                        properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_OPEN, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_SAVE, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_PRINT, "false");
                        // hide the status bar
                        properties.set(PropertiesManager.PROPERTY_SHOW_STATUSBAR, "false");
                        // hide a few toolbars, just to show how the prefered size of the viewer changes.
                        properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FORMS, "false");
                        properties.set(PropertiesManager.PROPERTY_SHOW_UTILITYPANE_ANNOTATION, "false");
                        swingController.getDocumentViewController().setAnnotationCallback(
                                new org.icepdf.ri.common.MyAnnotationCallback(swingController.getDocumentViewController()));
                        SwingViewBuilder factory = new SwingViewBuilder(swingController, properties);
                        viewerPanel = factory.buildViewerPanel();
                        viewerPanel.revalidate();
                        SwingNode swingNode = new SwingNode();
                        swingNode.setContent(viewerPanel);
                        Preview.setCenter(swingNode);
                        /*
                        // add toolbar to the top.
                        FlowPane toolBarFlow = new FlowPane();
                        JToolBar mainToolbar = factory.buildCompleteToolBar(true);
                        buildJToolBar(toolBarFlow, mainToolbar);
                        borderPane.setTop(toolBarFlow);
                        // main utility pane and viewer
                        SwingNode swingNode = new SwingNode();
                        viewerPanel = factory.buildUtilityAndDocumentSplitPane(true);
                        swingNode.setContent(viewerPanel);
                        borderPane.setCenter(swingNode);
                        // the page view menubar
                        FlowPane statusBarFlow = new FlowPane();
                        buildButton(statusBarFlow, factory.buildPageViewSinglePageNonConToggleButton());
                        buildButton(statusBarFlow, factory.buildPageViewSinglePageConToggleButton());
                        buildButton(statusBarFlow, factory.buildPageViewFacingPageNonConToggleButton());
                        buildButton(statusBarFlow, factory.buildPageViewFacingPageConToggleButton());
                        borderPane.setBottom(statusBarFlow);
                        */
                    });
                } catch (InterruptedException | InvocationTargetException ex) {
                    Logger.getLogger(NovaProvaFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                 return true;
                }

                @Override
                protected void succeeded() {
                 boolean codigo = getValue();
                    spinner.hide();
                }
            };
            Thread t = new Thread(tarefaCargaPg);
            t.setDaemon(true);
            t.start();
            spinner.showAndWait();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SingletonConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.generatePreview();
        this.Sair.setOnAction(this::SairClick);
        this.Voltar.setOnAction(this::VoltarClick);
        this.Novo.setOnAction(this::NovoClick);
        this.Config.setOnAction(this::ConfigClick);
        this.Salvar.setOnAction(this::SalvarClick);
        this.addquest.setOnAction(this::AddQuestClick);
        this.delquest.setOnAction(this::DelClick);
        this.Gerar.setOnAction(this::generateProvas);
        this.removeall.setOnAction(this::removeAllClick);
        this.vbox.getChildren().addListener(new ListChangeListener<Node>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
                generatePreview();
            }
        });
    }
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void VoltarClick(ActionEvent evt){
        if(save_flag){
            ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
            boolean result = ConfirmDialogFXMLController.Display(rb.getString("Current Exam"), rb.getString("Save Exam"));
            if(result){
                SalvarClick(evt);
            }
        }
        PrincipalFXMLController prin = new PrincipalFXMLController(null);
        this.hide();
        prin.show();
    }
    
    public void SairClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        if(save_flag){
            boolean result = ConfirmDialogFXMLController.Display(rb.getString("Current Exam"), rb.getString("Save Exam"));
            if(result){
                SalvarClick(evt);
            }
        }
        boolean result = ConfirmDialogFXMLController.Display(rb.getString("Exit"), rb.getString("Leave"));
        if(result){
            System.exit(0);
        }
    }
    
    public void NovoClick(ActionEvent evt){ 
        if(save_flag){
            ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
            boolean result = ConfirmDialogFXMLController.Display(rb.getString("Current Exam"), rb.getString("Save Exam"));
            if(result){
                SalvarClick(evt);
            }
        }
        int antigaProvaID = this.Prova.getID();
        this.Prova = new ProvaBean();
        this.Prova.setID(-1);
        // Cria uma nova prova com as questões da atual
        CabecaFormFXMLController cabecalho = new CabecaFormFXMLController(null, this.Prova);
        cabecalho.showAndWait();
        if(this.Prova.getID() == -3)
            this.Prova.setID(antigaProvaID);
        else
            this.save_flag = true;
        this.generatePreview();
    }
    
    public void SalvarClick(ActionEvent evt){ //Falta Salvar Questões
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        ProvaDAO dao = new ProvaDAO();
        QuestaoAbertaProvaDAO qadao = new QuestaoAbertaProvaDAO();
        QuestaoFechadaProvaDAO qfdao = new QuestaoFechadaProvaDAO();
        String suc;
        try {
            if(this.Prova.getID() == -2){
                dao.Insert(this.Prova);
                int last = dao.Get_Last_ID();
                questAbt.stream().map((b) -> {
                    QuestaoAbertaProvaBean nbean = new QuestaoAbertaProvaBean();
                    nbean.setProva_ID(last);
                    nbean.setQuestaoAberta_ID(b.getID());
                    return nbean;
                }).forEachOrdered((nbean) -> {
                    qadao.Insert(nbean);
                });
                questFec.stream().map((b) -> {
                    QuestaoFechadaProvaBean nbean = new QuestaoFechadaProvaBean();
                    nbean.setProva_ID(last);
                    nbean.setQuestaoFechada_ID(b.getID());
                    return nbean;
                }).forEachOrdered((nbean) -> {
                    qfdao.Insert(nbean);
                });
                suc = rb.getString("Success Adding");
                this.Prova.setID(dao.Get_Last_ID());
            }else{
                dao.Edit(Prova);
                QuestaoAbertaProvaBean qapbean = new QuestaoAbertaProvaBean();
                QuestaoFechadaProvaBean qfpbean = new QuestaoFechadaProvaBean();
                qapbean.setProva_ID(Prova.getID());
                qfpbean.setProva_ID(Prova.getID());
                qadao.DeleteAll(qapbean);
                qfdao.DeleteAll(qfpbean);
                int last = Prova.getID();
                questAbt.stream().map((b) -> {
                    QuestaoAbertaProvaBean nbean = new QuestaoAbertaProvaBean();
                    nbean.setProva_ID(last);
                    nbean.setQuestaoAberta_ID(b.getID());
                    return nbean;
                }).forEachOrdered((nbean) -> {
                    qadao.Insert(nbean);
                });
                questFec.stream().map((b) -> {
                    QuestaoFechadaProvaBean nbean = new QuestaoFechadaProvaBean();
                    nbean.setProva_ID(last);
                    nbean.setQuestaoFechada_ID(b.getID());
                    return nbean;
                }).forEachOrdered((nbean) -> {
                    qfdao.Insert(nbean);
                });
                suc = rb.getString("Success Saving");
            }
            save_flag = false;
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Success"), suc, null);
            mes.showAndWait();
        } catch (AppSQLException ex) {
            String error_message;
            if(this.Prova.getID() == -2){
                error_message = rb.getString("Add database error");
            }else{
                error_message = rb.getString("Modify database error");
            }
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), error_message + ex.getBaseException().getMessage(), null);
            mes.showAndWait();
        }
    }
    
    public void ConfigClick(ActionEvent evt){
        int antigaProvaID = this.Prova.getID();
        CabecaFormFXMLController cabecalho = new CabecaFormFXMLController(null ,this.Prova);
        cabecalho.showAndWait();
        if(this.Prova.getID() == -3)
            this.Prova.setID(antigaProvaID);
        else{
            this.verificaQuestoesFechadas();
            this.vbox.getChildren().clear();
            this.atualizaQuestoes();
            save_flag = true;
        }
        this.generatePreview();
    }
    
    private void verificaQuestoesFechadas(){
        QuestaoFechadaDAO dao = new QuestaoFechadaDAO();
        for(int i = 0; i < questFec.size();i++){
            QuestaoFechadaBean bean = questFec.get(i);
            String result = dao.GetNumsAlts(bean);
            int c = Integer.parseInt(result.split(" : ")[0]);
            int e = Integer.parseInt(result.split(" : ")[1]);
            int pc = Integer.parseInt(this.Prova.getTipo().split(":")[0]);
            int pe = Integer.parseInt(this.Prova.getTipo().split(":")[1]);
            if(c<pc && e<pe)
                questFec.remove(i);
        }
    }
    
    public void AddQuestClick(ActionEvent evt){
        AddQuestProvaFXMLController a = new AddQuestProvaFXMLController(null,this.Prova,this.questAbt,this.questFec,this.vbox);
        a.showAndWait();
        this.generatePreview();
        save_flag = true;
    }
    
    private void createResizeListeners(Scene scene, JComponent viewerPanel) {
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            SwingUtilities.invokeLater(() -> {
                viewerPanel.setSize(new Dimension(newValue.intValue(), (int) scene.getHeight()));
                viewerPanel.setPreferredSize(new Dimension(newValue.intValue(), (int) scene.getHeight()));
                viewerPanel.repaint();
            });
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            SwingUtilities.invokeLater(() -> {
                viewerPanel.setSize(new Dimension((int) scene.getWidth(), newValue.intValue()));
                viewerPanel.setPreferredSize(new Dimension((int) scene.getWidth(), newValue.intValue()));
                viewerPanel.repaint();
            });
        });
    }

    private void openDocument(String document) {
        SwingUtilities.invokeLater(() -> {
            swingController.openDocument(document);
            viewerPanel.revalidate();
        });
    }
    
    private void openTemp(byte[] a){
        SwingUtilities.invokeLater(() -> {
            swingController.openDocument(a,0,a.length,"Preview", null);
            viewerPanel.revalidate();
        });
    }

    private void buildButton(FlowPane flowPane, AbstractButton jButton){
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(jButton);
        flowPane.getChildren().add(swingNode);
    }

    private void buildJToolBar(FlowPane flowPane, JToolBar jToolBar){
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(jToolBar);
        flowPane.getChildren().add(swingNode);
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
    
    private void atualizaQuestoes(){ //método para refazer a interface que mostra as questões
        try {
            this.questAbt.forEach((a)->{
                this.vbox.getChildren().add(this.createQuestAbtLabel(a));
            });
            this.questFec.forEach((a)->{
                this.vbox.getChildren().add(this.createQuestFecLabel(a));
            });
        } catch (Exception e) {
            ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Reload interface error"), e.getMessage(), null);
            a.showAndWait();
        }
    }
    
    private void DelClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        for (Iterator<Node> iterator = this.vbox.getChildren().iterator(); iterator.hasNext(); ) {
            Node a = iterator.next();
            Label b = ((Label)a);
            if(b.getBorder() != null){
                if(b.getText().contains(rb.getString("Open question id") + ": ")){
                    int id = Integer.parseInt(b.getText().split(rb.getString("Open question id") + ": ")[1].split("\n" + rb.getString("Statement") + ": ")[0]);
                    this.questAbt.removeIf(c -> {
                        return c.getID() == id;
                    });
                }else{
                    int id = Integer.parseInt(b.getText().split(rb.getString("Closed question id") + ": ")[1].split("\n" + rb.getString("Statement") + ": ")[0]);
                    this.questFec.removeIf(c -> {
                        return c.getID() == id;
                    });
                }
                iterator.remove();
            }
        }
        this.generatePreview();
        save_flag = true;
    }
    
    private void generatePreview(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        PdfWriter writer = new PdfWriter(this.temp_pdf);
        PdfDocument doc = new PdfDocument(writer);
        Document temp = new Document(doc, PageSize.A4);
        HeaderHandler hh = new HeaderHandler();
        hh.setInfo(rb.getString("Exam") + " n.X");
        doc.addEventHandler(PdfDocumentEvent.START_PAGE, hh);
        try {
            PdfFont font = null;
            font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            //Instituição
            Paragraph inst = new Paragraph(this.Prova.getInst()).setFont(font)
                    .setFontSize(this.Prova.getTamanho()+2)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidthPercent(100)
                    .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER);
            
            //Curso e Data
            Paragraph cursodata = new Paragraph(rb.getString("Course") + ": " + this.Prova.getCurso()).setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            cursodata.add(new Tab());
            cursodata.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            SimpleDateFormat datef = new SimpleDateFormat("dd/MM/YYYY");
            cursodata.add(rb.getString("Date") + ": " + datef.format(this.Prova.getData())).setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            cursodata.setMarginTop(0.1f);
            cursodata.setMarginBottom(0.1f);
            //Materia e Turma
            MateriaDAO mdao = new MateriaDAO();
            MateriaBean mbean = new MateriaBean();
            mbean.setID(this.Prova.getMateria_id());
            Paragraph materiaturma = new Paragraph(rb.getString("Subject") + ": " + mdao.Get_One(mbean).getNome()).setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            materiaturma.add(new Tab());
            materiaturma.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            materiaturma.add(rb.getString("Class") + ": " + this.Prova.getTurma()).setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            materiaturma.setMarginTop(0.1f);
            materiaturma.setMarginBottom(0.1f);
            //Professor e Valor
            Paragraph profval = new Paragraph(rb.getString("Teacher") + ": " + this.Prova.getProf()).setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            profval.add(new Tab());
            profval.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            profval.add(rb.getString("Score") + ": " + this.Prova.getNota()).setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            profval.setMarginTop(0.1f);
            profval.setMarginBottom(0.1f);
            //Nome Aluno e Nota
            Paragraph alunonota = new Paragraph(rb.getString("Student") + ":").setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            alunonota.add("____________________________________________________").setFont(font).setFontSize(12);
            alunonota.add(new Tab());
            alunonota.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
            alunonota.add(rb.getString("Score") + ":______").setFont(font)
                    .setFontSize(this.Prova.getTamanho());
            alunonota.setMarginTop(0.1f);
            alunonota.setMarginBottom(0.1f);
            //Nota
            Paragraph nota = new Paragraph(rb.getString("Score") + ": " + this.Prova.getNota()).setFont(font)
                    .setFontSize(this.Prova.getTamanho())
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT);
            //Cabeçalho
            temp.add(inst);
            temp.add(cursodata);
            temp.add(materiaturma);
            temp.add(profval);
            temp.add(alunonota);
            int i = questAbt.size();
            int u = questFec.size();
            int z = Math.max(i, u);
            int count = 1;
            for(int y = 0 ; y < z ; y++){
                if(y < u){
                    Paragraph questfec = new Paragraph(count + "- " + questFec.get(y).getEnunciado()).setFont(font).setFontSize(this.Prova.getTamanho())
                            .setHorizontalAlignment(HorizontalAlignment.LEFT).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                    temp.add(questfec);
                    int num_alt = 0;
                    switch(this.Prova.getTipo()){
                        case "3:5":
                            num_alt = 5;
                            break;
                        case "2:4":
                        case "3:4":
                        case "2:3":
                            num_alt = 4;
                            break;
                        default:
                            break;
                    }
                    AlternativaDAO altdao = new AlternativaDAO();
                    ArrayList<AlternativaBean> alters = altdao.Get_All_From_Quest(questFec.get(y));
                    boolean certa = false;
                    int qtd = alters.size();
                    int pos = 0;
                    int numeracao = 0;
                    String alt_list = "abcde";
                    for(int t = 0 ; t < num_alt ; t++){
                        if(t==4 && certa == false){
                            t--;
                            pos++;
                        }else{
                            if(alters.get(pos).isCerta() && certa == false){
                                certa = true;
                                Paragraph alts = new Paragraph().setFont(font).setFontSize(this.Prova.getTamanho()-1).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                                alts.add(alt_list.charAt(numeracao++)+") "+alters.get(pos++).getTexto()+"  ");
                                alts.setMarginTop(0f);
                                alts.setMarginBottom(0f);
                                temp.add(alts);
                            }else if(alters.get(pos).isCerta() && certa == true){
                                pos++;
                                t--;
                            }else{
                                Paragraph alts = new Paragraph().setFont(font).setFontSize(this.Prova.getTamanho()-1).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                                alts.add(alt_list.charAt(numeracao++)+") "+alters.get(pos++).getTexto()+"  ");
                                alts.setMarginTop(0f);
                                alts.setMarginBottom(0f);
                                temp.add(alts);
                            }
                        }
                    }
                    count++;
                }
                if(y < i){
                    Paragraph questabt = new Paragraph(count+"- "+questAbt.get(y).getEnunciado()).setFont(font).setFontSize(this.Prova.getTamanho())
                            .setHorizontalAlignment(HorizontalAlignment.LEFT).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                    temp.add(questabt);
                    for(int l = 0; l < questAbt.get(y).getNumLinhas(); l++){
                        Paragraph line = new Paragraph("______________________________________________________________________________").setFont(font).setFontSize(12)
                                .setHorizontalAlignment(HorizontalAlignment.LEFT).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                        line.setMarginTop(0.05f);
                        line.setMarginBottom(0.05f);
                        temp.add(line);
                    }
                    count++;
                }
            }
            temp.close();
//            PdfCanvas canvas = new PdfCanvas(temp.getPdfDocument().getFirstPage());
//            DeviceCmyk blackColor = new DeviceCmyk(0.f, 0.f, 0.f, 1.f);
            this.openTemp(this.temp_pdf.toByteArray());
        } catch (IOException ex) {
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), ex.getMessage(), null);
            a.showAndWait();
        }
    }
    
    private void generateProvas(ActionEvent evt){
        if(save_flag){
            ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
            boolean result = ConfirmDialogFXMLController.Display(rb.getString("Current Exam"), rb.getString("Save Exam"));
            if(result){
                SalvarClick(evt);
            }
        }
        GerarProvaFXMLController a = new GerarProvaFXMLController(null,this.Prova,this.questAbt,this.questFec);
        a.showAndWait();
    }
    
    private void removeAllClick(ActionEvent evt){
        this.questAbt.clear();
        this.questFec.clear();
        this.vbox.getChildren().clear();
    }
    
    private void createViewer(BorderPane borderPane) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                // create the viewer ri components.
                swingController = new SwingController();
                swingController.setIsEmbeddedComponent(true);
                PropertiesManager properties = new PropertiesManager(System.getProperties(),
                        ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));
                // read/store the font cache.
                ResourceBundle messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);
                new FontPropertiesManager(properties, System.getProperties(), messageBundle);
                properties.set(PropertiesManager.PROPERTY_HIDE_UTILITYPANE, "true");
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION_TEXT, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_ANNOTATION_HIGHLIGHT, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_UPANE, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_SEARCH, "false");
                properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "0.60");
                properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_OPEN, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_SAVE, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_UTILITY_PRINT, "false");
                // hide the status bar
                properties.set(PropertiesManager.PROPERTY_SHOW_STATUSBAR, "false");
                // hide a few toolbars, just to show how the prefered size of the viewer changes.
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FORMS, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_UTILITYPANE_ANNOTATION, "false");
                swingController.getDocumentViewController().setAnnotationCallback(
                        new org.icepdf.ri.common.MyAnnotationCallback(swingController.getDocumentViewController()));
                SwingViewBuilder factory = new SwingViewBuilder(swingController, properties);
                viewerPanel = factory.buildViewerPanel();
                viewerPanel.revalidate();
                SwingNode swingNode = new SwingNode();
                swingNode.setContent(viewerPanel);
                borderPane.setCenter(swingNode);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(NovaProvaFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
