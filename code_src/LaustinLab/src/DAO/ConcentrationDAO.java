package DAO;


import exceptions.ConstraintException;
import exceptions.DBException;
import model.Concentration;
import utility.Store;

import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.writeToLog;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 19/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql de la table Concentration dans la base de données pharmacy.
 */
public class ConcentrationDAO extends DAO<Concentration> {
    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public ConcentrationDAO(Connection conn) {
        super(conn);
    }

    /**
     * Méthode de création d'une Concentration dans la base de donnée.
     *
     * @param obj : la Concentration à créer dans la BDD
     * @return boolean
     *      true : si l'objet à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(Concentration obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `concentration` (`id`, `symbole`, `dose`) VALUES (NULL, ?, ?)");
            prepState.setString(1, obj.getSymbole());
            prepState.setDouble(2,obj.getDose());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(), Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){ // dialogBox + inscription dans le logfile
            throw new ConstraintException(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notCreate.title"));
        }
        return false;
    }

    /**
     * Méthode de suppresion des données d'une Concentration dans la base de donnée
     * Si au moins un MedicamentConcentration à comme Concentration obj, alors obj ne sera pas supprimé de la base de données.
     * @param obj : l'objet à supprimer dans la BDD
     * @return boolean
     *      true : si l'objet à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(Concentration obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `concentration` WHERE `concentration`.`id` = ?");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se){
            throw new DBException( Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notDeleted.title"));
        }
    }

    /**
     * Méthode de mise à jour des données d'une Concentration dans la base de donnée
     *
     * @param obj : la Concentration à mettre à jour dans la BDD
     * @return boolean
     *      true : si l'objet à bien été mis à jour en BDD
     *      false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(Concentration obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `concentration` SET `symbole` = ?, `dose` = ? WHERE `concentration`.`id` = ? ");
            prepState.setString(1, obj.getSymbole());
            prepState.setDouble(2,obj.getDose());
            prepState.setInt(3,obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){// inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.nullPointer.content") );
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException( Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * Méthode de recherche des données d'une Concentration à partir de son identifiant
     *
     * @param id : l'identifiant de la Concentration recherchée
     * @return Concentration : l'objet trouvé
     */
    @Override
    public Concentration find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `concentration` c  WHERE c.id = ?");
            prepState.setInt(1, id);
            ResultSet result = prepState.executeQuery();
            if (result.first()) {
                return new Concentration(id, result.getString(2), result.getDouble(3));
            }
            prepState.close();
        } catch(SQLException e){
            throw new DBException( Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notFound.title") );
        }
        return null;
    }


    /**
     * Méthode recherche de toutes les Concentration de la table Concentration
     *
     * @return ArrayList<Concentration> : une liste avec toute les Concentration trouvée
     */
    @Override
    public ArrayList<Concentration> findAll() throws DBException {
        ArrayList<Concentration> lstConcentration=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `concentration`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                PaysDAO p = DAOFactory.getPaysDAO();
                lstConcentration.add(
                        new Concentration(result.getInt(1), result.getString(2), result.getDouble(3))
                );
            }
            prepState.close();
            return lstConcentration;
        } catch (SQLException e) {
            throw new DBException("findAll() :"+ Store.bundle.getString("ConcentrationDAO.DialogBoxErr.notFound.title") );
        }
    }
}
