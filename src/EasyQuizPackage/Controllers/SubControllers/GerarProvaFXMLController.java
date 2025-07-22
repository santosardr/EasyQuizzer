/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyQuizPackage.Controllers.SubControllers;

import EasyQuizPackage.Controllers.HeaderHandler;
import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.Controllers.SpinnerDialogFXMLController;
import EasyQuizPackage.Interfaces.Questao;
import EasyQuizPackage.JDBC.SingletonConnection;
import EasyQuizPackage.Main.Main;
import EasyQuizPackage.Models.Beans.AlternativaBean;
import EasyQuizPackage.Models.Beans.MateriaBean;
import EasyQuizPackage.Models.Beans.PermutacaoBean;
import EasyQuizPackage.Models.Beans.ProvaBean;
import EasyQuizPackage.Models.Beans.QuestaoAbertaBean;
import EasyQuizPackage.Models.Beans.QuestaoFechadaBean;
import EasyQuizPackage.Models.DAOs.MateriaDAO;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Arthur
 */
public class GerarProvaFXMLController extends Stage implements Initializable{
    @FXML
    private JFXTextField local;
    @FXML
    private JFXTextField qtd_provas;
    @FXML
    private JFXButton escolher;
    @FXML
    private JFXButton gerar;
    @FXML
    private JFXButton cancelar;
    @FXML
    private JFXProgressBar pb;
    private ProvaBean prova;
    private ArrayList<Questao> quests;
    private Integer num_alt;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    private boolean questaoUnicaAberta(){
        int cont = 0, aberta = 0;
        for( Questao quest : quests ) {
            cont++;
            if(cont > 1)
                break;
            if(quest instanceof QuestaoAbertaBean) {
                aberta++;
            }
        }
        if(cont == 1 && aberta == 1)
            return true;
        else
            return false;
    }
    public GerarProvaFXMLController(Parent parent, ProvaBean prova, ArrayList<QuestaoAbertaBean> questabt, ArrayList<QuestaoFechadaBean> questfec){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        this.setTitle(rb.getString("Easy Quizzer") + " - " + rb.getString("Generate Exams"));
        this.setMinWidth(620);
        this.setMinHeight(264);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EasyQuizPackage/Views/GerarProvas.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(rb);
        try{
            Scene cena = new Scene((Parent) fxmlLoader.load());
            cena.getStylesheets().add(GerarProvaFXMLController.class.getResource("/css/jfoenix-fonts.css").toExternalForm());
            cena.getStylesheets().add(GerarProvaFXMLController.class.getResource("/css/jfoenix-design.css").toExternalForm());
            cena.getStylesheets().add(GerarProvaFXMLController.class.getResource("/Resources/Principal.css").toExternalForm());
            setScene(cena);
            this.getIcons().add(new Image(GerarProvaFXMLController.class.getResourceAsStream(Main.LOGO)));
            this.prova = prova;
            this.quests = new ArrayList<>();
            this.quests.addAll(questabt);
            this.quests.addAll(questfec);
            this.local.setDisable(true);
            this.escolher.setOnAction(this::escolherClick);
            this.gerar.setOnAction(this::gerarClick);
            this.cancelar.setOnAction(this::cancelarClick);
            pb.setProgress(0);
        }
        catch (FileNotFoundException ex){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("File not found") + ": " + ex.getMessage(), null);
            mes.showAndWait();
        }
        catch (IOException e){
            MessageDialogFXMLController mes = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Open generate exams error") + ": " + e.getMessage(), null);
            mes.showAndWait();
        }
    }
    private void escolherClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        DirectoryChooser dc = new DirectoryChooser();
        File choosen = dc.showDialog(this);
        if(choosen == null && this.local.getText().equals("")){
            MessageDialogFXMLController md = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Choose directory to generate exams"), null);
            md.showAndWait();
        }else{
            this.local.setText(choosen.getAbsolutePath());
        }
    }
    private void gerarClick(ActionEvent evt){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        String errors = "";
        boolean error = false;
        if(this.local.getText().equals("")){
            error = true;
            errors += rb.getString("Choose directory to generate exams") + "\n";
        }
        int max = 0;
        switch(this.prova.getTipo()){
            case "2:3":
                max = 12;
                num_alt = 4;
                break;
            case "2:4":
                max = 48;
                num_alt = 4;
                break;
            case "3:4":
                max = 72;
                num_alt = 4;
                break;
            case "3:5":
                max = 360;
                num_alt = 5;
                break;
            default:
                break;
        }
        // Caso qtd_provas seja int, coloca em qtd. Em caso de overflow/outros erros, deixa 0. Se não for int, fica -1.
        int qtd = -1;
        if(this.qtd_provas.getText().replaceAll("\\d+","").length() != 0){
            error = true;
            errors += rb.getString("Field") + " \"" + rb.getString("Number of exams") + "\" " + rb.getString("can not contain letters") + "\n";
        } else {
            try {
                qtd = Integer.parseInt(this.qtd_provas.getText());
            } catch (Exception e) {
                qtd = 0;
            }
        }
        if(qtd == 0){
            error = true;
            errors += rb.getString("Invalid number of exams") + "\n";
            this.qtd_provas.setText("");
        }
        if(qtd > max){
            error = true;
            errors += rb.getString("Too many exams") + " (" + Integer.toString(max) + ")" + "\n";
            this.qtd_provas.setText("");
        }
        if(error){
            MessageDialogFXMLController msg = new MessageDialogFXMLController(rb.getString("Invalid Data"), rb.getString("The following errors were found") + ":\n" + errors, null);
            msg.showAndWait();
        }else{
            //Gerar Provas
            ArrayList<PermutacaoBean> provas = new ArrayList<>();
            final int qtd2 = qtd;
            SpinnerDialogFXMLController spinner = new SpinnerDialogFXMLController(rb.getString("Exams"), rb.getString("Generating exams"), null);
            Task<Boolean> tarefa2 = new Task<Boolean>(){
                    @Override
                    protected Boolean call() throws Exception{
                        boolean verifica = true;
                        int rodada = 1;
                        do{
                            System.out.println("Turn: "+rodada++);
	                   verifica = true;
                            if( questaoUnicaAberta() ){
                                PermutacaoBean aux = gerarPermutacao();
                                while(provas.size() != qtd2){
                                    provas.add(aux);
                                }
                            }
                            else{
                                // CORREÇÃO 13: Adicionar contador de tentativas para evitar loop infinito
                                int tentativas = 0;
                                int maxTentativas = qtd2 * 1000; // Limite máximo de tentativas
                                
                                while(provas.size() != qtd2){
                                    tentativas++;
                                    if(tentativas > maxTentativas) {
                                        System.err.println("ERRO CRÍTICO: Excedido limite de tentativas (" + maxTentativas + ") para gerar " + qtd2 + " provas únicas.");
                                        System.err.println("Provas geradas até agora: " + provas.size());
                                        break;
                                    }
                                    
                                    if(tentativas % 100 == 0) {
                                        System.out.println("Tentativa " + tentativas + " - Provas geradas: " + provas.size() + "/" + qtd2);
                                    }
                                    
                                    PermutacaoBean aux = gerarPermutacao();
                                    // CORREÇÃO 14: Verificar se gerarPermutacao retornou null
                                    if(aux == null) {
                                        System.err.println("ERRO: gerarPermutacao retornou null na tentativa " + tentativas);
                                        continue;
                                    }
                                    
                                    // CORREÇÃO 15: Verificar se a permutação tem questões válidas
                                    boolean permutacaoValida = true;
                                    for(Questao q : aux.getQuestoes()) {
                                        if(q == null) {
                                            System.err.println("ERRO: Questão null encontrada na permutação");
                                            permutacaoValida = false;
                                            break;
                                        }
                                        if(q instanceof QuestaoFechadaBean) {
                                            QuestaoFechadaBean qf = (QuestaoFechadaBean)q;
                                            if(qf.getSelecionadas() == null || qf.getSelecionadas().size() == 0) {
                                                System.err.println("ERRO: Questão " + qf.getID() + " sem alternativas selecionadas");
                                                permutacaoValida = false;
                                                break;
                                            }
                                        }
                                    }
                                    
                                    if(!permutacaoValida) {
                                        continue;
                                    }
                                    
                                    if(!existePerm(aux,provas)){
                                        provas.add(aux);
					aux=null;
                                    }
                                }
				System.gc();
                            }
                            //Verificar Distribuição
                            Iterator<PermutacaoBean> ite = provas.iterator();
                                int numerador = 1;
                                while(ite.hasNext()){ //Percorre Todas as Provas
                                    PermutacaoBean aux = ite.next();
                                    int a = 0, b = 0, c = 0, d = 0, e = 0, total_aberta = 0, total_fec = 0;
                                    for( Questao quest : aux.getQuestoes()){ //Percorre Todas as Questões de Uma Prova
                                        if(quest instanceof QuestaoFechadaBean){ //Se Fechada Contabiliza a certa
                                            total_fec++;
                                            QuestaoFechadaBean aux2 = ((QuestaoFechadaBean)quest);
                                            for(int z = 0; z < aux2.getSelecionadas().size(); z++){
                                                // CORREÇÃO 17: Verificação defensiva contra alternativas null
                                                if(aux2.getSelecionadas().get(z) != null && aux2.getSelecionadas().get(z).isCerta()){
                                                    switch(z){
                                                        case 0:
                                                            a++;
                                                            break;
                                                        case 1:
                                                            b++;
                                                            break;
                                                        case 2:
                                                            c++;
                                                            break;
                                                        case 3:
                                                            d++;
                                                            break;
                                                        case 4:
                                                            e++;
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        //else{
                                        //    total_aberta++;
                                        //}
                                    }
                                    double ac = (double)a/(double)total_fec;
                                    double bc = (double)b/(double)total_fec;
                                    double cc = (double)c/(double)total_fec;
                                    double dc = (double)d/(double)total_fec;
                                    double ec = (double)e/(double)total_fec;
                                    double limiar_up = 0.35;
                                    double limiar_down = 0.15;
                                    System.out.println("Test: "+numerador+"| A = "+ac+" B = "+bc+" C = "+cc+" D = "+dc+" E = "+ec+" L_UP = "+limiar_up+" L_DOWN = "+limiar_down+" |"+ " finished  = "+ provas.size() );
                                    if(  ac>limiar_up  || ac<limiar_down) {
                                        ite.remove();
                                        verifica = false;
                                        //System.out.println("X AC");
                                    }else if( bc>limiar_up  ||  bc<limiar_down){
                                        ite.remove();
                                        verifica = false;
                                        //System.out.println("X BC");
                                    }else if(  cc>limiar_up  ||  cc<limiar_down){
                                        ite.remove();
                                        verifica = false;
                                        //System.out.println("X CC");
                                    }else if( dc>limiar_up  ||  dc<limiar_down) {
                                        ite.remove();
                                        verifica = false;
                                        //System.out.println("X DC");
                                    }else if( num_alt==5 && ( ec>limiar_up  ||  ec<limiar_down) ){ // Somente se a prova tiver 5 alternativas por questão
                                        ite.remove();
                                        verifica = false;
                                        //System.out.println("X EC");
                                    }
                                    //System.out.println("Numerador: "+numerador );
                                    numerador++;
                                }
                        }while(!verifica);
                        return true;
                    }
                    @Override
                    protected void succeeded(){
                        spinner.hide();
                    }
            };
            Thread t = new Thread(tarefa2);
            t.setDaemon(true);
            t.start();
            spinner.showAndWait();
            try {
                t.join();
            } catch (InterruptedException e) {
                //
            }
            //Gerar Arquivos
            Path cam_provas = Paths.get(local.getText() + "/" + rb.getString("Exams"));
            try {
                if(Files.notExists(cam_provas)){
                    new File(cam_provas.toUri()).mkdirs();
                }else{
                    FileUtils.cleanDirectory(new File(cam_provas.toUri()));
                }
            } catch (IOException e) {
                MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), rb.getString("Open directory error") + ": " + e.getMessage(), null);
                a.showAndWait();
            }
            SpinnerDialogFXMLController spinner2 = new SpinnerDialogFXMLController(rb.getString("Exams"), rb.getString("Exporting files"), null);
            Task<Boolean> tarefa3 = new Task<Boolean>(){
                @Override
                protected Boolean call(){
                    generateFiles(local.getText(), provas);
                    return true;
                }
                @Override
                protected void succeeded(){
                    spinner2.hide();
                }
            };
            Thread t2 = new Thread(tarefa3);
            t2.setDaemon(true);
            t2.start();
            spinner2.showAndWait();
            try {
                t2.join();
            } catch (InterruptedException e) {
            }
            pb.setProgress(1.0);
            MessageDialogFXMLController suc = new MessageDialogFXMLController(rb.getString("End"), rb.getString("Success generating"), null);
            suc.showAndWait();
            this.close();
        }
    }
    private PermutacaoBean gerarPermutacao(){
	System.out.println(".");
        PermutacaoBean bean = new PermutacaoBean();
        ArrayList<Questao> temp = new ArrayList<>();
        temp.addAll(this.quests);
        Random random = new Random();
        
        // CORREÇÃO 16: Contador de tentativas para evitar loop infinito em gerarPermutacao
        int tentativasPermutacao = 0;
        int maxTentativasPermutacao = this.quests.size() * 100;
        
        while(bean.size() != this.quests.size()){
            tentativasPermutacao++;
            if(tentativasPermutacao > maxTentativasPermutacao) {
                System.err.println("ERRO CRÍTICO: Excedido limite de tentativas em gerarPermutacao");
                return null;
            }
            int index = random.nextInt(temp.size());
            Questao aux = null;
            if(temp.get(index) instanceof QuestaoAbertaBean){
                aux = temp.remove(index);
            }else{
                int total = 0;
                switch(this.prova.getTipo()){
                    case "2:3":
                    case "2:4":
                    case "3:4":
                        total = 4;
                        break;
                    case "3:5":
                        total = 5;
                        break;
                    default:
                        break;
                }
                // CORREÇÃO 12: Verificar se randomAlts retornou null
                aux = randomAlts((QuestaoFechadaBean)temp.remove(index),total);
                if(aux == null) {
                    System.err.println("ERRO: randomAlts retornou null, tentando novamente...");
                    continue; // Tenta novamente sem incrementar o bean
                }
            }
            bean.addQuestao(aux);
        }
	temp=null;
        return bean;
    }
    private QuestaoFechadaBean randomAlts(QuestaoFechadaBean a, int total){
        // CORREÇÃO 1: Obter listas frescas do banco para evitar reutilização
        ArrayList<AlternativaBean> certas = a.getAlternativasCertas();
        ArrayList<AlternativaBean> erradas = a.getAlternativasErradas();
        
        // CORREÇÃO 2: Criar cópias das listas para não modificar as originais
        ArrayList<AlternativaBean> certasCopia = new ArrayList<>(certas);
        ArrayList<AlternativaBean> erradasCopia = new ArrayList<>(erradas);
        
        // CORREÇÃO 3: Validação defensiva - verificar se há alternativas suficientes
        if(certasCopia.size() == 0) {
            System.err.println("ERRO CRÍTICO: Questão " + a.getID() + " não possui alternativas certas!");
            return null;
        }
        if(erradasCopia.size() < (total - 1)) {
            System.err.println("ERRO CRÍTICO: Questão " + a.getID() + " não possui alternativas erradas suficientes! Necessário: " + (total-1) + ", Disponível: " + erradasCopia.size());
            return null;
        }
        
        QuestaoFechadaBean randomizada = new QuestaoFechadaBean();
        randomizada.setEnunciado(a.getEnunciado());
        randomizada.setID(a.getID());
        randomizada.setTopico_ID(a.getTopico_ID());
        Random random = new Random();
        AlternativaBean[] temporario = new AlternativaBean[total];
        int index = 0;
        
        // CORREÇÃO 4: Try-catch para capturar possíveis exceções
        try {
            // Seleciona uma alternativa certa
            index = random.nextInt(certasCopia.size());
            AlternativaBean certa = certasCopia.get(index);
            int pos = random.nextInt(total);
            temporario[pos] = certa;
            
            // Seleciona alternativas erradas
            int faltam = total-1;
            while(faltam > 0){
                // CORREÇÃO 5: Verificação adicional antes de acessar lista
                if(erradasCopia.size() == 0) {
                    System.err.println("ERRO: Lista de alternativas erradas vazia durante processamento da questão " + a.getID());
                    break;
                }
                
                index = random.nextInt(erradasCopia.size());
                // CORREÇÃO 6: Remove da CÓPIA, não da lista original
                AlternativaBean errada = erradasCopia.remove(index);
                do{
                    index = random.nextInt(total);
                }while(temporario[index] != null);
                temporario[index] = errada;
                faltam--;
            }
            
            // CORREÇÃO 7: Verificação final - garantir que todas as posições foram preenchidas
            for(int i = 0; i < total; i++) {
                if(temporario[i] == null) {
                    System.err.println("ERRO: Posição " + i + " do array temporário está null para questão " + a.getID());
                }
            }
            
            // CORREÇÃO 8: Filtrar nulls antes de adicionar (defensivo)
            for(AlternativaBean alt : temporario) {
                if(alt != null) {
                    randomizada.getSelecionadas().add(alt);
                }
            }
            
        } catch (Exception e) {
            System.err.println("EXCEÇÃO em randomAlts para questão " + a.getID() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
        return randomizada;
    }
    private boolean existePerm(PermutacaoBean bean, ArrayList<PermutacaoBean> lista){
        boolean existe = true;
        if(lista.size() > 0){
            for(PermutacaoBean cp : lista){
                for(int i =0 ; i < cp.getQuestoes().size(); i++){
                    if(cp.getQuestoes().get(i) instanceof QuestaoAbertaBean && bean.getQuestoes().get(i) instanceof QuestaoFechadaBean){
                        existe = false;
                        break;
                    }
                    if(cp.getQuestoes().get(i) instanceof QuestaoFechadaBean && bean.getQuestoes().get(i) instanceof QuestaoAbertaBean){
                        existe = false;
                        break;
                    }
                    if(cp.getQuestoes().get(i) instanceof QuestaoAbertaBean && bean.getQuestoes().get(i) instanceof QuestaoAbertaBean){
                        if(((QuestaoAbertaBean)cp.getQuestoes().get(i)).getID() != ((QuestaoAbertaBean)bean.getQuestoes().get(i)).getID()){
                            existe = false;
                            break;
                        }
                    }
                    if(cp.getQuestoes().get(i) instanceof QuestaoFechadaBean && bean.getQuestoes().get(i) instanceof QuestaoFechadaBean){
                        // CORREÇÃO 9: Comparar questões por ID primeiro
                        QuestaoFechadaBean questao1 = (QuestaoFechadaBean)cp.getQuestoes().get(i);
                        QuestaoFechadaBean questao2 = (QuestaoFechadaBean)bean.getQuestoes().get(i);
                        
                        // Se são questões diferentes, não precisa comparar alternativas
                        if(questao1.getID() != questao2.getID()) {
                            existe = false;
                            break;
                        }
                        
                        // CORREÇÃO 10: Verificação defensiva contra listas null ou vazias
                        ArrayList<AlternativaBean> alts1 = questao1.getSelecionadas();
                        ArrayList<AlternativaBean> alts2 = questao2.getSelecionadas();
                        
                        if(alts1 == null || alts2 == null) {
                            System.err.println("ERRO: Lista de alternativas selecionadas é null para questão " + questao1.getID());
                            existe = false;
                            break;
                        }
                        
                        if(alts1.size() != alts2.size()) {
                            existe = false;
                            break;
                        }
                        
                        // CORREÇÃO 11: Comparar alternativas corretamente (ID da alternativa com ID da alternativa)
                        for(int f = 0 ; f < alts1.size() ; f++){
                            if(alts1.get(f) == null || alts2.get(f) == null) {
                                System.err.println("ERRO: Alternativa null encontrada na posição " + f + " da questão " + questao1.getID());
                                existe = false;
                                break;
                            }
                            if(alts1.get(f).getID() != alts2.get(f).getID()){
                                existe = false;
                                break;
                            }
                        }
                    }
                }
                if(existe == false)
                    break;
            }
        }else{
            existe = false;
        }
        return existe;
    }
    private void generateFiles(String path, ArrayList<PermutacaoBean> provas){
        ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
        int contador = 1;
        try{
            PdfWriter writer2 = new PdfWriter(path + "/" + rb.getString("Exams") + "/" + rb.getString("Answer") + ".pdf");
            PdfDocument doc2 = new PdfDocument(writer2);
            Document temp2 = new Document(doc2, PageSize.A4);
            for(PermutacaoBean prova : provas){
                PdfWriter writer = new PdfWriter(path + "/" + rb.getString("Exams") + "/" + rb.getString("exam") + contador + ".pdf");
                PdfDocument doc = new PdfDocument(writer);
                Document temp = new Document(doc, PageSize.A4);
                HeaderHandler hh = new HeaderHandler();
                hh.setInfo(rb.getString("Exam") + " n." + contador);
                doc.addEventHandler(PdfDocumentEvent.START_PAGE, hh);
                PdfFont font = null;
                font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                //Instituição
                Paragraph inst = new Paragraph(this.prova.getInst()).setFont(font)
                        .setFontSize(this.prova.getTamanho()+2)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setWidthPercent(100)
                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER);

                //Curso e Data
                Paragraph cursodata = new Paragraph(rb.getString("Course") + ": " + this.prova.getCurso()).setFont(font)
                        .setFontSize(this.prova.getTamanho());
                cursodata.add(new Tab());
                cursodata.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
                SimpleDateFormat datef = new SimpleDateFormat("dd/MM/YYYY");
                cursodata.add(rb.getString("Date") + ": " + datef.format(this.prova.getData())).setFont(font)
                        .setFontSize(this.prova.getTamanho());
                cursodata.setMarginTop(0.1f);
                cursodata.setMarginBottom(0.1f);
                //Materia e Turma
                MateriaDAO mdao = new MateriaDAO();
                MateriaBean mbean = new MateriaBean();
                mbean.setID(this.prova.getMateria_id());
                Paragraph materiaturma = new Paragraph(rb.getString("Subject") + ": " + mdao.Get_One(mbean).getNome()).setFont(font)
                        .setFontSize(this.prova.getTamanho());
                materiaturma.add(new Tab());
                materiaturma.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
                materiaturma.add(rb.getString("Class") + ": " + this.prova.getTurma()).setFont(font)
                        .setFontSize(this.prova.getTamanho());
                materiaturma.setMarginTop(0.1f);
                materiaturma.setMarginBottom(0.1f);
                //Professor e Valor
                Paragraph profval = new Paragraph(rb.getString("Teacher") + ": " + this.prova.getProf()).setFont(font)
                        .setFontSize(this.prova.getTamanho());
                profval.add(new Tab());
                profval.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
                profval.add(rb.getString("Max Score") + ": " + this.prova.getNota()).setFont(font)
                        .setFontSize(this.prova.getTamanho());
                profval.setMarginTop(0.1f);
                profval.setMarginBottom(0.1f);
                //Nome Aluno e Nota
                Paragraph alunonota = new Paragraph(rb.getString("Student") + ":").setFont(font)
                        .setFontSize(this.prova.getTamanho());
                alunonota.add("__________________________________________________").setFont(font).setFontSize(12);
                alunonota.add(new Tab());
                alunonota.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
                alunonota.add(rb.getString("Score") + ":______").setFont(font)
                        .setFontSize(this.prova.getTamanho());
                alunonota.setMarginTop(0.1f);
                alunonota.setMarginBottom(0.1f);
                //Nota
                Paragraph nota = new Paragraph(rb.getString("Score") + ": " + this.prova.getNota()).setFont(font)
                        .setFontSize(this.prova.getTamanho())
                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT);
                //Cabeçalho
                temp.add(inst);
                temp.add(cursodata);
                temp.add(materiaturma);
                temp.add(profval);
                temp.add(alunonota);
                int count = 1;
                for(int y = 0 ; y < prova.getQuestoes().size() ; y++){
                    if(prova.getQuestao(y) instanceof QuestaoFechadaBean){
                        QuestaoFechadaBean questFec = ((QuestaoFechadaBean)prova.getQuestao(y));
                        Paragraph questfec = new Paragraph(count+"- "+questFec.getEnunciado()).setFont(font).setFontSize(this.prova.getTamanho())
                                .setHorizontalAlignment(HorizontalAlignment.LEFT).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                        temp.add(questfec);
                        int num_alt = 0;
                        switch(this.prova.getTipo()){
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
                        ArrayList<AlternativaBean> alters = questFec.getSelecionadas();
                        int qtd = alters.size();
                        int numeracao = 0;
                        String alt_list = "abcde";
                        for(int t = 0 ; t < qtd ; t++){
                            Paragraph alts = new Paragraph().setFont(font).setFontSize(this.prova.getTamanho()-1).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                            alts.add(alt_list.charAt(numeracao++)+") "+alters.get(t).getTexto()+"  ");
                            alts.setMarginTop(0f);
                            alts.setMarginBottom(0f);
                            temp.add(alts);
                        }
                        count++;
                    }else{
                        QuestaoAbertaBean questAbt = ((QuestaoAbertaBean)prova.getQuestao(y));
                        Paragraph questabt = new Paragraph(count+"- "+questAbt.getEnunciado()).setFont(font).setFontSize(this.prova.getTamanho())
                                .setHorizontalAlignment(HorizontalAlignment.LEFT).setWidthPercent(90f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.JUSTIFIED);
                        temp.add(questabt);
                        for(int l = 0; l < questAbt.getNumLinhas(); l++){
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
                //Gabarito
                font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                count = 1;
                String alt_list = "ABCDE";
                Paragraph linha = new Paragraph().setFont(font).setFontSize(this.prova.getTamanho()-2).setWidthPercent(95f).setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT);
                linha.setMarginTop(0.3f);
                linha.setMarginBottom(0.3f);
                linha.add(rb.getString("Version") + " " + contador + ": ");
                for(Questao a : prova.getQuestoes()){
                    if(a instanceof QuestaoFechadaBean){
                        ArrayList<AlternativaBean> alternas = ((QuestaoFechadaBean)a).getSelecionadas();
                        for(int y =0 ; y < alternas.size(); y++){
                            if(alternas.get(y).isCerta()){
                                linha.add(count+"- "+alt_list.charAt(y)+"  ");
                                break;
                            }
                        }
                    }else{
                        linha.add(count+"- " + rb.getString("Open") + "(" + ((QuestaoAbertaBean)a).getID() + ")  ");
                    }
                    count++;
                }
                temp2.add(linha);
                contador++;
            }
            temp2.close();
        }catch(IOException e){
            MessageDialogFXMLController a = new MessageDialogFXMLController(rb.getString("Error"), e.getMessage(), null);
            a.showAndWait();
        }
    }
    private void cancelarClick(ActionEvent evt){
        this.hide();
    }
}
