package DAO;
/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 02/02/2021
 *     Modification : 02/02/2021
 *     Revision : 1.0
 *     Description : Cette class permet d'effectuer des requetes sur le DATABASE pharmacy
 *     Utilisation : Classe Abstraite utilisée en tant que parent;
 *     @param <T> : le type d'objet Utilisé
 */
import exceptions.ConstraintException;
import exceptions.DBException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public abstract class DAO<T> {
    protected Connection conn;

    public void close(boolean commit) throws SQLException {
        if (commit){
            if ( !this.conn.getAutoCommit() ){ this.conn.commit(); }
        }
        this.conn.close();
    }
    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     */
    public DAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Méthode de création
     * @param obj : l'objet à créer dans la BDD
     * @return boolean
     *     true : si l'object à bien été créé en BDD
     *     false : si l'objet n'as pas été crée en BDD
     */
    public abstract boolean create(T obj) throws DBException, ConstraintException;
    /**
     * Méthode de suppresion
     * @param obj : l'objet à supprimer dans la BDD
     * @return boolean
     *     true : si l'object à bien été supprimé en BDD
     *     false : si l'objet n'as pas été supprimé en BDD
     */
    public abstract boolean delete(T obj) throws DBException, ConstraintException;
    /**
     * Méthode de mise à jour
     * @param obj : l'objet à mettre à jour dans la BDD
     * @return boolean
     *     true : si l'object à bien été mis à jour en BDD
     *     false : si l'objet n'as pas été mis à jour en BDD
     */
    public abstract boolean update(T obj) throws DBException, ConstraintException;
    /**
     * Méthode de recherche des information
     * @param id : l'identifiant de l'objet recherché
     * @return <T> l'objet trouvé
     */
    public abstract T find(int id) throws DBException;

    /**
     * Méthode recherche de tout les objets d'une table
     * @return ArrayList<T> : une liste avec tout les Objets trouvée
     */
    public abstract ArrayList<T> findAll() throws DBException;
}
