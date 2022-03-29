package controller;

import DAO.DAOFactory;
import DAO.UtilisateurDAO;
import com.jfoenix.controls.JFXButton;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;
import model.Utilisateur;
import utility.DialogBox;
import utility.Store;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static utility.Store.config;
import static utility.Utility.getHash;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 09/03/2021
 *     Modification : 15/03/2021
 *     Revision : 0.9
 *     Description : Classe controller pour la vue view.UtilisateurView.fxml et view.popUpUtilisateur.fxml
 */
public class UtilisateurController extends MainContent{

    private UtilisateurDAO userDAO;
    @FXML
    private TableView<Utilisateur> mainTableView;
    @FXML
    private TableColumn tableColumnId,tableColumnName,tableColumnFirstName,tableColumnLogin;
    @FXML
    private AnchorPane anchorOldPass;
    @FXML
    private TextField tfName,tfFirstName,tfLogin;
    @FXML
    private PasswordField pfOldPassword,pfNewPassword,pfConfirmPassword;
    @FXML
    private VBox vBoxPopUp;
    @FXML
    private JFXButton btnConfirm;

    /**
     * Constructeur de UtilisateurController
     * @param stage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     * @param model : le model de application
     */
    public UtilisateurController(Stage stage,Model model) {
        super(stage,model);
        try {
            this.userDAO = DAOFactory.getUtilisateurDAO();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("DBException.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin Constructeur


    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * Cette méthode va lancer showAll() afin d'afficher tous les Utilisateur au moment ou le controlleur est initialisé
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAll();
    }

    /**
     * Méthode d'affichage de tout les utilisateurs contenu dans la base de données dans mainTableView
     */
    private void showAll() {
        ArrayList<Utilisateur> lstUtilisateur = null;
        try {
            lstUtilisateur = userDAO.findAll();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notFound.content"));
        }
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tableColumnLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        /* attribution de tous les Utilisateurs dans mainTableView */
        mainTableView.setItems( FXCollections.observableArrayList(lstUtilisateur) );
    }// fin showAll
    /**
     * Méthode de création de la fenetre de création d'un utilisateur (view.popUpUtilisateur.fxml)
     * @throws IOException
     */
    @FXML
    private void create() throws IOException {
        Stage stage = createPopUp("popUpUtilisateur",this);
        /* retrait de anchorOldPass pour la popUpUtilisateur en mode création */
        for (Node n: vBoxPopUp.getChildren()) {
            AnchorPane anchorPane = (AnchorPane) n;
            if ( anchorPane.equals(anchorOldPass) ){
                vBoxPopUp.getChildren().remove(anchorPane);
                break;
            }
        }
        stage.show();
    }// fin create

    /**
     * Méthode qui vérifie la validité du formulaire de création de l'utilisateur grace aux expressions régulières
     * @return boolean
     *      true : si le formulaire est valide
     *      false : si le formulaire est invalide
     */
    private boolean checkValide(){
        Pattern name = Pattern.compile("^(([A-za-z]+[\\s]{1}[A-za-z]+)|([A-Za-z]+))$");
        Pattern login = Pattern.compile("^([A-Za-z0-9]){4,20}$");
        Pattern password = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        if ( !(name.matcher(tfName.getText()).find()
                && name.matcher(tfFirstName.getText()).find()
                && login.matcher(tfLogin.getText()).find()
                && password.matcher(pfNewPassword.getText()).find()) ){
            DialogBox.info("Info",Store.bundle.getString("view.usermanagement.noncorrectinfo"));
            return false;
        }
        if( !pfNewPassword.getText().equals(pfConfirmPassword.getText()) ){
            DialogBox.info("Info",Store.bundle.getString("view.inscription.errorcheckpassword"));
            return false;
        }
        return true;
    }// fin checkValide

    /**
     * Méthode de création d' un utilisateur dans la base de donnée
     * @param e : récuperation de l'évènement
     * @throws NoSuchAlgorithmException
     */
    @FXML
    private void createUtilisateur(ActionEvent e) throws NoSuchAlgorithmException {
        if (checkValide()) {
            Utilisateur utilisateur = new Utilisateur(-1,tfName.getText(),tfFirstName.getText(),
                    tfLogin.getText(),getHash(pfConfirmPassword.getText()));
            //creation de la facture dans la base de donnée
            try {
                if ( userDAO.create(utilisateur) ){
                    showAll();
                    closeStage(e);
                }else{//sinon affichage d'une erreur
                    DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.content")+"\n"+
                                    Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.samelogin"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
            }
        }// fin if, pas de else car checkValide affiche les erreurs en cas de formulaire non valide
    }// fin createUtilisateur

    /**
     * Methode permettant la mise à jour des données dans mainTableView
     * cette méthode n'ouvre pas de popUp mais permet la modification de certaines tableColumn
     */
    @FXML
    private void update(){
        mainTableView.setEditable(true);
        mainTableView.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        editingColumn(tableColumnFirstName);
        editingColumn(tableColumnName);
        editingColumn(tableColumnLogin);
    }//fin update

    /**
     * Méthode d'attibution d'instruction pour un TableColumn
     * cette méthode permet de faire la modification d'un Utilisateur dans la base de données
     * @param column : la colonne à editer
     */
    private void editingColumn(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            Utilisateur u = (Utilisateur) e.getTableView().getItems().get(e.getTablePosition().getRow());
            switch (column.getId()) {
                case "tableColumnName" -> u.setNom(e.getNewValue().toString());
                case "tableColumnFirstName" -> u.setPrenom(e.getNewValue().toString());
                case "tableColumnLogin" -> u.setLogin(e.getNewValue().toString());
            }
            try {
                if ( !userDAO.update(u) ){
                    DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.title")
                            ,Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
            showAll();
        });
    }// fin editingColumn

    /**
     * Méthode de suppression d'un utilisateur
     * Cette méthode fait la mise à jour de la DB et aussi la mise à jour de l'interface
     */
    @FXML
    private void delete() {
        Utilisateur utilisateur = mainTableView.getSelectionModel().getSelectedItem();
        try {
            if (utilisateur != null) {
                if (DialogBox.confirm(Store.bundle.getString("Utilisateur.DialogBox.sureToDelete.title"),
                        Store.bundle.getString("Utilisateur.DialogBox.sureToDelete"))) {
                    if (userDAO.delete(utilisateur)) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.content"));
                    }// fin if (creation utilisateur)
                } // fin if Dialog.confirm
            } else {
                DialogBox.info("Info", Store.bundle.getString("view.utilisateur.dialog.needutilisateur"));
            }// fin if (utilisateur != null)
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        }// fin try catch
        catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }
        showAll();
    }// fin delete

    /**
     * Méthode d'ouverture de la view.popUpUtilisateur.fxml
     * Cette méthode initialise certain Textfield dans le nouveau Stage.
     * Cette méthode modifie le onAction de btnConfirm afin de ne pas créér un nouvelle utilisateur mais de modifier l'utilisateur
     * @throws IOException
     */
    @FXML
    private void openUpdate() throws IOException {
        Utilisateur utilisateur = mainTableView.getSelectionModel().getSelectedItem();
        if (utilisateur != null) {
            // creation de popUpUtilisateur
            Stage stage = createPopUp("popUpUtilisateur",this);
            // intialisation
            tfName.setText(utilisateur.getNom());
            tfFirstName.setText(utilisateur.getPrenom());
            tfLogin.setText(utilisateur.getLogin());
            //Modification de onAction de btnConfirm
            btnConfirm.setOnAction(null);
            btnConfirm.setOnAction(actionEvent -> {
                Utilisateur updateUser = null; // nouvel Utilisateur afin de ne pas modifier utilisateur si la modification ne se fait pas
                try {
                    //vérification de l'ancien mot de passe afin de modifier, le mot de passe administrateur est aussi utilisable
                    if ( getHash(pfOldPassword.getText()).equals(utilisateur.getPassword()) ||
                            getHash(pfOldPassword.getText()).equals(config.get("admin")) ){
                        //Verification de la correspondance de pfNewPassword et pfConfirmPassword
                        if ( pfNewPassword.getText().equals(pfConfirmPassword.getText()) ){
                            try {
                                // creation d'un utilisateur dupliqué afin de ne pas modifier utilisateur en cas d'annulation ou de problème
                                updateUser = new Utilisateur(utilisateur.getId(),
                                        tfName.getText(),tfFirstName.getText(),tfLogin.getText(),
                                        getHash(pfConfirmPassword.getText()));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                        //vérification si l'update se fait dans la base de donnée
                        if ( userDAO.update(updateUser) ){
                            showAll();
                            closeStage(actionEvent);
                        }else{//sinon affichage d'une erreur
                            DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.title"),
                                    Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.nullPointer.content"));
                        }
                    }else{
                        DialogBox.info("Info", Store.bundle.getString("Utilisateur.DialogBox.oldPassNotCorrespond"));
                    }// fin if else vérification de l'ancien mot de passe afin de modifier

                } catch (ConstraintException throwables) {
                    DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.title"),
                            Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
                } catch (DBException throwables) {
                    DialogBox.error(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.title"),
                            Store.bundle.getString("DBException.Message"));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            });
            stage.show();
        }else{//Affichage d'un message pour spécifier qu'il faut selectionner un utilisateur
            DialogBox.info("info", Store.bundle.getString("view.utilisateur.dialog.needutilisateur"));
        }
    }//fin openUpdate
}
