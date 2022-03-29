/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 26/01/2021
 *     Modification : 05/02/2021
 *     Revision : 0.2-alpha
 *     Description : Classe permettant la gestion des exceptions liées à la base de donnée
 *     Cas d'utilisations : Si la base de donnée n'existe pas, si le serveur n'est pas connecté, ..
 */
package exceptions;

import java.sql.SQLException;

import static utility.Utility.writeToLog;
/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 29/01/2021
 *     Modification : 29/01/2021
 *     Revision : 1.0
 *     Description : Classe exception pour la base de donnée
 */
public class DBException extends SQLException {

    /**
     * constructeur DBException
     * Affiche un message d'erreur avec un contenu dans une interface
     * Ecrit le message d'erreur dans un fichier log
     */
    public DBException(){
        super();
        writeToLog(this.getClass().toString(),this.getMessage());
    }
    /**
     * constructeur DBException
     * Affiche un message d'erreur avec un contenu dans une interface
     * Ecrit le message d'erreur dans un fichier log
     * @param message : le message de l'erreur
     */
    public DBException(String message){
        super(message);
        writeToLog(this.getClass().toString(),this.getMessage());
    }
}
