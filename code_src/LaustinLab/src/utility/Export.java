package utility;


import javafx.scene.control.TableView;
import model.Facture;
import org.apache.poi.hssf.usermodel.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static utility.Utility.today;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 01/03/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Class pour Exporter différente tableau ou objet en fichier
 */
public class Export<T> {

    private String fullFile;
    private String name;
    private String path;
    private Facture facture;

    /**
     * Constructeur
     */
    public Export(){
        this.path=".";
        this.name= today().toString()+"_laustinlab";
        this.fullFile=this.path+"\\"+this.name;
    }

    /**
     * Methode de creation d'un fichier excel contenant le tableView
     * La premiere ligne du ficher sera le titre de chaque colonne
     * le reste des lignes sera le contenu du Tableview
     * @param tableView : le Tableau des valeurs à écrire dans le fichier Excel
     * @throws IOException
     */
    public void excel(TableView<T> tableView) throws IOException {
        //creation de workbook + sheet + 1ere column
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        HSSFSheet hssfSheet=  hssfWorkbook.createSheet("Sheet1");
        HSSFRow firstRow= hssfSheet.createRow(0);

        //création et customisation du style
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        HSSFFont font = hssfWorkbook.createFont();
        font.setBold(true);
        style.setFont(font);

        // mise en place des titres de chaque colonne
        for (int i=0; i<tableView.getColumns().size();i++){
            firstRow.createCell(i).setCellValue(tableView.getColumns().get(i).getText());
            firstRow.getCell(i).setCellStyle(style);
            hssfSheet.setColumnWidth(i,30*256);
        }

        for (int row=0; row<tableView.getItems().size();row++){
            HSSFRow hssfRow= hssfSheet.createRow(row+1);
            for (int col=0; col<tableView.getColumns().size(); col++){
                Object celValue = tableView.getColumns().get(col).getCellObservableValue(row).getValue();
                try {
                    if (celValue != null && Double.parseDouble(celValue.toString()) != 0.0) {
                        hssfRow.createCell(col).setCellValue(Double.parseDouble(celValue.toString()));
                    }
                } catch (  NumberFormatException e ){
                    hssfRow.createCell(col).setCellValue(celValue.toString());
                }
            }
        }
        //Sauvegrade du fichier et close du workbook
        try {
            hssfWorkbook.write(new FileOutputStream(this.getFullFile()));
            hssfWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode de creation d'un fichier Excel d'un Facture
     * @param facture
     * @param tableView
     * @throws IOException
     */
    public void excelFacture(Facture facture, TableView<T> tableView) throws IOException {
        //creation de workbook + sheet + 1ere column
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        HSSFSheet hssfSheet=  hssfWorkbook.createSheet("Fact_num_"+facture.getId());
        HSSFRow rowFact = hssfSheet.createRow(0);
        HSSFRow rowContentFact = hssfSheet.createRow(1);
        HSSFRow firstRow= hssfSheet.createRow(4);

        //création et customisation du style
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        HSSFFont font = hssfWorkbook.createFont();
        font.setBold(true);
        style.setFont(font);

        rowFact.createCell(0).setCellValue("id");
        rowFact.createCell(1).setCellValue(Store.bundle.getString("view.client"));
        rowFact.createCell(2).setCellValue(Store.bundle.getString("view.utilisateur"));
        rowFact.createCell(3).setCellValue(Store.bundle.getString("view.dateheure"));
        rowFact.createCell(4).setCellValue(Store.bundle.getString("view.total"));
        rowFact.createCell(5).setCellValue(Store.bundle.getString("view.entreprise"));
        rowFact.createCell(6).setCellValue("TVA");

        rowContentFact.createCell(0).setCellValue(facture.getId());
        rowContentFact.createCell(1).setCellValue(facture.getClient()+"");
        rowContentFact.createCell(2).setCellValue(facture.getUtilisateur()+"");
        rowContentFact.createCell(3).setCellValue(facture.getDateHeure()+"");
        rowContentFact.createCell(4).setCellValue(facture.getTotal()+"");
        rowContentFact.createCell(5).setCellValue(facture.getEntreprise()+"");
        rowContentFact.createCell(6).setCellValue(facture.getEntreprise().getNumTva()+"");
        // mise en place des titres de chaque colonne

        for (int i=0; i<tableView.getColumns().size();i++){
            firstRow.createCell(i).setCellValue(tableView.getColumns().get(i).getText());
            firstRow.getCell(i).setCellStyle(style);
            hssfSheet.setColumnWidth(i,30*256);
        }

        for (int row=0; row<tableView.getItems().size();row++){
            HSSFRow hssfRow= hssfSheet.createRow(5+row);
            for (int col=0; col<tableView.getColumns().size(); col++){
                Object celValue = tableView.getColumns().get(col).getCellObservableValue(row).getValue();
                try {
                    if (celValue != null && Double.parseDouble(celValue.toString()) != 0.0) {
                        hssfRow.createCell(col).setCellValue(Double.parseDouble(celValue.toString()));
                    }
                } catch (  NumberFormatException e ){
                    hssfRow.createCell(col).setCellValue(celValue.toString());
                }
            }
        }
        //Sauvegrade du fichier et close du workbook
        try {
            hssfWorkbook.write(new FileOutputStream(this.getFullFile()));
            hssfWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * méthode de création d'un fichier xml où l'integralité du tableView y est repris
     * @param tableView : le tableau que l'on souhaite ecrire sur en fichier xml
     */
    public void xml(TableView<T> tableView){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            // élément de racine
            Document doc = docBuilder.newDocument();
            Element racine = doc.createElement("list");
            doc.appendChild(racine);

            // création de chaque objet
            for (int row=0; row<tableView.getItems().size();row++){
                Element objEl = doc.createElement("object");
                racine.appendChild(objEl);
                for (int col=0; col<tableView.getColumns().size(); col++){
                    Object celValue = tableView.getColumns().get(col).getCellObservableValue(row).getValue();
                    Element el = doc.createElement(tableView.getColumns().get(col).getText().replace(" ","_"));
                    objEl.appendChild(el);
                    el.appendChild(doc.createTextNode(celValue+""));
                }
            }

            // ecriture du contenu dans le fichier xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult resultat = new StreamResult(new File(this.getFullFile()));
            transformer.transform(source, resultat);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullFile() {
        return fullFile;
    }

    public void setFullFile(String fullFile) {
        this.fullFile = fullFile;
    }

    public static void main(String[] args) {

    }
}