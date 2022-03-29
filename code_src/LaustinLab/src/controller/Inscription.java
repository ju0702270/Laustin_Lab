package controller;

import DAO.DAOFactory;
import DAO.UtilisateurDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Utilisateur;
import utility.DialogBox;
import utility.Store;
import utility.Utility;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 23/02/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe controller permettant les interactions avec la vue view.inscription.fxml
 */
public class Inscription extends MasterControl {
    private UtilisateurDAO uDAO = null;
    @FXML
    private TextField tfName;
    @FXML private Label lblErrorName;
    @FXML
    private TextField tfFirstName;
    @FXML private Label lblErrorFirstName;
    @FXML
    private TextField tfLogin;
    @FXML private Label lblErrorLogin;
    @FXML
    private PasswordField pfPassword;
    @FXML private Label lblErrorPassword;
    @FXML
    private PasswordField pfCheckPassword;
    @FXML private Label lblErrorCheckPassword;

    /**
     * Constructeur de Inscription
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public Inscription(Stage primaryStage) {
        super(primaryStage);
        try {
            this.uDAO = DAOFactory.getUtilisateurDAO();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("DBException.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin Constructeur

    /**
     * Méthode pour rendre non visible les Label d'erreur de chaque entrée utilisateur
     */
    private void cleanLblError(){
        lblErrorName.setVisible(false);
        lblErrorFirstName.setVisible(false);
        lblErrorLogin.setVisible(false);
        lblErrorPassword.setVisible(false);
        lblErrorCheckPassword.setVisible(false);
    }// fin cleanLblError

    /**
     * Méthode qui vérifie la validité du formulaire d'inscription gràce aux expressions régulière
     * @return boolean
     *      true : si le formulaire est valide
     *      false : si le formulaire est invalide
     */
    private boolean checkValide(){
        boolean isValide = true;
        Pattern name = Pattern.compile("^(([A-za-z]+[\\s]{1}[A-za-z]+)|([A-Za-z]+))$");
        Pattern login = Pattern.compile("^([A-Za-z0-9]){4,20}$");
        Pattern password = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        Matcher m;
        cleanLblError();
        if ( !name.matcher(tfName.getText()).find() ) {
            lblErrorName.setVisible(true);
            isValide= false;
        }
        if( !name.matcher(tfFirstName.getText()).find() ){
            lblErrorFirstName.setVisible(true);
            isValide= false;
        }
        if ( !login.matcher(tfLogin.getText()).find() ){
            lblErrorLogin.setVisible(true);
            isValide= false;
        }
        if ( !password.matcher(pfPassword.getText()).find() ){
            lblErrorPassword.setVisible(true);
            isValide= false;
        }
        if( !pfPassword.getText().equals(pfCheckPassword.getText()) ){
            lblErrorCheckPassword.setVisible(true);
            isValide= false;
        }
        return isValide;
    }//fin checkValide

    /**
     * Méthode de Validation définitive de l'Utilisateur
     * Cette méthode vérifie si le formulaire est valide, ensuite elle tentera de créer l'utilisateur dans la Base de données
     * Si l'utilisateur n'est pas créé dans la base de donnée, un message d'erreur sera affiché
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws DBException
     */
    @FXML
    private void validate() throws IOException, NoSuchAlgorithmException{
        if(checkValide()){
            Utilisateur u = new Utilisateur(-1,tfName.getText(), tfFirstName.getText(),tfLogin.getText(),
                    Utility.getHash(pfPassword.getText()));
            try {
                if ( !uDAO.create(u) ){
                    DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.content")+"\n" +
                                    Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.samelogin"));
                }else{
                    /* attribution de l'utilisateur courant et ouverture de la vue principal */
                    ArrayList<Utilisateur> lstU = uDAO.findAll();
                    u = lstU.get(lstU.size()-1);
                    this.model.setCurrentUser(u);
                    openMainScene(this.model);
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.samelogin"));
            }
        }
    }//fin validate

    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
}
