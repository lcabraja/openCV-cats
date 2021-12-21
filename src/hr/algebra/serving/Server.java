/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.model.CachedFile;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class Server {

    public static final int PORT = 12345;
    public static final String HOST = "localhost";
    private static final Queue<Pair<CachedFile, Rect[]>> OUTPUT_QUEUE = new LinkedList<>();
    private static Thread serverThread = null;

    public static void startServer() {
        if (serverThread == null) {
            serverThread = new Thread(() -> sendUpdates());
            serverThread.start();
        }
    }

    public static void stopServer() {
        serverThread.interrupt();
    }

    public static void enqueueMessage(Pair<CachedFile, Rect[]> message) {
        OUTPUT_QUEUE.add(message);
    }

    private static void sendUpdates() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port:  " + clientSocket.getPort());
                new Thread(() -> sendExternalizableRequest(clientSocket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void sendExternalizableRequest(Socket clientSocket) {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (OUTPUT_QUEUE.size() > 0) {
                oos.writeObject(OUTPUT_QUEUE.remove());
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
