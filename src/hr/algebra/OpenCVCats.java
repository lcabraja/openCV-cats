/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.controllers.MainMenuController;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.opencv.core.Core;

/**
 *
 * @author lcabraja
 */
public class OpenCVCats extends Application {

    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
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

    private void showMainMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/MainMenu.fxml"));
        Scene scene = new Scene(root);
        getMainStage().setScene(scene);
        getMainStage().show();
    }

}
