package controller;

import DAO.DAOFactory;
import DAO.MedicamentDAO;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import controller.component.DateOnlyTableCell;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import model.Medicament;
import utility.DialogBox;
import utility.Store;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 10/03/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe controller pour la vue view.MedicamentView.fxml et view.popUpMedicament.fxml
 */
public class MedicamentController extends MainContent{
    private  final MedicamentDAO medDAO;
    @FXML
    private TableView<Medicament> mainTableView;
    @FXML
    private TableColumn tableColumnIdMed, tableColumnLibMed, tableColumnDateBrev,tableColumnPrescr;
    @FXML
    private JFXTextField tfLibelle;
    @FXML
    private JFXDatePicker dpBrevet;
    @FXML
    private JFXCheckBox checkBoxPresc;
    /**
     * Constructeur de MédicamentController
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public MedicamentController(Stage primaryStage) {
        super(primaryStage);
        this.medDAO = DAOFactory.getMedicamentDAO();
    }// fin constructeur
    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * Cette méthode va lancer showAll() afin d'afficher tous les Medicaments au moment où le controlleur est initialisé
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAll();
    }// fin initialise

    /**
     * Méthode d'affichage de tout les Medicament dans mainTableView
     */
    private void showAll(){
        ArrayList<Medicament> lstMedicament = null;
        try {
            lstMedicament = medDAO.findAll();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("MedicamentDAO.DialogBoxErr.notFound.content"));
        }
        tableColumnIdMed.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnLibMed.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        tableColumnDateBrev.setCellValueFactory(new PropertyValueFactory<>("dateBrevet"));
        tableColumnPrescr.setCellValueFactory(new PropertyValueFactory<>("prescritptionRequise"));
        mainTableView.setItems( FXCollections.observableArrayList(lstMedicament) );
    }// fin showAll
    /**
     * Méthode de création de la fenetre de création d'un Medicament (view.popUpMedicament.fxml)
     * @throws IOException
     */
    @FXML
    private void create() throws IOException {
        Stage stage = createPopUp("popUpMedicament",this);
        dpBrevet.setValue(LocalDate.now());
        stage.show();
    }// fin create
    /**
     * Méthode de création d'un Medicament en tant que tels
     * @param event
     */
    @FXML
    private void createMedicament(Event event){
        /* validation */
        if ( dpBrevet.getValue() != null && !tfLibelle.getText().equals("") ){
            try {
                if ( medDAO.create( new Medicament(-1,checkBoxPresc.isSelected(), Date.valueOf(dpBrevet.getValue()), tfLibelle.getText()) )){//end new Medicament
                    showAll();
                    closeStage(event);
                }else{
                    DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("MedicamentDAO.DialogBoxErr.notCreate.content"));
                }//end if else
            } catch (DBException | ConstraintException throwables) {
                throwables.printStackTrace();
                DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            }
        }else{
            DialogBox.info("Info", Store.bundle.getString("view.toutremplir"));
        }
    }

    /**
     * Methode permettant la mise à jour des données dans mainTableView
     * cette méthode permet la modification de certaines tableColumn
     */
    @FXML
    private void update() throws DBException {
        mainTableView.setEditable(true);
        mainTableView.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnLibMed.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnDateBrev.setCellFactory(DateOnlyTableCell.forTableColumn());
        tableColumnPrescr.setEditable(true);
        tableColumnPrescr.setCellFactory(ComboBoxTableCell.forTableColumn(true,false));
        editingColumn(tableColumnLibMed);
        editingColumn(tableColumnDateBrev);
        editingColumn(tableColumnPrescr);
    }// fin update

    /**
     * Méthode d'ajout d'une action onEditCommit sur une TableColumn
     * cette méthode modifie également le Medicament dans la base de données
     * @param column : la TableColumn à modifier
     */
    private void editingColumn(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            Medicament medicament = (Medicament) e.getTableView().getItems().get(e.getTablePosition().getRow());
            switch (column.getId()) {
                case "tableColumnLibMed" -> medicament.setLibelle( e.getNewValue().toString() );
                case "tableColumnDateBrev" -> medicament.setDateBrevet( (Date) e.getNewValue() );
                case "tableColumnPrescr" -> medicament.setPrescritptionRequise( (Boolean) e.getNewValue() );
            }
            try {
                if (!this.medDAO.update(medicament)) {
                    DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.title")
                            , Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.content"));
                }
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
            showAll();
        });
    }// fin editingColumn

    /**
     * Méthode de suppression d'un Medicament
     * Cette méthode fait la mise à jour de la DB et aussi la mise à jour de l'interface
     */
    @FXML
    private void delete(){
        Medicament medicament = mainTableView.getSelectionModel().getSelectedItem();
        try {
            if (medicament != null) {
                /* Demande à l'utilisateur s'il est sûr de vouloir supprimer */
                if (DialogBox.confirm(Store.bundle.getString("Medicament.DialogBox.sureToDelete.title"),
                        Store.bundle.getString("Medicament.DialogBox.sureToDelete"))) {
                    if (this.medDAO.delete(medicament)) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("MedicamentDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            } else {
                DialogBox.info("Info", Store.bundle.getString("Medicament.DialogBox.need"));
            }
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        } catch (ConstraintException throwables) {
            DialogBox.error(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("MedicamentDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }
    }// fin delete
}
