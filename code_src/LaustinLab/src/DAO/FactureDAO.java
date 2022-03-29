package DAO;

import exceptions.ConstraintException;
import exceptions.DBException;
import model.Client;
import model.Facture;
import model.LigneFacture;
import model.Utilisateur;
import utility.Store;

import java.sql.*;
import java.util.ArrayList;

import static utility.Utility.*;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 19/02/2021
 *     Modification : 21/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant les requetes mysql de la table  facture dans la base de donnée pharmacy.
 */
public class FactureDAO extends DAO<Facture>{
    private LigneFactureDAO ligneFacDAO;
    private ClientDAO clientDAO;
    private UtilisateurDAO utilisateurDAO;

    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public FactureDAO(Connection conn) {
        super(conn);
        this.clientDAO=DAOFactory.getClientDAO();
        try {
            this.utilisateurDAO=DAOFactory.getUtilisateurDAO();
        } catch (DBException throwables) {
            throwables.printStackTrace();
        }
        this.ligneFacDAO=DAOFactory.getLigneFactureDAO();

    }

    /**
     * Méthode de création d'une Facture dans la base de données
     * Cette méthode va créé les LigneFacture contenue dans obj dans la base de donnée
     * @param obj : la Facture à créée dans la base de données
     * @return boolean
     *      *      true : si l'object à bien été créé en BDD
     *      *      false : si l'objet n'as pas été crée en BDD
     * @throws DBException
     */
    @Override
    public boolean create(Facture obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("INSERT INTO `facture` (`id`, `idClient`, `idUtilisateur`, `dateHeure`, `totalHTVA`, `idEntreprise`)" +
                    " VALUES (NULL, ?, ?, ?, ?, ?)");
            if (obj.getClient() == null) {
                prepState.setNull(1, Types.INTEGER);
            } else {
                prepState.setInt(1, obj.getClient().getId());
            }
            if ((obj.getUtilisateur() == null)) {
                prepState.setNull(2, Types.INTEGER);
            } else {
                prepState.setInt(2, obj.getUtilisateur().getId());
            }
            prepState.setTimestamp(3, obj.getDateHeure() );
            prepState.setDouble(4,obj.getTotal());
            prepState.setInt(5,obj.getEntreprise().getId() );
            prepState.executeUpdate();
            prepState.close();
            //mise à jour et ajout des lignes factures.
            ArrayList<Facture> lstFact= findAll();
            Facture currentFact = lstFact.get(lstFact.size()-1);
            for ( LigneFacture l: obj.getLigneFacture() ) {
                l.setFacture(currentFact);
                this.ligneFacDAO.create(l);
            }
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("FactureDAO.DialogBoxErr.notCreated.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException e){ // dialogBox + inscription dans le logfile
            throw new ConstraintException(Store.bundle.getString("FactureDAO.DialogBoxErr.notCreated.constraintViolation.content"));
        } catch (SQLException e){
            throw new DBException(Store.bundle.getString("FactureDAO.DialogBoxErr.notCreate.content"));
        }
        return false;
    }

    /**
     * Méthode de suppression d'un Facture dans la base de donnée
     * Cette méthode va aussi supprimer toute les LigneFacture ayant trait à obj
     * @param obj : la Facture à créée dans la base de données
     * @return boolean
     *          true : si l'object à bien été créé en BDD
     *          false : si l'objet n'as pas été crée en BDD
     * @throws DBException
     * @throws ConstraintException
     */
    @Override
    public boolean delete(Facture obj) throws DBException, ConstraintException {
        try {
            //suppression de toutes les lignes factures ayant rapport avec la factures.
            for (LigneFacture l: this.ligneFacDAO.findAll(obj.getId())) {
                this.ligneFacDAO.delete(l);
            }
            //suppression de la Facture
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("DELETE FROM `facture` WHERE `facture`.`id` = ?");
            prepState.setInt(1, obj.getId());
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            throw new ConstraintException(Store.bundle.getString("FactureDAO.DialogBoxErr.notDeleted.constraintViolation.content"));
        }catch (SQLException se){
            throw new DBException( Store.bundle.getString("FactureDAO.DialogBoxErr.notDeleted.content"));
        }
    }

