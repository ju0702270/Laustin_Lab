package model;

import java.util.Date;
import java.util.Objects;

/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour le lot dans lequel se retrouve un médicament.
 */
public class Lot {
    private int id;
    private Date dateFabrication;
    private Date datePeremption;
    private MedicamentConcentration medicamentConcentration;
    private int quantite;



    /**
     * Constructeur Lot
     * @param id : la clé primaire du Lot
     * @param quantite : la quantité actuelle de médicament dans le Lot.
     */
    public Lot(int id,Date dateFabrication,Date datePeremption,MedicamentConcentration medicamentConcentration, int quantite ){
        this.id= id;
        this.dateFabrication= dateFabrication;
        this.datePeremption= datePeremption;
        this.medicamentConcentration=medicamentConcentration;
        this.quantite= quantite;
    }


    /**
     * getter de l'id du Lot
     * @return int : la clé primaire du Lot
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id du Lot
     * @param id : la clé primaire du Lot
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter de la date de fabrication du lot
     * @return Date : la date de fabrication du lot
     */
    public java.sql.Date getDateFabrication() {
        return new java.sql.Date(dateFabrication.getTime());
    }

    /**
     * setter de la date de fabrication du lot
     * @param dateFabrication : la nouvelle date de fabrication du lot
     */
    public void setDateFabrication(Date dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    /**
     * getter de la date d'expiration du lot
     * @return Date : la date d'expiration du lot
     */
    public java.sql.Date getDatePeremption() {
        return new java.sql.Date(datePeremption.getTime());
    }

    /**
     * setter de la date d'expiration du lot
     * Attention : dateExpiration doit être aprés dateFabrication.
     * @param datePeremption : la nouvelle date d'expiration du lot
     */
    public void setDatePeremption(Date datePeremption) {
        if (datePeremption.after(this.dateFabrication) ) {
            this.datePeremption = datePeremption;
        }
    }

    public MedicamentConcentration getMedicamentConcentration() {
        return medicamentConcentration;
    }

    public void setMedicamentConcentration(MedicamentConcentration medicamentConcentration) {
        this.medicamentConcentration = medicamentConcentration;
    }

    /**
     * getter de la quantité de medicamentConcentration dans le lot
     * @return int : la quantité actuelle de medicament_concentration
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * setter de la quantité de medicamentConcentration
     * @param quantite : la nouvelle quantite de medicamentConcentration dans le lot
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lot lot = (Lot) o;
        return id == lot.id &&
                quantite == lot.quantite &&
                Objects.equals(dateFabrication, lot.dateFabrication) &&
                Objects.equals(datePeremption, lot.datePeremption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateFabrication, datePeremption, quantite);
    }

    @Override
    public String toString() {
        return "Lot: "+ id + " quantite: " + quantite ;
    }



}
