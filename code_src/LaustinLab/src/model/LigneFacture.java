package model;

import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour une ligne de Facture
 */
public class LigneFacture {

    private Facture facture;
    private MedicamentConcentration medicamentConcentration;
    private int quantite;
    private double prixTotal;

    /**
     * Constructeur ligneFacture
     * @param facture : représente la facture associée
     * @param medicamentConcentration : médicamentConcentration qui se trouve sur cette ligneFacture
     * @param quantite : quantite de medicamentConcentration sur cette ligneFacture
     * @param prixTotal : prix total HTVA de la ligneFacture
     */
    public LigneFacture(Facture facture, MedicamentConcentration medicamentConcentration, int quantite, double prixTotal) {
        this.facture=facture;
        this.medicamentConcentration=medicamentConcentration;
        this.quantite=quantite;
        this.prixTotal=prixTotal;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
    /**
     * getter du MedicamentConcentration relatif à la LigneFacture
     * @return le MedicamentConcentration relatif à la LigneFacture
     */
    public MedicamentConcentration getMedicamentConcentration() {
        return medicamentConcentration;
    }

    /**
     * setter du MedicamentConcentration relatif à la LigneFacture
     * @param medicamentConcentration : nouveau MedicamentConcentration relatif à la LigneFacture
     */
    public void setMedicamentConcentration(MedicamentConcentration medicamentConcentration) {
        this.medicamentConcentration = medicamentConcentration;
    }

    /**
     * getter de la Quantite de MedicamentConcentration relatif à la LigneFacture
     * @return entier : la quantité de MedicamentConcentration relatif à la LigneFacture
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * setter de la Quantite de MedicamentConcentration relatif à la LigneFacture
     * @param quantite : la nouvelle quantité de MedicamentConcentration relatif à la LigneFacture
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * getter du PrixTotal HTVA de la LigneFacture
     * @return double : le Prix total Hors TVA de la ligne facture
     */
    public double getPrixTotal() {
        return prixTotal;
    }

    /**
     * setter du PrixTotal HTVA de la LigneFacture
     * @param prixTotal : le nouveau prix total HTVA de la LigneFacutre
     */
    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneFacture that = (LigneFacture) o;
        return quantite == that.quantite && Double.compare(that.prixTotal, prixTotal) == 0 && Objects.equals(facture, that.facture) && Objects.equals(medicamentConcentration, that.medicamentConcentration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facture, medicamentConcentration, quantite, prixTotal);
    }

    @Override
    public String toString() {
        return this.medicamentConcentration+ " (Qty:"+this.quantite+")";
    }
}
