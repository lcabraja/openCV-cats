/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.utils.SerializationUtils;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class FileCache implements Cache {

    private static Cache singleton = null;

    public static Cache getInstance() {
        if (singleton == null) {
            singleton = new FileCache();
        }
        return singleton;
    }

    @Override
    public boolean contains(File imageFile) {
        Optional<Map<String, Rect[]>> serializedFile
                = SerializationUtils.<Map<String, Rect[]>>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
        return serializedFile.isPresent();
    }

    @Override
    public Optional<Rect[]> getFaceRects(File imageFile) {
        Optional<Map<String, Rect[]>> serializedFile
                = SerializationUtils.<Map<String, Rect[]>>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
        if (serializedFile.isPresent() && serializedFile.get().containsKey(imageFile.getAbsoluteFile())) {
            return Optional.of(serializedFile.get().get(imageFile.getAbsoluteFile()));
        }
        return Optional.empty();
    }

    @Override
    public void setFaceRects(File imageFile, Rect[] facesArray) {
        SerializationUtils.updateSerializedItem(imageFile.getAbsoluteFile(), SerializationUtils.RECT_SERIALIZATION);
    }

}
