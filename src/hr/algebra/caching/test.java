/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.OpenCVCats;
import hr.algebra.model.CachedImage;
import hr.algebra.model.DetailedImageViewHolder;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.SerializationUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 *
 * @author lcabraja
 */
public class test {

    public static byte[] method(File file)
            throws IOException {

        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int) file.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();

        // Returning above byte array
        return arr;
    }

    public static void main(String[] args) throws IOException {
        Optional<File> uploadFile = FileUtils.uploadFile(OpenCVCats.getMainStage(), null, FileUtils.Extensions.JPG);

        if (uploadFile.isPresent()) {
            CachedImage ci = new CachedImage(uploadFile.get(), null, method(uploadFile.get()));
            SerializationUtils.updateSerializedItem(ci, SerializationUtils.RECT_SERIALIZATION);
            Optional<CachedImage> ci2 = SerializationUtils.<CachedImage>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
            if (ci2.isPresent()) {
                System.out.println(ci2);
                System.out.println(ci.getImage().get().equals(ci2.get().getImage()));
            }

        }

    }

}
