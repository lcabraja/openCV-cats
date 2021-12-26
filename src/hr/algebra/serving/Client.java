/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.model.CachedFile;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author lcabraja
 */
public class Client {

    private static Thread clientThread = null;

    public static void sendImage() {
        clientThread = new Thread(() -> sendImageThread());
        clientThread.start();
    }

    private static void sendImageThread() {
        try (Socket clientSocket = new Socket(Server.HOST, Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            send(clientSocket);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void send(Socket clientSocket) {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            // TODO: reimplement client
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
