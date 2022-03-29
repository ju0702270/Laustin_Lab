package model;

import java.util.Objects;

/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour la forme galénique d'un médicament.
 */
public class Forme {
    private int id;
    private String libelle;
    private String etat;
    /**
     * Constructeur Forme
     * @param id : l'id venant de la DB
     * @param libelle : le libelle, nom ou dénomination de la forme
     * @param etat : l'état de la forme (ex: solide, liquide, ..)
     */
    public Forme(int id, String libelle, String etat){
        this.id=id;
        this.libelle=libelle;
        this.etat=etat;
    }
    /**
     * getter de l'etat de la Forme
     * @return String : l'état de la forme (ex: solide, liquide, ..)
     */
    public String getEtat() {
        return etat;
    }
    /**
     * setter etat de la Forme
     * @param etat : l'état de la forme (ex: solide, liquide, ..)
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }
    /**
     * getter de l'id de la forme
     * @return int : la clé primaire de la Forme
     */
    public int getId() {
        return id;
    }
    /**
     * setter de l'id de la forme
     * @param id : la clé primaire de la forme
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * getter du libelle de la forme
     * @return String : le libelle, nom ou dénomination de la forme
     */
    public String getLibelle() {
        return libelle;
    }
    /**
     * setter du libelle de la forme
     * @param libelle : le nouveau libelle, non ou dénomination de la forme.
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Forme forme = (Forme) o;
        return id == forme.id &&
                Objects.equals(libelle, forme.libelle) &&
                Objects.equals(etat, forme.etat);
    }

    @Override
    public String toString() {
        return this.libelle +" "+ this.etat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, etat);
    }
}
