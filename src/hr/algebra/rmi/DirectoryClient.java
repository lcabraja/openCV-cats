/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.model.CachedFile;
import java.awt.Rectangle;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author lcabraja
 */
public class DirectoryClient {

    public static final int PORT = 1099;
    public static final String HOSTNAME = "localhost";

    private Registry registry;
    private DirectoryService stub;

    public DirectoryClient() {
        try {
            System.out.println("Locating registry on " + HOSTNAME + ":" + PORT);
            registry = LocateRegistry.getRegistry(HOSTNAME, PORT);
            System.out.println("Initializaing stub");
            stub = (DirectoryService) registry.lookup(DirectoryService.REMOTE_OBJECT_NAME);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleRemoteCalls() throws RemoteException {
        System.out.println("Calling stub");
        Pair<CachedFile, Rectangle[]> imageData = stub.getImageData();
        System.out.println("Result: " + imageData);
        for (Rectangle object : imageData.getValue()) {
            System.out.println(object);
        }
    }
}
