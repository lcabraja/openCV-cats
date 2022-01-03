/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.model.CachedResult;
import hr.algebra.utils.OCVUtils;
import hr.algebra.utils.SerializationUtils;
import java.awt.Rectangle;
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
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        Optional<Map<CachedResult, Rect[]>> serializedFile
                = getSerializedFile();
        return serializedFile.isPresent();
    }

    @Override
    public Optional<Rect[]> getFaceRects(File imageFile, String classifierPath) {
        System.out.println("getFaceRects @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        Optional<Map<CachedResult, Rect[]>> serializedFile
                = getSerializedFile();
        if (serializedFile.isPresent()
                && serializedFile.get().containsKey(cf)) {
            return Optional.of(serializedFile.get().get(cf));
        }
        return Optional.empty();
    }

    private Optional<Map<CachedResult, Rect[]>> getSerializedFile() {
        System.out.println("getSerializedFile @ " + getClass().toString());
        Optional<Map<CachedResult, Rectangle[]>> serializedFile
                = SerializationUtils.<Map<CachedResult, Rectangle[]>>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
        if (serializedFile.isPresent()) {
            Map<CachedResult, Rect[]> convertMap = new HashMap<>();
            for (Map.Entry<CachedResult, Rectangle[]> entry : serializedFile.get().entrySet()) {
                convertMap.put(entry.getKey(), OCVUtils.rectangleArrayToRectArray(entry.getValue()));
            }
            return Optional.of(convertMap);
        }
        return Optional.empty();
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

        Map<CachedResult, Rect[]> previousSerialization;
        if (contains(imageFile, classifierPath)) {
            previousSerialization = getSerializedFile().get();
        } else {
            previousSerialization = new HashMap<>();
        }
        previousSerialization.put(cf, facesArray);

        Map<CachedResult, Rectangle[]> convertMap = new HashMap<>();
        for (Map.Entry<CachedResult, Rect[]> entry : previousSerialization.entrySet()) {
            convertMap.put(entry.getKey(), OCVUtils.rectArrayToRectangleArray(entry.getValue()));
        }
        SerializationUtils.updateSerializedItem((Serializable) convertMap, SerializationUtils.RECT_SERIALIZATION);
    }

}
