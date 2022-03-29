package DAO;

/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 20/02/2021
 *     Modification : 21/02/2021
 *     Revision : 0.7
 *     Description : Classe permettant les requetes mysql de la table  ligne_facture dans la base de données pharmacy.
 */
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Facture;
import model.LigneFacture;
import model.MedicamentConcentration;
import utility.Store;

import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.writeToLog;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 19/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql de la table ligne_facture dans la base de données pharmacy.
 */
public class LigneFactureDAO extends DAO<LigneFacture> {
    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public LigneFactureDAO(Connection conn) {
        super(conn);
    }

    /**
     * Méthode de création d'un LigneFacture daans la base de donnée
     *
     * @param obj : la LigneFacture à créer dans la BDD
     * @return boolean
     *      true : si l'object à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(LigneFacture obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `ligne_facture` (`idFacture`, `idMedicamentConcentration`, `quantite`, `prixTotal`) " +
                    "VALUES (?, ?, ?, ?) ");
            prepState.setInt(1, obj.getFacture().getId());
            prepState.setInt(2, obj.getMedicamentConcentration().getId() );
            prepState.setInt(3, obj.getQuantite());
            prepState.setDouble(4,obj.getPrixTotal());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//dialogBox + inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreated.nullPointer.content"));;
        } catch (SQLIntegrityConstraintViolationException e){ // dialogBox + inscription dans le logfile
            throw new ConstraintException(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notCreate.content"));
        }
        return false;
    }

    /**
     * Méthode de suppression d'une LigneFacture de la base de données
     *
     * @param obj : la LigneFacture à supprimer dans la BDD
     * @return boolean
     *      true : si l'object à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(LigneFacture obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `ligne_facture` WHERE " +
                    "`ligne_facture`.`idFacture` = ? AND `ligne_facture`.`idMedicamentConcentration` = ?");
            prepState.setInt(1, obj.getFacture().getId());
            prepState.setInt(2,obj.getMedicamentConcentration().getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se){
            throw new DBException( Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notDeleted.content"));
        }
    }

    /**
     * Méthode de mise à jour d'une LigneFacture dans la base de donné
     * Ne modifie pas la clé primaire.
     * @param obj : la LigneFacture à mettre à jour dans la BDD
     * @return boolean
     *      true : si l'object à bien été mis à jour en BDD
     *      false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(LigneFacture obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `ligne_facture` SET `quantite` = ?," +
                    " `prixTotal` = ? " +
                    "  WHERE `ligne_facture`.`idFacture` = ?" +
                    " AND `ligne_facture`.`idMedicamentConcentration` = ? ");
            prepState.setInt(1,obj.getQuantite());
            prepState.setDouble(2,obj.getPrixTotal());
            prepState.setInt(3, obj.getFacture().getId());
            if (obj.getMedicamentConcentration() == null) {
                prepState.setNull(4, Types.INTEGER);
            }else{
                prepState.setInt(4,obj.getMedicamentConcentration().getId());
            }
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//dialogBox + inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw  new DBException(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * !!!Méthode qui retrourne null!!!
     *
     * @param id : l'identifiant de l'objet recherché
     * @return <T> l'objet trouvé
     */
    @Override
    public LigneFacture find(int id) throws DBException {
        return null;
    }

