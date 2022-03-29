package DAO;

import DataBase.DbConnexion;
import exceptions.DBException;

import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory {

    protected static Connection conn= null;


    static {
        try {
            conn = DbConnexion.getInstance();
        } catch (DBException throwables) {

        }
    }

    public static void close(boolean commit) throws SQLException {
        try{
            if (commit){
                if ( !conn.getAutoCommit() ){ conn.commit(); }
            }
            conn.close();
        }catch (NullPointerException e){
            throw new DBException();
        } catch (SQLException throwables) {
            throw new DBException();
        }
    }

    /**
     * Méthode static getUtilisateurDAO, c'est la première méthode utilisée, elle servira donc à verifier que la base de données
     * est correctement connectée
     * @return
     * @throws DBException
     */
    public static UtilisateurDAO getUtilisateurDAO() throws DBException {
        return new UtilisateurDAO(DbConnexion.getInstance());
    }
    public static PaysDAO getPaysDAO(){
        return new PaysDAO(conn);
    }
    public static VilleDAO getVilleDAO(){
        return new VilleDAO(conn);
    }
    public static ClientDAO getClientDAO(){
        return new ClientDAO(conn);
    }
    public static ConcentrationDAO getConcentrationDAO(){
        return new ConcentrationDAO(conn);
    }
    public static ConditionnementDAO getConditionnementDAO(){
        return new ConditionnementDAO(conn);
    }
    public static FormeDAO getFormeDAO(){
        return new FormeDAO(conn);
    }
    public static LotDAO getLotDAO(){
        return new LotDAO(conn);
    }
    public static FactureDAO getFactureDAO(){ return  new FactureDAO(conn); }
    public static MedicamentDAO getMedicamentDAO(){return  new MedicamentDAO(conn); }
    public static LigneFactureDAO getLigneFactureDAO(){return  new LigneFactureDAO(conn); }
    public static MedicamentConcentrationDAO getMedicamentConcentrationDAO(){
        return new MedicamentConcentrationDAO(conn);
    }

}
