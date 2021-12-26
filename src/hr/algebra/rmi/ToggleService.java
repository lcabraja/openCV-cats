/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author lcabraja
 */
public interface ToggleService extends Remote {

    String REMOTE_OBJECT_NAME = "hr.algebra.rmi.ToggleService";
    public void toggleCss() throws RemoteException;
}
