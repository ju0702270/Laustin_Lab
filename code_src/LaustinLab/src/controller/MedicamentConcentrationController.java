package controller;

import DAO.DAOFactory;
import DAO.MedicamentConcentrationDAO;
import Main.Main;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import model.*;
import utility.DialogBox;
import utility.Store;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 10/03/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe controller pour la vue view.MedicamentConcentrationView.fxml et view.popUpMedicamentConcentration.fxml
 */
public class MedicamentConcentrationController extends MainContent{

    private MedicamentConcentrationDAO medConcDAO;
    @FXML
    private AnchorPane anchorLot,anchorMedicament,anchorSpecification;
    @FXML
    private TableView<MedicamentConcentration> mainTableView;
    @FXML
    private TableColumn<MedicamentConcentration, Integer> tableColumnId;
    @FXML
    private TableColumn<Object, Object> tableColumnMedicament;
    @FXML
    private TableColumn<Object, Object> tableColumnConcentration;
    @FXML
    private TableColumn<Object, Object>  tableColumnConditionnement;
    @FXML
    private TableColumn<Object, Object>  tableColumnForme;
    @FXML
    private TableColumn<MedicamentConcentration,Double> tableColumnPrix;
    @FXML
    private TableColumn<MedicamentConcentration,Double> tableColumnTva;
    @FXML
    private TableColumn<MedicamentConcentration,Integer> tableColumnStock;
    @FXML
    private JFXComboBox<Medicament> comboMedicament;
    @FXML
    private JFXComboBox<Concentration> comboConcentration;
    @FXML
    private JFXComboBox<Conditionnement> comboConditionnement;
    @FXML
    private JFXComboBox<Forme> comboForme;
    @FXML
    private JFXTextField tfPrix;
    @FXML
    private JFXTextField tfTva;

