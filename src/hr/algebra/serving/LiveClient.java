/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.OpenCVCats;
import hr.algebra.model.SerializableImage;
import hr.algebra.utils.ImageUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;

/**
 *
 * @author lcabraja
 */
public class LiveClient {

    private static Thread clientThread = null;

    public static void requestImage(ObjectProperty<Image> canvas) {
        clientThread = new Thread(() -> sendImageThread(canvas));
        clientThread.start();
    }

    private static void sendImageThread(ObjectProperty<Image> canvas) {
        try (Socket clientSocket = new Socket(OpenCVCats.getSettings().getDefaultHost(), LiveServer.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            send(clientSocket, canvas);
        } catch (IOException ex) {
            Logger.getLogger(LiveClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void send(Socket clientSocket, ObjectProperty<Image> canvas) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            SerializableImage serializableImage = (SerializableImage) ois.readObject();
            ImageUtils.onFXThread(canvas, serializableImage.getImage());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(LiveClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
