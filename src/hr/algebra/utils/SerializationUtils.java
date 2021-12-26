/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lcabraja
 */
public class SerializationUtils {

    public static final String UI_SERIALIZATION = "ui.ser";
    public static final String RECT_SERIALIZATION = "rects.ser";

    public static <T> Optional<T> fetchSerializaedItem(String location) {
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(location))) {
            T deserializedItem = (T) ois.readObject();
            return Optional.of(deserializedItem);
        } catch (IOException | ClassNotFoundException ex) {
            return Optional.empty();
        }
    }

    public static <T extends Serializable> void updateSerializedItem(T serializableItem, String location) {
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(location))) {
            oos.writeObject(serializableItem);
        } catch (IOException ex) {
            Logger.getLogger(SerializationUtils.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
