package model;

import java.util.Objects;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe modele pour un Client
 */
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String denomination;
    private String numTva;
    private String rue;
    private String numero;
    private Ville ville;
    private String telephone;
    private String email;

    /**
     * Constructeur Client
     * @param id : représente la clé primaire du Client
     * @param nom : nom du Client
     * @param prenom : prenom du Client
     * @param denomination : dénomination de l'entreprise du Client
     * @param numTva : numéro de TVA de l'entreprise cliente
     * @param rue : rue de l'adresse de livraison
     * @param numero : numero de l'adresse de livraison
     * @param ville : ville de l'adresse de livraison
     * @param telephone : numero de téléphone du Client
     * @param email : e-mail officiel du Client
     */
    public Client(int id, String nom, String prenom, String denomination, String numTva, String rue
                    , String numero, Ville ville, String telephone, String email)
    {
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.denomination=denomination;
        this.numTva=numTva;
        this.rue=rue;
        this.numero=numero;
        this.ville=ville;
        this.telephone=telephone;
        this.email=email;
    }
    /**
     * Constructeur Client
     * @param id : représente la clé primaire du Client
     * @param denomination : dénomination de l'entreprise du Client
     * @param numTva : numéro de TVA de l'entreprise cliente
     * @param rue : rue de l'adresse de livraison
     * @param numero : numero de l'adresse de livraison
     * @param ville : ville de l'adresse de livraison
     * @param telephone : numero de téléphone du Client
     * @param email : e-mail officiel du Client
     */
    public Client(int id, String denomination, String numTva, String rue
            , String numero, Ville ville, String telephone, String email)
    {
        this.id=id;
        this.denomination=denomination;
        this.numTva=numTva;
        this.rue=rue;
        this.numero=numero;
        this.ville=ville;
        this.telephone=telephone;
        this.email=email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getNumTva() {
        return numTva;
    }

    public void setNumTva(String numTva) {
        this.numTva = numTva;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id && numero == client.numero && Objects.equals(nom, client.nom) && Objects.equals(prenom, client.prenom) && Objects.equals(denomination, client.denomination) && Objects.equals(numTva, client.numTva) && Objects.equals(rue, client.rue) && Objects.equals(ville, client.ville) && Objects.equals(telephone, client.telephone) && Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, denomination, numTva, rue, numero, ville, telephone, email);
    }

    @Override
    public String toString() {
        return denomination;
    }
}
