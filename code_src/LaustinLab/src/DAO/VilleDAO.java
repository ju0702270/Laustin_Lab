package DAO;

import exceptions.ConstraintException;
import exceptions.DBException;
import model.Ville;

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
 *     Description : Classe permettant les requetes mysql des villes dans la base de données pharmacy.
 *     Cette classe n'utilise que des find et findAll.
 */
public class VilleDAO extends DAO<Ville> {
    /**
     * Constructeur DAO
     * initialise conn : Objet Connection instancié pour la DB pharmacy
     *
     * @param conn : la connexion à la base de donnée
     */
    public VilleDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(Ville obj) throws DBException {
        return false;
    }

    @Override
    public boolean delete(Ville obj) throws DBException, ConstraintException {
        return false;
    }

    @Override
    public boolean update(Ville obj) throws DBException {
        return false;
    }

    @Override
    public Ville find(int id) throws DBException {
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `ville`  WHERE `ville`.id = ?");
            prepState.setInt(1, id );
            ResultSet result = prepState.executeQuery();
            if( result.first() ){
                PaysDAO p = DAOFactory.getPaysDAO();
                return new Ville(id, result.getInt(2), result.getString(3),p.find(result.getInt(4)));
            }
            prepState.close();
        } catch (SQLException e) {
            throw new DBException( "Erreur dans la recherche du pays "+id );
        }
        return null;
    }

    @Override
    public ArrayList<Ville> findAll() throws DBException {
        ArrayList<Ville> lstVille=new ArrayList<>();
        try {
            PreparedStatement prepState = this.conn.prepareStatement( "SELECT * FROM `ville`");
            ResultSet result = prepState.executeQuery();
            while( result.next() ){
                PaysDAO p = DAOFactory.getPaysDAO();
                lstVille.add( new Ville(result.getInt(1),
                                        result.getInt(2),
                                        result.getString(3),
                                        p.find(result.getInt(4)) )
                );
            }
            prepState.close();
            return lstVille;
        } catch (SQLException e) {
            throw new DBException( "Erreur dans la recherche des Utilisateurs" );
        }
    }
}
