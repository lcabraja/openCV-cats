/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.model.CachedFile;
import java.rmi.RemoteException;
import java.util.List;
import javafx.util.Pair;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class DirectoryServiceImpl implements DirectoryService {

    @Override
    public String getDirectoryPath() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getFiles() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pair<CachedFile, Rect[]> getImageData() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
