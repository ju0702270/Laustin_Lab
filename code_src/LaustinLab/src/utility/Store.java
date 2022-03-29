package utility;

import DAO.DAOFactory;
import exceptions.DBException;
import javafx.stage.FileChooser;
import model.Client;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 23/02/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe permettabt de stocker des attribut ou fonction utilie a l'application entière
 */
public class Store {
    private static Import importer = new Import();
    public static HashMap<String, String> config;

    static {
        try {
            config = importer.getConfig();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    public static Locale locale = new Locale(config.get("lang").substring(0,2),config.get("lang").substring(3,5));
    public static ResourceBundle bundle = ResourceBundle.getBundle("strings",locale);
    public static String appName = config.get("appname");


    /**
     * Méthode de changement de Locale
     * cette méthode va écrire dans le fichier config.xml le changement de langue
     * @param lang : String de 5 caractères contenant les paramètres de la nouvelle Locale
     *             Exemple : en_US
     */
    public static void setLocale(String lang) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        locale =new Locale(lang.substring(0,2),lang.substring(3,5));
        bundle = ResourceBundle.getBundle("strings", locale);
        Utility.rewriteConfig("lang",lang);
    }

    /**
     * Méthode de récupération de l'objet client correspondant à l'entreprise
     * @return Client: entreprise
     * @throws DBException
     */
    public static Client getEntreprise() throws DBException {
        return DAOFactory.getClientDAO().find(Integer.parseInt(config.get("idEntreprise")));
    }
}
