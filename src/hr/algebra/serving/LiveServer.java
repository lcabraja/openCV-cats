/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.model.SerializableImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author lcabraja
 */
public class LiveServer {

    public static final int PORT = 23456;
    public static final String HOST = "localhost";
    public static SerializableImage serializableImage = null;

    private static Thread serverThread = null;

    public static void startServer() {
        if (serverThread == null) {
            serverThread = new Thread(() -> receiveMessages());
            serverThread.start();
        }
    }

    public static void stopServer() {
        serverThread.interrupt();
    }

    private static void receiveMessages() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port:  " + clientSocket.getPort());
                new Thread(() -> processMessage(clientSocket)).start();
            }
        } catch (BindException ex) {
            System.err.println("Unable to bind TCP Port");
        } catch (IOException ex) {
            Logger.getLogger(LiveServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processMessage(Socket clientSocket) {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            oos.writeObject(serializableImage);
        } catch (IOException ex) {
            Logger.getLogger(LiveServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
