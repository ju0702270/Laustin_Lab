package model;
/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Enum des différent état de forme galénique possible
 */
public enum Etat {
    LIQUIDE("liquide"),SOLIDE("solide"),SOUPLE("souple"),SEMISOLIDE("semi-solide"),
    PRESURISE("présurisé"), GAZEUX("gazeux");

    private final String minuscule;

    private Etat(String minuscule) {
        this.minuscule= minuscule;
    }

    public String getMinuscule() {
        return minuscule;
    }
}
