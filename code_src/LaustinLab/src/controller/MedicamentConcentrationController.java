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
     * Constructeur de M??dicamentConcentrationController
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public MedicamentConcentrationController(Stage primaryStage) {
        super(primaryStage);
        this.medConcDAO = DAOFactory.getMedicamentConcentrationDAO();
    }// fin constructeur

    /**
     * M??thode impl??ment??e pour les controlleurs en javaFX
     * Cette m??thode va lancer showAll() afin d'afficher tous les MedicamentConcentration au moment o?? le controlleur est initialis??
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAll();
    }// fin initialize

    /**
     * M??thode de chargement d'une vue dans un AnchorPane pass?? en param??tre
     * @param fxmlView : le fichier FXML servant de vue ?? integrer au AnchorPanel panToLoad
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
    /*  Les m??thodes de chargements des vues sp??cifique ?? chaque ongets */
    /**
     * M??thode de chargement de la vue LotView.fxml dans l'onglet lot
     */
    @FXML
    private void loadLot(){
        loader("LotView", new LotController(stage), anchorLot);
    }

    /**
     * M??thode de chargement de la vue MedicamentView.fxml dans l'onglet Medicament
     */
    @FXML
    private void loadMedicament(){
        loader("MedicamentView", new MedicamentController(stage), anchorMedicament);
    }

    /**
     * M??thode de chargement de la vue Specificiteview.fxml dans l'onglet Sp??cificit??
     * Les sp??cificit??s repr??sente les Concentrations d'un m??dicament, la forme gal??nique, le conditionnement.
     */
    @FXML
    private void loadSpecification(){
        loader("SpecificiteView", new SpecificiteController(stage),anchorSpecification);
    }

    /**
     * M??thode d'affichage de tout les MedicamentConcentration dans mainTableView
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
     * M??thode de cr??ation de la fenetre de cr??ation d'un MedicamentConcentration (view.popUpMedicamentConcentration.fxml)
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
     * M??thode de cr??ation d'un MedicamentConcentration en tant que tels,
     * cette m??thode relance l'utilisateur vers la fenetre de cr??ation d'un lot car pour ??tre visible dans l'interface, le m??dicament concentration doit
     * ??tre li?? ?? un lot.
     * @param event
     */
    @FXML
    private void createMedConc(Event event){
        /* Dialog pour pr??venir l'utilisateur de cr??er un lot associ?? */
        DialogBox.info("Info", Store.bundle.getString("MedicamentConcentration.Dialog.needCreateLot"));
        try{
            /* V??rification de la validit?? */
            if ( comboMedicament.getValue() != null && comboConcentration.getValue() !=null && comboConditionnement.getValue() != null &&
                    comboForme.getValue() != null && Double.parseDouble(tfPrix.getText()) >= 0.0 && Double.parseDouble(tfTva.getText()) >= 0.0){
                /* cr??ation de l'objet m??dicament */
                MedicamentConcentration medicamentConcentration = new MedicamentConcentration(-1, comboMedicament.getValue(),
                        comboConcentration.getValue(),comboConditionnement.getValue(),comboForme.getValue(),Double.parseDouble(tfPrix.getText()),new ArrayList<Lot>(),
                        Double.parseDouble(tfTva.getText()));
                if ( this.medConcDAO.create(medicamentConcentration) ){
                    /* on r??cup??re le dernier m??dicament ajout?? dans la DB donc, celui que l'on vient de cr??er */
                    ArrayList<MedicamentConcentration> lstMedConc = medConcDAO.findAllWithoutLot();
                    MedicamentConcentration justCreateMedConc= lstMedConc.get(lstMedConc.size()-1);
                    /* cr??ation de la popUp de cr??ation du lot associ?? */
                    LotController lotController = new LotController(this.stage);
                    Stage stageTemp = createPopUp("popUpLot",lotController);
                    lotController.initPopUp();
                    lotController.comboMedConc.getSelectionModel().select(justCreateMedConc);
                    lotController.comboMedConc.setDisable(true);// pour forcer l'utilisateur ?? cr??er le bon lot associ??
                    lotController.cancelPopUpLot.setOnAction(null);
                    lotController.btnConfirmLot.setOnAction(e -> {// r??atribution de la m??thode createLot ?? btnComfirmLot + showAll()
                        lotController.createLot(e);
                        showAll(); // afin de rafraichir le tableau Medicament concentr??
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
     * Methode permettant la mise ?? jour des donn??es dans mainTableView
     * cette m??thode permet la modification de certaines tableColumn
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
     * M??thode d'ajout d'une action onEditCommit sur une TableColumn
     * cette m??thode modifie ??galement le MedicamentConcentration dans la base de donn??es
     * @param column : la TableColumn ?? modifier
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
                /* modification dans la base de donn??e */
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
     * M??thode de suppression d'un MedicamentConcentration
     * Cette m??thode fait la mise ?? jour de la DB et aussi la mise ?? jour de l'interface
     */
    @FXML
    private void delete(){
        MedicamentConcentration medicamentConcentration = mainTableView.getSelectionModel().getSelectedItem();
        try{
            if (medicamentConcentration != null){
                /* Demande ?? l'utilisateur s'il est s??r de vouloir supprimer */
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
