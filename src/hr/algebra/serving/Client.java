/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.model.CachedFile;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class Client {

    private static final Queue<Pair<CachedFile, Rect[]>> INPUT_QUEUE = new LinkedList<>();
    private static Pair<CachedFile, Rect[]> lastMessage = null;
    private static Thread listeningThread = null;
    private static Consumer<Pair<CachedFile, Rect[]>> changeListener = null;

    private static Optional<Pair<CachedFile, Rect[]>> checkForMessages() {
        if (INPUT_QUEUE.size() > 0) {
            return Optional.of(INPUT_QUEUE.remove());
        }
        return Optional.empty();

    }

    public static void setListener(Consumer<Pair<CachedFile, Rect[]>> changeListener) {
        Client.changeListener = changeListener;
    }

    public static void removeListener() {
        changeListener = null;
    }

    public static void startListening() {
        listeningThread = new Thread(() -> listenForMessages());
        listeningThread.start();
    }

    public static void stopListening() {
        listeningThread.interrupt();
    }

    private static void enqueueRequest(Pair<CachedFile, Rect[]> message) {
        if (lastMessage != null
                && message != null
                && !message.getKey().equals(lastMessage.getKey())
                && !Arrays.equals(message.getValue(), lastMessage.getValue())) {
            lastMessage = message;
            INPUT_QUEUE.add(message);
            if (changeListener != null) {
                changeListener.accept(message);
            }
            System.out.println(message);
        }
    }

    private static void listenForMessages() {
        try (Socket clientSocket = new Socket(Server.HOST, Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            while (!Thread.currentThread().isInterrupted()) {
                enqueueRequest(receiveExternalizableRequest(clientSocket));
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Pair<CachedFile, Rect[]> receiveExternalizableRequest(Socket clientSocket) {
        try {
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            return (Pair<CachedFile, Rect[]>) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
