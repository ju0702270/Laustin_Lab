package DAO;


import exceptions.ConstraintException;
import exceptions.DBException;
import model.Lot;
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
 *     Description : Classe permettant les requetes mysql des Lot dans la base de données pharmacy.
 */
public class LotDAO extends DAO<Lot> {
    private MedicamentConcentrationDAO medConcDAO;

    /**
     * Constructeur LotDAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public LotDAO(Connection conn) {
        super(conn);
        this.medConcDAO = DAOFactory.getMedicamentConcentrationDAO();
    }

    /**
     * Méthode de création d'un Lot
     *
     * @param obj : le Lot à créer dans la BDD
     * @return boolean
     *      true : si l'object à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(Lot obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `lot` " +
                    " (`id`, `dateFabrication`, `datePeremption`,`idMedicamentConcentration` , `quantiteActuelle`)" +
                    " VALUES (?, ?, ?, ?, ?) ");
            prepState.setInt(1, obj.getId());
            prepState.setDate(2, obj.getDateFabrication());
            prepState.setDate(3, obj.getDatePeremption());
            prepState.setInt(4, obj.getMedicamentConcentration().getId());
            prepState.setInt(5, obj.getQuantite());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){
            writeToLog(e.getClass().toString(),Store.bundle.getString("LotDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("LotDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("LotDAO.DialogBoxErr.notCreate.title") );
        }
        return false;
    }

    /**
     * Méthode de suppresion d'un Lot dans la base de donnée
     * @param obj : le Lot à supprimer dans la BDD
     * @return boolean
     *      true : si l'object à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(Lot obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `lot` WHERE `lot`.`id` = ?");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se) {
            throw new DBException(Store.bundle.getString("LotDAO.DialogBoxErr.notDeleted.title"));
        }

    }

    /**
     * Méthode de mise à jour du Lot dans la base de donnée
     *
     * @param obj : le Lot à mettre à jour dans la BDD
     * @return boolean
     *      true : si l'object à bien été mis à jour en BDD
     *      false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(Lot obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `lot` SET " +
                    "`dateFabrication` = ?" +
                    ", `datePeremption` = ? " +
                    ", `quantiteActuelle` = ? " +
                    ", `idMedicamentConcentration` = ? "+
                    "WHERE `lot`.`id` = ? ");
            prepState.setDate(1, obj.getDateFabrication());
            prepState.setDate(2, obj.getDatePeremption());
            prepState.setInt(3, obj.getQuantite());
            prepState.setInt(4,obj.getMedicamentConcentration().getId());
            prepState.setInt(5,obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException(Store.bundle.getString("LotDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * Méthode de recherche des données d'un Lot depuis la base de données
     *
     * @param id : l'identifiant de l'objet recherché
     * @return Lot : l'objet trouvé
     */
    @Override
    public Lot find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `lot`  WHERE `lot`.id = ?");
            prepState.setInt(1, id);
            ResultSet result = prepState.executeQuery();
            if (result.first()) {
                return new Lot(id, result.getDate(2), result.getDate(3),
                        this.medConcDAO.find(result.getInt(4)), result.getInt(5));
            }
            prepState.close();
        } catch(SQLException e){
            throw new DBException(Store.bundle.getString("LotDAO.DialogBoxErr.notFound.content"));
        }
        return null;
    }

    /**
     * Methode recherche de tout les Lot de la table lot
     *
     * @return ArrayList<Lot> : une liste avec tout les Objets trouvée
     */
    @Override
    public ArrayList<Lot> findAll() throws DBException {
        ArrayList<Lot> lstLot=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `lot`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstLot.add(
                        new Lot(result.getInt(1), result.getDate(2), result.getDate(3),
                                this.medConcDAO.find(result.getInt(4)),result.getInt(5))
                );
            }
            prepState.close();
            return lstLot;
        } catch (SQLException e) {
            throw new DBException("findAll() :" +Store.bundle.getString("LotDAO.DialogBoxErr.notFound.content"));
        }
    }
    /**
     * Methode recherche de tout les Lot de la table lot par rapport au MedicamentConcentration
     *
     * @return ArrayList<Lot> : une liste avec tout les Objets trouvée
     */
    public ArrayList<Lot> findAll(MedicamentConcentration medicamentConcentration) throws DBException {
        ArrayList<Lot> lstLot=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `lot` WHERE `idMedicamentConcentration` = ?");
            prepState.setInt(1, medicamentConcentration.getId());
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstLot.add(
                        new Lot(result.getInt(1), result.getDate(2), result.getDate(3),
                                this.medConcDAO.find(result.getInt(4)),result.getInt(5))
                );
            }
            prepState.close();
            return lstLot;
        } catch (SQLException e) {
            throw new DBException("findAll() :" +Store.bundle.getString("LotDAO.DialogBoxErr.notFound.content"));
        }
    }



}
