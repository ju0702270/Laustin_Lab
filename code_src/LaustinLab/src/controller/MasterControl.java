package controller;

import DAO.DAOFactory;
import Main.Main;
import exceptions.DBException;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Model;
import utility.DialogBox;
import utility.Store;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe mère de tout les controlleurs de l'application
 */
public class MasterControl implements Initializable {

    protected Stage stage;
    protected Model model;
    @FXML public Menu menuLang;
    @FXML public MenuButton btnLang;
    @FXML public AnchorPane anchorMaster;
    protected boolean deployVer = false;
    @FXML private Button btnClose;

    /**
     * Constructeur de MasterControl
     * @param stage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public MasterControl(Stage stage) {
        this.stage= stage;
        this.model= new Model();
    }// fin Constructeur


    /**
     * Méthode de création d'une Scene
     * @param fxmlFile : String le nom du fichier FXML à charger ( ne pas mettre .fxml)
     * @param controller : MasterControl le controller de la vue.
     * @return Scene : la nouvelle scene avec la vue et le controller associé
     * @throws IOException
     */
    public Scene sceneCreator(String fxmlFile, MasterControl controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/"+fxmlFile+".fxml"),Store.bundle);
        loader.setController(controller);
        Parent root = loader.load();
        return new Scene(root);
    }// fin sceneCreator

    /**
     * Méthode de chargement d'une vue dans anchorMaster
     * Attention AnchorMaster doit être défini dans les fichiers FXML auquels vous souhaitez implémenter d'autre interface
     * @param fxmlView : String le nom du fichier FXML à charger ( ne pas mettre .fxml)
     * @param control : MasterControl le controller de la vue.
     */
    public void loadNodeContent(String fxmlView, MasterControl control){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/"+ fxmlView +".fxml"), Store.bundle);
        loader.setController(control);
        try {
            AnchorPane managePane = loader.load();
            AnchorPane.setLeftAnchor(managePane,0.0);
            AnchorPane.setBottomAnchor(managePane,0.0);
            AnchorPane.setRightAnchor(managePane,0.0);
            AnchorPane.setTopAnchor(managePane,0.0);
            anchorMaster.getChildren().clear();
            anchorMaster.getChildren().add(managePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// fin loadNodeContent

    /**
     * Méthode d'ouverture de la fenêtre loginView.fxml
     */
    @FXML
    public void openLogin(){
        Login login = new Login(this.stage);
        loadNodeContent("LoginView",login);
    }// fin openLogin

    /**
     * Métthode d'accès à la page principale de l'application => mainContent.fxml
     * @throws IOException
     */
    protected void openMainScene(Model model) throws IOException {
        Stage stg= new Stage();
        stg.initStyle(StageStyle.DECORATED);
        MainContent content = new MainContent(stg, model);
        stg.setScene(sceneCreator("mainContent", content));
        stg.setTitle(Store.appName);
        stg.getIcons().add(new Image("img/icon.png"));
        stg.setOnCloseRequest(e -> {
            try {
                DAOFactory.close(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                Platform.exit();
            }
        });
        stage.close();
        stg.show();
    }

    /**
     * Méthode de fermeture du Stage courant et fermeture de la connection à la base de données
     * @param e
     */
    @FXML
    public void closeStage(Event e) throws SQLException {
        try {
            DAOFactory.close(true);
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("DBException.title"),
                    Store.bundle.getString("DBException.Message"));
        }
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }// fin closeStage

    /**
     * Initialisation de FactureController
     * Cette méthode va lancer la fenetre de Login
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openLogin();
    }//fin initalize
}
