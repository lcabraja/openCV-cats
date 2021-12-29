/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import java.net.BindException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lcabraja
 */
public class RMIServiceHost {

    private static final int RANDOM_PORT_HINT = 0;
    public static final int RMI_PORT = 1099;

    private static DirectoryService toggleService;
    private static Registry registry;
    private static DirectoryService skeleton;

    private static boolean isInitialized = false;

    private RMIServiceHost() {
    }

    public static void startServices() {
        System.out.println("startServices @ " + RMIServiceHost.class);
        initializeServices();
        bindObjects();
    }

    public static void stopServices() {
        System.out.println("stopServices @ " + RMIServiceHost.class);
        unbindObjects();
    }

    private static void initializeServices() {
        toggleService = new DirectoryServiceImpl();
    }

    private static void bindObjects() {
        try {
            registry = LocateRegistry.createRegistry(RMI_PORT);
            skeleton = (DirectoryService) UnicastRemoteObject.exportObject(toggleService, RANDOM_PORT_HINT);
            registry.rebind(DirectoryService.REMOTE_OBJECT_NAME, skeleton);
            isInitialized = true;
            System.err.println("Object registered in RMI registry");
        } catch (RemoteException ex) {
            if (ex.getCause() instanceof BindException) {
                System.err.println("Unable to bind RMI Port");
                return;
            }
            Logger.getLogger(RMIServiceHost.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void unbindObjects() {
        if (isInitialized) {
            try {
                registry.unbind(DirectoryService.REMOTE_OBJECT_NAME);
                UnicastRemoteObject.unexportObject(toggleService, true);
                UnicastRemoteObject.unexportObject(registry, true);
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(RMIServiceHost.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
