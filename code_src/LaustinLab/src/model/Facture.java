package model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour une Facture
 */
public class Facture {
    private ArrayList<LigneFacture> ligneFacture;
    private int id;
    private Client client;
    private Utilisateur utilisateur;
    private Timestamp dateHeure;
    private Double total;
    private Client entreprise;


    /**
     * Constructeur Facture
     * @param id : représente la clé primaire de la Facture
     * @param client : le client à qui est adressée la Facture
     * @param utilisateur : l'utilisateur qui s'est occupé de la Facture
     * @param dateHeure : la date et l'heure de création de la Facture
     * @param ligneFacture : un ArrayList contenant tout les lignes factures ayant rapport avec la Facture
     * @param total : un Double représentant le prix total HTVA de la Facture.
     * @param entreprise : l'entreprise (Client) qui emet la Facture
     */
    public Facture(int id, Client client, Utilisateur utilisateur,Timestamp dateHeure, ArrayList<LigneFacture> ligneFacture,Double total, Client entreprise) {
        this.id=id;
        this.client=client;
        this.utilisateur=utilisateur;
        this.dateHeure=dateHeure;
        this.ligneFacture = ligneFacture;
        this.total= total;
        this.entreprise=entreprise;
    }


    public ArrayList<LigneFacture> getLigneFacture() {
        return ligneFacture;
    }

    public void setLigneFacture(ArrayList<LigneFacture> ligneFacture) {
        this.ligneFacture = ligneFacture;
    }

    /**
     * getter de l'id de la facture
     * @return entier : l'id de la facture
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id de la facture
     * @param id : le nouvel id de la facture
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter du Client vers qui la Facture est adressée
     * @return Client: le client vers qui la Facture est adressée
     */
    public Client getClient() {
        return client;
    }

    /**
     * setter du Client vers qui la Facture est adressée
     * @param client : le nouveau Client vers qui la Facture est adressée
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * getter de l'Utilisateur qui s'est occupé de la Facture
     * @return Utilisateur : l'utilisateur de l'application qui s'est occupé de la Facture
     */
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * setter de l'Utilisateur qui s'est occupé de la Facture
     * @param utilisateur : le nouveau Utilisateur qui s'est occupé de la Facture
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    /**
     * getter de la Date et Heure à laquelle la Facture a été créée
     * @return Timestamp : la Date et Heure à laquelle la Facture a été créée
     */
    public Timestamp getDateHeure() {
        return dateHeure;
    }

    /**
     * setter de la Date et Heure de la Facture
     * @param dateHeure : la nouvelle Date et Heure de la Facture
     */
    public void setDateHeure(Timestamp dateHeure) {
        this.dateHeure = dateHeure;
    }

    /**
     * getter du Total de la facture HTVA
     * @return double, le total de la facture HTVA
     */
    public Double getTotal() {
        return total;
    }

    /**
     * setter du total de la facture HTVA
     * @param total: le nouveau total de la facture HTVA
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * getter de l'entreprise qui emet la Facture.
     * @return Client: un objet Client représentant l'entreprise qui emet la facture
     */
    public Client getEntreprise() {
        return entreprise;
    }

    /**
     * setter de l'entreprise qui emet la Facture
     * @param entreprise : la nouvelle entreprise qui emet la Facture
     */
    public void setEntreprise(Client entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public String toString() {
        return this.getId()+" - "+this.getClient()+ " - "+ this.getDateHeure() +" - "+this.getTotal();
    }
}