    /**
     * Méthode de recherche des données d'une LigneFacture par rapport à sa Facture et son MedicamentConcentration
     * @param facture : La Facture correspondant à la LigneFacture recherchée
     * @param medicamentConcentration : le MedicamentConcentration correspondant à la LigneFacture recherchée
     * @return LigneFacture : la LigneFacture recherchée
     * @throws DBException
     */
    public LigneFacture find(Facture facture, MedicamentConcentration medicamentConcentration) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `ligne_facture` WHERE `idFacture`=? and `idMedicamentConcentration`=?");
            prepState.setInt(1, facture.getId());
            prepState.setInt(2, medicamentConcentration.getId());
            ResultSet result = prepState.executeQuery();
            if (result.first()) {
                return new LigneFacture(facture,medicamentConcentration,result.getInt(3), result.getDouble(4));
            }
            prepState.close();
        } catch(SQLException e){
            throw new DBException(Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notFound.content"));
        }
        return null;
    }

    /**
     * Méthode de recherche de chaque Lignefacture dont l'idMedicamentConcentration correspond au MedicamentConcentration passé en paramètre
     * @param medicamentConcentration : l'objet faisant référence à toutes les LigneFacture recherchées
     * @return ArrayList<LigneFacture>
     * @throws DBException
     */
    public ArrayList<LigneFacture> findAll(MedicamentConcentration medicamentConcentration) throws  DBException{
        ArrayList<LigneFacture> lstLiFa=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `ligne_facture` WHERE `idMedicamentConcentration`=?");
            prepState.setInt(1, medicamentConcentration.getId());
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstLiFa.add(
                        new LigneFacture(DAOFactory.getFactureDAO().find(result.getInt(1)),medicamentConcentration,
                        result.getInt(3), result.getDouble(4))
                );

            }
            prepState.close();
            return lstLiFa;
        }  catch (SQLException e) {
            throw new DBException("findAll(MedicamentConcentration m) :"
                    +Store.bundle.getString("LignefactureDAO.DialogBoxErr.notFound.content"));
        }
    }

    /**
     * Méthode de recherche de chaque ligne facture dont l'idFacture correspond au paramètre idFacture
     * @param idFacture : le numéro de facture pour filtrer la recherche
     * @return ArrayList<LigneFacture>
     * @throws DBException
     */
    public ArrayList<LigneFacture> findAll(int idFacture) throws  DBException{
        ArrayList<LigneFacture> lstLiFa=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `ligne_facture` WHERE `idFacture`=?");
            prepState.setInt(1, idFacture);
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstLiFa.add(
                        new LigneFacture(new Facture(idFacture,null,null,new Timestamp(System.currentTimeMillis()),null,0.0,null)
                                ,DAOFactory.getMedicamentConcentrationDAO().find(result.getInt(2)),
                                result.getInt(3), result.getDouble(4))
                );
            }
            prepState.close();
            return lstLiFa;
        } catch (SQLException e) {
            throw new DBException("findAll(int idFacture) :" +
                    Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notFound.content"));
        }
    }


    /**
     * Méthode de recherche de chaque LigneFacture dont la Facture correspond au paramètre facture
     * @param facture : la facture pour filtrer la recherche
     * @return ArrayList<LigneFacture>
     * @throws DBException
     */
    public ArrayList<LigneFacture> findAll(Facture facture) throws  DBException{
        ArrayList<LigneFacture> lstLiFa=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `ligne_facture` WHERE `idFacture`=?");
            prepState.setInt(1, facture.getId());
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstLiFa.add(
                        new LigneFacture(facture
                                ,DAOFactory.getMedicamentConcentrationDAO().find(result.getInt(2)),
                                result.getInt(3), result.getDouble(4))
                );
            }
            prepState.close();
            return lstLiFa;
        } catch (SQLException e) {
            throw new DBException("findAll(Facture f) :"+Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notFound.content"));
        }
    }

    /**
     * Méthode recherche de tout les objets de la table ligne_facture
     *
     * @return ArrayList<LigneFacture> : une liste avec tout les Objets trouvée
     */
    @Override
    public ArrayList<LigneFacture> findAll() throws DBException {
        ArrayList<LigneFacture> lstLiFa=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `ligne_facture`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstLiFa.add(
                        new LigneFacture(new Facture(result.getInt(1),null,null,new Timestamp(System.currentTimeMillis()),null,0.0,null)
                                ,DAOFactory.getMedicamentConcentrationDAO().find(result.getInt(2)),
                                result.getInt(3), result.getDouble(4))
                );
            }
            prepState.close();
            return lstLiFa;
        }  catch (SQLException e) {
            throw new DBException("findAll() :"+Store.bundle.getString("LigneFactureDAO.DialogBoxErr.notFound.content"));
        }
    }
}
