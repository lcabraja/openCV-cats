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
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author lcabraja
 */
public class DirectoryServiceImpl implements DirectoryService {

    private static List<File> files = null;
    private static String directory = null;

    public static List<File> getStaticFiles() {
        System.out.println("getStaticFiles @ " + DirectoryServiceImpl.class);
        return files;
    }

    public static void setStaticFiles(List<File> files) {
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
    public List<File> getFiles() throws RemoteException {
        System.out.println("getFiles @ " + getClass().toString());
        return files;
    }

    @Override
    public Solution getSolution(CachedResult cr) throws RemoteException {
        System.out.println("getSolution @ " + getClass().toString());
        Optional<Solution> solution = OpenCVCats.cache.getSolution(cr);
        System.out.println(solution);
        return solution.isPresent() ? solution.get() : null;
    }

    @Override
    public void setSolution(Solution solution) {
        System.out.println("setSolution @ " + getClass().toString());
        OpenCVCats.cache.setSolution(solution);
        System.out.println(solution);
    }
}
