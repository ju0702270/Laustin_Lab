package controller;

import DAO.ConcentrationDAO;
import DAO.ConditionnementDAO;
import DAO.DAOFactory;
import DAO.FormeDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import exceptions.ConstraintException;
import exceptions.DBException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Concentration;
import model.Conditionnement;
import model.Etat;
import model.Forme;
import utility.DialogBox;
import utility.Store;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 12/03/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe controller pour les vues spécifies (Concentration, Forme, Conditionnement)
 */
public class SpecificiteController extends MainContent{

    private ConcentrationDAO concentrationDAO;
    private FormeDAO formeDAO;
    private ConditionnementDAO conditDAO;
    @FXML
    private TableView<Concentration> tableConcentration;
    @FXML
    private TableView<Forme> tableForme;
    @FXML
    private TableView<Conditionnement> tableConditionnement;
    @FXML
    private TableColumn tableColumnDose,tableColumnSymbole,tableColumnLibForme,tableColumnEtat,tableColumnUnite;
    @FXML
    private JFXTextField tfDose,tfSymbole,tfLibelle;
    @FXML
    private JFXComboBox<Etat> comboEtat;
    @FXML
    private Spinner<Integer> spinConcentration;
    @FXML
    private HBox HBboxConcentration,HBoxForme,HBoxCondtionnement;
    @FXML
    private JFXButton btnCreateConcentration,btnCreateForme,btnCreateCondit;

    /**
     * Constructeur de SpecificiteController
     * @param primaryStage : le stage de la fenetre principal, afin de pouvoir effectuer des actions sur le stage
     */
    public SpecificiteController(Stage primaryStage) {
        super(primaryStage);
        this.concentrationDAO = DAOFactory.getConcentrationDAO();
        this.formeDAO = DAOFactory.getFormeDAO();
        this.conditDAO = DAOFactory.getConditionnementDAO();
    }

    /**
     * Méthode implémentée pour les controlleurs en javaFX
     * Cette méthode va lancer showAll() afin d'afficher tous les Conditionnements, les Formes, les Concentration au moment où le controlleur est initialisé
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAll();
    }
    /**
     * Méthode d'affichage des spécificités dans leurs TableView Respectif
     */
    private void showAll(){
        ArrayList<Concentration> lstConentration = null;
        ArrayList<Forme> lstForme = null;
        ArrayList<Conditionnement> lstConditionnement = null;
        try {
            lstConentration= concentrationDAO.findAll();
            lstConditionnement= conditDAO.findAll();
            lstForme = formeDAO.findAll();
        } catch (DBException throwables) {
            DialogBox.error(Store.bundle.getString("specificity.DialogBoxErr.notFound.title"),
                    Store.bundle.getString("specificity.DialogBoxErr.notFound.content"));
        }
        tableColumnDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tableColumnSymbole.setCellValueFactory(new PropertyValueFactory<>("symbole"));
        tableColumnLibForme.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        tableColumnEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        tableColumnUnite.setCellValueFactory(new PropertyValueFactory<>("unite"));
        tableConcentration.setItems(FXCollections.observableArrayList( lstConentration ));
        tableForme.setItems( FXCollections.observableArrayList( lstForme ) );
        tableConditionnement.setItems( FXCollections.observableArrayList( lstConditionnement ) );
    }// fin showAll

    /**
     * Méthode pour faire apparaitre les HBoxConcentration qui contient les champs à remplir pour creer une Concentration
     */
    @FXML
    private void createConcentration(){
        HBboxConcentration.setDisable(false);
        HBboxConcentration.setVisible(true);
        btnCreateConcentration.setOnAction(e -> addConcentration());
    }// fin createConcentration

    /**
     * Méthode de comfirmation et d'ajout d'une concentration dans la base de données
     */
    @FXML
    private void addConcentration(){
        if (!tfDose.getText().equals("") && !tfSymbole.getText().equals("")){
            try {
                if (concentrationDAO.create(new Concentration(-1, tfSymbole.getText(), Double.parseDouble(tfDose.getText())))) {
                    showAll();
                    /* nettoyage et mise en non visible du HBox associé */
                    tfDose.clear();
                    tfSymbole.clear();
                    HBboxConcentration.setDisable(true);
                    HBboxConcentration.setVisible(false);
                    btnCreateConcentration.setOnAction(e -> createConcentration());
                } else {
                    DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.content"));
                }//end if else
            } catch (NumberFormatException e ){
                DialogBox.info(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("view.formatDouble"));
            } catch (DBException throwables) {
                throwables.printStackTrace();
                DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreated.constraintViolation.content"));
            }
        }else{
            DialogBox.info("Info", Store.bundle.getString("view.toutremplir"));
        }
    }// fin addConcentration

