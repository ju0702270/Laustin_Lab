package model;

import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour une Ville
 */
public class Ville {
    private int id;
    private int codePostal;
    private String libelle;
    private Pays pays;

    /**
     * Conctructeur Ville
     * @param id: représente la clé primaire de la Ville
     * @param codePostal : le code postal de la Ville
     * @param libelle : le libelle, nom de la Ville
     * @param pays : le Pays dans lequel se trouve la ville
     */
    public Ville(int id, int codePostal, String libelle, Pays pays){
        this.id=id;
        this.codePostal=codePostal;
        this.libelle=libelle;
        this.pays=pays;
    }

    /**
     * getter de l'id de la Ville
     * @return int : l'id de la Ville
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id de la Ville
     * @param id : nouvel id de la Ville
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter du code postal de la Ville
     * @return int : le code postal de la Ville
     */
    public int getCodePostal() {
        return codePostal;
    }

    /**
     * setter du code postal de la Ville
     * @param codePostal : le nouveau code postal de la ville
     */
    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    /**
     * getter du libelle de la Ville
     * @return String: le libelle de la Ville
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * sette du libelle de la Ville
     * @param libelle : le nouveau libelle de la Ville
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * getter du pays dans lequel se trouve la ville
     * @return Pays: le pays dans lequel se trouve la Ville
     */
    public Pays getPays() {
        return pays;
    }

    /**
     * setter du pays
     * @param pays : nouveau pays dans lequel se trouve la Ville
     */
    public void setPays(Pays pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return codePostal + " - " + libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ville ville = (Ville) o;
        return id == ville.id && codePostal == ville.codePostal && Objects.equals(libelle, ville.libelle) && Objects.equals(pays, ville.pays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codePostal, libelle, pays);
    }
}
