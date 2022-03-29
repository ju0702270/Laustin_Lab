package model;

import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour une concentration d'un medicamentConcentration.
 */
public class Concentration {

    private int id;
    private String symbole;
    private double dose;

    /**
     * Constructeur Concentration
     * @param id : entier représentant la clé primaire dans la Base de donnée
     * @param symbole : le symbole de la concentration (ex: mg, ml, g,.. )
     * @param dose : double, dose de la concentration (ex: 100, 14.5, ..)
     */
    public Concentration(int id, String symbole, double dose){
        this.id=id;
        this.symbole=symbole;
        this.dose=dose;
    }

    /**
     * getter de l'id de la concentration
     * @return int: l'id de la concentration
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id de la concentration
     * @param id : nouvel id de la concentration
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter du symbole de la concentration (ex: mg, l, ml, g, ..)
     * @return String : symbole de la concentration
     */
    public String getSymbole() {
        return symbole;
    }

    /**
     * setter du symbole de la concentration
     * @param symbole : nouveau symbole de la concentration (ex: mg, l, ml, g, ..)
     */
    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    /**
     * getter de la dose de la concentration
     * @return double : la dose de la concentration
     */
    public double getDose() {
        return dose;
    }

    /**
     * setter de la dose de la concentration
     * @param dose : la nouvelle dose de la concentration
     */
    public void setDose(double dose) {
        this.dose = dose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concentration that = (Concentration) o;
        return id == that.id &&
                Double.compare(that.dose, dose) == 0 &&
                Objects.equals(symbole, that.symbole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbole, dose);
    }

    @Override
    public String toString() {
        return this.dose+ this.symbole;
    }
}
