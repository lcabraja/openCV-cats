/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.model.CachedFile;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    private static final Queue<Pair<CachedFile, Integer>> INPUT_QUEUE = new LinkedList<>();
    private static Thread clientThread = null;

    public static void enqueueMessage(Pair<CachedFile, Integer> message) {
        INPUT_QUEUE.add(message);
        clientThread = new Thread(() -> sendMessages());
        clientThread.start();
    }

    private static void sendMessages() {
        try (Socket clientSocket = new Socket(Server.HOST, Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            send2(clientSocket);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void send(Socket clientSocket) {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (INPUT_QUEUE.size() > 0) {
                Pair<CachedFile, Integer> message = INPUT_QUEUE.remove();
                oos.writeObject(message.getKey());
                oos.writeInt(message.getValue());
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void send2(Socket clientSocket) {
        try (DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {
            while (INPUT_QUEUE.size() > 0) {
                Pair<CachedFile, Integer> data = INPUT_QUEUE.remove();

                StringBuilder sb = new StringBuilder();
                sb.append("Analyzed: ");
                sb.append(data.getKey().getFilePath());
                sb.append(" using: ");
                sb.append(data.getKey().getClassifierPath());
                sb.append(" found ");
                sb.append(data.getValue());
                sb.append(" faces.");

                dos.writeUTF(sb.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
