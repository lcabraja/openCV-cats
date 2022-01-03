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

    public static final String HOST = "localhost";
    public static final Cache cache = new MemCache();
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
        initProcess();
        launch(args);
    }

    private static void initProcess() {
        RMIServiceHost.startServices();
        Server.startServer();
        LiveServer.startServer();

    }

    private void showMainMenu() throws IOException {
        ViewUtils.loadView(getClass().getResource("controllers/views/MainMenu.fxml"));
        getMainStage().show();
        getMainStage().show();
    }

    private void initStage(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle(generateWindowTitle());
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

    private String generateWindowTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(RMIServiceHost.isInitialized() ? "1" : "0");
        sb.append(Server.isInitialized() ? "1" : "0");
        sb.append(LiveServer.isInitialized() ? "1" : "0");
        sb.append(" ");
        sb.append("RMI: ");
        sb.append(RMIServiceHost.isInitialized());
        sb.append(" ");
        sb.append("TCP: ");
        sb.append(Server.isInitialized());
        sb.append(" ");
        sb.append("Live: ");
        sb.append(LiveServer.isInitialized());
        return sb.toString();
    }

}
