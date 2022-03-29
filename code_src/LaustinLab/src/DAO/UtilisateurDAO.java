package DAO;

import exceptions.ConstraintException;
import exceptions.DBException;
import model.Facture;
import model.LigneFacture;
import model.Utilisateur;
import utility.Store;
import utility.Utility;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.Now;
import static utility.Utility.writeToLog;


/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 18/02/2021
 *     Modification : 21/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql des utilisateur dans la base de données pharmacy.
 */
public class UtilisateurDAO extends DAO<Utilisateur> {

    /**
     * Constructeur
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public UtilisateurDAO(Connection conn) {
        super(conn);
    }


    /**
     * Méthode de création d'un Utilisateur
     * @param obj : l'Utilisateur à créer dans la BDD
     * @return boolean
     *     true : si l'Utilisateur a bien été créé en BDD
     *     false : si l'utilisateur n'as pas été crée en BDD
     */
    @Override
    public boolean create(Utilisateur obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `Utilisateurs` " +
                    "(`id`, `nom`, `prenom`, `login` ,`password`) VALUES (null,?,?,?,?) ");
            prepState.setString(1, obj.getNom());
            prepState.setString(2, obj.getPrenom());
            prepState.setString(3, obj.getLogin());
            prepState.setString( 4, obj.getPassword() );
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notCreate.content"));
        }
        return false;
    }

    /**
     * Methode de suppression des données d'un Utilisateur dans la base de donnée
     * @param obj : l'Utilisateur à supprimer dans la BDD
     * @return boolean
     *     true : si l'Utilisateur a bien été supprimé de la BDD
     *     false : si l'utilisateur n'as pas été supprimé de la BDD
     * @throws DBException
     */
    @Override
    public boolean delete(Utilisateur obj) throws DBException, ConstraintException {
        try {
            //Update des factures ayant pour Utilisateur obj
            FactureDAO facDAO= DAOFactory.getFactureDAO();
            for (Facture f: facDAO.findAll(obj) ) {
                f.setUtilisateur(null);
                facDAO.update(f);
            }
            //Suppression de l'Utilisateur
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `utilisateurs` WHERE utilisateurs.id= ? ");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se){
            throw new DBException(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notDeleted.content"));
        }
    }

    /**
     * Methode de mise à jour des données d'un Utilisateur dans la Base de données
     * @param obj : l'Utilisateur à mettre à jour dans la BDD
     * @return boolean
     *     true : si l'Utilisateur a bien été supprimé de la BDD
     *     false : si l'utilisateur n'as pas été supprimé de la BDD
     * @throws DBException
     */
    @Override
    public boolean update(Utilisateur obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `utilisateurs` SET `nom` = ?, `prenom` = ? , `login` = ?," +
                    " `password` = ? WHERE `utilisateurs`.`id` = ? ");
            prepState.setString(1, obj.getNom() );
            prepState.setString(2, obj.getPrenom() );
            prepState.setString(3, obj.getLogin() );
            prepState.setString( 4,obj.getPassword() );
            prepState.setInt(5,obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//dialogBox + inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException( Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * Methode de recherche des données d'un utilisateur dans la base de données
     * @param id : l'identifiant de l'Utilisateur recherché
     * @return Utilisateur : l'utilisateur ayant comme identifiant id
     * @throws DBException
     */
    @Override
    public Utilisateur find(int id) throws DBException {
        Utilisateur user = null;
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM Utilisateurs u WHERE u.id=?");
            prepState.setInt(1, id );
            ResultSet result = prepState.executeQuery();
            if( result.first() ){
                return new Utilisateur(id, result.getString("nom"), result.getString("prenom"),
                        result.getString("login"),result.getString("password"));
            }
            prepState.close();
        } catch (SQLException e) {
            throw new DBException(Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notFound.content"));
        }
        return null;
    }

    /**
     * Méthode de recherche de toutes les données des Utilisateurs se trouvant dans la table utilisateur
     * @return ArrayList<Utilisateur> : une liste contenant tout les Utilisateurs de la table utilisateur
     * @throws DBException
     */
    @Override
    public ArrayList<Utilisateur> findAll() throws DBException {
        ArrayList<Utilisateur> lstUser=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM Utilisateurs");
            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                lstUser.add(new Utilisateur(result.getInt("id"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getString("login"),
                        result.getString("password")));
            }
            prepState.close();
            return lstUser;
        } catch (NullPointerException | SQLException e) {
            throw new DBException("findAll() :" + Store.bundle.getString("UtilisateurDAO.DialogBoxErr.notFound.content"));
        }
    }


}