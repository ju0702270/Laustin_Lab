package model;

import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour un Pays
 */
public class Pays {
    private int id;
    private String libelle;

    /**
     * Constructeur Pays
     * @param id : entier représentant la clé primaire du Pays
     * @param libelle : chaine de caractère du nom, libelle du Pays
     */
    public Pays(int id, String libelle){
        this.id=id;
        this.libelle=libelle;
    }

    /**
     * getter de l'id du Pays
     * @return int : l'id du Pays
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id du Pays
     * @param id : le nouvel id du Pays
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter du libelle du Pays
     * @return String : le libelle du Pays
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * setter du libelle du Pays
     * @param libelle : le nouveau libelle du Pays
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pays pays = (Pays) o;
        return id == pays.id  && Objects.equals(libelle, pays.libelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle);
    }
}
