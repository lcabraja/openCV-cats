/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.serving;

import hr.algebra.OpenCVCats;
import hr.algebra.model.SerializableImage;
import hr.algebra.utils.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;

/**
 *
 * @author lcabraja
 */
public class Client {

    private Thread clientThread = null;

    // ---------------------------------------------------------- Consumer -----
    public void requestImage(File requestedFile, ObjectProperty<Image> canvas) {
        clientThread = new Thread(() -> sendImageThread(requestedFile, canvas));
        clientThread.start();
    }

    private void sendImageThread(File requestedFile, ObjectProperty<Image> canvas) {
        try (Socket clientSocket = new Socket(OpenCVCats.getSettings().getDefaultHost(), Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            send(clientSocket, requestedFile, canvas);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void send(Socket clientSocket, File requestedFile, ObjectProperty<Image> canvas) {
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

    // ---------------------------------------------------------- Consumer -----
    public void requestImage(File requestedFile, Consumer consumer) {
        clientThread = new Thread(() -> sendImageThread(requestedFile, consumer));
        clientThread.start();
    }

    private void sendImageThread(File requestedFile, Consumer consumer) {
        try (Socket clientSocket = new Socket(OpenCVCats.getSettings().getDefaultHost(), Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            send(clientSocket, requestedFile, consumer);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void send(Socket clientSocket, File requestedFile, Consumer consumer) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            oos.writeObject(requestedFile);
            SerializableImage serializableImage = (SerializableImage) ois.readObject();
            consumer.accept(serializableImage);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // ---------------------------------------------------------- Blocking -----

    public SerializableImage requestImageSync(File requestedFile) {
        try (Socket clientSocket = new Socket(OpenCVCats.getSettings().getDefaultHost(), Server.PORT)) {
            System.err.println("Client connecting onto: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            return send(clientSocket, requestedFile);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private SerializableImage send(Socket clientSocket, File requestedFile) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            oos.writeObject(requestedFile);
            SerializableImage serializableImage = (SerializableImage) ois.readObject();
            return serializableImage;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
