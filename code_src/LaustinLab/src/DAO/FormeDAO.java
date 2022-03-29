package DAO;

import exceptions.ConstraintException;
import exceptions.DBException;
import model.Forme;
import utility.Store;

import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.writeToLog;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 19/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql de la table forme dans la base de données pharmacy.
 */
public class FormeDAO extends DAO<Forme> {
    /**
     * Constructeur FormeDAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public FormeDAO(Connection conn) {
        super(conn);
    }

    /**
     * Méthode de création d'une Forme
     *
     * @param obj : la Forme à créer dans la BDD
     * @return boolean
     *      true : si l'object à bien été créé en BDD
     *      false : si l'objet n'as pas été crée en BDD
     */
    @Override
    public boolean create(Forme obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `forme` (`id`, `libelle`, `etat`) VALUES (NULL, ?, ?) ");
            prepState.setString(1, obj.getLibelle());
            prepState.setString(2,obj.getEtat());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("FormeDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){ // dialogBox + inscription dans le logfile
            throw new ConstraintException(Store.bundle.getString("FormeDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("FormeDAO.DialogBoxErr.notCreate.title"));
        }
        return false;
    }

    /**
     * Méthode de suppresion des données d'une Forme dans la base de donnée
     * Si au moins un MedicamentConcentration à comme Forme obj, alors obj ne sera pas supprimé de la base de données.
     * @param obj : la Forme à supprimer dans la BDD
     * @return boolean
     *      true : si l'objet à bien été supprimé en BDD
     *      false : si l'objet n'as pas été supprimé en BDD
     */
    @Override
    public boolean delete(Forme obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `forme` WHERE `forme`.`id` = ?");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("FormeDAO.DialogBoxErr.notDeleted.content"));
        }catch (SQLException se){
            throw new DBException( Store.bundle.getString("FormeDAO.DialogBoxErr.notDeleted.title"));
        }
    }

    /**
     * Méthode de mise à jour
     *
     * @param obj : l'objet à mettre à jour dans la BDD
     * @return boolean
     * true : si l'object à bien été mis à jour en BDD
     * false : si l'objet n'as pas été mis à jour en BDD
     */
    @Override
    public boolean update(Forme obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `forme` SET `libelle` = ?, `etat` = ? WHERE `forme`.`id` = ? ");
            prepState.setString(1, obj.getLibelle());
            prepState.setString(2,obj.getEtat());
            prepState.setInt(3,obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){// inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.nullPointer.content") );
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException( Store.bundle.getString("FormeDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * Méthode de recherche des données d'un forme gràce à son id
     *
     * @param id : l'identifiant de la forme recherché
     * @return Forme l'objet trouvé
     */
    @Override
    public Forme find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement("SELECT * FROM `forme`  WHERE `forme`.id = ?");
            prepState.setInt(1, id);
            ResultSet result = prepState.executeQuery();
            if (result.first()) {
                return new Forme(id,result.getString(2),result.getString(3));
            }
            prepState.close();
        } catch(SQLException e){
            throw new DBException( Store.bundle.getString("FormeDAO.DialogBoxErr.notFound.title") );
        }
        return null;
    }

    /**
     * Methode recherche de tout les objets Forme de la table forme
     *
     * @return ArrayList<Forme> : une liste avec toutes les Forme trouvée
     */
    @Override
    public ArrayList<Forme> findAll() throws DBException {
        ArrayList<Forme> lstForme=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `forme`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstForme.add(
                        new Forme(result.getInt(1),result.getString(2),result.getString(3))
                );
            }
            prepState.close();
            return lstForme;
        } catch (SQLException e) {
            throw new DBException("findAll() :"+ Store.bundle.getString("FormeDAO.DialogBoxErr.notFound.title") );
        }
    }
}
