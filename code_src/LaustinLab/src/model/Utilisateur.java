package model;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static utility.Utility.getHash;

/**
*     Author : Rochez Justin, Sapin Laurent
*     Creation : 29/01/2021
*     Modification : 29/01/2021
*     Revision : 1.0
*     Description : Classe modele pour un utilisateur de l'application.
*/
public class Utilisateur {

    private int id;
    private String nom;
    private String prenom;
    private String login;
    private String password;

    /**
     * Constructeur Utilisateur
     * @param id : clé primaire de l'utilisateur
     * @param nom : nom de l'utilisateur
     * @param prenom : prenom de l'utilisateur
     * @param login : login de l'utilisateur
     * @param password : mot de passe de l'utilisateur.
     */
    public Utilisateur(int id, String nom, String prenom, String login, String password){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.login=login;
        this.password=password;
    }
    /**
     * Constructeur Utilisateur
     * @param id : clé primaire de l'utilisateur
     * @param prenom : prenom de l'utilisateur
     * @param login : login de l'utilisateur
     * @param password : mot de passe de l'utilisateur.
     */
    public Utilisateur(int id, String prenom, String login, String password) {
        this.id=id;
        this.nom=null;
        this.prenom=prenom;
        this.login=login;
        this.password=password;
    }
    /**
     * Constructeur Utilisateur
     * @param id : clé primaire de l'utilisateur
     * @param nom : nom de l'utilisateur
     * @param login : login de l'utilisateur
     * @param password : mot de passe de l'utilisateur.
     */
    public Utilisateur( String nom, int id, String login, String password) {
        this.id=id;
        this.nom=nom;
        this.prenom=null;
        this.login=login;
        this.password=password;
    }
    /**
     * Constructeur Utilisateur
     * @param id : clé primaire de l'utilisateur
     * @param login : login de l'utilisateur
     * @param password : mot de passe de l'utilisateur.
     */
    public Utilisateur(int id, String login, String password) throws NoSuchAlgorithmException {
        this.id=id;
        this.nom=null;
        this.prenom=null;
        this.login=login;
        this.password=password;
    }

    /**
     * getter de l'id de l'utilisateur
     * @return int : l'id de l'utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     * setter de l'id de l'utilisateur
     * @param id : le nouvel id de l'utilisateur
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter du nom de famille de l'utilisateur
     * @return String : le nom de famille de l'utilisateur
     */
    public String getNom() {
        return nom;
    }

    /**
     * setter du nom de famille de l'utilisateur
     * @param nom : le nom de famille de l'utilisateur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * getter du prenom de l'utilisateur
     * @return String : le prenom de l'utilisateur
     */
    public String getPrenom() {
        return prenom;
    }
    /**
     * setter du prenom de l'utilisateur
     * @param prenom : le prenom de l'utilisateur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * getter du login de l'utilisateur
     * @return String : le login de l'utilisateur
     */
    public String getLogin() {
        return login;
    }

    /**
     * setter du login de l'utilisateur
     * @param login : le nouveau login de l'utilisateur
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * getter du mot de passe de l'utilisateur
     * @return String : le mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter du mot de passe de l'utilisateur
     * Cette méthode utilise l'algorithme SHA-256. le nouveau mot de passe doit donc être en clair
     * @param password : le nouveau mot de passe de l'utilisateur
     */
    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password =getHash(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id == that.id &&
                Objects.equals(nom, that.nom) &&
                Objects.equals(prenom, that.prenom) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, login, password);
    }

    @Override
    public String toString() {
        return login;
    }

    public static void main(String[] args) {
        Utilisateur u = new Utilisateur(1,"yo","yo","admin","bla");
        System.out.println(u.hashCode());
    }
}
