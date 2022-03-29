package controller;

import DataBase.DbConnexion;
import exceptions.DBException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import utility.Store;
import utility.Utility;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 26/01/2021
 *     Modification : 26/01/2021
 *     Revision : 0.9
 *     Description : Controller de version.fxml
 */

public class Version implements Initializable {
    @FXML
    public ScrollPane  scrollpanVersion;
    private String PathInstall= System.getProperty("user.dir"); // le path dans lequel est installé l'application (coté client)
    private String DateCreation= "2021-03-17";
    private double dblVersion =1.0;
    private String appName = Store.appName.replace(" ","_")+"_1.0";
    private String javaVersion = System.getProperty("java.version");
    private String OSdevelopper = "Windows10Pro"; //Windows 10 version 2004
    private String copyRight = "©Rochez Justin, Sapin Laurent";


    /**
     * méthode de création d'une chaine de caractère représentant le text de version de l'application
     * @return String : le texte complet sur les caractèristique de l'application
     */
    private String getVersion(){
        String textVersion;
        textVersion= "Product Version\n" +
                appName+"\n" +
                "\n" +
                "Build Information\n" +
                "Version: "+dblVersion+"\n" +
                "Date: "+DateCreation+"\n" +
                "\n" +
                "Logging\n" +
                "The default configuration stores logging output in a file named "+appName+".log (possibly followed by a trailing '.' and a digit).\n"+
                "The default file path is "+PathInstall+"\n" +
                "\n"+
                "Java\n" +
                javaVersion+"\n" +
                "\n" +
                "Operating System\n" +
                OSdevelopper+"\n" +
                "\n" +
                copyRight;
        return textVersion;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        scrollpanVersion.setContent(new Label(getVersion()));
    }
}
