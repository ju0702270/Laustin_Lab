package controller;


import DAO.DAOFactory;
import DAO.UtilisateurDAO;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import exceptions.DBException;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Utilisateur;
import org.xml.sax.SAXException;
import utility.DialogBox;
import utility.Store;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static utility.Utility.getHash;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe controller du login de l'utilisateur
 */
public class Login extends MasterControl {

    private UtilisateurDAO userDAO;

    @FXML private Menu menuLang;
    @FXML public JFXTextField textfield_pseudo;
    @FXML public JFXPasswordField passwordfield_password;
    @FXML private Label errorLabel;

    /**
     * Constructeur Login
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public Login(Stage primaryStage) {
        super(primaryStage);
        try {
            this.userDAO = DAOFactory.getUtilisateurDAO();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("DBException.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin Constructeur

    /**
     * Méthode de changement de langue pour le controller Login
     * Cette méthode va modifier la langue complète de l'application
     * les fichiers Resource Bundle contiennent les différents textes d'internationnalisation
     * @param e : l'évènement ciblé, défini donc quel langue sera appliquée
     *              Attention : e doit se trouver dans btnLang et chaque Item de btnLang doit être nommé comme la ressource Bundle associée (ex : en_US, fr_BE,...)
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     */
    @FXML
    private void changeLang(Event e) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        for ( MenuItem mi : btnLang.getItems() ) {
            if ( mi.equals(e.getTarget()) ) {
                Store.setLocale( mi.getText() );
                MasterControl master = new MasterControl(stage);
                this.stage.setScene(sceneCreator("master", master));
                this.stage.getScene().setFill(Color.TRANSPARENT);
                continue;
            }
        }
    }//end changeLang

    /**
     * Méthode de connection de l'utilisateur à l'application. Cette méthode va vérifier dans la base de donnée si l'utilisateur existe
     * et si son mot de passe est correct
     * @throws DBException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @FXML
    public void connectUtilisateur() throws IOException, NoSuchAlgorithmException {
        boolean errorIsVisible = false;
        try {
            for (Utilisateur u: userDAO.findAll() ) {
                /*  Validation */
                if ( u.getLogin().equals(this.textfield_pseudo.getText()) && u.getPassword().equals(getHash(this.passwordfield_password.getText())) ){
                    errorIsVisible = false;
                    /* attribution de l'utilisateur courant dans le model */
                    this.model.setCurrentUser(u);
                    this.openMainScene(this.model);
                    continue;
                }else{
                    errorIsVisible = true;
                }
            }
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notFound.content"));
        }
        this.errorLabel.setVisible(errorIsVisible);
    }//fin connectUtilisateur

    /**
     * Méthode d'ouverture de la vue inscription.fxml
     */
    @FXML
    public void openInscription(){
        Inscription inscription = new Inscription(stage);
        loadNodeContent("inscription",inscription);
    }// fin openInscription

    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }
}