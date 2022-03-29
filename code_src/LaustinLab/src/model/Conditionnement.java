package model;

import utility.Store;

import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour un Conditionnement d'un medicamentConcentration.
 */
public class Conditionnement {
    private int id;
    private int unite;

    /**
     * Constructeur Conditionnement
     * @param id : entier représentant la clé primaire dans la base de donnée
     * @param unite : entier répésentant le nombre d'unité du conditionnement (ex: un médicament est conditionné dans des boite de 100 unité)
     */
    public Conditionnement(int id, int unite){
        this.id=id;
        this.unite=unite;
    }

    /**
     * getter de l'id du conditionnement
     * @return int: l'id du conditionnement
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id du conditionnement
     * @param id : le nouvel id du conditionnement
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter de l'unite du conditionnement
     * @return int: l'unité du conditionnement
     */
    public int getUnite() {
        return unite;
    }

    /**
     * setter de l'unite du conditionnement
     * @param unite : la nouvelle unité du conditionnement
     */
    public void setUnite(int unite) {
        this.unite = unite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conditionnement that = (Conditionnement) o;
        return id == that.id &&
                unite == that.unite;
    }

    @Override
    public String toString() {
        return Store.bundle.getString("model.conditionnement.pacquet") +": "+ unite ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unite);
    }
}
