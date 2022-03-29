package controller;

import DAO.ClientDAO;
import DAO.DAOFactory;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.component.AutoCompleteBox;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import model.Client;
import model.Model;
import model.Ville;
import utility.DialogBox;
import utility.Store;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 14/03/2021
 *     Modification : 15/03/2021
 *     Revision : 0.9
 *     Description : Classe controller pour la vue view.ClientController.fxml et view.popUpClient.fxml
 */
public class ClientController extends MainContent{

    private ClientDAO clientDAO;
    @FXML
    private TableColumn tableColumnId,tableColumnName,tableColumnFirstName,tableColumnDenom,tableColumnTva,
            tableColumnRue,tableColumnNum,tableColumnVille,tableColumnTel,tableColumnMail;
    @FXML
    private TableView<Client> mainTableView;
    @FXML
    private JFXTextField tfName,tfFirstName,tfDenom,tfNumTva,tfRue,tfNumero,tfTelephone,tfMail;
    @FXML
    private JFXComboBox<Ville> comboVille;

    /**
     * Constructeur de ClientController
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     * @param model : le model de application
     */
    public ClientController(Stage primaryStage, Model model) {
        super(primaryStage, model);
        this.clientDAO= DAOFactory.getClientDAO();
    }


    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * Cette méthode va lancer showAll() afin d'afficher tous les clients au moment ou le controlleur est initialisé
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAll();
    }//end initialize

    /**
     * Méthode pour afficher dans mainTableView tous les clients repris dans la base de données
     */
    @FXML
    private void showAll() {
        ArrayList<Client> lstCLient = null;
        try {
            lstCLient= clientDAO.findAll();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("ClientDAO.DialogBoxErr.notFound.content"));
        }
        /* liaison entre les attributs de Client et les TableColumn */
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tableColumnDenom.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        tableColumnTva.setCellValueFactory(new PropertyValueFactory<>("numTva"));
        tableColumnRue.setCellValueFactory(new PropertyValueFactory<>("rue"));
        tableColumnNum.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        tableColumnTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        tableColumnMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        /* attribution de tous les clients dans le mainTableView */
        mainTableView.setItems( FXCollections.observableArrayList(lstCLient) );
    }// fin showAll()

    /**
     * Méthode de creation et affichage de la vue view.popUpClient.fxml
     * @throws IOException
     */
    @FXML
    private void create() throws IOException {
        Stage stage = createPopUp("popUpClient",this);
        /* init de toutes les Villes dans comboVille */
        try {
            comboVille.setItems( FXCollections.observableArrayList( DAOFactory.getVilleDAO().findAll() ) );
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notCreate.title"),
                    Store.bundle.getString("DBException.Message"));
        }
        new AutoCompleteBox( comboVille );// permet de faire de l'autocompletion sur le comboVille
        stage.show();
    }// fin create()

    /**
     * Méthode de confirmation de la création d'un Client dans la base de données
     * @param e : Récupération de l'évènement.
     * @throws DBException
     */
    @FXML
    private void confirmClient(Event e) throws  DBException {
        ObservableList<Ville> list = FXCollections.observableArrayList();
        Pattern mail = Pattern.compile("[a-zA-Z0-9-]{1,}@([a-zA-Z\\.])?[a-zA-Z]{1,}\\.[a-zA-Z]{1,4}");
        Pattern number = Pattern.compile("^[0-9]+$");
        try{
            /* reconvertion de comboVille en objet Ville car rendre éditable un ComboBox change ses items en chaine de caractère */
            for (Ville v : comboVille.getItems()) {
                String s = comboVille.getEditor().getText();
                if (v.toString().toLowerCase().contains(s.toLowerCase())) {
                    list.add(v);
                }
            }
            comboVille.setItems(list);
            comboVille.setEditable(false); // retrait de l'édition sur comboVille afin de ne pas récuperer une Ville
            comboVille.getSelectionModel().select(0); // selection de la valeur choisie par l'utilisateur
            /* vérification de la validité du mail */
            if (!tfMail.getText().equals("") && !mail.matcher(tfMail.getText()).find()) {
                DialogBox.info("Info", Store.bundle.getString("view.errormail"));
            /*  Vérification de la validié du numéro de téléphone */
            }else if ( !tfTelephone.getText().equals("") && !number.matcher(tfTelephone.getText()).find() ){
                DialogBox.info("Info", Store.bundle.getString("view.errornumero"));
            /* Vérification que tout les champs indispensable sont remplis */
            } else if (!tfDenom.getText().equals("") && !tfNumTva.getText().equals("") && !tfRue.getText().equals("")
                    && !tfNumero.getText().equals("")
                    && comboVille.getValue() != null
                    && !tfTelephone.getText().equals("") && !tfMail.getText().equals("")) {
                try {
                    /* creation du client dans la base de données */
                    if (clientDAO.create(new Client(-1, tfName.getText(), tfFirstName.getText(),
                            tfDenom.getText(), tfNumTva.getText(), tfRue.getText(),
                            tfNumero.getText(), comboVille.getValue(), tfTelephone.getText(), tfMail.getText()) )) {
                        showAll();
                        closeStage(e);
                    } else {
                        DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notCreate.title"),
                                Store.bundle.getString("ClientDAO.DialogBoxErr.notCreate.content"));
                    }//end if else
                } catch (DBException throwables) {
                    DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("DBException.Message"));
                } catch (ConstraintException throwables) {
                    DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("ClientDAO.DialogBoxErr.notCreated.constraintViolation.content"));
                }
            } else {
                /* Reset de comboVille */
                comboVille.setEditable(true);
                comboVille.setItems( FXCollections.observableArrayList(DAOFactory.getVilleDAO().findAll()) );
                DialogBox.info("Info", Store.bundle.getString("view.toutremplir"));
            }
        }catch (ClassCastException exception){
            /* Reset de comboVille */
            comboVille.setEditable(true);
            comboVille.setItems( FXCollections.observableArrayList(DAOFactory.getVilleDAO().findAll()) );
            /* Message d'erreur */
            DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notCreate.title"),
                    Store.bundle.getString("ville.Exception"));
        }
    }// fin confirmClient

    /**
     * Méthode donnant la possibilité à l'utilisateur d'éditer et mettre à jours les données Client dans mainTableView
     * Cette méthode permet à l'utilisateur de faire un double-clic sur un attribut d'un client pour pouvoir modifier
     * cette attribut. Pour comfirmer la modification, il faut cliquer sur Enter
     * @throws DBException
     */
    @FXML
    private void update() throws DBException {
        mainTableView.setEditable(true);
        mainTableView.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnDenom.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnTva.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnRue.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnNum.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnVille.setCellFactory(ComboBoxTableCell.forTableColumn( FXCollections.observableArrayList(DAOFactory.getVilleDAO().findAll()) ));
        tableColumnTel.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnMail.setCellFactory(TextFieldTableCell.forTableColumn());
        editingColumn(tableColumnName);
        editingColumn(tableColumnFirstName);
        editingColumn(tableColumnDenom);
        editingColumn(tableColumnTva);
        editingColumn(tableColumnRue);
        editingColumn(tableColumnNum);
        editingColumn(tableColumnVille);
        editingColumn(tableColumnTel);
        editingColumn(tableColumnMail);
    }// fin update

    /**
     * Méthode d'attibution d'instruction pour un TableColumn
     * cette méthode permet de faire la modification du Client dans la base de données
     * @param column : la colonne à editer
     */
    private void editingColumn(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            Client client = (Client) e.getTableView().getItems().get(e.getTablePosition().getRow());
            switch (column.getId()) {
                case "tableColumnName" -> client.setNom( e.getNewValue().toString() );
                case "tableColumnFirstName" -> client.setPrenom( e.getNewValue().toString() );
                case "tableColumnDenom" -> client.setDenomination( e.getNewValue().toString() );
                case "tableColumnTva" -> client.setNumTva( e.getNewValue().toString() );
                case "tableColumnRue" -> client.setRue( e.getNewValue().toString() );
                case "tableColumnNum" -> client.setNumero( e.getNewValue().toString() );
                case "tableColumnVille" -> client.setVille( (Ville) e.getNewValue() );
                case "tableColumnTel" -> client.setTelephone( e.getNewValue().toString() );
                case "tableColumnMail" -> {
                    Pattern mail = Pattern.compile("[a-zA-Z0-9-]{1,}@([a-zA-Z\\.])?[a-zA-Z]{1,}\\.[a-zA-Z]{1,4}");
                    if ( !mail.matcher(e.getNewValue().toString()).find()) {
                        DialogBox.info("Info", Store.bundle.getString("view.errormail"));
                    }else {

                        client.setEmail( e.getNewValue().toString() );
                    }
                }
            }//fin switch
            try {
                if (!this.clientDAO.update(client) ) {
                    DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.title")
                            , Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.content"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
            showAll();
        });
    }// fin editingColumn

    /**
     * Méthode de suppression d'un Client de la base de données
     */
    @FXML
    private void delete() {
        Client client = mainTableView.getSelectionModel().getSelectedItem();
        try {
            /* Si l'utilisateur ne selectionne pas de client dans mainTableView */
            if (client != null) {
                if (DialogBox.confirm(Store.bundle.getString("Client.DialogBox.sureToDelete.title"),
                        Store.bundle.getString("Client.DialogBox.sureToDelete"))) {
                    if (clientDAO.delete(client)) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("ClientDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            } else {
                DialogBox.info("Info", Store.bundle.getString("Client.need"));
            }

        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("ClientDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("ClientDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin delete
}
