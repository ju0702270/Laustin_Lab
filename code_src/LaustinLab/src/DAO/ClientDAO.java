package DAO;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 19/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql de la table client dans la base de données pharmacy.
 */

import exceptions.ConstraintException;
import exceptions.DBException;
import model.Client;
import model.Facture;
import utility.Store;

import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.writeToLog;

public class ClientDAO extends DAO<Client>{
    /**
     * Constructeur ClientDAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public ClientDAO(Connection conn) {
        super(conn);
    }

    /**
     * Méthode de création du Client
     *
     * @param obj : le Client à créer dans la BDD
     * @return boolean
     *      true : si l'objet à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(Client obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `client` " +
                    "(`id`, `nom`, `prenom`, `denomination`, `numTva`, `rue`, `numero`, `idVille`, `telephone`, `email`) " +
                    "VALUES (NULL, ? , ? , ? , ? , ? , ? , ? , ? , ?) ");
            prepState.setString(1, obj.getNom() );
            prepState.setString(2, obj.getPrenom() );
            prepState.setString(3, obj.getDenomination() );
            prepState.setString(4,obj.getNumTva());
            prepState.setString(5,obj.getRue() );
            prepState.setString(6, obj.getNumero() );
            prepState.setInt(7, obj.getVille().getId() );
            prepState.setString(8,obj.getTelephone());
            prepState.setString(9,obj.getEmail());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("ClientDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){ // dialogBox + inscription dans le logfile
            throw new ConstraintException(Store.bundle.getString("ClientDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("ClientDAO.DialogBoxErr.notCreate.title"));
        }
        return false;
    }

    /**
     * Méthode de suppresion des données d'un Client dans la Base de donnée
     * Cette méthode va mettre à jour toutes les Factures associée à ce Client
     * @param obj : le Client dont il faut supprimer les données dans la BDD
     * @return boolean
     *      true : si l'object à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(Client obj) throws DBException, ConstraintException {
        try {
            FactureDAO fDAO = DAOFactory.getFactureDAO();
            //Update de toutes les facture associée au client
            for (Facture f: fDAO.findAll(obj)) {
                f.setClient(null);
                fDAO.update(f);
            }
            //suppression du Client
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `client` WHERE `client`.`id` = ?");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("ClientDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se){
            throw new DBException(Store.bundle.getString("ClientDAO.DialogBoxErr.notDeleted.content"));
        }
    }

    /**
     * Méthode de mise à jour des données d'un Client dans la base de données
     *
     * @param obj : le Client à mettre à jour dans la BDD
     * @return boolean
     *      true : si l'objet à bien été mis à jour en BDD
     *      false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(Client obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `client` SET `nom` = ?," +
                    " `prenom` = ?," +
                    " `denomination` = ?," +
                    " `numTva` = ?," +
                    " `rue` = ?," +
                    " `numero` = ?," +
                    " `idVille` = ?," +
                    " `telephone` = ?," +
                    " `email` = ?" +
                    " WHERE `client`.`id` = ?");
            prepState.setString(1, obj.getNom() );
            prepState.setString(2, obj.getPrenom() );
            prepState.setString(3, obj.getDenomination() );
            prepState.setString(4,obj.getNumTva());
            prepState.setString(5,obj.getRue() );
            prepState.setString(6, obj.getNumero() );
            prepState.setInt(7, obj.getVille().getId() );
            prepState.setString(8,obj.getTelephone());
            prepState.setString(9,obj.getEmail());
            prepState.setInt(10,obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){// inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.nullPointer.content") );
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException( Store.bundle.getString("ClientDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * Méthode de recherche des données d'un Client dans la BDD
     *
     * @param id : l'identifiant de lu Client recherché
     * @return <Client> le Client trouvé
     */
    @Override
    public Client find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `client` WHERE id = ?");
            prepState.setInt(1, id );
            ResultSet result = prepState.executeQuery();
            if( result.first() ){
                VilleDAO v = DAOFactory.getVilleDAO();
                return new Client(id,   result.getString(2), result.getString(3),
                                        result.getString(4), result.getString(5),
                                        result.getString(6), result.getString(7),
                                        v.find(result.getInt(8)), result.getString(9),
                                        result.getString(10));
            }
            prepState.close();
        } catch (SQLException e) {
            throw new DBException(  Store.bundle.getString("ClientDAO.DialogBoxErr.notFound.content"));
        }
        return null;
    }

    /**
     * Méthode recherche de tout les client de la table Client
     *
     * @return ArrayList<Client> : une liste avec tout les Client trouvée
     */
    @Override
    public ArrayList<Client> findAll() throws DBException {
        ArrayList<Client> lstClient=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `client`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                VilleDAO v = DAOFactory.getVilleDAO();
                lstClient.add( new Client(result.getInt(1),
                        result.getString(2), result.getString(3),
                        result.getString(4), result.getString(5),
                        result.getString(6), result.getString(7),
                        v.find(result.getInt(8)), result.getString(9),
                        result.getString(10)) );
            }
            prepState.close();
            return lstClient;
        } catch (SQLException e) {
            throw new DBException(  Store.bundle.getString("ClientDAO.DialogBoxErr.notFound.content"));
        }
    }

}
