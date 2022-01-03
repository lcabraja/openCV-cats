/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import hr.algebra.model.Solution;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author lcabraja
 */
public interface DirectoryService extends Remote {

    String REMOTE_OBJECT_NAME = "hr.algebra.rmi.DirectoryService";

    public String getDirectoryPath() throws RemoteException;

    public List<String> getFiles() throws RemoteException;

    public Solution getImageData() throws RemoteException;
}
