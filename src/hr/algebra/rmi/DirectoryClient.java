/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.model.CachedFile;
import java.awt.Rectangle;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author lcabraja
 */
public class DirectoryClient implements DirectoryService {

    public static final String HOSTNAME = "localhost";

    private Registry registry;
    private DirectoryService stub;
    private boolean initialized = false;

    public DirectoryClient() {
        System.out.println("Locating registry on " + HOSTNAME + ":" + RMIServiceHost.RMI_PORT);
        try {
            registry = LocateRegistry.getRegistry(HOSTNAME, RMIServiceHost.RMI_PORT);
            stub = (DirectoryService) registry.lookup(DirectoryService.REMOTE_OBJECT_NAME);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getDirectoryPath() {
        if (!initialized) {
            return null;
        }
        System.out.println("getDirectoryPath @ " + getClass().toString());
        try {
            return stub.getDirectoryPath();
        } catch (RemoteException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<String> getFiles() throws RemoteException {
        if (!initialized) {
            return null;
        }
        return null;
    }

    @Override
    public Pair<CachedFile, Rectangle[]> getImageData() throws RemoteException {
        if (!initialized) {
            return null;
        }
        return null;
    }
}
