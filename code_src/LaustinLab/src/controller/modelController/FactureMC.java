package controller.modelController;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import model.Facture;
import model.LigneFacture;
/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 07/03/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe destinée à modifier une facture pour lui ajouter un ComboBox reprenant toutes les valeurs des LignesFactures associé à la facture
 *          Cette Classe est redéfini pour palier au problème d'affichage de javaFX
 */
public class FactureMC extends Facture {

    private JFXComboBox<LigneFacture> comboLigneFacture;

    /**
     * Constucteur
     * @param facture : la Facture à partir de laquel on va creer FactureMC
     */
    public FactureMC(Facture facture) {
        super(facture.getId(), facture.getClient(), facture.getUtilisateur(), facture.getDateHeure(), facture.getLigneFacture(), facture.getTotal(), facture.getEntreprise());
        this.comboLigneFacture = new JFXComboBox<LigneFacture>(FXCollections.observableArrayList(this.getLigneFacture()));
        this.comboLigneFacture.setPrefWidth(400);
        this.comboLigneFacture.getSelectionModel().select(0);
    }

    /**
     * Méthode getter pour les comboBox LigneFacture
     * @return
     */
    public ComboBox<LigneFacture> getComboLigneFacture() {
        return comboLigneFacture;
    }

    /**
     * Méthode setter pour les comboBox LigneFacture
     * @param comboLigneFacture
     */
    public void setComboLigneFacture(JFXComboBox<LigneFacture> comboLigneFacture) {
        this.comboLigneFacture = comboLigneFacture;
    }

    @Override
    public String toString() {
        return this.getId()+" - "+this.getClient()+ " - "+ this.getDateHeure() +" - "+this.getTotal();
    }
}
