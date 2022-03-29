package exceptions;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 02/02/2021
 *     Modification : 05/02/2021
 *     Revision : 1.0
 *     Description : Classe permettant la gestion des exceptions liée aux contraintes d'integrités
 *     Cas d'utilisation : lors de violations de contraintes d'integrités, exemple: lors d'un Delete donc l'id de l'objet est utilisé comme clé étrangère dans
 *                          une autre table.
 */
import utility.Store;

import java.sql.SQLIntegrityConstraintViolationException;

import static utility.Utility.writeToLog;

//TODO tester si cela fonctionne
public class ConstraintException extends SQLIntegrityConstraintViolationException {

    /**
     * constructeur ConstraintException
     * ecrit l'état SQL dans un fichier .log
     */
    public ConstraintException(){
        this(Store.bundle.getString("ConstraintException.title"));
    }
    /**
     * constructeur ConstraintException
     * ecrit l'état SQL dans un fichier .log
     * @param message : le message d'erreur personnalisé
     */
    public ConstraintException(String message){
        super(message);
        writeToLog(this.getClass().toString(),  message );
    }

}
