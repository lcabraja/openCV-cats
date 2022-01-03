/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.model.CachedResult;
import hr.algebra.utils.SerializationUtils;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class FileCache implements Cache {

    @Override
    public boolean contains(File imageFile, String classifierPath) {
        System.out.println("contains @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        Optional<Set<CachedResult>> serializedFile
                = getSerializedFile();
        return serializedFile.isPresent() && serializedFile.get().contains(cf);
    }

    @Override
    public Optional<CachedResult> getFaceRects(File imageFile, String classifierPath) {
        System.out.println("getFaceRects @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        Optional<Set<CachedResult>> deserializedCache = getSerializedFile();
        if (deserializedCache.isPresent()) {

            Set<CachedResult> results = deserializedCache.get();
            if (results.contains(cf)) {
                return Optional.of(results.stream().filter(cf::equals).findAny().orElse(null));
            }
        }
        return Optional.empty();
    }

    private Optional<Set<CachedResult>> getSerializedFile() {
        System.out.println("getSerializedFile @ " + getClass().toString());
        Optional<Set<CachedResult>> serializedFile
                = SerializationUtils.<Set<CachedResult>>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
        return serializedFile;
    }

    @Override
    public void setFaceRects(File imageFile, Rect[] facesArray, String classifierPath) {
        System.out.println("setFaceRects @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };

        Set<CachedResult> previousSerialization;
        if (contains(imageFile, classifierPath)) {
            previousSerialization = getSerializedFile().get();
        } else {
            previousSerialization = new HashSet<>();
        }
        previousSerialization.add(cf);
        SerializationUtils.updateSerializedItem((Serializable) previousSerialization, SerializationUtils.RECT_SERIALIZATION);
    }

}
