package controller;

import DAO.DAOFactory;
import DAO.LotDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import controller.component.DateOnlyTableCell;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Lot;
import model.MedicamentConcentration;
import utility.DialogBox;
import utility.Store;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import static java.time.LocalTime.now;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 10/03/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe controller pour la vue view.LotView.fxml et view.popUpLot.fxml
 */
public class LotController extends MainContent{

    private LotDAO lotDAO;
    @FXML
    private TableView<Lot> mainTableView;
    @FXML
    private TableColumn tableColumnIdLot,tableColumnFabrication,tableColumnPeremption,tableColumnMedicConc,tableColumnQuant;
    @FXML
    private JFXDatePicker dpFabrication,dpPeremption;
    @FXML
    public JFXComboBox<MedicamentConcentration> comboMedConc;// encapsulation en public afin que MedicamentConcentration puisse charger le Médicament concentré associé
    @FXML
    private Spinner<Integer> spinQuantity;
    @FXML
    public JFXButton cancelPopUpLot,btnConfirmLot;// Encapsulation public afin que MedicamentConcentration puisse modifier les actions sur ces boutons

    /**
     * Constructeur de LotController
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public LotController(Stage primaryStage) {
        super(primaryStage);
        this.lotDAO = DAOFactory.getLotDAO();
    }// fin constructeur

    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * Cette méthode va lancer showAll() afin d'afficher tous les lots au moment où le controlleur est initialisé
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (tableColumnIdLot != null) {
            showAll();
        }
    }// fin initialize

    /**
     * Méthode d'affichage de tout les lots dans mainTableView
     */
    @FXML
    private void showAll(){
        ArrayList<Lot> lstLot = null;
        try {
            lstLot = lotDAO.findAll();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("LotDAO.DialogBoxErr.notFound.content"));
        }
        tableColumnIdLot.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnFabrication.setCellValueFactory(new PropertyValueFactory<>("dateFabrication"));
        tableColumnPeremption.setCellValueFactory(new PropertyValueFactory<>("datePeremption"));
        tableColumnMedicConc.setCellValueFactory(new PropertyValueFactory<>("medicamentConcentration"));
        tableColumnQuant.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        mainTableView.setItems( FXCollections.observableArrayList(lstLot) );
    }// fin showAll

    /**
     * Méthode de création de la fenetre de création d'un Lot (view.popUpLot.fxml)
     * @throws IOException
     */
    @FXML
    private void create() throws IOException, DBException {
        Stage stage = createPopUp("popUpLot",this);
        initPopUp();
        stage.show();
    }// fin create

    /**
     * Méthode d'initialisation du popUpLot.fxm
     * @throws DBException
     */
    public void initPopUp() throws DBException {/*cette méthode est encapsulée en public pour que MedicamentConcentration puisse l'utiliser*/
        LocalDate today = LocalDate.now();
        dpFabrication.setValue(today);
        dpPeremption.setValue(today.plusYears(2));
        comboMedConc.setItems( FXCollections.observableArrayList(DAOFactory.getMedicamentConcentrationDAO().findAllWithoutLot()) );
        spinQuantity.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000));
    }// fin initPopUp
    /**
     * Méthode de création d'un Lot en tant que tels
     * @param event
     */
    @FXML
    public void createLot(Event event){
        /* validation */
        if ( dpPeremption.getValue() != null && dpFabrication.getValue() != null && comboMedConc.getValue() != null && spinQuantity.getValue() != null ){
            try {
                if ( lotDAO.create(  new Lot( Integer.parseInt( now().toString().substring(11))+comboMedConc.getValue().getId() ,
                        java.sql.Date.valueOf(dpFabrication.getValue()), java.sql.Date.valueOf( dpPeremption.getValue() ), comboMedConc.getValue(),
                        spinQuantity.getValue() )//end new Lot
                ) ){
                    if (tableColumnIdLot != null){ // technique de substitution qui permet d'utiliser LotController.showAll() uniquement lorsque LotView.fxml est instanciée.
                        showAll();
                    }
                    closeStage(event);
                }else{
                    DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("LotDAO.DialogBoxErr.notCreate.content"));
                }//end if else
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("LotDAO.DialogBoxErr.notCreated.constraintViolation.content"));
            }
        }else{
            DialogBox.info("Info", Store.bundle.getString("view.toutremplir"));
        }
    }// fin createLot

    /**
     * Methode permettant la mise à jour des données dans mainTableView
     * cette méthode permet la modification de certaines TableColumn
     */
    @FXML
    private void update() throws DBException {
        mainTableView.setEditable(true);
        mainTableView.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnFabrication.setCellFactory(DateOnlyTableCell.forTableColumn());
        tableColumnPeremption.setCellFactory(DateOnlyTableCell.forTableColumn());
        tableColumnMedicConc.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList( DAOFactory.getMedicamentConcentrationDAO().findAllWithoutLot() )));
        tableColumnQuant.setCellFactory(TextFieldTableCell.forTableColumn( new IntegerStringConverter() ));
        editingColumn(tableColumnFabrication);
        editingColumn(tableColumnPeremption);
        editingColumn(tableColumnMedicConc);
        editingColumn(tableColumnQuant);
    }// fin update

    /**
     * Méthode d'ajout d'une action onEditCommit sur une TableColumn
     * cette méthode modifie également le Lot dans la base de données
     * @param column : la TableColumn à modifier
     */
    private void editingColumn(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            Lot lot = (Lot) e.getTableView().getItems().get(e.getTablePosition().getRow());
            switch (column.getId()) {
                case "tableColumnFabrication" -> lot.setDateFabrication( (Date) e.getNewValue() );
                case "tableColumnPeremption" -> lot.setDatePeremption( (Date) e.getNewValue() );
                case "tableColumnMedicConc" -> lot.setMedicamentConcentration( (MedicamentConcentration) e.getNewValue() );
                case "tableColumnQuant" -> lot.setQuantite( Integer.parseInt(e.getNewValue().toString()) );
            }
            try {
                /* modification dans la base de donnée */
                if (!this.lotDAO.update(lot)) {
                    DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.title")
                            , Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.content"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
            showAll();
        });
    }// fin editingColumn

    /**
     * Méthode de suppression d'un Lot
     * Cette méthode fait la mise à jour de la DB et aussi la mise à jour de l'interface
     */
    @FXML
    private void delete(){
        Lot lot = mainTableView.getSelectionModel().getSelectedItem();
        try {
            if (lot != null) {
                /* Demande à l'utilisateur s'il est sûr de vouloir supprimer */
                if (DialogBox.confirm(Store.bundle.getString("Lot.DialogBox.sureToDelete.title"),
                        Store.bundle.getString("Lot.DialogBox.sureToDelete"))) {
                    if (this.lotDAO.delete(lot)) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            } else {
                DialogBox.info("Info", Store.bundle.getString("view.lot.dialog.need"));
            }
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }
    }// fin delete
}
