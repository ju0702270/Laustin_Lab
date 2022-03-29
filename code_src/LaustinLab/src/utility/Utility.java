/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 26/01/2021
 *     Modification : 26/01/2021
 *     Revision : 0.1-alpha
 *     Description : Classe regroupant plusieurs fonction utile
 */
package utility;

import model.LigneFacture;
import model.MedicamentConcentration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 23/02/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe utilitaire
 */
public class Utility {
    private static String appName = Store.appName.replace(" ","_")+"_"+ Store.config.get("version");
    /**
     * méthode d'écriture d'une chaine de caractère dans un fichier.
     * message format : [Date et Heure] : Style d'exception     Message de la cause.
     * @param Exception : chaine de caractère indiquant l'exception en cause
     * @param msg : chaine de caractère indiquant la cause.
     */
    public static void writeToLog( String Exception, String msg){
        File log = new File(appName+".log");// TODO :  ranger dans un path correct
        Calendar c = Calendar.getInstance();
        String prefix = "[" + LocalDateTime.now() + "] : ";
        FileWriter fw = null;
        try {
            fw = new FileWriter(log, true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        BufferedWriter output = new BufferedWriter(fw);
        try {
            output.write(prefix + Exception + "\t" + msg + "\n");
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// end writeToLog

    /**
     * méthode d'ajout d'un entier day à une date
     * @param date : la date à laquelle ajouter un ou des jours
     * @param day : les jours à ajouter
     * @return Date :  la date donnée avec les jours ajouté
     */
    public static Date addDay(Date date, int day){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }// fin addDay

    /**
     * Méthode de hashage d'un string avec l'algorithme SHA-256
     * @param strToHash : la chaine de caractère à hacher
     * @return String : une chaine de 64 caractères représentant le strToHash après être passé dans l'algorithme SHA-256
     * @throws NoSuchAlgorithmException
     */
    public static String getHash(String strToHash) throws NoSuchAlgorithmException {
        MessageDigest msg = MessageDigest.getInstance("SHA-256");
        byte[] hash = msg.digest(strToHash.getBytes(StandardCharsets.UTF_8));
        StringBuilder str = new StringBuilder();
        for (byte b : hash) {
            str.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return  str.toString();
    }//end getHash

    /**
     * méthode de creation de la date d'aujourd'hui en chaine de caractère yyyy-MM-dd
     * @return java.sql.Date la date d'aujourd'hui en format yyyy-MM-dd
     */
    public static java.sql.Date today(){//todo check
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        return date;
    }//end today

    /**
     * Méthode de calcul dans Timestamp de l'instant présent
     * @return Timestamp de l'instant présent
     */
    public static Timestamp Now(){
        return new Timestamp(System.currentTimeMillis());
    }


    /**
     * Fonction de calcul du prix total HTVA d'un ligne de facture
     * @param medConc : le MedicamentConcentration qui nous renseignera le prix
     * @param quant : la quantité de MedicamentConcentration
     * @return double
     */
    public static double getTotalLiFact(MedicamentConcentration medConc, int quant){
        return medConc.getPrix()*quant;
    }

    /**
     * Fonction de calcul du prix total HTVA d'une facture
     * @param lstLiFact : la liste des Lignes Factures
     * @return double le prix total HTVA de la facture calculé sur base des lignes Factures
     */
    public static double getTotalFact(ArrayList<LigneFacture> lstLiFact){
        double total = 0;
        for (LigneFacture l: lstLiFact ) {
            total+= l.getPrixTotal();
        }
        return total;
    }

    /**
     * Fonction qui renvoi une chaine avec le réél double arrondi au supérieur 2 chiffres après la virgule
     * @param dblToModif: valeur à modifier
     * @return Chaine 0.00
     */
    public static String dblFormat0_00(double dblToModif) {

        DecimalFormat FormatEuro = new DecimalFormat ("0.00");
        FormatEuro.setRoundingMode (RoundingMode.HALF_UP);
        return FormatEuro.format(dblToModif);
    }// end dblFormat

    /**
     * Méthode pour changer la valeur d'un élément du ficher config.xml
     * @param key : la clé de la valeur à modifier
     * @param newValue : la nouvelle valeur
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    public static void rewriteConfig(String key, String newValue) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        String file = System.getProperty("user.dir")+"\\src\\config.xml" ;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getElementsByTagName(key).item(0).setTextContent(newValue);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource src = new DOMSource(doc);
        StreamResult res = new StreamResult(new File(file));
        transformer.transform(src, res);
    }
}
