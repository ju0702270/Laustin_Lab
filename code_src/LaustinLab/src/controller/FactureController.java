package controller;

import DAO.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import controller.component.DateTableCell;
import controller.modelController.FactureMC;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import utility.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import static utility.DialogBox.xlsFileSaver;
import static utility.Utility.dblFormat0_00;
import static utility.Utility.getTotalFact;

/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 07/03/2021
 *     Modification : 15/03/2021
 *     Revision : 0.9
 *     Description : Classe controlleur de la vue view.factureView.fxml
 *                   cette classe gère les factures et les factures avec lignes factures
 */
public class FactureController extends MainContent {


    private LigneFactureDAO liFacDAO;
    private FactureDAO factureDAO;
    @FXML
    private AnchorPane anchorContainerLot;
    @FXML
    private Label lblTotal,lblTotalCreate,lblTotalAdd,lblNumFact;
    @FXML
    private JFXComboBox<Facture> comboFacture;
    @FXML
    private JFXComboBox<Utilisateur> comboUtilsateur;
    @FXML
    private JFXComboBox<Client> comboClient;
    @FXML
    private  JFXComboBox<Client> comboEntreprise;
    @FXML
    private JFXDatePicker dpDateHeure;
    @FXML
    private JFXComboBox<MedicamentConcentration> comboLigne,comboAddMedConc;
    @FXML
    private JFXComboBox<Lot> comboLotAssociated;
    @FXML
    private Spinner<Integer> spinQuantity,spinnerAdd,spinRetour;
    @FXML
    private VBox vBoxLiFac,vBoxRetour;
    @FXML
    private  HBox hBoxLiFac;
    @FXML
    private JFXButton btnCreateLiFac;
    @FXML
    private CheckBox checkBoxSendFact;
    @FXML
    private TableView<Facture> mainTableView;
    @FXML
    private TableView<LigneFacture> tableViewLine;
    @FXML
    private TableColumn<Object, Object> tableColumnId;
    @FXML
    private TableColumn tableColumnClient;
    @FXML
    private TableColumn tableColumnUtilisateur;
    @FXML
    private TableColumn tableColumndateHeure;
    @FXML
    private TableColumn<Object, Object> tableColumnLigneFacture;
    @FXML
    private TableColumn tableColumnTotal;
    @FXML
    private TableColumn tableColumnTotalLigne;
    @FXML
    private TableColumn tableColumnQuantity;
    @FXML
    private TableColumn tableColumnMedConc;
    @FXML
    private TableColumn tableColumnFacture;
    @FXML
    private TableColumn tableColumnEntreprise;


    /**
     * Constructeur de FactureController
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     * @param model : le model de application
     */
    public FactureController(Stage stage, Model model) {
        super(stage,model);
        this.factureDAO = DAOFactory.getFactureDAO();
        this.liFacDAO= DAOFactory.getLigneFactureDAO();
    }
    /**
     * Initialisation de FactureController
     * Cette méthode va mettre en place les élèments ayant besoin dêtre affiché ou initialisé
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateComboFacture();
        showAll();
    }// fin initialize

    /**
     * Méthode de création d'une liste de FactureMC contenant toutes les factures de la base de données
     * @return ArrayList<FactureMC>
     * @throws DBException
     */
    private ArrayList<FactureMC> createLstMC() throws DBException {
        ArrayList<FactureMC> lstFacture = new ArrayList<>();
        for (Facture f: factureDAO.findAll() ) {
            lstFacture.add( new FactureMC(f) );
        }
        return lstFacture;
    }// fin createLstMC

    /**
     * Méthode affichant toutes les factures dans mainTableView
     */
    @FXML
    private void showAll() {
        ArrayList<FactureMC> lstFacture = null;
        try {
            lstFacture = createLstMC();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.content"));
        }
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        tableColumnUtilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        tableColumndateHeure.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        tableColumnLigneFacture.setCellValueFactory(new PropertyValueFactory<>("comboLigneFacture"));
        tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tableColumnEntreprise.setCellValueFactory(new PropertyValueFactory<>("entreprise"));
        mainTableView.setItems( FXCollections.observableArrayList(lstFacture) );
    }// fin showAll

