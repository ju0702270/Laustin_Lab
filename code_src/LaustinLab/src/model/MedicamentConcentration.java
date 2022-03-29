package model;

import java.util.ArrayList;
import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour un MedicamentConcentration
 *                   le MédicamentConcentration représente le Medicament sous une certaine concentration et conditionné d'une certaine façon.
 */
public class MedicamentConcentration {



    private double tva;
    private int stock;
    private ArrayList<Lot> lots;
    private int id;
    private Medicament medicament;
    private Concentration concentration;
    private Conditionnement conditionnement;
    private Forme forme;
    private double prix;

    /**
     * Constructeur MedicamentConcentration
     * @param id : représente la clé primaire du MedicamentConcentration
     * @param medicament : le Medicament utilisé
     * @param concentration : la concentration utilisée
     * @param conditionnement : le Conditionnement du MedicamentConcentration
     * @param forme : la forme du MedicamentConcentration
     * @param prix : le prix unitaire du MedicamentConcentration
     * @param lots : ArrayList<Lot> reprenant tout les lots contenants le MedicamentConcentration
     */
    public MedicamentConcentration(int id, Medicament medicament, Concentration concentration, Conditionnement conditionnement
                                    , Forme forme, double prix, ArrayList<Lot> lots, double tva)
    {//TODO ajouter TVA
        this.id=id;
        this.medicament=medicament;
        this.concentration=concentration;
        this.conditionnement=conditionnement;
        this.forme=forme;
        this.prix=prix;
        this.lots=lots;
        this.tva=tva;
        this.stock= getStock();// attribut présent uniquement pour l'interface javaFX
    }

    /**
     * Méthode de sousstraction d'une quantité de stock dans les lots associés au MedicamentConcentration
     * @param aSoustraire : la quantité à soustraire
     */
    public void subQuantity(int aSoustraire){
        ArrayList<Lot> lstLots= (ArrayList<Lot>) this.lots.clone();
        ArrayList<Lot> triLot = new ArrayList<>();
        for (int i=0; i < this.lots.size(); i++) {
            Lot lastLost = getLastDatePeremption(lstLots);
            lstLots.remove(  lastLost );
            triLot.add( lastLost  );
        }
        for (Lot l: triLot) {
            if (l.getQuantite() == 0){
                continue;
            }else if (l.getQuantite()-aSoustraire < 0 ){
                // On retire la quantité de lot a aSoustraire car aSoustraire est plus grand que l.getQuantite()
                aSoustraire -= l.getQuantite();
                l.setQuantite(0);// on met la quantité de l à 0
                //boucle sur le suivant
            }else if (l.getQuantite()-aSoustraire >= 0){
                l.setQuantite( l.getQuantite() - aSoustraire );
                break;
            }
        }
    }

    /**
     * Méthode qui retrouve la derniere date de préremption dans une ArrayList<Lot>
     * @param lst : la liste dans laquel rechercher
     * @return
     */
    private Lot getLastDatePeremption(ArrayList<Lot> lst){
        Lot currentLot= lst.get(0);
        //Trouve le plus grand
        for (Lot l : lst) {
            if ( l.getDatePeremption().after(currentLot.getDatePeremption()) ){
                currentLot= l;
            }
        }
        return currentLot;
    }


    /**
     * Méthode de calcul du stock par rapport aux quantités présentent dans les lots
     * @return
     */
    public int getStock(){
        this.stock = 0;
        if (this.lots != null){
            for (Lot lot: this.lots) {
                this.stock+= lot.getQuantite();
            }
        }
        return this.stock;
    }

    public ArrayList<Lot> getLots() {
        return lots;
    }

    public void setLots(ArrayList<Lot> lots) {
        this.lots = lots;
    }

    /**
     * getter de l'id de MedicamentConcentration
     * @return int : l'id de MedicamentConcentration
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id de MedicamentConcentration
     * @param id : le nouvel id de MedicamentConcentration
     */
    public void setId(int id) {
        this.id = id;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    /**
     * getter du Medicament de MedicamentConcentration
     * @return Medicament: le Medicament de MedicamentConcentration
     */
    public Medicament getMedicament() {
        return medicament;
    }

    /**
     * setter du Medicament
     * @param medicament : le nouveau Medicament de MedicamentConcentration
     */
    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    /**
     * getter de la Concentration
     * @return Concentration : la concentration du MedicamentConcentration
     */
    public Concentration getConcentration() {
        return concentration;
    }

    /**
     * setter de la Concentration
     * @param concentration : la nouvelle concentration du MedicamentConcentration
     */
    public void setConcentration(Concentration concentration) {
        this.concentration = concentration;
    }

    /**
     * getter du Conditionnement
     * @return Conditionnement: le conditionnement du MedicamentConcentration
     */
    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    /**
     * setter du Conditionnement
     * @param conditionnement : la nouvelle concentration du MedicamentConcentration
     */
    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }


    /**
     * getter de la forme galénique
     * @return Forme : la forme galénique du MedicamentConcentration
     */
    public Forme getForme() {
        return forme;
    }

    /**
     * setter de la forme galénique
     * @param forme : la nouvelle forme galénique du MedicamentConcentration
     */
    public void setForme(Forme forme) {
        this.forme = forme;
    }

    /**
     * getter du prix
     * @return double: la prix du MedicamentConcentration
     */
    public double getPrix() {
        return prix;
    }

    /**
     * setter du prix
     * @param prix : nouveau prix du MedicamentConcentration
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicamentConcentration that = (MedicamentConcentration) o;
        return id == that.id &&
                Double.compare(that.prix, prix) == 0 &&
                Objects.equals(medicament, that.medicament) &&
                Objects.equals(concentration, that.concentration) &&
                Objects.equals(conditionnement, that.conditionnement) &&
                Objects.equals(forme, that.forme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, medicament, concentration, conditionnement, forme, prix);
    }

    @Override
    public String toString() {
        return this.medicament+ " ("+this.concentration+")";
    }
}
