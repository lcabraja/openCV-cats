/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class MemCache implements Cache {

    private Map<File, Map<String, Rect[]>> faces = new HashMap<>();

    @Override
    public boolean contains(File imageFile, String classifierPath) {
        System.out.println("contains @ " + getClass().toString());
        if (faces.containsKey(imageFile)) {
            return faces.get(imageFile).containsKey(classifierPath);
        }
        return false;
    }

    @Override
    public Optional<Rect[]> getFaceRects(File imageFile, String classifierPath) {
        System.out.println("getFaceRects @ " + getClass().toString());
        if (faces.containsKey(imageFile)) {
            if (faces.get(imageFile).containsKey(classifierPath)) {
                return Optional.of(faces.get(imageFile).get(classifierPath));
            }
        }
        return Optional.empty();
    }

    @Override
    public void setFaceRects(File imageFile, Rect[] facesArray, String classifierPath) {
        System.out.println("setFaceRects @ " + getClass().toString());
        if (faces.containsKey(imageFile)) {
            faces.get(imageFile).put(classifierPath, facesArray);
        } else {
            Map<String, Rect[]> newFace = new HashMap<>();
            newFace.put(classifierPath, facesArray);
            faces.put(imageFile, newFace);
        }
    }

}
