/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.model.CachedFile;
import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.util.List;
import javafx.util.Pair;

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
    public Pair<CachedFile, Rectangle[]> getImageData() throws RemoteException {
        CachedFile cf = new CachedFile();
        cf.setClassifierPath(REMOTE_OBJECT_NAME);
        cf.setFilePath("filepath");
        return new Pair<>(cf, new Rectangle[]{
            new Rectangle(1, 2, 3, 4),
            new Rectangle(5, 6, 7, 8)
        });
    }
}
