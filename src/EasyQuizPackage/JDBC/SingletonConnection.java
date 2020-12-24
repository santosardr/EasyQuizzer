package EasyQuizPackage.JDBC;

import EasyQuizPackage.Controllers.ChooseIdiomFXMLController;
import EasyQuizPackage.Controllers.MessageDialogFXMLController;
import EasyQuizPackage.Controllers.SpinnerDialogFXMLController;
import EasyQuizPackage.Controllers.SubControllers.DBConfigDiagFXMLController;
import EasyQuizPackage.Controllers.SubControllers.DBPasswordDiagFXMLController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import org.postgresql.util.PSQLException;

public class SingletonConnection {

    private volatile static Connection instancia;
    
    public static String endereco_adm = "localhost:5432/postgres";
    public static String user_adm = "postgres";
    public static String pass_adm = "postgres";
    
    public static String endereco = "localhost:5432/easyquizzerdb";
    public static String user = "easyquizzer";
    public static String pass = "easyquizzer";
    
    public static String database = "easyquizzerdb";
    
    private SingletonConnection() {
    }
    
    public static void updateEndereco() {
        String[] end = endereco_adm.split("/");
        endereco = end[0] + "/" + database;
    }

    public static Connection GetInstance() {
        try {
            if (SingletonConnection.instancia == null || SingletonConnection.instancia.isClosed()) {
                while (true) {
                    synchronized (SingletonConnection.class) {
                        if (SingletonConnection.instancia == null || SingletonConnection.instancia.isClosed()) {
                            try {
                                //1) Tenta fazer uma conexão com o banco do sistema
                                SingletonConnection.instancia = DriverManager.getConnection("jdbc:postgresql://" + endereco, user, pass);
                                SingletonConnection.instancia.setAutoCommit(false);
                                
                                //Verifica se o BD esta atualizado e atualiza se necessário
                                checkDBversion();
                                
                                //2) Se a conexão foi bem sucedida não mais o que fazer. Entretanto, ...
                                break;
                            } catch (PSQLException ps) {
                                //3) Se a conexão foi mal sucedida tentaremos criar o banco de dados do sistema.
                                //   Para esse propósito necessita dos dados do usuário administrador do banco
                                //   Vamos considerar que a porta default e o usuário estão padronizados. Por isso
                                //   é solicitada apenas a senha do usuário administrador.
                                
                                // Selecionando idioma para a criação do BD
                                ChooseIdiomFXMLController ci = new ChooseIdiomFXMLController(null);
                                ci.showAndWait();
                                String idiom = ChooseIdiomFXMLController.getIdiom();
                                ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", new Locale(idiom));
                                try {
                                    //3) Solicita a senha do usuário administrador até conseguir conexão ou atingir 4 tentativas.
                                    int tentativas = 0;
                                    while (tentativas < 4 && (SingletonConnection.instancia == null || SingletonConnection.instancia.isClosed())) {
                                        try {
                                            //Uma vez obtida a senha do usuário administrador testa uma nova conexão
                                            tentativas++;
                                            SingletonConnection.instancia = DriverManager.getConnection("jdbc:postgresql://" + endereco_adm, user_adm, pass_adm);
                                        } catch (SQLException e) {
                                            //Se a conexão do usuário administrador não foi bem sucedida então oferece 3 chances ao usuário
                                            //do sistema a possibilidade de modificar as configurações padronizadas para tentar uma conexão
                                            if(tentativas == 4)
                                                System.exit(1);
                                            
                                            DBConfigDiagFXMLController dbd = new DBConfigDiagFXMLController(endereco_adm, user_adm, pass_adm, rb, null);
                                            dbd.showAndWait();
                                        }
                                    }
                                    
                                    //Se saiu do loop acima então uma conexão com o usuário administrador foi bem sucedida.
                                    //Parte para a criação do banco de dados: usuário e banco
                                    try{
                                        // Verifica se o usuário já existe
                                        SingletonConnection.instancia.createStatement();
                                        PreparedStatement stmt = SingletonConnection.instancia.prepareStatement("SELECT 1 FROM pg_roles WHERE rolname='" + user + "'");
                                        ResultSet rs = stmt.executeQuery();
                                        
                                        // Se o usuário não existe, cria
                                        if(!rs.next()){
                                            try {
                                                SingletonConnection.instancia.createStatement();
                                                PreparedStatement stmt_createUser = SingletonConnection.instancia.prepareStatement("CREATE ROLE " + user + " PASSWORD '" + pass + "' NOSUPERUSER CREATEDB NOCREATEROLE INHERIT LOGIN");
                                                stmt_createUser.execute();
                                            } catch (SQLException exp) {
                                                MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Create db user error") + ": " + exp.getMessage() + " " + exp.getSQLState(), rb, null);
                                                f.showAndWait();
                                                System.exit(-1);
                                            }
                                        }
                                    } catch (SQLException exp) {
                                        MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Access db error") + ": " + exp.getMessage() + " " + exp.getSQLState(), rb, null);
                                        f.showAndWait();
                                    }
                                    
                                    //Se saiu da verificação acima então o usuário existe.
                                    //Parte para a criação do banco de dados
                                    try{
                                        // Verifica se o banco já existe
                                        SingletonConnection.instancia.createStatement();
                                        PreparedStatement stmt = SingletonConnection.instancia.prepareStatement("SELECT 1 FROM pg_database WHERE datname='" + database + "'");
                                        ResultSet rs = stmt.executeQuery();
                                        
                                        // Se o banco não existe...
                                        if(!rs.next()){
                                            SpinnerDialogFXMLController spinner = new SpinnerDialogFXMLController(rb.getString("Database"), rb.getString("Creating database"), rb, null);
                                            Task<Boolean> tarefaCargaPg = new Task<Boolean>() {
                                                // Cria o banco e as tabelas
                                                @Override
                                                protected Boolean call() throws Exception {
                                                    return SingletonConnection.CreateDatabase(rb);
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
                                            t.join();
                                            // Para o auto-commit
                                            SingletonConnection.instancia.setAutoCommit(false);
                                            SingletonConnection.setIdiom(idiom);
                                            MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Success creating db"), rb, null);
                                            f.showAndWait();
                                        }
                                    } catch (SQLException exp) {
                                        MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Access db error") + ": " + exp.getMessage() + " " + exp.getSQLState(), rb, null);
                                        f.showAndWait();
                                    }
                                    
                                    // Usuário e banco ok, loga no easyquizzer e encerra o loop. Em caso de erro encerra o programa
                                    try {
                                        SingletonConnection.instancia = DriverManager.getConnection("jdbc:postgresql://" + endereco, user, pass);
                                        SingletonConnection.instancia.setAutoCommit(false);
                                    } catch (PSQLException pse) {
                                        System.exit(1);
                                    }
                                    break;
                                } catch (SQLException e) {
                                    MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Error") + ": " + e.getMessage(), rb, null);
                                    f.showAndWait();
                                    System.exit(-1);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(SingletonConnection.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            MessageDialogFXMLController f = new MessageDialogFXMLController("Database", "Error: " + e.getMessage(), null, null);
            f.showAndWait();
            System.exit(-1);
        }
        return SingletonConnection.instancia;
    }
    
    // Cria o Banco de Dados
    private static boolean CreateDatabase(ResourceBundle rb) {
        String statement = "";
        try {
            // Arquivo com o código SQL para criar as tabelas
            BufferedReader reader = new BufferedReader(new InputStreamReader(SingletonConnection.class.getResourceAsStream("/Resources/DatabaseCreator.sql")));
            String aux;
            // Cria o BD
            SingletonConnection.instancia.createStatement();
            PreparedStatement stmt = SingletonConnection.instancia.prepareStatement("CREATE DATABASE " + database + " WITH OWNER = " + user + " ENCODING = 'UTF8' TABLESPACE = pg_default CONNECTION LIMIT = -1;");
            stmt.execute();
            // Acessa pelo usuario easyquizzer
            SingletonConnection.instancia = DriverManager.getConnection("jdbc:postgresql://" + endereco, user, pass);
            // Copia o código de criação das tabelas
            while ((aux = reader.readLine()) != null) {
                statement = statement.concat(aux);
            }
            // Cria as tabelas
            SingletonConnection.instancia.createStatement();
            stmt = SingletonConnection.instancia.prepareStatement(statement);
            return stmt.execute();
        } catch (FileNotFoundException e) {
            MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Create db file error") + ": " + e.getMessage(), rb, null);
            f.showAndWait();
            System.exit(-1);
            return false;
        } catch (IOException e) {
            MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Create db full file error") + ": " + e.getMessage(), rb, null);
            f.showAndWait();
            System.exit(-1);
            return false;
        } catch (SQLException exp) {
            MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Create db error") + ": " + exp.getMessage() + " " + exp.getSQLState(), rb, null);
            f.showAndWait();
            System.exit(-1);
            return false;
        }
    }
    
    // Verifica se o BD esta atualizado, e atualiza se não estiver
    private static void checkDBversion(){
        try {
            SingletonConnection.instancia.createStatement();
            PreparedStatement stmt = SingletonConnection.instancia.prepareStatement("SELECT 1 from pg_tables where tableowner = 'easyquizzer' AND tablename = 'idioma'");
            ResultSet rs = stmt.executeQuery();
            // Se nao houver tabela de idiomas, o BD esta desatualizado
            if(!rs.next()){
                ChooseIdiomFXMLController ci = new ChooseIdiomFXMLController(null);
                ci.showAndWait();
                String idiom = ChooseIdiomFXMLController.getIdiom();
                ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", new Locale(idiom));
                // Cria tabela de idiomas
                String statement = "CREATE TABLE idioma(Codigo_Idioma VARCHAR(100) DEFAULT 'en' NOT NULL PRIMARY KEY); INSERT INTO idioma VALUES (DEFAULT);";
                SingletonConnection.instancia.createStatement();
                stmt = SingletonConnection.instancia.prepareStatement(statement);
                stmt.execute();
                SingletonConnection.instancia.commit();
                // Atualiza a tabela de provas
                statement = "ALTER TABLE prova ADD COLUMN nome_prova VARCHAR(100); UPDATE prova SET nome_prova = (CAST(id AS VarChar(100)));";
                SingletonConnection.instancia.createStatement();
                stmt = SingletonConnection.instancia.prepareStatement(statement);
                stmt.execute();
                SingletonConnection.instancia.commit();
                SingletonConnection.setIdiom(idiom);
            }
        } catch (SQLException exp) {
            MessageDialogFXMLController f = new MessageDialogFXMLController("Database", "Access database error" + ": " + exp.getMessage() + " " + exp.getSQLState(), null, null);
            f.showAndWait();
        }
    }
    
    // Edita o idioma atual do prorama, armazenando no BD
    public static void setIdiom(String idioma){
        try {
            SingletonConnection.instancia.createStatement();
            PreparedStatement stmt = SingletonConnection.instancia.prepareStatement("UPDATE idioma SET codigo_idioma = '" + idioma + "'");
            stmt.execute();
            SingletonConnection.instancia.commit();
        } catch (SQLException exp) {
            MessageDialogFXMLController f = new MessageDialogFXMLController("Database", "Access database error" + ": " + exp.getMessage() + " " + exp.getSQLState(), null, null);
            f.showAndWait();
        }
    }
    
    // Retorna o idioma atual do programa, armazenado no BD
    public static Locale getLanguage(){
        SingletonConnection.GetInstance();  // Testa/atualiza conexao ou cria o BD na primeira vez
        String idioma = "en";
        try {
            SingletonConnection.instancia.createStatement();
            PreparedStatement stmt = SingletonConnection.instancia.prepareStatement("SELECT * from idioma");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            idioma = rs.getString("codigo_idioma");
        } catch (SQLException exp) {
            //new MessageDialogFXMLController("Banco de Dados", "Erro ao Acessar Banco de Dados" + ": " + exp.getMessage() + " " + exp.getSQLState(), null).showAndWait();
            return (new Locale("en"));
        }
        return (new Locale(idioma));
    }
    
    // Verifica quantas linhas tem uma tabela
    private static int getRowQuantity(String table) {
        try {
            PreparedStatement stmt = instancia.prepareStatement("select count(*) from " + table);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException exp) {
            ResourceBundle rb = ResourceBundle.getBundle("Resources.Locales.messages", SingletonConnection.getLanguage());
            MessageDialogFXMLController f = new MessageDialogFXMLController(rb.getString("Database"), rb.getString("Access db error") + ": " + exp.getMessage() + " " + exp.getSQLState(), null);
            f.showAndWait();
        }
        return -1;
    }
    
    // Verifica se há questões abertas ou questões fechadas com alternativas suficientes para fazer uma prova
    public static boolean populatedDB() {
        return ((getRowQuantity("alternativa") > 3) || (getRowQuantity("questao_aberta") > 0));
    }
}