    /**
     * Méthode de suppression dans la base de donnée d'une concentration
     */
    @FXML
    private void deleteConcentration() {
        Concentration concentration = tableConcentration.getSelectionModel().getSelectedItem();
        try {
            if (concentration != null) {
                /* demande à l'utilisateur si il est sûr de vouloir supprimer */
                if (DialogBox.confirm(Store.bundle.getString("Concentration.sureToDelete.title"),
                        Store.bundle.getString("Concentration.sureToDelete"))) {
                    if (this.concentrationDAO.delete(concentration)) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            } else {
                DialogBox.info("Info", Store.bundle.getString("Concentration.need"));
            }
        }catch (ConstraintException e){
            /* Important car les concentrations liée à un Medicament concentré ne peuvent pas être supprimée */
            DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin deleteConcentration

    /**
     * Methode permettant la mise à jour des données dans tableConcentration
     * cette méthode permet la modification de certaines tableColumn
     */
    @FXML
    private void updateConcentration(){
        tableConcentration.setEditable(true);
        tableConcentration.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnDose.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableColumnSymbole.setCellFactory(TextFieldTableCell.forTableColumn());
        editingConcentration(tableColumnDose);
        editingConcentration(tableColumnSymbole);
    }// fin updateConcentration

    /**
     * Méthode d'ajout d'une action onEditCommit sur une TableColumn
     * cette méthode modifie également la concentration dans la base de données
     * @param column : la TableColumn à modifier
     */
    private void editingConcentration(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            Concentration concentration = (Concentration) e.getTableView().getItems().get(e.getTablePosition().getRow());
            try{
                if (tableColumnDose.equals(column)) {
                    concentration.setDose(Double.parseDouble(e.getNewValue().toString()));
                } else if (tableColumnSymbole.equals(column)) {
                    concentration.setSymbole(e.getNewValue().toString());
                }
                if (!this.concentrationDAO.update(concentration)) {
                    DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.title")
                            , Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.content"));
                }
            }catch(NumberFormatException error){
                DialogBox.info(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("view.formatDouble"));
            }catch (ConstraintException error){
                DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }catch (DBException error){
                DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.title"),
                        Store.bundle.getString("DBException.Message"));
            }
            showAll();
        });
    }// fin editingColumn

    /**
     * Méthode pour faire apparaitre les HBoxforme qui contient les champs à remplir pour creer une Forme
     */
    @FXML
    private void createForme(){
        comboEtat.setItems(FXCollections.observableArrayList( Etat.values() ));
        comboEtat.getSelectionModel().select(0);
        HBoxForme.setDisable(false);
        HBoxForme.setVisible(true);
        btnCreateForme.setOnAction(e -> addForme());
    }

