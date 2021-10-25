/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.controllers.MainMenuController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import org.opencv.core.Core;

/**
 *
 * @author lcabraja
 */
public class OpenCVCats extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        showMainMenu();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    private void showMainMenu() {

        try {
            // load the FXML resource
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/MainMenu.fxml"));
            // store the root element so that the controllers can use it
            BorderPane rootElement = (BorderPane) loader.load();
            // create and style a scene
            Scene scene = new Scene(rootElement, 800, 600);
            // create the stage with the given title and the previously created
            // scene
            mainStage.setTitle("JavaFX meets OpenCV");
            mainStage.setScene(scene);
            // show the GUI
            mainStage.show();

            // set the proper behavior on closing the application
            MainMenuController controller = loader.getController();
            mainStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    controller.setClosed();
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
