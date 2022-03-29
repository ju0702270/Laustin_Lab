package controller;

import Main.Main;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Model;
import org.xml.sax.SAXException;
import utility.Export;
import utility.Store;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static utility.DialogBox.xlsFileSaver;
import static utility.DialogBox.xmlFileSaver;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 23/02/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe controller du contenu principal de l'application
 */
public class MainContent  extends MasterControl {


    @FXML
    protected TableView mainTableView;
    private double xOffset;
    private double yOffset;

    /**
     * Constructeur de MainContent
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     * @param model : le model pour les Controlleur enfant qui en auront besoin
     */
    public MainContent(Stage primaryStage, Model model) {
        super(primaryStage);
        this.model = model;
    }
    /**
     * Constructeur de MainContent
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public MainContent(Stage primaryStage) {
        super(primaryStage);
    }

    /**
     * Méthode de création d'un fenetre pop-up
     * Cette fenetre est un objet Stage
     * ce stage sera une Fenetre Modal c'est à dire qu'il empechera toutes les interactions avec les autres fenetres de l'application
     * @throws IOException
     * @return Stage : la fenetre à affichicher (il ne reste qu'a utiliser la méthode show()
     */
    @FXML
    protected Stage createPopUp(String fxml, MainContent controlleur) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/"+ fxml +".fxml"),Store.bundle);
        loader.setController(controlleur);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("img/icon.png"));
        Scene sc = new Scene(root);
        stage.setScene(sc);
        stage.setTitle(fxml.replace("popUp",""));
        /* comme le stage est en style TRANSPARENT, impossible de déplacer la fenetre si l'utilisateur le fait. Les fonctions suivante pallie à ce problème */
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - yOffset);
            }
        });
        return stage;
    }// fin create

    /**
     * Méthode de changement de langue pour le controller MainContent
     * Cette méthode va modifier la langue complète de l'application
     * les fichiers Resource Bundle contiennent les différents textes d'internationnalisation
     * @param e : l'évènement ciblé, défini donc quel langue sera appliquée
     *              Attention : e doit se trouver dans btnLang et chaque Item de btnLang doit être nommé comme la ressource Bundle associée (ex : en_US, fr_BE,...)
     * @param e
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     */
    @FXML
    public void changeLang(Event e) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        for ( MenuItem mi : menuLang.getItems() ) {
            if ( mi.equals(e.getTarget()) ) {
                Store.setLocale( mi.getText() );
                this.stage.setScene(sceneCreator("mainContent", new MainContent(stage,this.model)));
                continue;
            }
        }
    }//end changeLang

    /**
     * Méthode d'ouverture d'un stage affichant certain renseignement sur l'application
     * @throws IOException
     */
    @FXML
    public void showVersion() throws IOException {
        if (!deployVer){
            deployVer = true;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/version.fxml"));
            Parent root = loader.load();
            Stage verStage = new Stage();
            verStage.setScene(new Scene(root));
            verStage.setResizable(false);
            verStage.setTitle("Version");
            verStage.getIcons().add(new Image("img/icon.png"));
            verStage.setOnCloseRequest(e -> {
                deployVer = false;
            });
            verStage.show();
        }
    }

    /**
     * Méthode pour l'ouverture de la vue Utilisateur
     */
    @FXML
    public void openUtilisateurManagement(){
        loadNodeContent("UtilisateurView",new UtilisateurController(stage,this.model));
    }
    /**
     * Méthode pour l'ouverture de la vue Medicament
     */
    @FXML
    public void openMedicamentManagement(){
        loadNodeContent("MedicamentConcentrationView", new MedicamentConcentrationController(stage));
    }
    /**
     * Méthode pour l'ouverture de la vue Client
     */
    @FXML
    public void openClientManagement(){
        loadNodeContent("ClientView", new ClientController(stage,this.model));
    }

    /**
     * Méthode pour l'ouverture de la vue Facutre
     */
    @FXML
    public void openFactureManagement(){
        loadNodeContent("factureView", new FactureController(stage,this.model));
    }



    /**
     * Méthode d'exportation en excel ou en xml de mainTableView
     * !!! implique que chaque Controlleur voulant utiliser cette méthode doit avoir un TableView nommé mainTableView
     * à l'emplacement fichier choisi par l'utilisateur
     * @param e
     * @throws IOException
     */
    @FXML
    private void export(Event e) throws IOException {
        File file;
        Export export = new Export();
        if ( ((MenuItem)e.getTarget()).getText().equals("excel") ) {
            file= xlsFileSaver(this.stage);
            if (file != null) {
                export.setFullFile(file.toString());
                export.excel(mainTableView);
            }
        }else{
            file= xmlFileSaver(this.stage);
            if (file != null) {
                export.setFullFile(file.toString());
                export.xml(mainTableView);
            }
        }
    }// fin exportFact

    /**
     * Méthode de fermeture du Stage courant
     * @param e
     */
    @FXML
    public void closeStage(Event e){
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }// fin closeStage



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openMedicamentManagement();
    }
}
