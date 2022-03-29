package utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 01/03/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe utile pour créer des boite de dialogue
 */
public class DialogBox {
    /**
     * Méthode de création d'une boite de dialogue de confirmation
     * @param title
     * @param content
     * @return boolean : true si l'utilisateur confirme
     *                   false si l'utilisateur ne confirme pas
     */
    public static boolean confirm(String title, String content) {
        Alert dialogC= new Alert(AlertType.CONFIRMATION);
        setIcon(dialogC);
        dialogC.setTitle(title);
        dialogC.setHeaderText(null);
        dialogC.setContentText(content);
        Optional<ButtonType> answer= dialogC.showAndWait();
        return answer.get() == ButtonType.OK;
    }

    /**
     * Méthode de création d'une boite de dialogue affichant une information
     * @param title
     * @param Content
     */
    public static void info(String title, String Content) {
        Alert dialogI = new Alert(AlertType.INFORMATION);
        setIcon(dialogI);
        dialogI.setTitle(title);
        dialogI.setHeaderText(null);
        dialogI.setContentText(Content);
        dialogI.showAndWait();
    }

    /**
     * Méthode de création d'une boite de dialogue affichant une erreur
     * @param title
     * @param Content
     */
    public static void error(String title, String Content) {
        Alert dialogI = new Alert(AlertType.ERROR);
        setIcon(dialogI);
        dialogI.setTitle(title);
        dialogI.setHeaderText(null);
        dialogI.setContentText(Content);
        dialogI.showAndWait();
    }

    /**
     * Méthode de création d'une boite de dialogue posant une question et donnant le choix à l'utilisateur OUI NON ANNULER
     * @param title
     * @param content
     * @return boolean : true si l'utilisateur répond oui
     *                   false si l'utilisateur répond non ou cancel
     */
    public static boolean YesNoCancel(String title, String content) {
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        setIcon(dialog);
        dialog.setHeaderText(null);
        dialog.setContentText(content);
        ButtonType Yes= new ButtonType("Yes");
        ButtonType No = new ButtonType("No");
        ButtonType Cancel = new ButtonType("Cancel");
        dialog.getButtonTypes().setAll(Yes,No,Cancel);
        Optional<ButtonType> choice = dialog.showAndWait();
        return (choice.get() == Yes);
    }

    /**
     * Méthode de création d'une boite de dialogue affichant l'explorateur de fichier.
     * @param stage
     * @return : le file (chemin absolu compris) à sauvegarder
     */
    public static File fileSaver(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML files", "*.xml"),
                new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser.showSaveDialog(stage);

    }
    /**
     * Méthode de création d'une boite de dialogue affichant l'explorateur de fichier.
     * Spécialisé pour les fichier excel
     * @param stage
     * @return : le file (chemin absolu compris) à sauvegarder
     */
    public static File xlsFileSaver(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls"));
        return fileChooser.showSaveDialog(stage);
    }
    /**
     * Méthode de création d'une boite de dialogue affichant l'explorateur de fichier.
     * Spécialisé pour les fichiers xml
     * @param stage
     * @return : le file (chemin absolu compris) à sauvegarder
     */
    public static File xmlFileSaver(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        return fileChooser.showSaveDialog(stage);
    }

    /**
     * Méthode d'attribution de l'icone de l'application
     * @param dialog
     */
    private static void setIcon(Alert dialog){
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("img/icon.png"));
    }
}
