/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.caching.Cache;
import hr.algebra.caching.MemCache;
import hr.algebra.model.Descriptor;
import hr.algebra.rmi.RMIServiceHost;
import hr.algebra.serving.LiveServer;
import hr.algebra.serving.Server;
import hr.algebra.utils.ViewUtils;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

/**
 *
 * @author lcabraja
 */
public class OpenCVCats extends Application {

    public static Cache cache = new MemCache();
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        initStage(primaryStage);
        mainStage = primaryStage;
        showMainMenu();
    }

    /**
     *
     * @param args the command line arguments
     */
    @Descriptor("Main method")
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        RMIServiceHost.startServices();
        Server.startServer();
        LiveServer.startServer();
        launch(args);
    }

    private void showMainMenu() throws IOException {
        ViewUtils.loadView(getClass().getResource("controllers/views/MainMenu.fxml"));
        getMainStage().show();
        getMainStage().show();
    }

    private void initStage(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            try {
                RMIServiceHost.stopServices();
                Server.stopServer();
                LiveServer.stopServer();
                stop();
                System.exit(0);
            } catch (Exception ex) {
                System.exit(1);
            }
        });
    }

}
