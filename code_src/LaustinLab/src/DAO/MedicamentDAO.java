package DAO;


import exceptions.ConstraintException;
import exceptions.DBException;
import model.Medicament;
import model.MedicamentConcentration;
import utility.Store;

import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.writeToLog;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 18/02/2021
 *     Modification : 21/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql des medicament dans la base de données pharmacy.
 */
public class MedicamentDAO extends DAO<Medicament> {
    /**
     * Constructeur MedicamentDAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn
     */
    public MedicamentDAO(Connection conn) {
        super(conn);
    }

    /**
     * Méthode de création d'un Medicament
     *
     * @param obj : l'objet à créer dans la BDD
     * @return boolean
     *      true : si l'object à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(Medicament obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `medicament` (`id`, `prescriptionRequise`, `dateBrevet`, `libelle`) " +
                    "VALUES (NULL, ?, ?, ?) ");
            prepState.setBoolean(1, obj.isPrescritptionRequise());
            prepState.setDate(2, obj.getDateBrevet());
            prepState.setString(3, obj.getLibelle());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("MedicamentDAO.DialogBoxErr.notCreate.content"));
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ConstraintException(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        }
        return false;
    }

    /**
     * Méthode de  d'un Medicament
     *
     * @param obj : le Medicament à supprimer dans la BDD
     * @return boolean
     *      true : si l'object à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(Medicament obj) throws DBException, ConstraintException {
        try {
            //Suppression des MedicamentConcentration ayant pour Medicament obj
            MedicamentConcentrationDAO medConcDAO = DAOFactory.getMedicamentConcentrationDAO();
            for (MedicamentConcentration m: medConcDAO.findAll(obj) ) {
                medConcDAO.delete(m);
            }
            //Suppression de obj
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `medicament` WHERE `medicament`.`id` = ? ");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        }catch (SQLException se){
            throw new DBException(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notDeleted.content"));
        }
    }

    /**
     * Méthode de mise à jour d'un Medicament
     *
     * @param obj : le Medicament à mettre à jour dans la BDD
     * @return boolean
     *      true : si l'object à bien été mis à jour en BDD
     *      false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(Medicament obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `medicament` SET `prescriptionRequise` = ?," +
                                                                " `dateBrevet` = ?," +
                                                                " `libelle` = ? WHERE `medicament`.`id` = ? ");
            prepState.setBoolean(1, obj.isPrescritptionRequise());
            prepState.setDate(2, obj.getDateBrevet());
            prepState.setString(3, obj.getLibelle());
            prepState.setInt(4, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (NullPointerException e){//dialogBox + inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        }
        return false;
    }

    /**
     * Méthode de recherche des données d'un Medicament
     *
     * @param id : l'identifiant de l'objet recherché
     * @return Medicament : l'objet trouvé
     */
    @Override
    public Medicament find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `medicament`  WHERE `medicament`.id = ?");
            prepState.setInt(1, id);
            ResultSet result = prepState.executeQuery();
            if (result.first()) {
                return new Medicament(id,result.getBoolean(2),result.getDate(3), result.getString(4));
            }
            prepState.close();
        } catch (SQLException e) {
            throw new DBException(Store.bundle.getString("MedicamentDAO.DialogBoxErr.notFound.content"));
        }
        return null;
    }

    /**
     * Methode recherche de tout les Medicament de la table `medicament`
     *
     * @return ArrayList<Medicament> : une liste avec tout les Medicament trouvée
     */
    @Override
    public ArrayList<Medicament> findAll() throws DBException {
        ArrayList<Medicament> lstMedic=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `medicament`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstMedic.add(
                        new Medicament(result.getInt(1),result.getBoolean(2),
                                result.getDate(3), result.getString(4))
                );
            }
            prepState.close();
            return lstMedic;
        } catch (SQLException e) {
            throw new DBException("findAll() :"+Store.bundle.getString("MedicamentDAO.DialogBoxErr.notFound.content"));
        }
    }

}
