/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.model.SerializableImage;
import hr.algebra.utils.ImageUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;

/**
 *
 * @author lcabraja
 */
public class Client {

    private static Thread clientThread = null;

    public static void requestImage(File requestedFile, ObjectProperty<Image> canvas) {
        clientThread = new Thread(() -> sendImageThread(requestedFile, canvas));
        clientThread.start();
    }

    private static void sendImageThread(File requestedFile, ObjectProperty<Image> canvas) {
        try (Socket clientSocket = new Socket(Server.HOST, Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            send(clientSocket, requestedFile, canvas);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void send(Socket clientSocket, File requestedFile, ObjectProperty<Image> canvas) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            oos.writeObject(requestedFile);
            SerializableImage serializableImage = (SerializableImage) ois.readObject();
            ImageUtils.onFXThread(canvas, serializableImage.getImage());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
