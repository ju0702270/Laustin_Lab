package model;

import java.sql.Date;
import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour un Medicament
 */
public class Medicament {
    private int id;
    private boolean prescritptionRequise;
    private Date dateBrevet;
    private String libelle;

    /**
     * Constructeur Medicament
     * @param id : entier représentant la clé primaire du Medicament.
     * @param prescriptionRequise : Boolean pour savoir si une prescription du medecin est requise ou pas
     * @param dateBrevet : Date du brevet.
     * @param libelle : String libelle du Medicament.
     */
    public Medicament(int id, boolean prescriptionRequise, Date dateBrevet, String libelle){
        this.id=id;
        this.prescritptionRequise=prescriptionRequise;
        this.dateBrevet=  dateBrevet;
        this.libelle=libelle;
    }



    /**
     * getter de l'id du Medicament
     * @return int : l'id du Medicament
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id du Medicament
     * @param id: le nouvel id du Medicament
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * methode pour savoir si oui ou non la prescription du médecin est requise pour ce Medicament
     * @return boolean
     */
    public boolean isPrescritptionRequise() {
        return prescritptionRequise;
    }

    /**
     * setter de la prescription requise du Medicament
     * @param prescritptionRequise : nouvelle valeur pour savoir si la prescription est requise
     */
    public void setPrescritptionRequise(boolean prescritptionRequise) {
        this.prescritptionRequise = prescritptionRequise;
    }

    /**
     * gette de la date du brevet du Medicament
     * @return Date : la date du brevet du Medicament
     */
    public Date getDateBrevet() {
        return new Date(dateBrevet.getTime());
    }

    /**
     * setter de la date du brevet du Medicament
     * @param dateBrevet : la nouvelle date du brevet du Medicament
     */
    public void setDateBrevet(Date dateBrevet) {
        this.dateBrevet = dateBrevet;
    }

    /**
     * getter du libelle du Medicament
     * @return String : le libelle du Medicament
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * setter du libelle du Medicament
     * @param libelle : nouveau libelle du Medicament
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicament that = (Medicament) o;
        return id == that.id &&
                prescritptionRequise == that.prescritptionRequise &&
                Objects.equals(dateBrevet, that.dateBrevet) &&
                Objects.equals(libelle, that.libelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prescritptionRequise, dateBrevet, libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}
