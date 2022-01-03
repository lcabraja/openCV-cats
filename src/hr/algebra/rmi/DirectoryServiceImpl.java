/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.model.Solution;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author lcabraja
 */
public class DirectoryServiceImpl implements DirectoryService {

    private static List<String> files = null;
    private static String directory = null;

    public static List<String> getStaticFiles() {
        System.out.println("getStaticFiles @ " + DirectoryServiceImpl.class);
        return files;
    }

    public static void setStaticFiles(List<String> files) {
        System.out.println("setStaticFiles @ " + DirectoryServiceImpl.class);
        DirectoryServiceImpl.files = files;
    }

    public static String getStaticDirectory() {
        System.out.println("getStaticDirectory @ " + DirectoryServiceImpl.class);
        return directory;
    }

    public static void setStaticDirectory(String directoryPath) {
        System.out.println("setStaticDirectory @ " + DirectoryServiceImpl.class + " " + directoryPath);
        directory = directoryPath;
    }

    @Override
    public String getDirectoryPath() throws RemoteException {
        System.out.println("getDirectoryPath @ " + getClass().toString());
        return directory;
    }

    @Override
    public List<String> getFiles() throws RemoteException {
        System.out.println("getFiles @ " + getClass().toString());
        return files;
    }

    @Override
    public Solution getImageData() throws RemoteException {
        return new Solution();
    }
}
