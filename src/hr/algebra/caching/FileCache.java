/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.model.CachedFile;
import hr.algebra.utils.SerializationUtils;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class FileCache implements Cache {

    @Override
    public boolean contains(File imageFile, String classifierPath) {
        System.out.println("contains @ " + getClass().toString());
        CachedFile cf = new CachedFile() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        Optional<Map<CachedFile, Rect[]>> serializedFile
                = getSerializedFile();
        return serializedFile.isPresent();
    }

    @Override
    public Optional<Rect[]> getFaceRects(File imageFile, String classifierPath) {
        System.out.println("getFaceRects @ " + getClass().toString());
        CachedFile cf = new CachedFile() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        Optional<Map<CachedFile, Rect[]>> serializedFile
                = getSerializedFile();
        if (serializedFile.isPresent()
                && serializedFile.get().containsKey(cf)) {
            return Optional.of(serializedFile.get().get(cf));
        }
        return Optional.empty();
    }

    private Optional<Map<CachedFile, Rect[]>> getSerializedFile() {
        System.out.println("getSerializedFile @ " + getClass().toString());
        Optional<Map<CachedFile, Rect[]>> serializedFile
                = SerializationUtils.<Map<CachedFile, Rect[]>>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
        return serializedFile;
    }

    @Override
    public void setFaceRects(File imageFile, Rect[] facesArray, String classifierPath) {
        System.out.println("setFaceRects @ " + getClass().toString());
        CachedFile cf = new CachedFile() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };

        Map<CachedFile, Rect[]> previousSerialization;
        if (contains(imageFile, classifierPath)) {
            previousSerialization = getSerializedFile().get();
            previousSerialization.put(cf, facesArray);
        } else {
            previousSerialization = new HashMap<>();
        }
        SerializationUtils.updateSerializedItem((Serializable) previousSerialization, SerializationUtils.RECT_SERIALIZATION);
    }

}
