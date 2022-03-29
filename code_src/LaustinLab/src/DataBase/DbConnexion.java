package DataBase;
import exceptions.DBException;
import javafx.application.Platform;
import utility.DialogBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 22/01/2021
 *     Modification : 22/01/2021
 *     Revision : 1.0
 *     Description : Classe permettant la connection à la base de donnée nommée pharmacy.
 *                   Utilisation du Design pattern Singleton
 *     @return :un Object Connection.
 *     Utilisation : Ex. MyConnection = DbConnexion.getInstance( );
 */
public class DbConnexion {
    private static Connection conn;
    private volatile  static DbConnexion single;

    /**
     * Constructeur privé de DbConnexion
     * Ce constructeur instancie un Objet Connection à la base de donnée pharmacy
     */
    private DbConnexion() throws DBException {
        String user = "root";//TODO changer le user et password si la base de donnée se retrouve en ligne
        String passwd = "";
        String server = "mysql";
        String url = "jdbc:" + server + "://127.0.0.1:3306/pharmacy?autoReconnect=true&useSSL=false";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, passwd);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DBException();
        }
    }//end constructor

    /**
     * fonction static qui verifie que l"instantiation de l'objet Connection soit unique.
     * @return Connection conn : la connection à la base de donnée.
     */
    public static Connection getInstance() throws DBException {
        if( single==null ){
            synchronized ( Connection.class ){
                if ( single==null ) {
                    single = new DbConnexion();
                }
            }
        }
        return conn;
    }//end getInstance
}
