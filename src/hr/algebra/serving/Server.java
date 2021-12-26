/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.model.CachedFile;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
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
//    private static final Queue<Pair<CachedFile, Integer>> OUTPUT_QUEUE = new LinkedList<>();
    private static final Queue<String> OUTPUT_QUEUE = new LinkedList<>();

    private static Thread serverThread = null;
//    private static Consumer<Pair<CachedFile, Rect[]>> listener = null;
    private static Consumer<String> listener = null;

    public static void startServer() {
        if (serverThread == null) {
            serverThread = new Thread(() -> receiveMessages());
            serverThread.start();
        }
    }

    public static void stopServer() {
        serverThread.interrupt();
    }

//    public static void setListener(Consumer<Pair<CachedFile, Rect[]>> changeListener) {
    public static void setListener(Consumer<String> changeListener) {
        listener = changeListener;
    }

    public static void removeListener() {
        listener = null;
    }

    private static void receiveMessages() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port:  " + clientSocket.getPort());
                new Thread(() -> processMessage2(clientSocket)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processMessage(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            do {
                CachedFile cachedFile = (CachedFile) ois.readObject();
                int numberOfRects = ois.readInt();
                Pair<CachedFile, Integer> pair = new Pair<>(cachedFile, numberOfRects);
//                OUTPUT_QUEUE.add(pair);
                System.out.println("Available " + ois.available());
            } while (ois.available() > 0);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processMessage2(Socket clientSocket) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream())) {
            if (listener != null) {
                listener.accept(dis.readUTF());
            } else {
                OUTPUT_QUEUE.add(dis.readUTF());
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