    /**
     * Méthode de mise à jour de la Facture dans la base de données
     * @param obj : la Facture à créée dans la base de données
     * @return boolean
     *           true : si l'object à bien été créé en BDD
     *           false : si l'objet n'as pas été crée en BDD
     * @throws DBException
     */
    @Override
    public boolean update(Facture obj) throws DBException, ConstraintException {
        try {
            this.conn.setAutoCommit(false);
            PreparedStatement prepState = this.conn.prepareStatement("UPDATE `facture` SET " +
                                                        "`idClient` = ?, " +
                                                        "`idUtilisateur` = ?, " +
                                                        "`dateHeure` = ?, " +
                                                        "`totalHTVA` = ? " +
                                                        " WHERE `facture`.`id` = ? ");
            if (obj.getClient() == null) {
                prepState.setNull(1, Types.INTEGER);
            } else {
                prepState.setInt(1, obj.getClient().getId());
            }
            if ((obj.getUtilisateur() == null)) {
                prepState.setNull(2, Types.INTEGER);
            } else {
                prepState.setInt(2, obj.getUtilisateur().getId());
            }
            prepState.setTimestamp(3, obj.getDateHeure() );
            prepState.setDouble(4,obj.getTotal() );
            prepState.setInt( 5,obj.getId() );
            prepState.executeUpdate();
            prepState.close();
            this.conn.commit();
            return true;
        } catch (NullPointerException e){//dialogBox + inscription dans le logfile
            writeToLog(e.getClass().toString(),Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.nullPointer.content"));
        } catch (SQLIntegrityConstraintViolationException se) {
            throw new ConstraintException(Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.constraintViolation.content"));
        } catch (SQLException se){
            throw new DBException(Store.bundle.getString("FactureDAO.DialogBoxErr.notUpdate.title"));
        }
        return false;
    }

    /**
     * Méthode de recherche d'un Facture dans la base de données grâce à sont id
     * @param id : l'identifiant de l'objet recherché
     * @return Facture : l'objet Facture correspondand.
     * @throws DBException
     */
    @Override
    public Facture find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `facture` f WHERE f.id=?");
            prepState.setInt(1, id );
            ResultSet result = prepState.executeQuery();
            if( result.first() ){
                return new Facture(
                            id,
                            this.clientDAO.find( result.getInt(2) ),
                            this.utilisateurDAO.find( result.getInt(3) ),
                            result.getTimestamp(4 ),
                            this.ligneFacDAO.findAll(id),
                            result.getDouble(5),
                            this.clientDAO.find( result.getInt(6 )
                        )
                );
            }
            prepState.close();
        } catch(SQLException e){
            throw new DBException(Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.content"));
        }
        return null;
    }

    /**
     * Méthode de recherche de toute les Factures dans la Base de données
     * @return ArraiList<Facture> : la liste de toute les Facture de la table `facture`
     * @throws DBException
     */
    @Override
    public ArrayList<Facture> findAll() throws DBException {
        ArrayList<Facture> lstFacture=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `facture`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstFacture.add(
                        new Facture(result.getInt(1),
                                            this.clientDAO.find( result.getInt(2) ),
                                            this.utilisateurDAO.find( result.getInt(3) ),
                                            result.getTimestamp(4 ),
                                            this.ligneFacDAO.findAll(result.getInt(1)),
                                            result.getDouble(5),
                                            this.clientDAO.find( result.getInt(6) )
                        )
                );
            }
            prepState.close();
            return lstFacture;
        }  catch (SQLException e) {
            throw new DBException(Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.content"));
        }
    }

    /**
     * méthode de recherche de toute les Facture dans la base de données ayant comme client c
     * @param c : le client associé au différentes Facture recherchée
     * @return ArrayList<Facture> : une liste des Factures recherchées
     * @throws DBException
     */
    public ArrayList<Facture> findAll(Client c) throws DBException {
        ArrayList<Facture> lstFacture=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `facture` WHERE `idClient`=?");
            prepState.setInt(1, c.getId() );
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstFacture.add(
                        new Facture(result.getInt(1),
                                this.clientDAO.find( result.getInt(2) ),
                                this.utilisateurDAO.find( result.getInt(3) ),
                                result.getTimestamp(4 ),
                                this.ligneFacDAO.findAll(result.getInt(1)),
                                result.getDouble(5),
                                this.clientDAO.find( result.getInt(6) )
                        )
                );
            }
            prepState.close();
            return lstFacture;
        }catch (SQLException e) {
            throw new DBException(Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.content"));
        }
    }

    /**
     * méthode de recherche de tout les Facture ayant pour Utilisateur u
     * @param u : l'Utilisateur associé aux différentes Factures recherchées
     * @return ArrayList<Facture> : une liste des Factures recherchées
     * @throws DBException
     */
    public ArrayList<Facture> findAll(Utilisateur u) throws DBException {
        ArrayList<Facture> lstFacture=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `facture` WHERE `idUtilisateur`=?");
            prepState.setInt(1, u.getId() );
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                lstFacture.add(
                        new Facture(result.getInt(1),
                                this.clientDAO.find( result.getInt(2) ),
                                this.utilisateurDAO.find( result.getInt(3) ),
                                result.getTimestamp(4 ),
                                this.ligneFacDAO.findAll(result.getInt(1)),
                                result.getDouble(5),
                                this.clientDAO.find( result.getInt(6) )
                        )
                );
            }
            prepState.close();
            return lstFacture;
        } catch (SQLException e) {
            throw new DBException("findAll(Utilisateur u) :"+ Store.bundle.getString("FactureDAO.DialogBoxErr.notFound.content"));
        }
    }
}
