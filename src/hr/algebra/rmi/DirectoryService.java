/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.model.CachedFile;
import java.awt.Rectangle;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author lcabraja
 */
public interface DirectoryService extends Remote {

    String REMOTE_OBJECT_NAME = "hr.algebra.rmi.DirectoryService";

    public String getDirectoryPath() throws RemoteException;

    public List<String> getFiles() throws RemoteException;

    public Pair<CachedFile, Rectangle[]> getImageData() throws RemoteException;
}
