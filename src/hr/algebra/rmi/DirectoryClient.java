/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.OpenCVCats;
import hr.algebra.model.CachedResult;
import hr.algebra.model.Solution;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lcabraja
 */
public class DirectoryClient implements DirectoryService {

    private Registry registry;
    private DirectoryService stub;
    private boolean initialized = false;

    public DirectoryClient() {
        System.out.println("Locating registry on " + OpenCVCats.getSettings().getDefaultHost() + ":" + RMIServiceHost.RMI_PORT);
        try {
            registry = LocateRegistry.getRegistry(OpenCVCats.getSettings().getDefaultHost(), RMIServiceHost.RMI_PORT);
            stub = (DirectoryService) registry.lookup(DirectoryService.REMOTE_OBJECT_NAME);
            initialized = true;
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getDirectoryPath() {
        System.out.println("getDirectoryPath @ " + getClass().toString());
        if (!initialized) {
            System.out.println("DirectoryClient uninitialized...");
            return null;
        }
        try {
            return stub.getDirectoryPath();
        } catch (RemoteException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<File> getFiles() {
        System.out.println("getFiles @ " + getClass().toString());
        if (!initialized) {
            System.out.println("DirectoryClient uninitialized...");
            return null;
        }
        try {
            return stub.getFiles();
        } catch (RemoteException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Solution getSolution(CachedResult cr) {
        System.out.println("getSolution @ " + getClass().toString());
        if (!initialized) {
            System.out.println("DirectoryClient uninitialized...");
            return null;
        }
        try {
            return stub.getSolution(cr);
        } catch (RemoteException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void setSolution(Solution solution) {
        System.out.println("setSolution @ " + getClass().toString());
        if (!initialized) {
            System.out.println("DirectoryClient uninitialized...");
            return;
        }
        try {
            stub.setSolution(solution);
        } catch (RemoteException ex) {
            Logger.getLogger(DirectoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
