package model;

import exceptions.DBException;
import utility.Store;

import java.util.Hashtable;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 07/03/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe Model qui contient certain élément important de l'application
 */
public class Model {

    private Client entreprise;
    private Utilisateur currentUser;
    private Hashtable<String, Object> hashLst;

    /**
     * Constructeur
     */
    public Model(){
        this.hashLst = new Hashtable<String, Object>();
    }

    /**
     * Méthode retournant l'entreprise
     * @return
     * @throws DBException
     */
    public Client getEntreprise() throws DBException {
        return Store.getEntreprise();
    }

    /**
     * Setter de l'entreprise
     * @param entreprise
     */
    public void setEntreprise(Client entreprise) {
        this.entreprise = entreprise;
    }

    /**
     * getter de hashLst
     * @return
     */
    public Hashtable<String, Object> getHashLst(){
        return this.hashLst;
    }

    /**
     * Méthode d'ajout d'un élément dans hashLst si la clé existe déjà, la clé ancienne sera remplacée ainsi que les valeurs
     * @param key
     * @param value
     */
    public void addElement(String key, Object value){
        if ( !this.hashLst.containsKey(key) ){
            this.hashLst.put(key,value);
        }else{
            this.hashLst.replace(key,value);
        }
    }

    /**
     * getter de currentUser
     * @return
     */
    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    /**
     * setter de currentUser
     * @param currentUser
     */
    public void setCurrentUser(Utilisateur currentUser) {
        this.currentUser = currentUser;
    }


}
