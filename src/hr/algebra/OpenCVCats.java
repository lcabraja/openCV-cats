/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.caching.Cache;
import hr.algebra.caching.MemCache;
import hr.algebra.model.Descriptor;
import hr.algebra.rmi.ToggleService;
import hr.algebra.rmi.ToggleServiceImpl;
import hr.algebra.serving.Server;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.opencv.core.Core;

/**
 *
 * @author lcabraja
 */
public class OpenCVCats extends Application {

//    public static Cache cache = new MemCache();
    public static Cache cache = new MemCache();
    private static Stage mainStage;
    //private static ToggleServiceImpl jdniService = new ToggleServiceImpl();
    
//    public static ToggleServiceImpl getJdniService() {
//        return jdniService;
//    }
//
//    public static void setJdniService(ToggleServiceImpl jdniService) {
//        OpenCVCats.jdniService = jdniService;
//    }

    public static Stage getMainStage() {
        return mainStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        mainStage = primaryStage;
//        FXTesting.start(primaryStage); System.exit(0); if (true) return;
        showMainMenu();
    }

    /**
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
        //idk();
    }

    @Descriptor("Test")
    private void showMainMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("controllers/views/MainMenu.fxml"));
        Scene scene = new Scene(root);
        getMainStage().setScene(scene);
        getMainStage().show();
    }
    
//    private static void idk() {
//        System.out.println("Server started...");
//        
//        ToggleServiceImpl server = new ToggleServiceImpl();
//        try {
//            ToggleService stub = (ToggleService) UnicastRemoteObject
//                    .exportObject((ToggleService) server, 0);
//            
//            Registry registry = LocateRegistry.createRegistry(1099);
//            
//            System.out.println("RMI Registry created!");
//            
//            registry.rebind("MessengerService", stub);
//            
//            System.out.println("Service binded...");
//            
//        } catch (RemoteException ex) {
//            Logger.getLogger(OpenCVCats.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}