    /**
     * Constructeur de MédicamentConcentrationController
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public MedicamentConcentrationController(Stage primaryStage) {
        super(primaryStage);
        this.medConcDAO = DAOFactory.getMedicamentConcentrationDAO();
    }// fin constructeur

    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * Cette méthode va lancer showAll() afin d'afficher tous les MedicamentConcentration au moment où le controlleur est initialisé
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAll();
    }// fin initialize

    /**
     * Méthode de chargement d'une vue dans un AnchorPane passé en paramètre
     * @param fxmlView : le fichier FXML servant de vue à integrer au AnchorPanel panToLoad
     * @param control : le controlleur de la vue
     * @param paneToLoad : le AnchorPane dans lequel charger la vue fxmlView
     */
    private void loader(String fxmlView, MasterControl control, AnchorPane paneToLoad){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/"+ fxmlView +".fxml"), Store.bundle);
        loader.setController(control);
        try {
            AnchorPane managePane = loader.load();
            AnchorPane.setLeftAnchor(managePane,0.0);
            AnchorPane.setBottomAnchor(managePane,0.0);
            AnchorPane.setRightAnchor(managePane,0.0);
            AnchorPane.setTopAnchor(managePane,0.0);
            paneToLoad.getChildren().clear();
            paneToLoad.getChildren().add(managePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// fin loader
    /*  Les méthodes de chargements des vues spécifique à chaque ongets */
    /**
     * Méthode de chargement de la vue LotView.fxml dans l'onglet lot
     */
    @FXML
    private void loadLot(){
        loader("LotView", new LotController(stage), anchorLot);
    }

    /**
     * Méthode de chargement de la vue MedicamentView.fxml dans l'onglet Medicament
     */
    @FXML
    private void loadMedicament(){
        loader("MedicamentView", new MedicamentController(stage), anchorMedicament);
    }

    /**
     * Méthode de chargement de la vue Specificiteview.fxml dans l'onglet Spécificité
     * Les spécificités représente les Concentrations d'un médicament, la forme galénique, le conditionnement.
     */
    @FXML
    private void loadSpecification(){
        loader("SpecificiteView", new SpecificiteController(stage),anchorSpecification);
    }

    /**
     * Méthode d'affichage de tout les MedicamentConcentration dans mainTableView
     */
    @FXML
    private void showAll() {
        ArrayList<MedicamentConcentration> lstMedicamentConcentration = null;
        try {
            lstMedicamentConcentration = medConcDAO.findAll();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notFound.content"));
        }
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnMedicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
        tableColumnConcentration.setCellValueFactory(new PropertyValueFactory<>("concentration"));
        tableColumnConditionnement.setCellValueFactory(new PropertyValueFactory<>("conditionnement"));
        tableColumnForme.setCellValueFactory(new PropertyValueFactory<>("forme"));
        tableColumnPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        tableColumnTva.setCellValueFactory(new PropertyValueFactory<>("tva"));
        tableColumnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainTableView.getItems().clear();
        mainTableView.setItems( FXCollections.observableArrayList(lstMedicamentConcentration) );
    }// fin showAll
    /**
     * Méthode de création de la fenetre de création d'un MedicamentConcentration (view.popUpMedicamentConcentration.fxml)
     * @throws IOException
     */
    @FXML
    private void create() throws IOException, DBException {
        Stage stage = createPopUp("popUpMedicamentConcentration",this);
        comboMedicament.setItems(FXCollections.observableArrayList( DAOFactory.getMedicamentDAO().findAll() ));
        comboConcentration.setItems(FXCollections.observableArrayList( DAOFactory.getConcentrationDAO().findAll() ));
        comboConditionnement.setItems(FXCollections.observableArrayList( DAOFactory.getConditionnementDAO().findAll() ));
        comboForme.setItems(FXCollections.observableArrayList( DAOFactory.getFormeDAO().findAll() ));
        stage.show();
    }// fin create

    /**
     * Méthode de création d'un MedicamentConcentration en tant que tels,
     * cette méthode relance l'utilisateur vers la fenetre de création d'un lot car pour être visible dans l'interface, le médicament concentration doit
     * être lié à un lot.
     * @param event
     */
    @FXML
    private void createMedConc(Event event){
        /* Dialog pour prévenir l'utilisateur de créer un lot associé */
        DialogBox.info("Info", Store.bundle.getString("MedicamentConcentration.Dialog.needCreateLot"));
        try{
            /* Vérification de la validité */
            if ( comboMedicament.getValue() != null && comboConcentration.getValue() !=null && comboConditionnement.getValue() != null &&
                    comboForme.getValue() != null && Double.parseDouble(tfPrix.getText()) >= 0.0 && Double.parseDouble(tfTva.getText()) >= 0.0){
                /* création de l'objet médicament */
                MedicamentConcentration medicamentConcentration = new MedicamentConcentration(-1, comboMedicament.getValue(),
                        comboConcentration.getValue(),comboConditionnement.getValue(),comboForme.getValue(),Double.parseDouble(tfPrix.getText()),new ArrayList<Lot>(),
                        Double.parseDouble(tfTva.getText()));
                if ( this.medConcDAO.create(medicamentConcentration) ){
                    /* on récupère le dernier médicament ajouté dans la DB donc, celui que l'on vient de créer */
                    ArrayList<MedicamentConcentration> lstMedConc = medConcDAO.findAllWithoutLot();
                    MedicamentConcentration justCreateMedConc= lstMedConc.get(lstMedConc.size()-1);
                    /* création de la popUp de création du lot associé */
                    LotController lotController = new LotController(this.stage);
                    Stage stageTemp = createPopUp("popUpLot",lotController);
                    lotController.initPopUp();
                    lotController.comboMedConc.getSelectionModel().select(justCreateMedConc);
                    lotController.comboMedConc.setDisable(true);// pour forcer l'utilisateur à créer le bon lot associé
                    lotController.cancelPopUpLot.setOnAction(null);
                    lotController.btnConfirmLot.setOnAction(e -> {// réatribution de la méthode createLot à btnComfirmLot + showAll()
                        lotController.createLot(e);
                        showAll(); // afin de rafraichir le tableau Medicament concentré
                    });
                    stageTemp.show();
                    closeStage(event);
                }else{
                    DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreate.content"));
                }
            }else{// si le formulaire n'est pas valide, affichage d'un message
                DialogBox.info("Info", Store.bundle.getString("view.toutremplir"));
            }
        }catch (NumberFormatException e){
            DialogBox.info("Info", Store.bundle.getString("view.formatDouble") );
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreate.title"),
                    Store.bundle.getString("DBException.Message"));
        } catch (IOException e) {
            DialogBox.info("Info", Store.bundle.getString("MedicamentConcentration.openPopUpLot"));
        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreate.title"),
                    Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        }
    }// fin createMedConc


    /**
     * Methode permettant la mise à jour des données dans mainTableView
     * cette méthode permet la modification de certaines tableColumn
     */
    @FXML
    private void update() throws DBException {
        mainTableView.setEditable(true);
        mainTableView.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnMedicament.setCellFactory( ComboBoxTableCell.forTableColumn( FXCollections.observableArrayList( DAOFactory.getMedicamentDAO().findAll()) ));
        tableColumnConcentration.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList( DAOFactory.getConcentrationDAO().findAll() )));
        tableColumnConditionnement.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList( DAOFactory.getConditionnementDAO().findAll() )));
        tableColumnForme.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList( DAOFactory.getFormeDAO().findAll() )));
        tableColumnPrix.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableColumnTva.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        editingColumn(tableColumnMedicament);
        editingColumn(tableColumnConcentration);
        editingColumn(tableColumnConditionnement);
        editingColumn(tableColumnForme);
        editingColumn(tableColumnPrix);
        editingColumn(tableColumnTva);
    }// fin update

    /**
     * Méthode d'ajout d'une action onEditCommit sur une TableColumn
     * cette méthode modifie également le MedicamentConcentration dans la base de données
     * @param column : la TableColumn à modifier
     */
    private void editingColumn(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            MedicamentConcentration mc = (MedicamentConcentration) e.getTableView().getItems().get(e.getTablePosition().getRow());
            switch (column.getId()) {
                case "tableColumnMedicament" -> mc.setMedicament( (Medicament) e.getNewValue() );
                case "tableColumnConcentration" -> mc.setConcentration( (Concentration) e.getNewValue() );
                case "tableColumnConditionnement" -> mc.setConditionnement( (Conditionnement) e.getNewValue() );
                case "tableColumnForme" -> mc.setForme( (Forme) e.getNewValue() );
                case "tableColumnPrix" -> mc.setPrix( Double.parseDouble(e.getNewValue().toString()) );
                case "tableColumnTva" -> mc.setTva( Double.parseDouble(e.getNewValue().toString()) );
            }
            try {
                /* modification dans la base de donnée */
                if ( !this.medConcDAO.update(mc) ){
                    DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.title")
                            ,Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.content"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
            showAll();
        });
    }// fin editingColumn

    /**
     * Méthode de suppression d'un MedicamentConcentration
     * Cette méthode fait la mise à jour de la DB et aussi la mise à jour de l'interface
     */
    @FXML
    private void delete(){
        MedicamentConcentration medicamentConcentration = mainTableView.getSelectionModel().getSelectedItem();
        try{
            if (medicamentConcentration != null){
                /* Demande à l'utilisateur s'il est sûr de vouloir supprimer */
                if( DialogBox.confirm(Store.bundle.getString("MedicamentConcentrationDAO.DialogBox.sureToDelete.title"),
                        Store.bundle.getString("MedicamentConcentrationDAO.DialogBox.sureToDelete")) ) {
                    if ( this.medConcDAO.delete(medicamentConcentration) ) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            }else{
                DialogBox.info("Info", Store.bundle.getString("view.medicamentconcentration.dialog.need"));
            }
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }
    }//fin delete

}
