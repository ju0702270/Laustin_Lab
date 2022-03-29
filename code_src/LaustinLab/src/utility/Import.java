package utility;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 01/03/2021
 *     Modification : 017/03/2021
 *     Revision : 1.0
 *     Description : Class pour importer différente tableau ou objet depuis des fichiers
 */
public class Import {
    private String name;
    private String path;

    /**
     * Constructeur
     */
    public Import(){
        this.path=".";
        this.name= "2021-03-01_laustinlab";
    }

    /**
     * Méthode d'importation des données d'un fichier xml dans une hashMap
     * le fichier doit être parsé correctement, une "list" contient tout les "object" qui eux contiennent leurs différents attribut (selon le type d'objet)
     * @return HashMap : contenantune liste d'objet avec tout les attributs de l'objet ainsi que leur valeur
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public HashMap<String, Object> xml() throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(this.name+".xml");
        doc.getDocumentElement().normalize();

        // Recherche tous les objects contenu dans le fichier
        NodeList list = doc.getElementsByTagName("object");
        HashMap<String, Object> hashMap= new HashMap<>();

        // Pour chaque élément XML de la liste
        for (int i = 0; i < list.getLength(); i = i + 1) {
            Node node = list.item(i);
            HashMap<String, Object> hashContenu = new HashMap<>();

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                // Récupère l'object
                Element object = (Element)node;
                // ajout de l'objet dans la hashContenu;
                for (int j = 0; j < object.getChildNodes().getLength(); j++) {
                    hashContenu.put(object.getChildNodes().item(j).getNodeName(),
                            object.getChildNodes().item(j).getTextContent());
                }
                //ajout de la hashContenu dans hashMap
                hashMap.put(Integer.toString(i),hashContenu);
            }
        }
        return hashMap;
    }

    /**
     * Methode de récuperation de la configuration
     * @return HashMap : contenant toute la configuration de l'application
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public HashMap<String, String> getConfig() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(System.getProperty("user.dir")+"\\src\\config.xml");
        doc.getDocumentElement().normalize();

        // Recherche de la configuration
        NodeList list = doc.getElementsByTagName("config");
        HashMap<String, String> hashContenu = new HashMap<>();
        Node node = list.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            // Récupère l'object
            Element object = (Element)node;
            // ajout de l'objet dans la hashContenu;
            for (int j = 0; j < object.getChildNodes().getLength(); j++) {
                hashContenu.put(object.getChildNodes().item(j).getNodeName(),object.getChildNodes().item(j).getTextContent());
            }
        }
        return hashContenu;
    }

    /**
     * Methode de lecture d'une sheet d'un fichier Excel
     * La sheet doit être parsée comme suit : les titres en ligne 0
     * Les Valeurs selon les titres en dessous.
     * @return HashMap : le contenu de la sheet Excel 0
     * @throws IOException
     */
    public HashMap<String,Object> excel() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(this.path+"\\"+this.name + ".xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        // importation de la premiere sheet Excel
        HSSFSheet sheet = workbook.getSheetAt(0);

        // On va iterer sur chaque ligne dans la page
        Iterator<Row> rowIterator = sheet.iterator();
        Row firstRow = sheet.getRow(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        int j = 0;

        while (rowIterator.hasNext()) {
            HashMap<String, Object> hashContenu = new HashMap<>();
            Row row = rowIterator.next();
            for (int i = 0; i < row.getLastCellNum(); i++) {
                hashContenu.put(firstRow.getCell(i).toString(), row.getCell(i));
            }
            hashMap.put(j+"",hashContenu);
            j++;

        }
        hashMap.remove(0+"");
        return hashMap;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
