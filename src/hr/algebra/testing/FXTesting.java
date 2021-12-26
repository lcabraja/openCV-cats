/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.testing;

import hr.algebra.utils.FileUtils;
import hr.algebra.utils.SerializationUtils;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;

/**
 *
 * @author lcabraja
 */
public class FXTesting {
//
//    public static byte[] method(File file)
//            throws IOException {
//        Desktop.getDesktop().open(file);
//
//        // Creating an object of FileInputStream to
//        // read from a file
//        InputStream fl = new FileInputStream(file);
//
//        // Now creating byte array of same length as file
//        byte[] arr = new byte[(int) file.length()];
//
//        // Reading file content to byte array
//        // using standard read() method
//        fl.read(arr);
//
//        // lastly closing an instance of file input stream
//        // to avoid memory leakage
//        fl.close();
//
//        // Returning above byte array
//        return arr;
//    }
//
//    public static File method2(byte[] bytes) throws IOException {
//        File written = new File("C:\\Users\\lcabraja\\Documents\\NetBeansProjects\\openCV-cats\\output.jpg");
//        try (FileOutputStream fos = new FileOutputStream(written)) {
//            fos.write(bytes);
//        } catch (IOException ex) {
//            Logger.getLogger(FXTesting.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Desktop.getDesktop().open(written);
//        return written;
//    }
//
//    public static void start(Stage primaryStage) throws IOException {
//        Optional<File> uploadFile = FileUtils.uploadFile(primaryStage, "yoo", FileUtils.Extensions.JPG);
//
//        if (uploadFile.isPresent()) {
//            CachedImage ci = new CachedImage(uploadFile.get(), null, method(uploadFile.get()));
//            SerializationUtils.updateSerializedItem(ci, SerializationUtils.RECT_SERIALIZATION);
//            Optional<CachedImage> ci2 = SerializationUtils.<CachedImage>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
//            if (ci2.isPresent()) {
//                System.out.println(Arrays.equals(ci.getImage().get(), ci2.get().getImage().get()));
//            }
//
//        }
//    }
}