    /**
     * Méthode affichant toutes les ligneFactures d'une facture selectionnée
     * La facture relative à ces ligneFactures se trouve dans comboFacture
     */
    @FXML
    private void showLine(){
        /* une facture doit être mise dans comboFacture afin d'en extraire toutes les ligens de facture */
        if (comboFacture.getValue() != null){
            ArrayList<LigneFacture> lstLiFac = null;
            try {
                lstLiFac = liFacDAO.findAll(comboFacture.getValue());
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.title"),
                        Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.content"));
            }
            tableColumnFacture.setCellValueFactory(new PropertyValueFactory<>("facture"));
            tableColumnMedConc.setCellValueFactory(new PropertyValueFactory<>("medicamentConcentration"));
            tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            tableColumnTotalLigne.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));
            tableViewLine.setItems(FXCollections.observableArrayList(lstLiFac));
            lblTotal.setText(dblFormat0_00(comboFacture.getValue().getTotal()));
        }
    }//fin showLine

    /**
     * Méthode de création de la fenetre de création d'une Facture (view.popUpFacture.fxml)
     * @throws IOException
     */
    @FXML
    private void create() throws IOException {
        Stage stage = createPopUp("popUpFacture",this);
        initCreate();
        stage.show();
    }// fin create

    /**
     * Méthode d'initialisation du contenu de la vue popUpFacture
     * Cette Méthode met certaine valeur par défaut afin de faire gagner du temps à l'utilisateur
     */
    private void initCreate(){
        try {
            comboLigne.setItems( FXCollections.observableArrayList(DAOFactory.getMedicamentConcentrationDAO().findAll()) );
            comboLigne.getSelectionModel().select(0);
            initSpinner();
            //set de l'utilisateur comme valeur par défaut
            comboUtilsateur.setItems( FXCollections.observableArrayList( DAOFactory.getUtilisateurDAO().findAll() ) );

            comboUtilsateur.getSelectionModel().select(this.model.getCurrentUser());
            comboClient.setItems( FXCollections.observableArrayList( DAOFactory.getClientDAO().findAll() ) );
            //set de l'entreprise comme valeur par défaut
            //TODO voir pourquoi le nom de l'entreprise n'apparait pas dans le combobox
            comboEntreprise.setItems( FXCollections.observableArrayList( DAOFactory.getClientDAO().findAll() ) );
            comboEntreprise.getSelectionModel().select(this.model.getEntreprise());
            dpDateHeure.setValue(LocalDate.now());
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.content"));
        }
    }//end initCreate

    /**
     * Méthode de création d'une facture dans la base de donnée
     * Cette Méthode vérifie si tous les champs devant être rempli le sont correctement.
     * @throws DBException
     */
    @FXML
    private void createFacture(ActionEvent e) throws DBException {
        /* Si tous les champs sont corrects */
        if (comboUtilsateur.getValue() != null
                && comboClient.getValue() != null
                && comboEntreprise.getValue() != null
                && dpDateHeure.getValue() != null
                && createLstLiFac().size() != 0)
        {
            /* creation du Timestamp de la date choisi */
            LocalDate date = dpDateHeure.getValue();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
            cal.set(Calendar.MONTH, date.getMonthValue() - 1);
            cal.set(Calendar.YEAR, date.getYear());
            Timestamp timestamp = new Timestamp(cal.getTime().getTime());

            /* creation des lignefacture en rapport avec la facture qui va être créée */
            /* attention, les ligneFactures actuelles ne sont pas associées à aucune facture, ce sont les DAO qui feront l'opération*/
            ArrayList<LigneFacture> lstLigneFacture = createLstLiFac();
            /* création de l'objet Facture */
            Facture fac =new Facture(-1,
                    comboClient.getValue(),comboUtilsateur.getValue(),
                    timestamp,
                    lstLigneFacture,
                    Double.parseDouble( lblTotalCreate.getText()),
                    comboEntreprise.getValue());
            /* creation de la facture dans la base de donnée */
            try {
                if ( factureDAO.create(fac) ){// si la facture est bien créée, on diminue les quantités dans le stock
                    /* On récupere les Lignes factures associée */
                    ArrayList<Facture> lstFact =factureDAO.findAll();
                    lstLigneFacture = lstFact.get(lstFact.size()-1).getLigneFacture();
                    MedicamentConcentrationDAO medConcDAO = DAOFactory.getMedicamentConcentrationDAO();
                    try {
                        /* diminution des quantités et mise à jours du médicament et donc des lots associciés */
                        for (LigneFacture lf: lstLigneFacture ) {
                            lf.getMedicamentConcentration().subQuantity(lf.getQuantite());
                            medConcDAO.update(lf.getMedicamentConcentration());
                        }
                    } catch (ConstraintException throwables) {
                        DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.title"),
                                Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
                    }
                    /* envoi de la facture par mail si checkBoxSendFact est selectionné */
                    if (checkBoxSendFact.isSelected()){
                        sendToClient(lstFact.get(lstFact.size()-1));
                    }
                    closeCreateStage(e);
                }else{//sinon affichage d'une erreur
                    DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("FactureDAO.DialogBoxErr.notCreate.content"));
                }
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("FactureDAO.DialogBoxErr.notCreated.constraintViolation.content"));
            }
        }else{// sinon
            DialogBox.info(Store.bundle.getString("FactureDAO.DialogBoxErr.notCreate.title"),
                    Store.bundle.getString("view.toutremplir"));
        }
    }//fin createFacture


    /**
     * Méthode donnant la possibilité à l'utilisateur d'éditer et mettre à jour les données Facture dans mainTableView
     * ainsi que les LigneFactures dans tableViewLine
     * Cette méthode permet à l'utilisateur de faire un double-clic sur un attribut d'une Facture ou d'une LigneFacture
     * pour pouvoir modifier cette attribut.
     * Pour comfirmer la modification, il faut cliquer sur Enter
     */
    @FXML
    private void update() throws DBException {
        mainTableView.setEditable(true);
        mainTableView.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableViewLine.setEditable(true);
        tableViewLine.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnClient.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList( DAOFactory.getClientDAO().findAll() )));
        tableColumnUtilisateur.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList( DAOFactory.getUtilisateurDAO().findAll() )));
        tableColumndateHeure.setCellFactory(DateTableCell.forTableColumn());
        tableColumnQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        editingColumn(tableColumnClient);
        editingColumn(tableColumnUtilisateur);
        editingColumn(tableColumndateHeure);
        editingColumnforLine(tableColumnQuantity);
    }// fin update

    /**
     * Méthode d'ajout d'une action onEditCommit sur une TableColumn
     * cette méthode permet de faire la modification d'une Facture dans la base de données
     * @param column : la colonne à editer
     */
    private void editingColumn(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            Facture f = (Facture) e.getTableView().getItems().get(e.getTablePosition().getRow());
            switch (column.getId()) {
                case "tableColumndateHeure" -> f.setDateHeure((Timestamp) e.getNewValue());
                case "tableColumnUtilisateur" -> f.setUtilisateur((Utilisateur) e.getNewValue());
                case "tableColumnClient" -> f.setClient((Client) e.getNewValue());
                case "tableColumnTotal" -> f.setTotal(Double.parseDouble(e.getNewValue().toString()));
            }
            try {
                if ( !factureDAO.update(f) ){
                    DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.title")
                            ,Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.title")
                        ,Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
            showAll();
        });
    }// fin editingColumn
    /**
     * Méthode d'ajout d'une action onEditCommit sur une TableColumn
     * cette méthode permet de faire la modification d'une Facture dans la base de données
     * @param column : la colonne à editer
     */
    private void editingColumnforLine(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            LigneFacture lf = (LigneFacture) e.getTableView().getItems().get(e.getTablePosition().getRow());
            switch (column.getId()) {
                case "tableColumnQuantity": {
                    ArrayList<LigneFacture> lst = new ArrayList<>(e.getTableView().getItems());
                    lf.setQuantite((Integer) e.getNewValue());
                    lf.setPrixTotal(Utility.getTotalLiFact(lf.getMedicamentConcentration(), lf.getQuantite()));
                    lf.getFacture().setTotal(Utility.getTotalFact(lst));
                }
            }
            try {
                if ( !liFacDAO.update(lf) || !factureDAO.update(lf.getFacture()) ){//
                    DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.title")
                            ,Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
                }else{
                    updateComboFacture();
                    showLine();
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.title")
                        ,Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
        });
    }// fin editingColumnforLine

    /**
     * Méthode de suppression d'une Facture
     * Cette méthode fait la mise à jour de la DB et aussi la mise à jour de l'interface
     * @throws DBException
     */
    @FXML
    private void delete() throws DBException {
        Facture facture = mainTableView.getSelectionModel().getSelectedItem();
        try{
            if (facture != null){
                if( DialogBox.confirm(Store.bundle.getString("Facture.DialogBox.sureToDelete.title"),
                        Store.bundle.getString("Facture.DialogBox.sureToDelete")) ) {
                    if (factureDAO.delete(facture)) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("FactureDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            }else{
                DialogBox.info("Info", Store.bundle.getString("view.facture.dialog.needfacture"));
            }
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("FactureDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("FactureDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }
    }//fin delete

    /**
     * Methode de suppression d'une LigneFacture dans le tableViewLine ainsi que dans la base de données
     * cette méthode fait la mise à jours des données, aussi en interface
     */
    @FXML
    private void deleteLine(){
        LigneFacture ligneFacture = tableViewLine.getSelectionModel().getSelectedItem();
        try{
            if (ligneFacture!= null){
                if (DialogBox.confirm( Store.bundle.getString("LigneFacture.DialogBox.sureToDelete.title"),
                        Store.bundle.getString("LigneFacture.DialogBox.sureToDelete"))) {
                    if (liFacDAO.delete(ligneFacture)) {
                        updateComboFacture();
                        comboFacture.getValue().setTotal(Utility.getTotalFact(comboFacture.getValue().getLigneFacture()));
                        lblTotal.setText(dblFormat0_00(comboFacture.getValue().getTotal()));
                    } else {
                        DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            }else{
                DialogBox.info("Info", Store.bundle.getString("view.lignefacture.dialog.needline"));
            }
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }
    }//fin deleteLine

    /**
     * Méthode de retour de stock, cette méthode est utilisée lorsque le client souhaite faire un retour de stock
     * en utilisant cette méthode, le stock de chaque lot est remis à jour
     * @throws IOException
     */
    @FXML
    private void retour() throws IOException {
        LigneFacture ligneFacture = tableViewLine.getSelectionModel().getSelectedItem();
        Stage stage = createPopUp("popUpRetour",this);
        /* on charge le model avec une liste des lots qui auront des retours + avec la LigneFacture en rapport */
        this.model.addElement("lstLotRetour", new ArrayList<Lot>() );
        this.model.addElement("currentLiFac", ligneFacture);
        try {
            /* initialisation des Nodes dans popUpRetour.fxml */
            if (ligneFacture != null) {
                lblNumFact.setText(ligneFacture.toString());
                comboLotAssociated.setItems( FXCollections.observableArrayList( DAOFactory.getLotDAO().findAll(ligneFacture.getMedicamentConcentration()) ) );
                comboLotAssociated.getSelectionModel().select(0);
                spinRetour.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000));
                stage.show();
            }else {
                DialogBox.info("Info", Store.bundle.getString("view.lignefacture.dialog.needline"));
            }
        } catch (Exception throwables) {
            DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin retour

    /**
     * Méthode d'ajout d'une ligne de médicaments concentré à retourner
     * Dans la pratique cette méthode ajoute des lots auquel il faudra rajouter des quantités
     */
    @FXML
    private void addLineRetour(){
        if ( spinRetour.getValue() != 0){
            comboLotAssociated.getValue().setQuantite(spinRetour.getValue());
            ((ArrayList<Lot>)this.model.getHashLst().get("lstLotRetour")).add(comboLotAssociated.getValue());
            Label lblRetour = new Label(   Store.bundle.getString("view.lotnumber")+ " : "+comboLotAssociated.getValue().getId() +"   ➡ "+
                    Store.bundle.getString("view.medicamentconcentration")+ " : "+
                    comboLotAssociated.getValue().getMedicamentConcentration() + "    ➡  " +
                    Store.bundle.getString("view.retour")+ " : "+ spinRetour.getValue());
            vBoxRetour.getChildren().add(lblRetour);
            spinRetour.decrement(spinRetour.getValue());
        }
    }// fin addLineRetour

    /**
     * Méthode de confirmation des retours
     * @param event
     */
    @FXML
    private void doRetour(Event event){
        LotDAO lotDAO = DAOFactory.getLotDAO();
        int quantiteRetour = 0;
        try {
            LigneFacture lifac = (LigneFacture) this.model.getHashLst().get("currentLiFac");
            vBoxRetour.getChildren().clear();
            /* calcul de la quantité totale a retourner */
            for (Lot lot: ((ArrayList<Lot>)this.model.getHashLst().get("lstLotRetour"))) {
                quantiteRetour += lot.getQuantite();
            }
            for (Lot lot: ((ArrayList<Lot>)this.model.getHashLst().get("lstLotRetour"))) {
                /* verification que l'on ne retourne pas plus de Medicament que ce qui se trouve sur la ligne facture */
                if (lifac.getQuantite() - quantiteRetour >=0){
                    Lot dbLot = lotDAO.find(lot.getId());
                    dbLot.setQuantite(dbLot.getQuantite()+ lot.getQuantite());
                    lotDAO.update(dbLot);
                    lifac.setQuantite(lifac.getQuantite() - lot.getQuantite());
                }else{
                    DialogBox.info("Info",Store.bundle.getString("view.pasassezaretourner"));
                    break;
                }
            }
            liFacDAO.update(lifac);
            showLine();
            closeStage(event);
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.title"),
                    Store.bundle.getString("DBException.Message"));
        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }
    }// fin doRetour

    /**
     * Méthode d'initalisation et apparition du contenu de hBoxLiFac qui va afficher les
     * différents nodes utiles à la créations d'une LigneFacture
     */
    @FXML
    private void createLiFac(){
        try {
            comboAddMedConc.setItems( FXCollections.observableArrayList(DAOFactory.getMedicamentConcentrationDAO().findAll()) );
            comboAddMedConc.getSelectionModel().select(0);
            initSpinAdd();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreate.title"),
                    Store.bundle.getString("DBException.Message"));
        }
        hBoxLiFac.setDisable(false);
        hBoxLiFac.setVisible(true);
        btnCreateLiFac.setOnAction(e -> addLiFac());
    }

    /**
     * Méthode qui utilise la méthode addLiFac lorsque l'utilisateur appuie sur Enter
     * @param e
     */
    @FXML
    private void onEnterKeyPress(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER){
            addLiFac();
        }
    }// fin onEnterKeyPress

    /**
     * Méthode d'initialisation de spinAdd
     */
    @FXML
    private void initSpinAdd(){
        int maxVal = (comboAddMedConc.getValue() != null) ? comboAddMedConc.getValue().getStock() : 0;
        spinnerAdd.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxVal));
        spinnerAdd.getEditor().textProperty().addListener((obs, oldValue, newValue) ->{ // mise à jour de lblTotalAdd lors du changement de valeur
            lblTotalAdd.setText( dblFormat0_00( Utility.getTotalLiFact(comboAddMedConc.getValue(), Integer.parseInt(newValue)) ) );
        });
        if (maxVal == 0){
            comboAddMedConc.setStyle("-fx-border-color: red;");
        }else {
            comboAddMedConc.setStyle("");
        }
    }//fin initSpinner

    /**
     * Méthode de comfirmartion de l'ajout d'une LigneFacture dans la base de données
     */
    private void addLiFac(){
        if ( comboAddMedConc.getValue() != null && spinnerAdd.getValue()!= 0 ){
            try {
                if ( liFacDAO.create(new LigneFacture(comboFacture.getValue(), comboAddMedConc.getValue(), spinnerAdd.getValue(),
                        Utility.getTotalLiFact(comboAddMedConc.getValue(),spinnerAdd.getValue())))){
                    /* remise à jour des Nodes comme avant l'ajout */
                    updateComboFacture();
                    comboFacture.getValue().setTotal(Utility.getTotalFact(comboFacture.getValue().getLigneFacture()));
                    lblTotal.setText(dblFormat0_00(comboFacture.getValue().getTotal()));
                    comboAddMedConc.getValue().subQuantity(spinnerAdd.getValue());
                    DAOFactory.getMedicamentConcentrationDAO().update(comboAddMedConc.getValue());
                    factureDAO.update(comboFacture.getValue());
                    hBoxLiFac.setDisable(true);
                    hBoxLiFac.setVisible(false);
                    btnCreateLiFac.setOnAction(e -> createLiFac());
                    showLine();
                } else {
                    DialogBox.info(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreate.content"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreated.constraintViolation.content"));
            }
        }
    }// fin addLiFac


    /**
     * Méthode d'update du ComboBox comboFacture
     * comboFacture est très important dans l'affichage des lignes factures
     */
    private void updateComboFacture(){
        try {
            int comboPosn = 0;
            if (comboFacture.getItems().size() != 0){
                comboPosn = comboFacture.getSelectionModel().getSelectedIndex();
                comboFacture.getItems().clear();
                comboFacture.setItems(FXCollections.observableArrayList(factureDAO.findAll()));
                comboFacture.getSelectionModel().select(comboPosn);
            }else {
                comboFacture.setItems(FXCollections.observableArrayList(factureDAO.findAll()));
                comboFacture.getSelectionModel().select(0);
            }
        } catch (DBException throwables) {
            DialogBox.error("error", Store.bundle.getString("LigneFacture.errorupdateComboFact"));
        }
    }// fin updateComboFacture

    /**
     * Méthode d'exportation en excel d'une facture ainsi que toutes ses ligneFacture
     * à l'emplacement fichier choisi par l'utilisateur
     * @throws IOException
     */
    @FXML
    private void exportLine() throws IOException {
        File file= xlsFileSaver(this.stage);
        Export export= new Export();
        if (file != null) {
            export.setFullFile(file.toString());
            export.excelFacture(comboFacture.getValue(), tableViewLine);
        }
    }//fin exportLine

    /**
     * Méthode de fermeture de createStage (la fenetre de création de facture)
     * Cette méthode met à jour toutes les Factures et les ligneFactures dans l'application
     */
    @FXML
    private void closeCreateStage(ActionEvent e){
        showLine();
        showAll();
        closeStage(e);
    }// fin closeCreateStage


    /**
     * Méthode qui vérifie dans vBoxLiFac si il y a un HBox contenant medicamentConcentration
     * @param medicamentConcentration : le MedicamentConcentration recherché
     * @return
     *      -HBox: la HBox contenant le medicamentConcentration recherché
     *      -null s'il ne le trouve pas
     */
    private HBox findHbLine(MedicamentConcentration medicamentConcentration){
        for (Node node: vBoxLiFac.getChildren()) {
            HBox hb = (HBox) node;
            MedicamentConcentration medConc = ((JFXComboBox<MedicamentConcentration>)hb.getChildren().get(0)).getValue();
            if ( medConc.equals( medicamentConcentration ) ){
                return hb;
            }
        }
        return null;
    }//fin findHbLine

    /**
     * Méthode d'ajout d'un HBox contenant
     *      0: JFXComboBox<MedicamentConcentration> medConcLine : un combobox avec le MedicamentConcentration choisi
     *      1: Spinner<Integer> medConcSpin : un Spinner avec les quantités du MedicamentConcentration choisi
     *      2: Label totalLigne: un Label contenant le prix caclulé et mis en forme du MedicamentConcentration choisi * les quantités
     *      3: JFXButton cancelBtn: un bouton permettant de supprimer le HBox de vBoxLiFac
     * Cette méthode utilise findHbLine afin de ne pas dupliquer les MedicamentConcentrations
     */
    @FXML
    private void addLine() throws DBException {
        if (spinQuantity.getValue() != 0){
            HBox BoxLine = new HBox();
            JFXComboBox<MedicamentConcentration> medConcLine;
            Spinner<Integer> medConcSpin;
            JFXButton cancelBtn;
            Label totalLigne= new Label();
            int valSpin = 0;
            //Vérification que le MedicamentConcentration que l'utilisateur souhaite ajouter, n'est pas présent dans vBoxLiFac
            HBox ExistBox = findHbLine( comboLigne.getValue() );

            if (ExistBox != null){// si le MedicamentConcentration est déjà présent alors on ajoute simplement la quantité
                Spinner quantitySpinner = ((Spinner<Integer>)ExistBox.getChildren().get(1));
                quantitySpinner.increment(spinQuantity.getValue());
            }else{
                //init du comboBox du libellé MedicamentConcentration
                medConcLine = new JFXComboBox<>( FXCollections.observableArrayList( DAOFactory.getMedicamentConcentrationDAO().findAll()) );
                medConcLine.getSelectionModel().select(comboLigne.getValue());
                medConcLine.setMaxWidth(Double.MAX_VALUE);
                medConcLine.setMinWidth(300);
                //init du spinner associé
                valSpin = (comboLigne.getValue() != null) ? medConcLine.getValue().getStock() : 0;
                medConcSpin = new Spinner<>();
                medConcSpin.setMaxWidth(84);
                medConcSpin.setMinWidth(84);
                medConcSpin.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, valSpin));
                medConcSpin.getValueFactory().setValue(spinQuantity.getValue());
                medConcSpin.valueProperty().addListener(new ChangeListener<Integer>() {
                    @Override
                    public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                        totalLigne.setText(dblFormat0_00(medConcLine.getValue().getPrix()*medConcSpin.getValue()));
                        updateLblTotalCreate();
                    }
                });
                //Modifie le spinner associé en cas de changement de valeur
                medConcLine.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        int valSpin = (comboLigne.getValue() != null) ? medConcLine.getValue().getStock() : 0;
                        medConcSpin.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, valSpin));
                        totalLigne.setText(dblFormat0_00(medConcLine.getValue().getPrix()*medConcSpin.getValue()));
                        updateLblTotalCreate();
                    }
                });
                //attribution du total de la ligne
                totalLigne.setMinWidth(75.0);
                totalLigne.setAlignment(Pos.CENTER_RIGHT);
                totalLigne.setText(dblFormat0_00(medConcLine.getValue().getPrix()*medConcSpin.getValue()));
                //init du bouton d'annulation
                cancelBtn = new JFXButton("-");
                cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        vBoxLiFac.getChildren().remove(BoxLine);
                        updateLblTotalCreate();
                    }
                });
                //configuration design de BoxLine
                BoxLine.setSpacing(60.0);
                BoxLine.setAlignment(Pos.TOP_CENTER);
                BoxLine.setMaxWidth(Double.MAX_VALUE);
                BoxLine.getChildren().addAll(medConcLine,medConcSpin,totalLigne,cancelBtn);

                vBoxLiFac.getChildren().add(BoxLine);
                updateLblTotalCreate();
            }//fin else
        }// fin if
    }//fin addLine

    /**
     * Méthode de mise à jour du prix total de la facture dans la fenetre de création de la facture
     * Cette méthode met à jour lblTotalCreate (le label d'affichage du total de view.popUpFacture.fxml)
     */
    private void updateLblTotalCreate() {
        Double total= getTotalFact(createLstLiFac());
        lblTotalCreate.setText(dblFormat0_00( total ));
    }// fin updateLblTotalCreate

    /**
     * Méthode de création de toutes les LigneFactures liée à la facture que l'on souhaite créée dans la fenetre de création de facture
     * Attention l'attribut facture de chaque LigneFacture dans l'ArrayList de retour sera null.
     * @return ArrayList<LigneFacture> : une liste contenant toutes les LigneFactures liée à la facture que l'on souhaite créée
     */
    private ArrayList<LigneFacture> createLstLiFac(){
        ArrayList<LigneFacture> lstLiFac= new ArrayList<>();
        for (Node node: vBoxLiFac.getChildren()) {
            HBox hb = (HBox) node;
            MedicamentConcentration medConc = ((JFXComboBox<MedicamentConcentration>)hb.getChildren().get(0)).getValue();
            int quant = ((Spinner<Integer>)hb.getChildren().get(1)).getValue();
            Double totalLine = Double.parseDouble( ((Label)hb.getChildren().get(2)).getText() );
            lstLiFac.add(new LigneFacture(null,medConc,quant,totalLine));//attention la facture est null pour le moment
        }
        return lstLiFac;
    }//fin createLstLiFac



    /**
     * Méthode d'initialisation du spinner (spinQuantity)
     * Cette méthode donne au spinQuantity sa valeur maximum et minimum. De plus si le MedicamentConcentration est à 0, comboLigne sera entouré de rouge
     */
    @FXML
    private void initSpinner(){
        int maxVal = (comboLigne.getValue() != null) ? comboLigne.getValue().getStock() : 0;
        spinQuantity.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxVal));
        if (maxVal == 0){
            comboLigne.setStyle("-fx-border-color: red;");
        }else {
            comboLigne.setStyle("");
        }
    }//fin initSpinner

    /**
     * Méthode de création d'un message mail.
     * Cette méthode va formatter la facture en une chaine de caractère afin de l'envoyer en message texte par mail
     * @param facture
     * TODO modifier le message en HTML
     */
    private void sendToClient(Facture facture){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
        MailBox mailBox= new MailBox();
        String mailClient = facture.getClient().getEmail();
        String title= "Facture_"+facture.getId();
        String content=Store.bundle.getString("Facture.mail.header")+"\n\n";

        //Creation du contenu
        content+= Store.bundle.getString("view.facture")+" : "+facture.getId()+"\n";
        content+= Store.bundle.getString("view.dateheure")+" : "+sdf.format(facture.getDateHeure())+"\n";
        content+= Store.bundle.getString("view.lignefacture")+ ": "+"\n";
        for (LigneFacture lf :facture.getLigneFacture()) {
            content += "    -"+lf.getMedicamentConcentration().getId()+" "+lf.getMedicamentConcentration()+" "+
                    Store.bundle.getString("view.quantite")+": "+lf.getQuantite()+ " " +
                    dblFormat0_00(lf.getPrixTotal())+ " Euros\n";
        }
        content+="\n"+Store.bundle.getString("view.total")+": "+dblFormat0_00(facture.getTotal())+ " Euros\n\n";
        try {
            content+=Store.bundle.getString("Facture.mail.footer")+"\n"+this.model.getEntreprise().getDenomination()+",";
            content+="\n\n"+this.model.getEntreprise().getNumTva();
        } catch (DBException throwables) {
            throwables.printStackTrace();
        }
        //envoi du mail
        mailBox.sendTxt(mailClient,title,content);
    }//fin sendToClient
}
