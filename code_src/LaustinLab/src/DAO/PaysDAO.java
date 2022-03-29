package DAO;

import exceptions.ConstraintException;
import exceptions.DBException;
import model.Pays;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 18/02/2021
 *     Modification : 21/02/2021
 *     Revision : 0.5
 *     Description : Classe permettant les requetes mysql des pays dans la base de données pharmacy.
 *     Cette classe n'utilise que des find et findAll.
 */
public class PaysDAO extends DAO<Pays> {
    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public PaysDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(Pays obj) throws DBException {
        return false;
    }

    @Override
    public boolean delete(Pays obj) throws DBException, ConstraintException {
        return false;
    }

    @Override
    public boolean update(Pays obj) throws DBException {
        return false;
    }

    @Override
    public Pays find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `pays` WHERE `pays`.id = ?");
            prepState.setInt(1, id );
            ResultSet result = prepState.executeQuery();
            if( result.first() ){
                return new Pays(id, result.getString(2) );
            }
            prepState.close();
        } catch (SQLException e) {
            throw new DBException( "Erreur dans la recherche du pays "+id );
        }
        return null;
    }

    @Override
    public ArrayList<Pays> findAll() throws DBException {
        ArrayList<Pays> lstPays=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `pays`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                PaysDAO p = DAOFactory.getPaysDAO();
                lstPays.add( new Pays(result.getInt(1), result.getString(2)) );
            }
            prepState.close();
            return lstPays;
        } catch (SQLException e) {
            throw new DBException( "Erreur dans la recherche des Utilisateurs" );
        }
    }
}
