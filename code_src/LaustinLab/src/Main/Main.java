package Main;

import DAO.DAOFactory;
import controller.Login;
import controller.MasterControl;
import exceptions.DBException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utility.DialogBox;
import utility.Store;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
/**
 * @author Rochez Justin, Sapin Laurent
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe Main de l'application
 */
public class Main extends Application {
    private double xOffset;
    private double yOffset;
    @Override
    public void start(Stage primaryStage) {
        try {
            MasterControl master = new MasterControl(primaryStage);
            /* Attribution de la langue */
            Locale locale = Store.locale;
            ResourceBundle bundle = ResourceBundle.getBundle("strings", locale);
            /*  load de view.master.fxml ==> le container de base de toute l'application */
            URL fxmlLoc = getClass().getClassLoader().getResource("view/master.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLoc,bundle);
            loader.setController(master);
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            /* réglage du primaryStage*/
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.getIcons().add(new Image("img/icon.png"));
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setTitle(Store.appName);
            primaryStage.setOnCloseRequest(e -> {
                try {
                    DAOFactory.close(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }finally {
                    Platform.exit();
                }
            });
            primaryStage.show();
            scene.setFill(Color.TRANSPARENT);
            /* comme le primaryStage est en style TRANSPARENT, impossible de déplacer la fenetre si l'utilisateur le fait. Les fonctions suivante pallie à ce problème */
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    xOffset = mouseEvent.getSceneX();
                    yOffset = mouseEvent.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    primaryStage.setX(mouseEvent.getScreenX() - xOffset);
                    primaryStage.setY(mouseEvent.getScreenY() - yOffset);
                }
            });
        } catch(Exception e) {
            DialogBox.error(Store.bundle.getString("Error.OpenApp.title"),
                    Store.bundle.getString("Error.OpenApp.Content"));
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
