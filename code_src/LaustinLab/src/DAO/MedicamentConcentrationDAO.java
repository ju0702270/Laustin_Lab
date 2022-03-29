package DAO;


import exceptions.ConstraintException;
import exceptions.DBException;
import model.LigneFacture;
import model.Lot;
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
 *     Description : Classe permettant les requetes mysql des medicament_concentration dans la base de données pharmacy.
 */
public class MedicamentConcentrationDAO extends DAO<MedicamentConcentration>{
    private MedicamentDAO medicamentDAO;
    private ConcentrationDAO concentrationDAO;
    private ConditionnementDAO conditionnementDAO;
    private FormeDAO formeDAO;

    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn
     */
    public MedicamentConcentrationDAO(Connection conn) {
        super(conn);
        medicamentDAO= DAOFactory.getMedicamentDAO();
        concentrationDAO=DAOFactory.getConcentrationDAO();
        conditionnementDAO=DAOFactory.getConditionnementDAO();
        formeDAO=DAOFactory.getFormeDAO();
    }

    /**
     * Méthode de création d'un MédicamentConcentration
     *
     * @param obj: l'objet MedicamentConcentration à créer dans la BDD
     *
     * @return boolean
     *      true : si l'object à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(MedicamentConcentration obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `medicament_concentration` " +
                    "                    (`id`, `idMedicament`, `idConcentration`, `idConditionnement`, `idForme`, `prix`, `tva`)" +
                    "                    VALUES (NULL, ?, ?, ?, ?, ?, ?)");
            prepState.setInt(1, obj.getMedicament().getId());
            prepState.setInt(2, obj.getConcentration().getId());
            prepState.setInt(3, obj.getConditionnement().getId());
            prepState.setInt(4, obj.getForme().getId());
            prepState.setDouble(5, obj.getPrix());
            prepState.setDouble(6, obj.getTva());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){ //inscription dans le logfile
            throw new ConstraintException(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notCreate.content"));
        }
        return false;
    }//end create

    /**
     * Méthode de suppression d'un MedicamentConcentration
     *
     * @param obj : l'objet MedicamentConcentration à supprimer dans la BDD
     * @return boolean
     *      true : si l'object à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(MedicamentConcentration obj) throws DBException, ConstraintException {
        try {
            //Suppression des LigneFacture associée
            LigneFactureDAO liFac =DAOFactory.getLigneFactureDAO();
            for (LigneFacture lf: liFac.findAll(obj) ) {
                liFac.delete(lf);
            }
            //Suppression des Lot associé
            LotDAO lotDAO = DAOFactory.getLotDAO();
            for ( Lot lot: obj.getLots() ) {
                lotDAO.delete(lot);
            }
            //Suppression du MedicamentConcentration
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `medicament_concentration` " +
                    "WHERE `medicament_concentration`.`id` = ? ");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se){
            throw new DBException(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notDeleted.content"));
        }
    }



    /**
     * Méthode de mise à jour du médicament concentration
     *
     * @param obj : l'objet à mettre à jour dans la BDD
     * @return boolean
     *     true : si l'object à bien été mis à jour en BDD
     *     false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(MedicamentConcentration obj) throws DBException, ConstraintException {
        try {
            LotDAO lotDAO= DAOFactory.getLotDAO();
            for (Lot l: obj.getLots()) {
                lotDAO.update(l);
            }
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `medicament_concentration` SET" +
                    " `idMedicament` = ?," +
                    " `idConcentration` = ?," +
                    " `idConditionnement` = ?," +
                    " `idForme` = ?," +
                    " `prix` = ?," +
                    " `tva` = ?"+
                    " WHERE `medicament_concentration`.`id` = ? ");
            prepState.setInt(1, obj.getMedicament().getId());
            prepState.setInt(2, obj.getConcentration().getId());
            prepState.setInt(3, obj.getConditionnement().getId());
            prepState.setInt(4, obj.getForme().getId());
            prepState.setDouble(5, obj.getPrix());
            prepState.setDouble(6,obj.getTva());
            prepState.setInt(7, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){// inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * Méthode de recherche des données d'un MedicamentConcentration
     *
     * @param id : l'identifiant du MedicamentConcentration recherché
     * @return <MedicamentConcentration> le MedicamentConcentration créé à partir de la base de donnée
     */
    @Override
    public MedicamentConcentration find(int id) throws DBException {
        try {
            ArrayList<Lot> lots = new ArrayList<>();
            MedicamentConcentration medicamentConcentration = null;
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM medicament_concentration mc INNER join Lot l" +
                    " on l.idMedicamentConcentration = mc.id where mc.`id` = ? ");
            prepState.setInt(1, id);
            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                if ( medicamentConcentration == null ){
                    medicamentConcentration = new MedicamentConcentration(id,  medicamentDAO.find(result.getInt(2)),
                            concentrationDAO.find(result.getInt(3)),
                            conditionnementDAO.find(result.getInt(4)),
                            formeDAO.find(result.getInt(5)),
                            result.getDouble(6),lots , result.getDouble(7));
                }
                lots.add(new Lot(result.getInt(8),
                        result.getDate(9),
                        result.getDate(10),
                        medicamentConcentration,result.getInt(12)));
            }
            prepState.close();
            return medicamentConcentration;
        } catch(SQLException e){
            throw  new DBException(Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notFound.content"));
        }
    }

    /**
     * Méthode recherche des données de toute les lignes dans pharmacy.medicament_concentration
     * Cette méthode va créer une ArrayList contenant tout les objets MedicamentConcentration qui représente la table
     *
     * @return ArrayList<MedicamentConcentration> : une liste avec tout les Objets MedicamentConcentration trouvé
     */
    @Override
    public ArrayList<MedicamentConcentration> findAll() throws DBException {
        ArrayList<MedicamentConcentration> lstMedic = new ArrayList<>();
        ArrayList<Lot> lots= new ArrayList<>();
        MedicamentConcentration medicamentConcentration = null;
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM medicament_concentration mc INNER join" +
                    " Lot l on l.idMedicamentConcentration = mc.id order BY mc.`id`");
            ResultSet result = prepState.executeQuery();
            int currentId = -1;
            while (result.next()) {
                if (currentId != result.getInt(1)){
                    currentId = result.getInt(1);
                    lots= new ArrayList<>();
                    medicamentConcentration = new MedicamentConcentration(result.getInt(1),
                            medicamentDAO.find(result.getInt(2)),
                            concentrationDAO.find(result.getInt(3)),
                            conditionnementDAO.find(result.getInt(4)),
                            formeDAO.find(result.getInt(5)),
                            result.getDouble(6),
                            lots, result.getDouble(7));

                    lstMedic.add(medicamentConcentration);
                }
                lots.add(new Lot(result.getInt(8),
                        result.getDate(9),
                        result.getDate(10),
                        medicamentConcentration,result.getInt(12)));
            }
            prepState.close();//todo verifier que tout les .close() le font
            return lstMedic;
        } catch (SQLException e) {
            throw new DBException( "findAll() :"+Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notFound.content"));
        }
    }

    /**
     * Méthode recherche des données de toute les lignes dans pharmacy.medicament_concentration
     * Cette méthode va créer une ArrayList contenant tout les objets MedicamentConcentration qui représente la table
     *
     * @return ArrayList<MedicamentConcentration> : une liste avec tout les Objets MedicamentConcentration trouvé
     */

    public ArrayList<MedicamentConcentration> findAllWithoutLot() throws DBException {
        ArrayList<MedicamentConcentration> lstMedic = new ArrayList<>();
        ArrayList<Lot> lots= new ArrayList<>();
        MedicamentConcentration medicamentConcentration = null;
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM medicament_concentration");
            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                    medicamentConcentration = new MedicamentConcentration(result.getInt(1),
                            medicamentDAO.find(result.getInt(2)),
                            concentrationDAO.find(result.getInt(3)),
                            conditionnementDAO.find(result.getInt(4)),
                            formeDAO.find(result.getInt(5)),
                            result.getDouble(6),
                            lots, result.getDouble(7));
                    lstMedic.add(medicamentConcentration);
            }
            prepState.close();//todo verifier que tout les .close() le font
            return lstMedic;
        } catch (SQLException e) {
            throw new DBException(  "findAll() :"+Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notFound.content"));
        }
    }

    /**
     *
     * Méthode recherche des données de toute les lignes dans pharmacy.medicament_concentration correspondant à un medicicament
     * Cette méthode va créer une ArrayList contenant tout les objets MedicamentConcentration qui sont associée au Medicament passé en paramètre
     * @param med: Le médicament nous indiquant quel medicament_concentration rechercher.
     * @return ArrayList<MedicamentConcentration> : une liste avec tout les Objets MedicamentConcentration trouvé
     * @throws DBException
     */
    public ArrayList<MedicamentConcentration> findAll(Medicament med) throws DBException {
        ArrayList<MedicamentConcentration> lstMedic = new ArrayList<>();
        ArrayList<Lot> lots= new ArrayList<>();
        MedicamentConcentration medicamentConcentration = null;
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM medicament_concentration mc INNER join" +
                    "  Lot l on l.idMedicamentConcentration = mc.id where mc.`idMedicament` = ? order BY mc.`id`");
            prepState.setInt(1, med.getId());
            ResultSet result = prepState.executeQuery();
            int currentId = -1;
            while (result.next()) {
                if (currentId != result.getInt(1)){
                    currentId = result.getInt(1);
                    lots= new ArrayList<>();
                    medicamentConcentration = new MedicamentConcentration(result.getInt(1),
                            medicamentDAO.find(result.getInt(2)),
                            concentrationDAO.find(result.getInt(3)),
                            conditionnementDAO.find(result.getInt(4)),
                            formeDAO.find(result.getInt(5)),
                            result.getDouble(6),
                            lots, result.getDouble(7));// TODO ajouter tva

                    lstMedic.add(medicamentConcentration);
                }
                lots.add(new Lot(result.getInt(8),
                        result.getDate(9),
                        result.getDate(10),
                        medicamentConcentration,result.getInt(12)));
            }
            prepState.close();
            return lstMedic;
        } catch (SQLException e) {
            throw new DBException( "findAll(Medicament med) :"+Store.bundle.getString("MedicamentConcentrationDAO.DialogBoxErr.notFound.content"));
        }
    }

}