    /**
     * Méthode de comfirmation et d'ajout d'une Forme dans la base de données
     */
    private void addForme(){
        if (!tfLibelle.getText().equals("") && comboEtat.getValue() != null){
            try {
                if (formeDAO.create( new Forme(-1, tfLibelle.getText(), comboEtat.getValue().getMinuscule() ))) {
                    showAll();
                    tfLibelle.clear();
                    HBoxForme.setDisable(true);
                    HBoxForme.setVisible(false);
                    btnCreateForme.setOnAction(e -> createForme());
                } else {
                    DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("FormeDAO.DialogBoxErr.notCreate.content"));
                }//end if else
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("FormeDAO.DialogBoxErr.notCreated.constraintViolation.content"));
            }
        }else{
            DialogBox.info("Info", Store.bundle.getString("view.toutremplir"));
        }
    }// fin addForme

    /**
     * Méthode de suppression dans la base de donnée d'une Forme
     */
    @FXML
    private void deleteForme(){
        Forme forme = tableForme.getSelectionModel().getSelectedItem();
        try {
            if (forme != null) {
                /* Demande à l'utilisateur si il est sûr de vouloir supprimer la forme */
                if (DialogBox.confirm(Store.bundle.getString("forme.sureToDelete.title"),
                        Store.bundle.getString("forme.sureToDelete"))) {
                    if (this.formeDAO.delete(forme)) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("FormeDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            } else {
                DialogBox.info("Info", Store.bundle.getString("forme.need"));
            }
        }catch (ConstraintException e){
            DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("FormeDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin deleteForme


    /**
     * Methode permettant la mise à jour des données dans tableForme
     * cette méthode permet la modification de certaines tableColumn
     */
    @FXML
    private void updateForme(){
        tableForme.setEditable(true);
        tableForme.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnLibForme.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnEtat.setCellFactory(ComboBoxTableCell.forTableColumn( FXCollections.observableArrayList(Etat.values() )));
        editingForme(tableColumnLibForme);
        editingForme(tableColumnEtat);
    }//fin updateForme

    /**
     * Méthode d'ajout d'une action onEditCommit sur une TableColumn
     * cette méthode modifie également la Forme dans la base de données
     * @param column : la TableColumn à modifier
     */
    private void editingForme(TableColumn column){
        column.setOnEditCommit(event -> {
            TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
            Forme forme = (Forme) e.getTableView().getItems().get(e.getTablePosition().getRow());
            try{
                if (tableColumnLibForme.equals(column)) {
                    forme.setLibelle( e.getNewValue().toString() );
                } else if (tableColumnEtat.equals(column)) {
                    forme.setEtat( e.getNewValue().toString() );
                }
                if (!this.formeDAO.update( forme )) {
                    DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.title")
                            , Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.content"));
                }
            }catch (DBException error){
                DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
            showAll();
        });
    }// fin editingForme

    /**
     * Méthode pour faire apparaitre les HBoxConditionnement qui contient les champs à remplir pour creer un conditionnement
     */
    @FXML
    private void createConditionnement(){
        spinConcentration.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000) );
        HBoxCondtionnement.setDisable(false);
        HBoxCondtionnement.setVisible(true);
        btnCreateCondit.setOnAction(e -> addConditionnement());
    }// fin createConditionnement

    /**
     * Méthode de comfirmation et d'ajout d'un Conditionnement dans la base de données
     */
    private void addConditionnement(){
        if ( spinConcentration.getValue() > 0 ){
            try {
                if (conditDAO.create( new Conditionnement(-1, spinConcentration.getValue()) )) {
                    showAll();
                    HBoxCondtionnement.setDisable(true);
                    HBoxCondtionnement.setVisible(false);
                    btnCreateCondit.setOnAction(e -> createConditionnement());
                } else {
                    DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"),
                            Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.content"));
                }//end if else
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"),
                        Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreated.constraintViolation.content"));
            }
        }else{
            DialogBox.info("Info", Store.bundle.getString("view.spinner.minone"));
        }
    }// fin addConditionnement

    /**
     * Méthode de suppression dans la base de donnée d'un Conditionnement
     */
    @FXML
    private void deleteConditionnement(){
        Conditionnement conditionnement = tableConditionnement.getSelectionModel().getSelectedItem();
        try {
            if (conditionnement != null) {
                /* Demande à l'utilisateur s'il est sûr de vouloir supprimer le conditionnement */
                if (DialogBox.confirm(Store.bundle.getString("conditionnement.sureToDelete.title"),
                        Store.bundle.getString("conditionnement.sureToDelete"))) {
                    if (this.conditDAO.delete( conditionnement )) {
                        showAll();
                    } else {
                        DialogBox.error(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.title"),
                                Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.content"));
                    }
                }
            } else {
                DialogBox.info("Info", Store.bundle.getString("conditionnement.need"));
            }
        }catch (ConstraintException e){
            DialogBox.error(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }catch (DBException u){
            DialogBox.error(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.title"),
                    Store.bundle.getString("DBException.Message"));
        }
    }// fin deleteConditionnement

    /**
     * Methode permettant la mise à jour des données dans tableConditionnement
     * cette méthode permet la modification de certaines tableColumn
     */
    @FXML
    private void updateConditionnement(){
        tableConditionnement.setEditable(true);
        tableConditionnement.setTooltip(new Tooltip(Store.bundle.getString("tooltip.update")));
        tableColumnUnite.setCellFactory(TextFieldTableCell.forTableColumn( new IntegerStringConverter() ));
        tableColumnUnite.setOnEditCommit(event ->{ // attribution d'une méthode lambda pour mettre à jour le conditionnement lorsqu'on change sa valeur dans tableColumnUnite
            try{
                TableColumn.CellEditEvent e = (TableColumn.CellEditEvent) event;
                Conditionnement conditionnement= (Conditionnement) e.getTableView().getItems().get(e.getTablePosition().getRow());
                conditionnement.setUnite( Integer.parseInt( e.getNewValue().toString() ) );
                if ( !conditDAO.update(conditionnement) ){
                    DialogBox.error(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notUpdate.title")
                            , Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notUpdate.content"));
                }
            }catch (NumberFormatException e){
                DialogBox.info(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notUpdate.title"),
                        Store.bundle.getString("view.formatInt"));
            } catch (DBException throwables) {
                DialogBox.error(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.title"),
                        Store.bundle.getString("DBException.Message"));
            } catch (ConstraintException throwables) {
                DialogBox.error(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.title"),
                        Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
            }
        });
    }// fin updateConditionnement

}
