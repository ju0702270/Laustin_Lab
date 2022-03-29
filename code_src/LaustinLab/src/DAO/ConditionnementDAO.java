package DAO;

import exceptions.ConstraintException;
import exceptions.DBException;
import model.Conditionnement;
import utility.Store;

import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.writeToLog;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 19/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql de la table Conditionnement dans la base de données pharmacy.
 */
public class ConditionnementDAO extends DAO<Conditionnement> {
    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public ConditionnementDAO(Connection conn) {
        super(conn);
    }

    /**
     * Méthode de création des données d'un Conditionnement dans la base de donnée
     *
     * @param obj : le Conditionnement à créer dans la BDD
     * @return boolean
     *      true : si l'objet à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(Conditionnement obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `conditionnement` (`id`, `unite`) VALUES (NULL, ?) ");
            prepState.setInt(1, obj.getUnite());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(), Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){ // dialogBox + inscription dans le logfile
            throw new ConstraintException(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException( Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notCreate.title"));
        }
        return false;    }

    /**
     * Méthode de suppression des données d'un Conditionnement dans la base de donnée
     *
     * @param obj : le Conditionnement à supprimer dans la BDD
     * @return boolean
     *      true : si l'objet à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(Conditionnement obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `conditionnement` WHERE `conditionnement`.`id` = ?");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se){
            throw new DBException(  Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notDeleted.title"));
        }
    }

    /**
     * Méthode de mise à jour des données d'un Condtionnement dans la base de données
     *
     * @param obj : le Condtionnement à mettre à jour dans la BDD
     * @return boolean
     *      true : si l'objet à bien été mis à jour en BDD
     *      false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(Conditionnement obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `conditionnement` SET `unite` = ? WHERE `conditionnement`.`id` = ? ");
            prepState.setInt(1, obj.getUnite());
            prepState.setInt(2,obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){// inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notUpdate.nullPointer.content") );
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException(  Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;    }

    /**
     * Méthode de recherche des données d'un Conditionnement dans la base de donnée
     *
     * @param id : l'identifiant du Condtionnement recherché
     * @return Conditionnement : l'objet trouvé
     */
    @Override
    public Conditionnement find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `conditionnement` c  WHERE c.id = ?");
            prepState.setInt(1, id);
            ResultSet result = prepState.executeQuery();
            if (result.first()) {
                return new Conditionnement(id,result.getInt(2));
            }
            prepState.close();
        } catch(SQLException e){
            throw new DBException(  Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notFound.title") );
        }
        return null;
    }

    /**
     * Methode recherche de tout les Condtionnement de la table conditionnement
     *
     * @return ArrayList<Condtionnement> : une liste avec tout les Conditionnement trouvé
     */
    @Override
    public ArrayList<Conditionnement> findAll() throws DBException {
        ArrayList<Conditionnement> lstCond=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `conditionnement`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                PaysDAO p = DAOFactory.getPaysDAO();
                lstCond.add(
                        new Conditionnement(result.getInt(1),result.getInt(2))
                );
            }
            prepState.close();
            return lstCond;
        } catch (SQLException e) {
            throw new DBException( "findAll() : "+ Store.bundle.getString("ConditionnementDAO.DialogBoxErr.notFound.title") );
        }
    }
}
