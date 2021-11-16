/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.utils.SerializationUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class ImageRectCache {

    private ImageRectCache() {
        loadLastValues();
        cachedRects = new HashMap<>();
    }

    private Map<String, Rect[]> cachedRects;

    public Map<String, Rect[]> getCachedRects() {
        return cachedRects;
    }

    private void loadLastValues() {
        Optional<Map<String, Rect[]>> serializedFile
                = SerializationUtils.<Map<String, Rect[]>>fetchSerializaedItem(SerializationUtils.RECT_SERIALIZATION);
        if (serializedFile.isPresent()) {
            cachedRects = serializedFile.get();
        }
    }
    
    private void writeNewValues() {
        SerializationUtils.updateSerializedItem("", SerializationUtils.RECT_SERIALIZATION);
    }
}
